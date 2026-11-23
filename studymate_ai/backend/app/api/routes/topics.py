import uuid
from typing import List
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.subject import TopicCreate, TopicUpdate, TopicResponse
from app.repositories.firestore_repository import repo
from app.services.timetable_service import timetable_service

router = APIRouter(tags=["Topics"])

@router.post("/topics", response_model=TopicResponse)
async def create_topic(payload: TopicCreate, user_id: str = Depends(get_current_user_id)):
    subject = await repo.get_document("subjects", payload.subjectId)
    if not subject or subject.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    topic_id = str(uuid.uuid4())
    now = datetime.now(timezone.utc)

    # Calculate initial priority score: P = 0.35D + 0.30U + 0.20W + 0.15I
    p_score = timetable_service.calculate_priority_score(
        difficulty=payload.difficulty,
        exam_date_str=subject.get("examDate"),
        understanding_rating=payload.understandingRating,
        importance=payload.importance
    )

    data = payload.model_dump()
    data.update({
        "topicId": topic_id,
        "userId": user_id,
        "priorityScore": p_score,
        "createdAt": now,
        "updatedAt": now
    })
    created = await repo.create_document("topics", topic_id, data)
    return created

@router.get("/subjects/{subject_id}/topics", response_model=List[TopicResponse])
async def list_subject_topics(subject_id: str, user_id: str = Depends(get_current_user_id)):
    topics = await repo.query_documents("topics", {"subjectId": subject_id, "userId": user_id})
    return topics

@router.put("/topics/{topic_id}", response_model=TopicResponse)
async def update_topic(topic_id: str, payload: TopicUpdate, user_id: str = Depends(get_current_user_id)):
    t = await repo.get_document("topics", topic_id)
    if not t or t.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Topic not found")

    update_dict = {k: v for k, v in payload.model_dump().items() if v is not None}
    
    # Recalculate priority score if factors changed
    subject = await repo.get_document("subjects", t["subjectId"])
    diff = update_dict.get("difficulty", t.get("difficulty", 3))
    imp = update_dict.get("importance", t.get("importance", 3))
    und = update_dict.get("understandingRating", t.get("understandingRating"))
    
    update_dict["priorityScore"] = timetable_service.calculate_priority_score(
        difficulty=diff,
        exam_date_str=subject.get("examDate") if subject else None,
        understanding_rating=und,
        importance=imp
    )

    updated = await repo.update_document("topics", topic_id, update_dict)
    return updated

@router.delete("/topics/{topic_id}")
async def delete_topic(topic_id: str, user_id: str = Depends(get_current_user_id)):
    t = await repo.get_document("topics", topic_id)
    if not t or t.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Topic not found")

    await repo.delete_document("topics", topic_id)
    return {"success": True, "message": "Topic deleted successfully"}
