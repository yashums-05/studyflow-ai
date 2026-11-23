from typing import Optional
from pydantic import BaseModel, Field

class SessionStartRequest(BaseModel):
    sessionId: str

class SessionCompleteRequest(BaseModel):
    actualMinutes: int = Field(ge=1)
    understandingRating: int = Field(ge=1, le=5) # 1: Poor, 5: Excellent
    notes: Optional[str] = ""
    topicStatus: Optional[str] = "completed" # completed or needs_revision

class SessionPauseRequest(BaseModel):
    elapsedMinutes: int = Field(ge=0)

class SessionStatusResponse(BaseModel):
    sessionId: str
    status: str
    actualMinutes: int
    understandingRating: Optional[int] = None
    notes: Optional[str] = None
