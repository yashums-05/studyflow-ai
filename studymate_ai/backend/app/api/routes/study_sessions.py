from typing import List, Optional
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.study_session import SessionStartRequest, SessionCompleteRequest, SessionPauseRequest, SessionStatusResponse
from app.schemas.timetable import StudySessionResponse
from app.repositories.firestore_repository import repo
from app.services.timetable_service import timetable_service

router = APIRouter(prefix="/study-sessions", tags=["Study Sessions"])

@router.post("/start", response_model=SessionStatusResponse)
async def start_study_session(req: SessionStartRequest, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("study_sessions", req.sessionId)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Session not found")

    updated = await repo.update_document("study_sessions", req.sessionId, {"status": "in_progress"})
    return SessionStatusResponse(
        sessionId=req.sessionId,
        status="in_progress",
        actualMinutes=updated.get("actualMinutes", 0),
        notes=updated.get("notes")
    )

@router.post("/{session_id}/complete", response_model=SessionStatusResponse)
async def complete_study_session(
    session_id: str,
    req: SessionCompleteRequest,
    user_id: str = Depends(get_current_user_id)
):
    s = await repo.get_document("study_sessions", session_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Session not found")

    # Update session record
    session_update = {
        "status": "completed",
        "actualMinutes": req.actualMinutes,
        "notes": req.notes
    }
    updated = await repo.update_document("study_sessions", session_id, session_update)

    # Update associated topic understanding rating and status
    topic_id = s.get("topicId")
    if topic_id:
        topic_doc = await repo.get_document("topics", topic_id)
        if topic_doc:
            subject_id = topic_doc.get("subjectId")
            subject_doc = await repo.get_document("subjects", subject_id) if subject_id else None
            
            new_p_score = timetable_service.calculate_priority_score(
                difficulty=topic_doc.get("difficulty", 3),
                exam_date_str=subject_doc.get("examDate") if subject_doc else None,
                understanding_rating=req.understandingRating,
                importance=topic_doc.get("importance", 3)
            )

            await repo.update_document("topics", topic_id, {
                "status": req.topicStatus or "completed",
                "understandingRating": req.understandingRating,
                "priorityScore": new_p_score
            })

    return SessionStatusResponse(
        sessionId=session_id,
        status="completed",
        actualMinutes=req.actualMinutes,
        understandingRating=req.understandingRating,
        notes=req.notes
    )

@router.post("/{session_id}/pause", response_model=SessionStatusResponse)
async def pause_study_session(
    session_id: str,
    req: SessionPauseRequest,
    user_id: str = Depends(get_current_user_id)
):
    s = await repo.get_document("study_sessions", session_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Session not found")

    updated = await repo.update_document("study_sessions", session_id, {
        "status": "scheduled",
        "actualMinutes": req.elapsedMinutes
    })
    return SessionStatusResponse(
        sessionId=session_id,
        status="scheduled",
        actualMinutes=req.elapsedMinutes,
        notes=updated.get("notes")
    )

@router.get("/history", response_model=List[StudySessionResponse])
async def get_session_history(user_id: str = Depends(get_current_user_id)):
    sessions = await repo.query_documents("study_sessions", {"userId": user_id, "status": "completed"})
    sessions.sort(key=lambda x: x.get("date", ""), reverse=True)
    return sessions
