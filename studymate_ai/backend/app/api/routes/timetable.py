from typing import List, Optional
from datetime import date, timedelta
from fastapi import APIRouter, Depends, HTTPException, status, Query
from app.core.dependencies import get_current_user_id
from app.schemas.timetable import (
    TimetableGenerateRequest,
    TimetableGenerateResponse,
    StudySessionResponse,
    StudySessionUpdate,
    RescheduleRequest,
    RescheduleResponse
)
from app.services.timetable_service import timetable_service
from app.repositories.firestore_repository import repo

router = APIRouter(prefix="/timetable", tags=["Timetable"])

@router.post("/generate", response_model=TimetableGenerateResponse)
async def generate_timetable(req: TimetableGenerateRequest, user_id: str = Depends(get_current_user_id)):
    result = await timetable_service.generate_timetable(
        user_id=user_id,
        start_date_str=req.startDate,
        days_to_schedule=req.daysToSchedule,
        daily_available_hours=req.dailyAvailableHours,
        preferred_start=req.preferredStudyStart,
        preferred_end=req.preferredStudyEnd,
        session_duration=req.sessionDurationMinutes,
        break_duration=req.breakDurationMinutes,
        include_revisions=req.includeRevisions,
        exam_prep_mode=req.examPrepMode,
        target_exam_subject_id=req.targetExamSubjectId
    )
    return result

@router.get("/daily", response_model=List[StudySessionResponse])
async def get_daily_timetable(
    target_date: Optional[str] = Query(None, alias="date"),
    user_id: str = Depends(get_current_user_id)
):
    req_date = target_date or date.today().strftime("%Y-%m-%d")
    sessions = await repo.query_documents("study_sessions", {"userId": user_id, "date": req_date})
    sessions.sort(key=lambda s: s.get("startTime", "00:00"))
    return sessions

@router.get("/weekly", response_model=List[StudySessionResponse])
async def get_weekly_timetable(
    start_date: Optional[str] = Query(None),
    user_id: str = Depends(get_current_user_id)
):
    if not start_date:
        today = date.today()
        monday = today - timedelta(days=today.weekday())
        start_date = monday.strftime("%Y-%m-%d")

    start_dt = date.fromisoformat(start_date)
    end_dt = start_dt + timedelta(days=6)
    end_date = end_dt.strftime("%Y-%m-%d")

    all_user_sessions = await repo.query_documents("study_sessions", {"userId": user_id})
    weekly = [s for s in all_user_sessions if start_date <= s.get("date", "") <= end_date]
    weekly.sort(key=lambda s: (s.get("date", ""), s.get("startTime", "00:00")))
    return weekly

@router.put("/sessions/{session_id}", response_model=StudySessionResponse)
async def update_session(session_id: str, payload: StudySessionUpdate, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("study_sessions", session_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Session not found")

    update_dict = {k: v for k, v in payload.model_dump().items() if v is not None}
    updated = await repo.update_document("study_sessions", session_id, update_dict)
    return updated

@router.delete("/sessions/{session_id}")
async def delete_session(session_id: str, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("study_sessions", session_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Session not found")

    await repo.delete_document("study_sessions", session_id)
    return {"success": True, "message": "Session deleted successfully"}

@router.post("/reschedule", response_model=RescheduleResponse)
async def reschedule_sessions(req: RescheduleRequest, user_id: str = Depends(get_current_user_id)):
    res = await timetable_service.reschedule_missed_sessions(
        user_id=user_id,
        session_id=req.sessionId,
        reschedule_all_missed=req.rescheduleAllMissed
    )
    return res
