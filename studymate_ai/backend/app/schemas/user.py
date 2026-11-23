from typing import Optional, List
from pydantic import BaseModel, EmailStr, Field
from datetime import datetime

class StudyPreferences(BaseModel):
    availableDays: List[str] = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    studyStartTime: str = "08:00"  # HH:MM
    studyEndTime: str = "22:00"    # HH:MM
    preferredSessionDuration: int = 45 # minutes
    shortBreakDuration: int = 10       # minutes
    longBreakDuration: int = 30        # minutes
    maximumDailyStudyHours: float = 6.0
    preferredLearningStyle: Optional[str] = "Visual & Practice"

class UserProfileCreate(BaseModel):
    fullName: str
    email: EmailStr
    photoUrl: Optional[str] = None
    college: str
    degree: str
    department: str
    semester: int = Field(ge=1, le=12)
    academicYear: str = "2024-2025"
    preferences: Optional[StudyPreferences] = None

class UserProfileUpdate(BaseModel):
    fullName: Optional[str] = None
    photoUrl: Optional[str] = None
    college: Optional[str] = None
    degree: Optional[str] = None
    department: Optional[str] = None
    semester: Optional[int] = None
    academicYear: Optional[str] = None
    preferences: Optional[StudyPreferences] = None

class UserProfileResponse(BaseModel):
    uid: str
    fullName: str
    email: str
    photoUrl: Optional[str] = None
    college: str
    degree: str
    department: str
    semester: int
    academicYear: str
    preferences: StudyPreferences
    createdAt: datetime
    updatedAt: datetime
