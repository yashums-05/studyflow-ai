# StudyFlow AI 🎓
> **Plan Better. Learn Smarter. Achieve More.**
> *AI-Powered Intelligent Study Planner & Academic Assistant for Android*

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/yashums-05/studymate-ai)

StudyFlow AI is a complete, advanced Android mobile application and cloud backend designed for engineering, college, and university students preparing for semester finals and competitive examinations.

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
- **Backend**: Java 17, Spring Boot 3.2.4, Embedded Tomcat, Spring Web, Jackson, Docker.
- **AI Engine**: Google Gemini API (with deterministic offline academic fallbacks).
- **Cloud Deployment**: Render (`onrender.com`) via Docker and Blueprint (`render.yaml`).

---

## 🚀 Cloud Deployment (Render)

### Option 1: 1-Click Blueprint Deploy
Click the button below to deploy the backend directly to Render's free tier:

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/yashums-05/studymate-ai)

### Option 2: Render Dashboard Manual Setup
1. Log in to [Render Dashboard](https://dashboard.render.com).
2. Click **New +** > **Web Service**.
3. Connect your GitHub repository `https://github.com/yashums-05/studymate-ai`.
4. Choose **Docker** as the environment:
   - **Dockerfile Path**: `backend_java/Dockerfile`
   - **Docker Context**: `backend_java`
   - **Health Check Path**: `/health`
5. Click **Deploy Web Service**.

Once deployed, your backend will be live at: `https://<service-name>.onrender.com`.

---

## 📱 Mobile App Configuration

Once your Render backend is live:
1. Open StudyFlow AI on your Android device.
2. Go to **Profile** > **Backend & Cloud Server**.
3. Enter your live Render URL: `https://<service-name>.onrender.com` (or leave empty to use local network IP).
4. Tap **Save & Verify Connection**.

---

## 📁 Repository Structure
```
├── mobile/                  # Flutter Android application
│   ├── lib/
│   │   ├── core/           # Design system, theme, routing, API constants
│   │   ├── features/       # Auth, Dashboard, Subjects, Timetable, AI Tutor, Analytics
│   │   └── shared/         # Models and API repositories
│   ├── test/               # Flutter tests
│   └── android/            # Gradle and Android configuration
├── backend_java/            # Java 17 Spring Boot 3.2.4 Backend
│   ├── src/main/java/      # Controllers, Services, Models, DTOs, Repositories
│   ├── src/main/resources/ # application.yml configuration
│   └── build.gradle        # Gradle dependencies and bootJar config
├── Dockerfile               # Root multi-stage container build for Render
├── render.yaml              # Render Blueprint deployment definition
├── docs/                    # Architecture, API docs, database schema
└── README.md
```
