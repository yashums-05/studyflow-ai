# StudyMate AI - REST API Documentation

Base URL: `http://localhost:8000/api/v1` (or `http://10.0.2.2:8000/api/v1` on Android emulator)
Interactive Swagger UI: `http://localhost:8000/docs`

---

## Authentication Header
All protected endpoints require a Bearer token:
```http
Authorization: Bearer <firebase_id_token_or_dev_token>
```

---

## Endpoints Summary

### 1. User Profile
- `POST /api/v1/users/profile` – Create or initialize student profile & preferences
- `GET /api/v1/users/profile` – Retrieve authenticated user profile
- `PUT /api/v1/users/profile` – Update study preferences and academic details

### 2. Subjects
- `POST /api/v1/subjects` – Add new academic subject
- `GET /api/v1/subjects` – List all subjects for current student with syllabus completion %
- `GET /api/v1/subjects/{subject_id}` – Get subject with nested units & topics
- `PUT /api/v1/subjects/{subject_id}` – Update subject details
- `DELETE /api/v1/subjects/{subject_id}` – Cascade delete subject and all nested topics

### 3. Units & Topics
- `POST /api/v1/units` – Add syllabus unit/module
- `POST /api/v1/topics` – Add syllabus topic (automatically calculates Priority Score $P$)
- `PUT /api/v1/topics/{topic_id}` – Update topic completion status or understanding rating
- `DELETE /api/v1/topics/{topic_id}` – Delete topic

### 4. Timetable
- `POST /api/v1/timetable/generate` – Generate intelligent study schedule using priority formula and constraint solver
- `GET /api/v1/timetable/daily?date=YYYY-MM-DD` – Retrieve daily scheduled study sessions
- `GET /api/v1/timetable/weekly?start_date=YYYY-MM-DD` – Retrieve weekly study sessions
- `POST /api/v1/timetable/reschedule` – Adaptively recover missed sessions into open recovery slots
- `PUT /api/v1/timetable/sessions/{session_id}` – Manually edit session start/end or priority
- `DELETE /api/v1/timetable/sessions/{session_id}` – Cancel session

### 5. Study Sessions & Focus
- `POST /api/v1/study-sessions/start` – Mark session in-progress
- `POST /api/v1/study-sessions/{session_id}/complete` – Complete session, record actual minutes & understanding rating (1-5)
- `POST /api/v1/study-sessions/{session_id}/pause` – Pause active session
- `GET /api/v1/study-sessions/history` – Retrieve completed session log

### 6. AI Academic Tutor
- `POST /api/v1/ai/chat` – Conversational academic tutor (Gemini 1.5 Flash)
- `POST /api/v1/ai/explain` – Deep concept explanation with real-world examples and exam tips
- `POST /api/v1/ai/generate-quiz` – Generate academic MCQs with answer verification
- `POST /api/v1/ai/recommendations` – Personalized study suggestions based on student weakness
- `POST /api/v1/ai/analyze-syllabus` – Extract subject outlines from raw text/PDF

### 7. Examinations
- `POST /api/v1/exams` – Schedule examination with date, target score, and countdown
- `GET /api/v1/exams` – List examinations ordered by nearest date
- `DELETE /api/v1/exams/{exam_id}` – Remove examination

### 8. Analytics & Dashboards
- `GET /api/v1/analytics/dashboard` – Overall summary, streak days, planned vs actual hours
- `GET /api/v1/analytics/weekly` – 7-day planned vs actual hours breakdown
- `GET /api/v1/analytics/subjects` – Subject-wise mastery, strongest topics & weakness alerts

### 9. PDF Documents & RAG
- `POST /api/v1/documents/upload` – Upload syllabus or lecture notes PDF (15MB limit)
- `GET /api/v1/documents` – List uploaded documents
- `POST /api/v1/documents/{document_id}/ask` – Grounded question-answering with citation context
