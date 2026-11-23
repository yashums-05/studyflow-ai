from typing import Optional, List
from pydantic import BaseModel, Field
from datetime import datetime

class TopicBase(BaseModel):
    topicName: str
    difficulty: int = Field(ge=1, le=5, default=3) # 1: Very Easy, 5: Very Hard
    estimatedMinutes: int = Field(ge=15, le=360, default=60)
    importance: int = Field(ge=1, le=5, default=3) # 1: Low, 5: Critical
    status: str = Field(default="not_started") # not_started, in_progress, completed, needs_revision
    understandingRating: Optional[int] = Field(ge=1, le=5, default=None)
    notes: Optional[str] = ""

class TopicCreate(TopicBase):
    subjectId: str
    unitId: str

class TopicUpdate(BaseModel):
    topicName: Optional[str] = None
    difficulty: Optional[int] = None
    estimatedMinutes: Optional[int] = None
    importance: Optional[int] = None
    status: Optional[str] = None
    understandingRating: Optional[int] = None
    notes: Optional[str] = None
    priorityScore: Optional[float] = None

class TopicResponse(TopicBase):
    topicId: str
    userId: str
    subjectId: str
    unitId: str
    priorityScore: float = 0.0
    createdAt: datetime
    updatedAt: datetime

class UnitBase(BaseModel):
    unitName: str
    order: int = 1

class UnitCreate(UnitBase):
    subjectId: str

class UnitUpdate(BaseModel):
    unitName: Optional[str] = None
    order: Optional[int] = None

class UnitResponse(UnitBase):
    unitId: str
    subjectId: str
    userId: str
    createdAt: datetime
    topics: List[TopicResponse] = []

class SubjectBase(BaseModel):
    subjectName: str
    subjectCode: str
    semester: int
    difficulty: int = Field(ge=1, le=5, default=3)
    colorHex: str = "#7556D9"
    examDate: Optional[str] = None # YYYY-MM-DD
    estimatedHours: float = 20.0

class SubjectCreate(SubjectBase):
    pass

class SubjectUpdate(BaseModel):
    subjectName: Optional[str] = None
    subjectCode: Optional[str] = None
    semester: Optional[int] = None
    difficulty: Optional[int] = None
    colorHex: Optional[str] = None
    examDate: Optional[str] = None
    estimatedHours: Optional[float] = None
    completionPercentage: Optional[float] = None

class SubjectResponse(SubjectBase):
    subjectId: str
    userId: str
    totalUnits: int = 0
    totalTopics: int = 0
    completedTopics: int = 0
    completionPercentage: float = 0.0
    createdAt: datetime
    units: List[UnitResponse] = []
