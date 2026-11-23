# StudyMate AI - Setup & Execution Guide

## 1. Backend Setup (FastAPI + Python 3.11)

### Prerequisites
- Python 3.11 installed.

### Installation & Execution Steps
1. Open PowerShell and navigate to the backend directory:
   ```powershell
   cd "d:\AI app\studymate_ai\backend"
   ```

2. Activate the virtual environment:
   ```powershell
   .\venv\Scripts\Activate.ps1
   ```

3. Configure Environment Variables:
   Copy `.env.example` to `.env`:
   ```powershell
   cp .env.example .env
   ```
   *Optional*: Set `GEMINI_API_KEY="your_api_key"` (obtainable from [Google AI Studio](https://aistudio.google.com/)). If unset, the backend automatically uses built-in high-quality academic models.

4. Run the FastAPI development server:
   ```powershell
   $env:PYTHONPATH = "d:\AI app\studymate_ai\backend"
   .\venv\Scripts\python.exe -m uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
   ```

5. Access Swagger API Documentation:
   Open browser at [http://localhost:8000/docs](http://localhost:8000/docs).

---

## 2. Firebase Configuration

1. Go to the [Firebase Console](https://console.firebase.google.com/) and create a project called `studymate-ai`.
2. Enable **Authentication** (Email/Password and Google Sign-In).
3. Enable **Cloud Firestore** in Production mode.
4. Deploy Firestore security rules:
   ```bash
   firebase deploy --only firestore:rules
   ```
   (Rules file located at `studymate_ai/firebase/firestore.rules`).
5. Deploy Firestore indexes:
   ```bash
   firebase deploy --only firestore:indexes
   ```
   (Indexes file located at `studymate_ai/firebase/firestore.indexes.json`).
6. Generate a private key JSON from **Project Settings $\to$ Service Accounts** and save path in `FIREBASE_CREDENTIALS_PATH`.

---

## 3. Mobile Frontend Setup (Flutter for Android)

### Prerequisites
- Flutter SDK 3.19+ and Android Studio with Android SDK API 34.

### Execution Steps
1. Navigate to the mobile directory:
   ```bash
   cd "d:\AI app\studymate_ai\mobile"
   ```
2. Fetch dependencies:
   ```bash
   flutter pub get
   ```
3. Run the app on an Android Emulator or connected physical device:
   ```bash
   flutter run -d android
   ```
4. Build the Release APK:
   ```bash
   flutter build apk --release
   ```
   Output location: `studymate_ai/mobile/build/app/outputs/flutter-apk/app-release.apk`
