# StudyMate AI 🎓
> **Plan Better. Learn Smarter. Achieve More.**
> *AI-Powered Intelligent Study Planner & Academic Assistant for Android*

StudyMate AI is a complete, advanced Android mobile application and cloud backend designed for engineering, college, and university students preparing for semester finals and competitive examinations.

---

## 🌟 Key Features

1. **Intelligent Timetable Generation**:
   - Uses a deterministic constraint-based algorithm powered by the weighted priority formula:
     $$P = 0.35D + 0.30U + 0.20W + 0.15I$$
     Where $D$ = Topic Difficulty, $U$ = Exam Urgency, $W$ = Student Weakness, and $I$ = Topic Importance.
   - Enforces conflict-free time slots, respects student daily study limits, and schedules automatic Pomodoro breaks.

2. **Adaptive Rescheduling Engine**:
   - Automatically detects missed study sessions and reallocates topics into future open recovery windows without overwriting manually edited schedules.

3. **Academic AI Tutor (Google Gemini)**:
   - Context-aware chatbot tailored to engineering curricula.
   - Step-by-step numerical explanations, formula breakdowns, code logic, and concept analogies.
   - AI Quiz Generator: Dynamically generates 4-option MCQs with instant explanations.

4. **PDF Syllabus Extraction**:
   - Upload course syllabus PDFs or raw text; the AI extracts Subject, Unit, and Topic hierarchies.
   - Interactive confirmation screen allows students to review before saving.

5. **Focus Mode & Pomodoro Timer**:
   - Customizable focus duration (25m/45m/60m) and breaks (5m/10m/15m).
   - Post-session understanding rating (1 to 5 stars) that automatically updates topic mastery and priority scores in the database.

6. **Real Analytics & Weak Subject Detection**:
   - Built with FL Chart: Daily and weekly study duration trends.
   - Performance-based weakness alerts detecting subjects needing revision.

---

## 🏗️ Technology Stack

- **Mobile Frontend**: Flutter 3.x, Dart, Material Design 3, Flutter Riverpod, GoRouter, Dio, FL Chart, Flutter Local Notifications.
- **Backend**: Python 3.11, FastAPI, Pydantic v2, PyPDF, Uvicorn.
- **AI Engine**: Google Gemini API (with fallback models).
- **Database & Auth**: Firebase Firestore & Firebase Authentication with security rules.

---

## 🚀 Quick Start Guide

### 1. Launch FastAPI Backend
```powershell
cd "d:\AI app\studymate_ai\backend"
$env:PYTHONPATH = "d:\AI app\studymate_ai\backend"
.\venv\Scripts\python.exe -m uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```
Interactive API docs available at: `http://localhost:8000/docs`

### 2. Run Test Suite
```powershell
.\venv\Scripts\python.exe -m pytest tests -v
```

### 3. Launch Flutter Mobile App
```bash
cd "d:\AI app\studymate_ai\mobile"
flutter pub get
flutter run -d android
```

---

## 📁 Repository Structure
```
studymate_ai/
├── mobile/                  # Flutter Android application
│   ├── lib/
│   │   ├── core/           # Design system, theme, routing, services
│   │   ├── features/       # Auth, Dashboard, Subjects, Timetable, AI Tutor, Analytics
│   │   └── shared/         # Models and API repositories
│   ├── test/               # Flutter tests
│   └── android/            # Gradle and Android configuration
├── backend/                # FastAPI application
│   ├── app/
│   │   ├── api/routes/     # REST controllers
│   │   ├── core/           # Security & config
│   │   ├── services/       # AI, Timetable, Analytics, Syllabus services
│   │   └── repositories/   # Firestore / in-memory data store
│   └── tests/              # Backend pytest suite
├── firebase/               # Firestore security rules and index definitions
├── docs/                   # Architecture, API docs, database schema, setup guides
├── README.md
└── DEVELOPMENT_STATUS.md
```
