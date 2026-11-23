from datetime import date, timedelta, datetime
from typing import Dict, Any, List
from app.repositories.firestore_repository import repo
from app.services.ai_service import ai_service

class AnalyticsService:
    """
    Computes real analytics from database records:
    - Syllabus completion
    - Planned vs actual study hours
    - Study streak
    - Weak subject detection
    """

    async def get_dashboard_analytics(self, user_id: str) -> Dict[str, Any]:
        today_str = date.today().strftime("%Y-%m-%d")

        # Fetch real records from repo
        subjects = await repo.query_documents("subjects", {"userId": user_id})
        topics = await repo.query_documents("topics", {"userId": user_id})
        sessions = await repo.query_documents("study_sessions", {"userId": user_id})
        exams = await repo.query_documents("exams", {"userId": user_id})

        total_subjects = len(subjects)
        total_topics = len(topics)
        completed_topics = len([t for t in topics if t.get("status") == "completed"])
        pending_topics = total_topics - completed_topics
        overall_pct = round((completed_topics / total_topics * 100), 1) if total_topics > 0 else 0.0

        # Today's sessions
        today_sessions = [s for s in sessions if s.get("date") == today_str]
        planned_mins_today = sum(s.get("estimatedMinutes", 0) for s in today_sessions)
        completed_mins_today = sum(s.get("actualMinutes", 0) for s in today_sessions if s.get("status") == "completed")
        remaining_today = len([s for s in today_sessions if s.get("status") in ["scheduled", "in_progress"]])

        # Calculate study streak
        # Look backwards from today
        streak = 0
        cur_check = date.today()
        # Create map of completed minutes by date
        completed_dates = set(s.get("date") for s in sessions if s.get("status") == "completed")
        
        while cur_check.strftime("%Y-%m-%d") in completed_dates or (cur_check == date.today() and streak == 0):
            if cur_check.strftime("%Y-%m-%d") in completed_dates:
                streak += 1
            cur_check -= timedelta(days=1)
            if streak > 365:
                break

        # Weekly total
        seven_days_ago = (date.today() - timedelta(days=7)).strftime("%Y-%m-%d")
        weekly_sessions = [s for s in sessions if s.get("date", "") >= seven_days_ago and s.get("status") == "completed"]
        weekly_hours = round(sum(s.get("actualMinutes", 0) for s in weekly_sessions) / 60.0, 1)

        # Weak topics
        weak_topics = [t.get("topicName") for t in topics if t.get("understandingRating") and t.get("understandingRating") <= 2]

        user_doc = await repo.get_document("users", user_id)
        user_name = user_doc.get("fullName", "Student") if user_doc else "Student"

        # AI Recommendations based on real progress
        recommendations = await ai_service.get_study_recommendations(
            user_name=user_name,
            total_topics=total_topics,
            completed_topics=completed_topics,
            upcoming_exams=exams,
            weak_topics=weak_topics
        )

        return {
            "totalSubjects": total_subjects,
            "totalTopics": total_topics,
            "completedTopics": completed_topics,
            "pendingTopics": pending_topics,
            "overallCompletionPercentage": overall_pct,
            "plannedStudyHoursToday": round(planned_mins_today / 60.0, 1),
            "completedStudyHoursToday": round(completed_mins_today / 60.0, 1),
            "remainingSessionsToday": remaining_today,
            "studyStreakDays": streak,
            "weeklyTotalStudyHours": weekly_hours,
            "upcomingExamsCount": len(exams),
            "aiRecommendations": recommendations
        }

    async def get_weekly_analytics(self, user_id: str) -> Dict[str, Any]:
        today = date.today()
        start_of_week = today - timedelta(days=today.weekday()) # Monday

        sessions = await repo.query_documents("study_sessions", {"userId": user_id})
        
        days_breakdown = []
        total_week_actual_mins = 0
        total_week_planned_mins = 0

        day_names = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
        for i in range(7):
            day_dt = start_of_week + timedelta(days=i)
            day_str = day_dt.strftime("%Y-%m-%d")
            
            day_sessions = [s for s in sessions if s.get("date") == day_str]
            planned = sum(s.get("estimatedMinutes", 0) for s in day_sessions) / 60.0
            actual = sum(s.get("actualMinutes", 0) for s in day_sessions if s.get("status") == "completed") / 60.0

            total_week_planned_mins += planned * 60
            total_week_actual_mins += actual * 60

            days_breakdown.append({
                "date": day_str,
                "dayName": day_names[i],
                "plannedHours": round(planned, 1),
                "actualHours": round(actual, 1)
            })

        ratio = round((total_week_actual_mins / total_week_planned_mins), 2) if total_week_planned_mins > 0 else 1.0

        return {
            "totalHoursThisWeek": round(total_week_actual_mins / 60.0, 1),
            "dailyBreakdown": days_breakdown,
            "plannedVsActualRatio": ratio,
            "studyStreak": 1 if total_week_actual_mins > 0 else 0
        }

    async def get_subject_analytics(self, user_id: str) -> Dict[str, Any]:
        subjects = await repo.query_documents("subjects", {"userId": user_id})
        topics = await repo.query_documents("topics", {"userId": user_id})
        sessions = await repo.query_documents("study_sessions", {"userId": user_id})

        items = []
        strongest = []
        needing_improvement = []

        for s in subjects:
            s_id = s["subjectId"]
            s_topics = [t for t in topics if t.get("subjectId") == s_id]
            s_sessions = [sess for sess in sessions if sess.get("subjectId") == s_id and sess.get("status") == "completed"]

            total_t = len(s_topics)
            completed_t = len([t for t in s_topics if t.get("status") == "completed"])
            pct = round((completed_t / total_t * 100), 1) if total_t > 0 else 0.0
            total_hrs = round(sum(sess.get("actualMinutes", 0) for sess in s_sessions) / 60.0, 1)

            # Check ratings
            ratings = [t.get("understandingRating") for t in s_topics if t.get("understandingRating")]
            avg_rating = (sum(ratings) / len(ratings)) if ratings else 3.0

            items.append({
                "subjectId": s_id,
                "subjectName": s.get("subjectName", "Subject"),
                "colorHex": s.get("colorHex", "#7556D9"),
                "totalTopics": total_t,
                "completedTopics": completed_t,
                "completionPercentage": pct,
                "totalStudyHours": total_hrs
            })

            if pct >= 70 or avg_rating >= 4.0:
                strongest.append(s.get("subjectName"))
            elif avg_rating <= 2.5 or (total_t > 0 and pct < 30):
                needing_improvement.append({
                    "subjectName": s.get("subjectName"),
                    "reason": f"Average understanding rating is {avg_rating:.1f}/5.0 with {total_t - completed_t} topics pending."
                })

        return {
            "subjects": items,
            "strongestSubjects": strongest,
            "subjectsNeedingImprovement": needing_improvement
        }

analytics_service = AnalyticsService()
