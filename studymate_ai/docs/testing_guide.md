# StudyMate AI - Testing Guide

## 1. Backend Automated Testing

The backend includes a test suite covering endpoints, deterministic scheduling, priority formula validation, and error boundaries.

### Executing Backend Tests
Navigate to the backend directory and execute:
```powershell
$env:PYTHONPATH = "d:\AI app\studymate_ai\backend"
.\venv\Scripts\python.exe -m pytest tests -v
```

### Test Coverage Highlights
1. **Priority Score Formula Verification**:
   - Validates that $P = 0.35D + 0.30U + 0.20W + 0.15I$ outputs $100.0$ under critical conditions and $4.5$ under lowest conditions.
2. **Deterministic Constraint Allocation**:
   - Ensures no overlapping study sessions exist.
   - Enforces maximum daily study hours.
   - Automatically inserts short and long Pomodoro breaks.
3. **Adaptive Rescheduling Engine**:
   - Missed session detection and non-destructive rescheduling into available slots without overwriting completed records.
4. **End-to-End API Workflows**:
   - User profile creation $\to$ Subject creation $\to$ Unit & Topic creation $\to$ Timetable generation $\to$ Session start & complete $\to$ Real Analytics metric updates.

---

## 2. Flutter Mobile Testing

Tests are located in `studymate_ai/mobile/test/`:
- `model_test.dart`: Validates serialization, deserialization, and null safety of all data transfer models.
- `widget_test.dart`: Validates app launch, ProviderScope initialization, and branding on the splash screen.

### Executing Mobile Tests
```bash
cd "d:\AI app\studymate_ai\mobile"
flutter test
```
