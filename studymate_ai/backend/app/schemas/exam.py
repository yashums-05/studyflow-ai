from typing import Optional
from pydantic import BaseModel
from datetime import datetime

class ExamBase(BaseModel):
    subjectId: str
    examName: str
    examDate: str # YYYY-MM-DD
    examTime: Optional[str] = "09:00" # HH:MM
    examType: str = "Semester Final" # Semester Final, Midterm, Unit Test, Quiz
    targetScore: Optional[float] = 90.0
    notes: Optional[str] = ""

class ExamCreate(ExamBase):
    pass

class ExamUpdate(BaseModel):
    examName: Optional[str] = None
    examDate: Optional[str] = None
    examTime: Optional[str] = None
    examType: Optional[str] = None
    targetScore: Optional[float] = None
    notes: Optional[str] = None

class ExamResponse(ExamBase):
    examId: str
    userId: str
    subjectName: Optional[str] = ""
    subjectCode: Optional[str] = ""
    subjectColor: Optional[str] = "#7556D9"
    daysRemaining: int = 0
    totalTopics: int = 0
    completedTopics: int = 0
    completionPercentage: float = 0.0
    createdAt: datetime
