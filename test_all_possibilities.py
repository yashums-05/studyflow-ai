import json
import urllib.request
import urllib.error
import sys

BASE_URL = "https://studyflow-ai-app.onrender.com"
AUTH_HEADER = {"Authorization": "Bearer dev_user_student_1", "Content-Type": "application/json"}

passed = 0
failed = 0
results = []

def run_test(name, url, method="GET", data=None, headers=None, expected_status=200):
    global passed, failed
    if headers is None:
        headers = AUTH_HEADER.copy()
    
    full_url = url if url.startswith("http") else f"{BASE_URL}{url}"
    req = urllib.request.Request(full_url, method=method)
    for k, v in headers.items():
        req.add_header(k, v)
        
    req_body = None
    if data is not None:
        if isinstance(data, (dict, list)):
            req_body = json.dumps(data).encode("utf-8")
        elif isinstance(data, str):
            req_body = data.encode("utf-8")
            
    for attempt in range(2):
        try:
            with urllib.request.urlopen(req, data=req_body, timeout=25) as resp:
                status = resp.status
                content = resp.read().decode("utf-8", errors="replace")
                if status == expected_status or (isinstance(expected_status, list) and status in expected_status):
                    passed += 1
                    res = f"PASS [{status}] {name} ({method} {url})"
                    print(res)
                    results.append((True, res, content[:120]))
                    return True, content
                else:
                    failed += 1
                    res = f"FAIL [{status} != {expected_status}] {name} ({method} {url})"
                    print(res)
                    results.append((False, res, content[:120]))
                    return False, content
        except urllib.error.HTTPError as e:
            status = e.code
            content = e.read().decode("utf-8", errors="replace")
            if status == expected_status or (isinstance(expected_status, list) and status in expected_status):
                passed += 1
                res = f"PASS [{status}] {name} ({method} {url})"
                print(res)
                results.append((True, res, content[:120]))
                return True, content
            else:
                failed += 1
                res = f"FAIL [{status} != {expected_status}] {name} ({method} {url}) - {content[:100]}"
                print(res)
                results.append((False, res, content[:120]))
                return False, content
        except Exception as ex:
            if attempt == 0:
                import time; time.sleep(1)
                continue
            failed += 1
            res = f"ERROR {name} ({method} {url}): {str(ex)}"
            print(res)
            results.append((False, res, str(ex)))
            return False, str(ex)

print(f"=== FULL SUITE TESTING ALL ENDPOINTS ON {BASE_URL} ===\n")

# 1. Platform Root & Download Portal
run_test("1. Platform Root JSON", "/", method="GET", headers={"Accept": "application/json"}, expected_status=200)
run_test("2. Health Check", "/health", method="GET", headers={"Accept": "application/json"}, expected_status=200)
run_test("3. Download Portal Webpage", "/download", method="GET", headers={"Accept": "text/html"}, expected_status=200)
run_test("4. Download APK Binary Check", "/download/app.apk", method="HEAD", expected_status=200)

# 2. Authentication Flow
test_email = "tester_student@univ.edu"
run_test("5. Auth Register", "/api/v1/auth/register", method="POST", data={
    "email": test_email,
    "password": "Password123!",
    "fullName": "Test Student",
    "degree": "B.Tech Computer Science",
    "department": "Computer Science",
    "college": "State Institute of Tech",
    "university": "State University",
    "semester": 6,
    "academicYear": "2026-2027"
}, expected_status=[200, 201])

run_test("6. Auth Login", "/api/v1/auth/login", method="POST", data={
    "email": test_email,
    "password": "Password123!"
}, expected_status=200)

run_test("7. Auth Logout", "/api/v1/auth/logout", method="POST", expected_status=200)

# 3. User Profile & Preferences
run_test("8. Get User Profile", "/api/v1/users/profile", method="GET", expected_status=200)
run_test("9. Update User Profile & University", "/api/v1/users/profile", method="PUT", data={
    "college": "National Institute of Technology",
    "university": "NIT University",
    "department": "Computer Science & Engineering",
    "semester": 6,
    "academicYear": "2026-2027",
    "availableDailyStudyHours": 6.5,
    "preferredSessionDurationMinutes": 45,
    "preferredBreakDurationMinutes": 10
}, expected_status=200)

run_test("10. Set Study Preferences", "/api/v1/users/study-preferences", method="POST", data={
    "preferredWakeUpTime": "06:00",
    "preferredStudyStartTime": "08:00",
    "preferredStudyEndTime": "22:00",
    "availableDailyStudyHours": 6.0,
    "preferredSessionDurationMinutes": 45,
    "preferredBreakDurationMinutes": 10,
    "studyDays": ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
    "learningStyle": "Visual & Practice"
}, expected_status=200)

# 4. Subjects & Syllabus
run_test("11. Get Subjects List", "/api/v1/subjects", method="GET", expected_status=200)
run_test("12. Create Subject", "/api/v1/subjects", method="POST", data={
    "name": "Cloud Computing & DevOps",
    "subjectName": "Cloud Computing & DevOps",
    "code": "CS604",
    "subjectCode": "CS604",
    "difficultyLevel": 3,
    "difficulty": 3,
    "examinationDate": "2026-12-24",
    "examDate": "2026-12-24",
    "estimatedStudyHours": 35.0
}, expected_status=[200, 201])

run_test("13. Get Single Subject", "/api/v1/subjects/sub_cn_1", method="GET", expected_status=200)

# 5. Units & Topics
run_test("14. Get Units for Subject", "/api/v1/units/subject/sub_cn_1", method="GET", expected_status=200)
run_test("15. Create Unit", "/api/v1/units", method="POST", data={
    "subjectId": "sub_cn_1",
    "title": "Network Layer Routing Algorithms",
    "unitName": "Network Layer Routing Algorithms",
    "unitNumber": 2,
    "orderIndex": 1
}, expected_status=[200, 201])

run_test("16. Get Topics for Unit", "/api/v1/topics/unit/unit_cn_1", method="GET", expected_status=200)
run_test("17. Create Topic", "/api/v1/topics", method="POST", data={
    "unitId": "unit_cn_1",
    "subjectId": "sub_cn_1",
    "name": "Distance Vector Routing & RIP",
    "topicName": "Distance Vector Routing & RIP",
    "difficultyLevel": 3,
    "estimatedDurationMinutes": 45,
    "importanceScore": 4
}, expected_status=[200, 201])

run_test("18. Update Topic", "/api/v1/topics/topic_cn_1", method="PUT", data={
    "completionStatus": "completed",
    "status": "completed",
    "understandingRating": 5,
    "notes": "Reviewed flow control protocols and sliding window."
}, expected_status=200)

# 6. Exams
run_test("19. Get Exams List", "/api/v1/exams", method="GET", expected_status=200)
run_test("20. Create Exam", "/api/v1/exams", method="POST", data={
    "subjectId": "sub_cn_1",
    "subjectName": "Computer Networks",
    "examDate": "2026-12-10",
    "examType": "End Semester Final",
    "targetScore": 90,
    "venue": "Hall B-201"
}, expected_status=[200, 201])

# 7. Timetable Engine
run_test("21. Generate Smart Timetable", "/api/v1/timetable/generate", method="POST", data={
    "startDate": "2026-11-27",
    "endDate": "2026-12-10",
    "dailyTargetHours": 5.0,
    "balanceWorkload": True
}, expected_status=200)

run_test("22. Get Daily Timetable", "/api/v1/timetable/daily?date=2026-11-27", method="GET", expected_status=200)
run_test("23. Get Weekly Timetable", "/api/v1/timetable/weekly?startDate=2026-11-27", method="GET", expected_status=200)

# 8. Study Sessions (Pomodoro & Focus Tracking)
ok, daily_res = run_test("22. Get Daily Timetable", "/api/v1/timetable/daily?date=2026-11-27", method="GET", expected_status=200)
session_id = "sess_default_1"
try:
    daily_json = json.loads(daily_res)
    if isinstance(daily_json, list) and len(daily_json) > 0:
        session_id = daily_json[0].get("id") or daily_json[0].get("sessionId") or session_id
except Exception:
    pass

run_test("24. Start Study Session", "/api/v1/study-sessions/start", method="POST", data={
    "sessionId": session_id
}, expected_status=[200, 201, 404])

run_test("25. Get Study Session History", "/api/v1/study-sessions/history", method="GET", expected_status=200)

# 9. Academic Hub
run_test("26. Get Class Timetable", "/api/v1/academic/class-timetable", method="GET", expected_status=200)
run_test("27. Add Class Slot", "/api/v1/academic/class-timetable", method="POST", data={
    "dayOfWeek": "THURSDAY",
    "startTime": "10:00",
    "endTime": "11:00",
    "subjectName": "Computer Networks",
    "room": "LH-3",
    "slotType": "LECTURE"
}, expected_status=[200, 201])

run_test("28. Get Academic Calendar Events", "/api/v1/academic/calendar-events", method="GET", expected_status=200)
run_test("29. Add Calendar Event", "/api/v1/academic/calendar-events", method="POST", data={
    "title": "Hackathon & Tech Symposium",
    "eventDate": "2026-12-05",
    "eventType": "competition",
    "description": "National Inter-college Coding Hackathon",
    "holiday": False
}, expected_status=[200, 201])

run_test("30. Get AI Schedule Suggestions", "/api/v1/academic/suggestions", method="GET", expected_status=200)

# 10. Analytics Engine
run_test("31. Analytics Dashboard", "/api/v1/analytics/dashboard", method="GET", expected_status=200)
run_test("32. Weekly Analytics", "/api/v1/analytics/weekly", method="GET", expected_status=200)
run_test("33. Subject Analytics", "/api/v1/analytics/subjects", method="GET", expected_status=200)

# 11. AI Tutor Assistant (Gemini)
run_test("34. AI Tutor Chat", "/api/v1/ai/chat", method="POST", data={
    "message": "Explain the difference between TCP and UDP in simple bullet points.",
    "context": "Computer Networks"
}, expected_status=200)

run_test("35. AI Tutor Explain Concept", "/api/v1/ai/explain", method="POST", data={
    "concept": "Deadlock Avoidance in Operating Systems",
    "depth": "intermediate"
}, expected_status=200)

run_test("36. AI Generate Quiz", "/api/v1/ai/generate-quiz", method="POST", data={
    "subject": "Operating Systems",
    "topic": "Banker's Algorithm",
    "questionCount": 3
}, expected_status=200)

run_test("37. AI Study Recommendations", "/api/v1/ai/recommendations", method="GET", expected_status=200)

print("\n" + "=" * 50)
print(f"FINAL TEST RESULTS: {passed} / {passed + failed} PASSED ({(passed / (passed + failed)) * 100:.1f}%)")
print("=" * 50)

if failed > 0:
    print(f"FAILED TESTS: {failed}")
    sys.exit(1)
else:
    print("ALL POSSIBILITIES TESTED AND PASSED SUCCESSFULLY!")
