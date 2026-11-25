# StudyMate AI - System Architecture

## 1. Architectural Overview

StudyMate AI is engineered as a decoupled, multi-tier intelligent study planner application designed specifically for Android:

```
[ Android Mobile Device (Flutter + Material 3) ]
                     │
                     ▼ HTTPS / REST (Dio + Bearer Token)
[ FastAPI Backend (Python 3.11) ]
     │               │                 │
     ▼               ▼                 ▼
[ Firebase Auth ] [ Firestore DB ] [ Google Gemini 1.5 Flash ]
```

---

## 2. Component Design

### A. Mobile Frontend (Flutter / Dart)
- **State Management**: Flutter Riverpod with immutable `AsyncValue` models for clean reactivity and testability.
- **Navigation**: `GoRouter` declarative routing supporting deep linking and state persistence across bottom navigation tabs.
- **Design System**: Material Design 3 theme with lavender/purple primary palette (`#7556D9`), rounded cards (16dp radius), responsive typography, and light/dark theme modes.
- **Analytics & Visualizations**: `fl_chart` for study trend bar charts and `percent_indicator` for syllabus completion circular indicators.
- **Local Reminders**: `flutter_local_notifications` for instant alerts on upcoming sessions, study streaks, and exam countdowns.

### B. Backend Engine (FastAPI)
- **Security & Authorization**: Firebase Token authentication middleware deriving user identity strictly from verified token claims (`uid`).
- **Data Access Layer**: Clean repository pattern abstracting cloud Firestore with in-memory fallback for offline testing and local CI.
- **Deterministic Scheduling Engine**:
  - Implements the weighted priority scoring formula:
    $$P = 0.35D + 0.30U + 0.20W + 0.15I$$
  - Greedy constraint-based slot allocator ensuring sessions respect daily study windows (`preferredStudyStart` to `preferredStudyEnd`), maximum daily study limits, and automatic Pomodoro break insertions (short breaks of 10 min and long breaks).
- **Adaptive Rescheduling Engine**:
  - Detects missed study sessions, preserves manual schedule modifications, recalculates urgency, and non-destructively reallocates missed topics into future available slots.

### C. AI Integration Pipeline (Google Gemini API)
- **Academic Chatbot**: System prompt configured to deliver structured, concept-driven explanations tailored to undergraduate curriculum.
- **Quiz Generator**: Generates 4-option MCQs with unambiguous correct answers and detailed explanations.
- **Syllabus Extractor**: Parses syllabus text/PDF into organized Subject $\to$ Unit $\to$ Topic hierarchies with user confirmation.
- **RAG & Document Assistant**: Grounds answers in student-uploaded PDF materials.
