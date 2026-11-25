# StudyMate AI — Development Status

**Last Updated:** 2026-11-23  
**Backend Status:** ✅ RUNNING on `http://localhost:8000`  
**Flutter Status:** ⚠️ Code Complete — Awaiting Flutter SDK Installation

---

## Phase Summary

| Phase | Description | Status |
|-------|-------------|--------|
| 1 | Project setup & architecture | ✅ Complete |
| 2 | Firebase configuration files | ✅ Complete |
| 3 | Python FastAPI backend | ✅ Complete + Tested |
| 4 | Flutter mobile UI — Auth & Onboarding | ✅ Complete |
| 5 | Flutter mobile UI — Dashboard & Navigation | ✅ Complete |
| 6 | Flutter mobile UI — Subjects & Syllabus | ✅ Complete |
| 7 | AI Timetable Generator (backend) | ✅ Complete + Tested |
| 8 | Focus Timer / Pomodoro | ✅ Complete (bug-fixed) |
| 9 | AI Tutor Chat + Quiz | ✅ Complete |
| 10 | Analytics Screen | ✅ Complete (real API data wired) |
| 11 | Exams Screen | ✅ Complete (new) |
| 12 | Session History Screen | ✅ Complete (new) |
| 13 | Flutter compilation / APK | ⚠️ Blocked — Flutter SDK not installed |

---

## Backend (Python FastAPI) ✅

**Server:** Running at `http://localhost:8000` (daemon task)  
**API Docs (Swagger):** `http://localhost:8000/docs`  
**Tests:** 6/6 PASSED  

### Endpoints Verified Live

| Endpoint | Method | Status |
|----------|--------|--------|
| `/health` | GET | ✅ |
| `/api/v1/users/me` | GET/PUT | ✅ |
| `/api/v1/subjects` | GET/POST | ✅ |
| `/api/v1/units` | POST | ✅ |
| `/api/v1/topics` | GET/POST/PUT | ✅ |
| `/api/v1/timetable/generate` | POST | ✅ |
| `/api/v1/timetable/daily` | GET | ✅ |
| `/api/v1/timetable/reschedule-missed` | POST | ✅ |
| `/api/v1/study-sessions/start` | POST | ✅ |
| `/api/v1/study-sessions/{id}/complete` | POST | ✅ |
| `/api/v1/study-sessions/history` | GET | ✅ |
| `/api/v1/exams` | GET/POST/DELETE | ✅ |
| `/api/v1/analytics/dashboard` | GET | ✅ |
| `/api/v1/analytics/weekly` | GET | ✅ |
| `/api/v1/ai/chat` | POST | ✅ |
| `/api/v1/ai/explain` | POST | ✅ |
| `/api/v1/ai/quiz` | POST | ✅ |
| `/api/v1/documents/upload` | POST | ✅ |

### Auth Mode
- **Dev mode:** Any token starting with `dev_user_` is accepted without Firebase (safe for local testing)
- **Production:** Set `ALLOW_DEV_AUTH=False` and configure `FIREBASE_CREDENTIALS_PATH`

---

## Flutter Mobile (Dart/Flutter) ✅ Code Complete

**All files are written. Compilation blocked by missing Flutter SDK.**

### Screens Implemented

| Screen | Status | Notes |
|--------|--------|-------|
| Splash Screen | ✅ | Auto-navigates after 2s |
| Welcome Screen | ✅ | Login / Register entry |
| Login Screen | ✅ | Email+password, dev token |
| Register Screen | ✅ | Full validation |
| Forgot Password | ✅ | Bug fixed (Border.all) |
| Academic Profile Setup | ✅ | Onboarding step 1 |
| Study Preferences | ✅ | Onboarding step 2 |
| Main Shell (5 tabs) | ✅ | Bottom nav, state preserved |
| Home Dashboard | ✅ | Real API data, quick actions |
| Subjects List | ✅ | Search, add, delete |
| Subject Detail | ✅ | Units, topics, progress |
| Syllabus Upload (PDF) | ✅ | Extract → confirm → save |
| Timetable Screen | ✅ | Daily + Weekly tabs |
| AI Tutor Chat | ✅ | Streaming-style chat |
| Quiz Screen | ✅ | MCQ with scoring |
| Focus Timer (Pomodoro) | ✅ | Bug fixed (~/ operator) |
| Analytics Screen | ✅ | Real data, bar chart |
| **Exams Screen** | ✅ | **NEW — Full CRUD** |
| **Session History** | ✅ | **NEW — Grouped by date** |
| Profile Screen | ✅ | Edit profile, logout |

### Bug Fixes Applied (This Session)

| File | Bug | Fix |
|------|-----|-----|
| `app_theme.dart` | `FontWeight.w640` invalid | → `FontWeight.w600` |
| `focus_timer_screen.dart` | `seconds // 60` (Python-style) | → `seconds ~/ 60` |
| `focus_timer_screen.dart` | Same error on actualMins | → `~/ 60` |
| `forgot_password_screen.dart` | `Border.parseBorder(Border.all(...))` invalid | → `Border.all(...)` |
| `study_session_model.dart` | `startTime` was String, broke history screen | → Added `DateTime get startTime` computed property |
| `exam_model.dart` | `examDate` was String, broken for `DateFormat` | → Changed to `DateTime` |
| `timetable_screen.dart` | `estimatedMinutes` removed from model | → `estimatedDurationMinutes` |
| `analytics_screen.dart` | Hardcoded bar chart data | → Wired to `GET /api/v1/analytics/weekly` |
| `home_dashboard_screen.dart` | `/timetable/generate` route missing | → Changed to `/ai-tutor` |
| `home_dashboard_screen.dart` | `e.examDate` was String | → Uses `DateFormat.format(e.examDate)` |

---

## To Run / Build

### Step 1 — Install Flutter SDK (One-Time)

```powershell
# Option A: Chocolatey (recommended)
choco install flutter -y
refreshenv

# Option B: Manual
# Download from https://flutter.dev/docs/get-started/install/windows
# Extract to C:\flutter
# Add C:\flutter\bin to PATH
```

### Step 2 — Verify Setup

```powershell
flutter doctor -v
# Must show: Flutter, Android toolchain, Android Studio
```

### Step 3 — Run the App

```powershell
# Using the setup script (auto-installs deps, analyzes, tests)
cd "D:\AI app\studymate_ai"
.\setup_and_launch.ps1

# Or manually:
cd "D:\AI app\studymate_ai\mobile"
flutter pub get
flutter analyze --no-fatal-infos
flutter test
flutter run -d android    # Needs an emulator running
```

### Step 4 — Build APK

```powershell
cd "D:\AI app\studymate_ai\mobile"
flutter build apk --debug    # Quick debug APK
flutter build apk --release  # Production APK
# Output: build\app\outputs\flutter-apk\app-release.apk
```

---

## Configuration Required Before Production

### Backend `.env`
```bash
cp "D:\AI app\studymate_ai\backend\.env.example" "D:\AI app\studymate_ai\backend\.env"
# Edit .env:
GEMINI_API_KEY=your_gemini_key_from_aistudio.google.com
FIREBASE_CREDENTIALS_PATH=path_to_serviceAccountKey.json
ALLOW_DEV_AUTH=False   # Disable dev auth in production
```

### Firebase Setup
1. Create project at [console.firebase.google.com](https://console.firebase.google.com)
2. Enable: Authentication (Email/Google) + Firestore
3. Download `google-services.json` → place in `mobile/android/app/`
4. Download service account JSON → set `FIREBASE_CREDENTIALS_PATH` in `.env`
5. Add Firebase packages to `pubspec.yaml`:
   ```yaml
   firebase_core: ^3.x.x
   firebase_auth: ^5.x.x
   cloud_firestore: ^5.x.x
   google_sign_in: ^6.x.x
   ```
6. Update `main.dart` to call `await Firebase.initializeApp(...)`

---

## Priority Score Formula (Documented)

```
Priority = 0.35×D + 0.30×U + 0.20×W + 0.15×I

Where (all normalized 0–100):
  D = Topic difficulty score
  U = Exam urgency score (days remaining → closer = higher)
  W = Student weakness score (inverse of understanding rating)
  I = Topic importance score
```

Weights are configurable in `backend/app/services/timetable_service.py` → `WEIGHTS` dict.

---

## Project Structure

```
D:\AI app\studymate_ai\
├── backend/                  # Python FastAPI backend
│   ├── app/
│   │   ├── api/routes/       # 10 route modules
│   │   ├── core/             # Auth, config, dependencies
│   │   ├── repositories/     # Firestore + in-memory fallback
│   │   ├── schemas/          # Pydantic v2 models
│   │   └── services/         # AI, timetable, analytics, notifications
│   ├── tests/                # 6 pytest tests (all passing)
│   ├── venv/                 # Python 3.11 virtualenv
│   ├── .env.example          # Template env vars
│   └── requirements.txt
├── mobile/                   # Flutter Android app
│   ├── lib/
│   │   ├── core/             # Theme, routing, services, constants
│   │   ├── features/         # 15+ feature screens
│   │   └── shared/           # Models, repositories
│   ├── android/              # Android platform config
│   ├── test/                 # Flutter unit tests
│   └── pubspec.yaml
├── firebase/
│   ├── firestore.rules       # Security rules
│   └── firestore.indexes.json
├── docs/                     # Architecture, API, DB schema docs
├── setup_and_launch.ps1      # One-click setup script
└── README.md
```
