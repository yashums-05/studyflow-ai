from typing import List, Dict, Any, Optional
from pydantic import BaseModel

class DayStudyData(BaseModel):
    date: str # YYYY-MM-DD
    dayName: str # Mon, Tue, etc.
    plannedHours: float
    actualHours: float

class SubjectProgressItem(BaseModel):
    subjectId: str
    subjectName: str
    colorHex: str
    totalTopics: int
    completedTopics: int
    completionPercentage: float
    totalStudyHours: float

class DashboardAnalyticsResponse(BaseModel):
    totalSubjects: int
    totalTopics: int
    completedTopics: int
    pendingTopics: int
    overallCompletionPercentage: float
    plannedStudyHoursToday: float
    completedStudyHoursToday: float
    remainingSessionsToday: int
    studyStreakDays: int
    weeklyTotalStudyHours: float
    upcomingExamsCount: int
    aiRecommendations: List[str]

class WeeklyAnalyticsResponse(BaseModel):
    totalHoursThisWeek: float
    dailyBreakdown: List[DayStudyData]
    plannedVsActualRatio: float
    studyStreak: int

class SubjectAnalyticsResponse(BaseModel):
    subjects: List[SubjectProgressItem]
    strongestSubjects: List[str]
    subjectsNeedingImprovement: List[Dict[str, Any]]
