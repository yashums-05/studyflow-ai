# StudyMate AI - Firebase Firestore Database Schema

## Collections Structure

### 1. `users`
- `uid`: string (Primary key, matches Firebase Auth UID)
- `fullName`: string
- `email`: string
- `photoUrl`: string (optional)
- `college`: string
- `department`: string
- `degree`: string
- `semester`: number (1-12)
- `academicYear`: string (e.g. "2024-2025")
- `preferences`: Map
  - `availableDays`: array<string>
  - `studyStartTime`: string (HH:MM)
  - `studyEndTime`: string (HH:MM)
  - `preferredSessionDuration`: number (minutes, default 45)
  - `shortBreakDuration`: number (minutes, default 10)
  - `longBreakDuration`: number (minutes, default 30)
  - `maximumDailyStudyHours`: number (default 6.0)
  - `preferredLearningStyle`: string
- `createdAt`: timestamp
- `updatedAt`: timestamp

### 2. `subjects`
- `subjectId`: string (UUID)
- `userId`: string (Indexed)
- `subjectName`: string
- `subjectCode`: string
- `semester`: number
- `difficulty`: number (1-5)
- `colorHex`: string
- `examDate`: string (YYYY-MM-DD, optional)
- `estimatedHours`: number
- `completionPercentage`: number
- `createdAt`: timestamp

### 3. `units`
- `unitId`: string (UUID)
- `subjectId`: string (Indexed)
- `userId`: string
- `unitName`: string
- `order`: number
- `createdAt`: timestamp

### 4. `topics`
- `topicId`: string (UUID)
- `userId`: string (Indexed)
- `subjectId`: string (Indexed)
- `unitId`: string (Indexed)
- `topicName`: string
- `difficulty`: number (1-5)
- `estimatedMinutes`: number
- `importance`: number (1-5)
- `status`: string (`not_started`, `in_progress`, `completed`, `needs_revision`)
- `understandingRating`: number (1-5, optional)
- `priorityScore`: number (0-100, calculated via $P = 0.35D + 0.30U + 0.20W + 0.15I$)
- `notes`: string
- `createdAt`: timestamp
- `updatedAt`: timestamp

### 5. `study_sessions`
- `sessionId`: string (UUID)
- `userId`: string (Indexed)
- `subjectId`: string
- `topicId`: string
- `date`: string (YYYY-MM-DD, Indexed)
- `startTime`: string (HH:MM)
- `endTime`: string (HH:MM)
- `estimatedMinutes`: number
- `actualMinutes`: number
- `sessionType`: string (`learning`, `revision`, `practice`, `mock_test`)
- `priority`: string (`critical`, `high`, `medium`, `low`)
- `status`: string (`scheduled`, `in_progress`, `completed`, `missed`, `rescheduled`)
- `notes`: string
- `createdAt`: timestamp

### 6. `exams`
- `examId`: string (UUID)
- `userId`: string (Indexed)
- `subjectId`: string
- `examName`: string
- `examDate`: string (YYYY-MM-DD, Indexed)
- `examTime`: string
- `examType`: string
- `targetScore`: number
- `notes`: string

### 7. `uploaded_documents`
- `documentId`: string (UUID)
- `userId`: string (Indexed)
- `fileName`: string
- `fileSize`: number
- `pageCount`: number
- `extractedSnippet`: string
- `fullText`: string
- `uploadedAt`: timestamp
