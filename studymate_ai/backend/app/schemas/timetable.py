from typing import Optional, List
from pydantic import BaseModel, Field
from datetime import datetime

class StudySessionBase(BaseModel):
    subjectId: str
    topicId: str
    date: str # YYYY-MM-DD
    startTime: str # HH:MM
    endTime: str # HH:MM
    estimatedMinutes: int
    actualMinutes: int = 0
    sessionType: str = "learning" # learning, revision, practice, mock_test, break
    priority: str = "medium" # critical, high, medium, low
    status: str = "scheduled" # scheduled, in_progress, completed, missed, rescheduled
    notes: Optional[str] = ""

class StudySessionCreate(StudySessionBase):
    pass

class StudySessionUpdate(BaseModel):
    startTime: Optional[str] = None
    endTime: Optional[str] = None
    actualMinutes: Optional[int] = None
    sessionType: Optional[str] = None
    priority: Optional[str] = None
    status: Optional[str] = None
    notes: Optional[str] = None

class StudySessionResponse(StudySessionBase):
    sessionId: str
    userId: str
    subjectName: Optional[str] = ""
    topicName: Optional[str] = ""
    subjectColor: Optional[str] = "#7556D9"
    createdAt: datetime

class TimetableGenerateRequest(BaseModel):
    startDate: str # YYYY-MM-DD
    daysToSchedule: int = Field(ge=1, le=30, default=7)
    dailyAvailableHours: Optional[float] = None
    preferredStudyStart: Optional[str] = None # HH:MM
    preferredStudyEnd: Optional[str] = None # HH:MM
    sessionDurationMinutes: Optional[int] = None
    breakDurationMinutes: Optional[int] = None
    includeRevisions: bool = True
    examPrepMode: bool = False
    targetExamSubjectId: Optional[str] = None

class TimetableGenerateResponse(BaseModel):
    success: bool
    totalSessions: int
    totalStudyHours: float
    startDate: str
    endDate: str
    sessions: List[StudySessionResponse]
    aiRecommendation: Optional[str] = None

class RescheduleRequest(BaseModel):
    sessionId: Optional[str] = None # Specific missed session, or null to reschedule all missed
    rescheduleAllMissed: bool = False
    emergencyExamMode: bool = False

class RescheduleResponse(BaseModel):
    success: bool
    rescheduledCount: int
    proposedSessions: List[StudySessionResponse]
    explanation: str
