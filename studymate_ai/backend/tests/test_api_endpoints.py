import pytest
from httpx import AsyncClient, ASGITransport
from datetime import date, timedelta
from app.main import app

HEADERS = {
    "Authorization": "Bearer dev_user_student_1"
}

@pytest.mark.asyncio
async def test_health_and_root():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        res = await ac.get("/health")
        assert res.status_code == 200
        data = res.json()
        assert data["status"] == "healthy"

        root_res = await ac.get("/")
        assert root_res.status_code == 200
        assert root_res.json()["status"] == "online"

@pytest.mark.asyncio
async def test_user_profile_crud():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        # Create profile
        profile_data = {
            "fullName": "Alex Mercer",
            "email": "alex.mercer@univ.edu",
            "college": "MIT Engineering",
            "degree": "B.Tech",
            "department": "Computer Science",
            "semester": 6,
            "academicYear": "2024-2025",
            "preferences": {
                "studyStartTime": "07:00",
                "studyEndTime": "21:00",
                "maximumDailyStudyHours": 5.0,
                "preferredSessionDuration": 45,
                "shortBreakDuration": 10
            }
        }
        res = await ac.post("/api/v1/users/profile", json=profile_data, headers=HEADERS)
        assert res.status_code == 200
        assert res.json()["fullName"] == "Alex Mercer"

        # Get profile
        get_res = await ac.get("/api/v1/users/profile", headers=HEADERS)
        assert get_res.status_code == 200
        assert get_res.json()["college"] == "MIT Engineering"

@pytest.mark.asyncio
async def test_subjects_and_topics_flow():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        # Create subject
        subj_payload = {
            "subjectName": "Operating Systems",
            "subjectCode": "CS302",
            "semester": 6,
            "difficulty": 4,
            "colorHex": "#7556D9",
            "examDate": (date.today() + timedelta(days=14)).strftime("%Y-%m-%d"),
            "estimatedHours": 30.0
        }
        s_res = await ac.post("/api/v1/subjects", json=subj_payload, headers=HEADERS)
        assert s_res.status_code == 200
        s_data = s_res.json()
        subj_id = s_data["subjectId"]

        # Create unit
        unit_payload = {
            "subjectId": subj_id,
            "unitName": "Unit 1: Process Management & CPU Scheduling",
            "order": 1
        }
        u_res = await ac.post("/api/v1/units", json=unit_payload, headers=HEADERS)
        assert u_res.status_code == 200
        unit_id = u_res.json()["unitId"]

        # Create topic
        topic_payload = {
            "subjectId": subj_id,
            "unitId": unit_id,
            "topicName": "Round Robin and Priority Scheduling",
            "difficulty": 4,
            "estimatedMinutes": 45,
            "importance": 5,
            "status": "not_started",
            "understandingRating": 2
        }
        t_res = await ac.post("/api/v1/topics", json=topic_payload, headers=HEADERS)
        assert t_res.status_code == 200
        topic_id = t_res.json()["topicId"]
        assert t_res.json()["priorityScore"] > 0

        # Create Exam
        exam_payload = {
            "subjectId": subj_id,
            "examName": "OS Mid-Term Examination",
            "examDate": (date.today() + timedelta(days=14)).strftime("%Y-%m-%d"),
            "examTime": "10:00",
            "examType": "Midterm",
            "targetScore": 95.0
        }
        e_res = await ac.post("/api/v1/exams", json=exam_payload, headers=HEADERS)
        assert e_res.status_code == 200
        assert e_res.json()["subjectName"] == "Operating Systems"

        # Generate Timetable
        tt_payload = {
            "startDate": date.today().strftime("%Y-%m-%d"),
            "daysToSchedule": 3,
            "dailyAvailableHours": 4.0,
            "includeRevisions": True
        }
        tt_res = await ac.post("/api/v1/timetable/generate", json=tt_payload, headers=HEADERS)
        assert tt_res.status_code == 200
        tt_data = tt_res.json()
        assert tt_data["success"] is True
        assert len(tt_data["sessions"]) > 0

        first_session = tt_data["sessions"][0]
        session_id = first_session["sessionId"]

        # Start session
        start_res = await ac.post("/api/v1/study-sessions/start", json={"sessionId": session_id}, headers=HEADERS)
        assert start_res.status_code == 200
        assert start_res.json()["status"] == "in_progress"

        # Complete session
        complete_res = await ac.post(
            f"/api/v1/study-sessions/{session_id}/complete",
            json={"actualMinutes": 45, "understandingRating": 4, "notes": "Understood Round Robin quantum tradeoffs."},
            headers=HEADERS
        )
        assert complete_res.status_code == 200
        assert complete_res.json()["status"] == "completed"

        # Check Analytics
        analytics_res = await ac.get("/api/v1/analytics/dashboard", headers=HEADERS)
        assert analytics_res.status_code == 200
        dash = analytics_res.json()
        assert dash["totalSubjects"] >= 1
        assert dash["completedTopics"] >= 1

@pytest.mark.asyncio
async def test_ai_endpoints():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        # Chat
        chat_res = await ac.post(
            "/api/v1/ai/chat",
            json={"message": "Explain Page Replacement Algorithms briefly."},
            headers=HEADERS
        )
        assert chat_res.status_code == 200
        assert "response" in chat_res.json()

        # Explain
        explain_res = await ac.post(
            "/api/v1/ai/explain",
            json={"subject": "Operating Systems", "topic": "Virtual Memory"},
            headers=HEADERS
        )
        assert explain_res.status_code == 200
        exp_data = explain_res.json()
        assert "simpleExplanation" in exp_data
        assert "detailedExplanation" in exp_data

        # Quiz Generation
        quiz_res = await ac.post(
            "/api/v1/ai/generate-quiz",
            json={"subject": "Operating Systems", "topic": "Deadlock Detection", "numQuestions": 3},
            headers=HEADERS
        )
        assert quiz_res.status_code == 200
        quiz_data = quiz_res.json()
        assert len(quiz_data["questions"]) >= 1
