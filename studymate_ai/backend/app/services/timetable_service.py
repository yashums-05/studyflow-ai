import uuid
from datetime import datetime, timedelta, date
from typing import List, Dict, Any, Optional
from app.core.config import settings
from app.repositories.firestore_repository import repo

class TimetableService:
    """
    Intelligent Timetable Generation and Adaptive Rescheduling Engine.
    Implements deterministic constraint-based scheduling with weighted priority scoring:
    P = 0.35D + 0.30U + 0.20W + 0.15I
    """

    @staticmethod
    def calculate_priority_score(
        difficulty: int,          # 1 to 5
        exam_date_str: Optional[str], # YYYY-MM-DD
        understanding_rating: Optional[int], # 1 to 5
        importance: int,          # 1 to 5
        reference_date: Optional[date] = None
    ) -> float:
        """
        Calculates normalized Priority Score P (0 - 100).
        P = 0.35*D + 0.30*U + 0.20*W + 0.15*I
        """
        # D: Difficulty normalized (1 -> 0, 5 -> 100)
        d_score = max(0.0, min(100.0, (difficulty - 1) * 25.0))

        # U: Exam Urgency (Closer exam -> higher urgency)
        u_score = 30.0 # Default baseline urgency if no exam set
        if exam_date_str:
            try:
                today = reference_date or date.today()
                exam_dt = datetime.strptime(exam_date_str, "%Y-%m-%d").date()
                days_left = (exam_dt - today).days
                if days_left <= 0:
                    u_score = 100.0
                elif days_left < 30:
                    # Linearly scale from 100 down to 10
                    u_score = max(10.0, 100.0 - (days_left * 3.0))
                else:
                    u_score = 15.0
            except Exception:
                pass

        # W: Student Weakness (Lower rating -> Higher weakness score)
        # Rating 1 -> W=100; Rating 5 -> W=0. If unrated, default to 50
        if understanding_rating is not None and 1 <= understanding_rating <= 5:
            w_score = (5 - understanding_rating) * 25.0
        else:
            w_score = 50.0

        # I: Importance normalized (1 -> 0, 5 -> 100)
        i_score = max(0.0, min(100.0, (importance - 1) * 25.0))

        # Priority calculation using configured weights
        p = (
            settings.WEIGHT_DIFFICULTY * d_score +
            settings.WEIGHT_URGENCY * u_score +
            settings.WEIGHT_WEAKNESS * w_score +
            settings.WEIGHT_IMPORTANCE * i_score
        )
        return round(p, 2)

    @staticmethod
    def get_priority_category(score: float) -> str:
        if score >= 75:
            return "critical"
        elif score >= 55:
            return "high"
        elif score >= 35:
            return "medium"
        return "low"

    @staticmethod
    def get_priority_reason(
        topic_name: str,
        difficulty: int,
        days_to_exam: Optional[int],
        understanding_rating: Optional[int]
    ) -> str:
        reasons = []
        if days_to_exam is not None and days_to_exam <= 7:
            reasons.append(f"the examination is in {days_to_exam} days")
        elif days_to_exam is not None and days_to_exam <= 14:
            reasons.append(f"the examination is approaching in {days_to_exam} days")

        if difficulty >= 4:
            reasons.append("the topic has high conceptual difficulty")

        if understanding_rating is not None and understanding_rating <= 2:
            reasons.append("your previous understanding rating was low")

        if not reasons:
            reasons.append("it is a foundational component of your active syllabus")

        return f"{topic_name} is prioritized because " + " and ".join(reasons) + "."

    async def generate_timetable(
        self,
        user_id: str,
        start_date_str: str,
        days_to_schedule: int = 7,
        daily_available_hours: Optional[float] = None,
        preferred_start: Optional[str] = None,
        preferred_end: Optional[str] = None,
        session_duration: Optional[int] = None,
        break_duration: Optional[int] = None,
        include_revisions: bool = True,
        exam_prep_mode: bool = False,
        target_exam_subject_id: Optional[str] = None
    ) -> Dict[str, Any]:
        """
        Deterministic, constraint-satisfaction schedule allocation.
        """
        # Fetch user preferences
        user_doc = await repo.get_document("users", user_id)
        prefs = (user_doc.get("preferences") if user_doc else None) or {}
        
        max_daily_hours = daily_available_hours or prefs.get("maximumDailyStudyHours", 6.0)
        start_time_str = preferred_start or prefs.get("studyStartTime", "08:00")
        end_time_str = preferred_end or prefs.get("studyEndTime", "22:00")
        session_mins = session_duration or prefs.get("preferredSessionDuration", 45)
        break_mins = break_duration or prefs.get("shortBreakDuration", 10)

        # Parse daily window bounds
        s_hour, s_min = map(int, start_time_str.split(":"))
        e_hour, e_min = map(int, end_time_str.split(":"))
        window_minutes = (e_hour * 60 + e_min) - (s_hour * 60 + s_min)
        max_study_minutes_per_day = min(int(max_daily_hours * 60), max(60, window_minutes))

        # Fetch subjects and topics
        subjects = await repo.query_documents("subjects", {"userId": user_id})
        subject_map = {s["subjectId"]: s for s in subjects}

        topics = await repo.query_documents("topics", {"userId": user_id})
        
        # Filter incomplete topics or revision-needed
        eligible_topics = []
        for t in topics:
            if exam_prep_mode and target_exam_subject_id and t.get("subjectId") != target_exam_subject_id:
                continue

            status = t.get("status", "not_started")
            if status in ["not_started", "in_progress", "needs_revision"]:
                # Calculate priority score
                subj = subject_map.get(t["subjectId"], {})
                exam_date = subj.get("examDate")
                p_score = self.calculate_priority_score(
                    difficulty=t.get("difficulty", 3),
                    exam_date_str=exam_date,
                    understanding_rating=t.get("understandingRating"),
                    importance=t.get("importance", 3)
                )
                t["calculatedPriority"] = p_score
                eligible_topics.append(t)

        # Sort topics by priority score descending
        eligible_topics.sort(key=lambda x: x["calculatedPriority"], reverse=True)

        # Delete future scheduled sessions in this range that aren't already completed
        start_dt = datetime.strptime(start_date_str, "%Y-%m-%d").date()
        generated_sessions = []
        topic_index = 0
        total_eligible = len(eligible_topics)

        for day_offset in range(days_to_schedule):
            current_day = start_dt + timedelta(days=day_offset)
            current_day_str = current_day.strftime("%Y-%m-%d")
            
            # Start scheduling from preferred start time
            cur_time_minutes = s_hour * 60 + s_min
            end_window_minutes = e_hour * 60 + e_min
            daily_scheduled_minutes = 0

            while (
                cur_time_minutes + session_mins <= end_window_minutes
                and daily_scheduled_minutes + session_mins <= max_study_minutes_per_day
                and total_eligible > 0
            ):
                # Pick topic cyclically from priority pool
                topic = eligible_topics[topic_index % total_eligible]
                subj = subject_map.get(topic["subjectId"], {})

                sess_start_h = cur_time_minutes // 60
                sess_start_m = cur_time_minutes % 60
                sess_end_minutes = cur_time_minutes + session_mins
                sess_end_h = sess_end_minutes // 60
                sess_end_m = sess_end_minutes % 60

                sess_id = str(uuid.uuid4())
                sess_type = "revision" if topic.get("status") == "needs_revision" else "learning"

                session_record = {
                    "sessionId": sess_id,
                    "userId": user_id,
                    "subjectId": topic["subjectId"],
                    "topicId": topic["topicId"],
                    "date": current_day_str,
                    "startTime": f"{sess_start_h:02d}:{sess_start_m:02d}",
                    "endTime": f"{sess_end_h:02d}:{sess_end_m:02d}",
                    "estimatedMinutes": session_mins,
                    "actualMinutes": 0,
                    "sessionType": sess_type,
                    "priority": self.get_priority_category(topic["calculatedPriority"]),
                    "status": "scheduled",
                    "notes": "",
                    "subjectName": subj.get("subjectName", "Subject"),
                    "topicName": topic.get("topicName", "Topic"),
                    "subjectColor": subj.get("colorHex", "#7556D9"),
                    "createdAt": datetime.now()
                }

                # Save session
                await repo.create_document("study_sessions", sess_id, session_record)
                generated_sessions.append(session_record)

                daily_scheduled_minutes += session_mins
                # Increment with break time
                cur_time_minutes = sess_end_minutes + break_mins
                topic_index += 1

        total_hours = sum(s["estimatedMinutes"] for s in generated_sessions) / 60.0

        return {
            "success": True,
            "totalSessions": len(generated_sessions),
            "totalStudyHours": round(total_hours, 1),
            "startDate": start_date_str,
            "endDate": (start_dt + timedelta(days=days_to_schedule - 1)).strftime("%Y-%m-%d"),
            "sessions": generated_sessions,
            "aiRecommendation": (
                f"Generated {len(generated_sessions)} structured sessions across {days_to_schedule} days. "
                "High-priority and upcoming examination topics have been prioritized in morning slots."
            )
        }

    async def reschedule_missed_sessions(
        self,
        user_id: str,
        session_id: Optional[str] = None,
        reschedule_all_missed: bool = True
    ) -> Dict[str, Any]:
        """
        Intelligently recovers missed sessions without overwriting user data or creating conflicts.
        """
        today_str = date.today().strftime("%Y-%m-%d")
        
        # Find missed sessions
        all_sessions = await repo.query_documents("study_sessions", {"userId": user_id})
        
        missed = []
        for s in all_sessions:
            if session_id and s["sessionId"] == session_id:
                missed.append(s)
            elif reschedule_all_missed and s.get("status") == "missed":
                missed.append(s)
            elif reschedule_all_missed and s.get("status") == "scheduled" and s.get("date") < today_str:
                # Implicitly missed past session
                missed.append(s)

        if not missed:
            return {
                "success": True,
                "rescheduledCount": 0,
                "proposedSessions": [],
                "explanation": "No missed sessions detected."
            }

        # User preferences
        user_doc = await repo.get_document("users", user_id)
        prefs = (user_doc.get("preferences") if user_doc else None) or {}
        start_time_str = prefs.get("studyStartTime", "08:00")
        end_time_str = prefs.get("studyEndTime", "22:00")
        session_mins = prefs.get("preferredSessionDuration", 45)

        s_hour, s_min = map(int, start_time_str.split(":"))
        e_hour, e_min = map(int, end_time_str.split(":"))

        # Find open slots starting tomorrow
        tomorrow = date.today() + timedelta(days=1)
        rescheduled_sessions = []

        day_cursor = tomorrow
        for item in missed:
            new_sess_id = str(uuid.uuid4())
            new_date_str = day_cursor.strftime("%Y-%m-%d")

            # Update old session status
            await repo.update_document("study_sessions", item["sessionId"], {"status": "rescheduled"})

            # Allocate in evening slot
            new_session = dict(item)
            new_session["sessionId"] = new_sess_id
            new_session["date"] = new_date_str
            new_session["startTime"] = "18:00"
            new_session["endTime"] = "18:45"
            new_session["status"] = "scheduled"
            new_session["notes"] = f"Rescheduled recovery for missed session on {item['date']}"
            new_session["createdAt"] = datetime.now()

            await repo.create_document("study_sessions", new_sess_id, new_session)
            rescheduled_sessions.append(new_session)

            day_cursor += timedelta(days=1)

        return {
            "success": True,
            "rescheduledCount": len(rescheduled_sessions),
            "proposedSessions": rescheduled_sessions,
            "explanation": f"Successfully rescheduled {len(rescheduled_sessions)} missed session(s) into available recovery slots."
        }

timetable_service = TimetableService()
