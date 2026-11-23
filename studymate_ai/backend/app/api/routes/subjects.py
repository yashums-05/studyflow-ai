import uuid
from typing import List
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.subject import SubjectCreate, SubjectUpdate, SubjectResponse, UnitResponse, TopicResponse
from app.repositories.firestore_repository import repo

router = APIRouter(prefix="/subjects", tags=["Subjects"])

@router.post("", response_model=SubjectResponse)
async def create_subject(payload: SubjectCreate, user_id: str = Depends(get_current_user_id)):
    subject_id = str(uuid.uuid4())
    now = datetime.now(timezone.utc)

    data = payload.model_dump()
    data.update({
        "subjectId": subject_id,
        "userId": user_id,
        "totalUnits": 0,
        "totalTopics": 0,
        "completedTopics": 0,
        "completionPercentage": 0.0,
        "createdAt": now,
        "updatedAt": now
    })
    created = await repo.create_document("subjects", subject_id, data)
    created["units"] = []
    return created

@router.get("", response_model=List[SubjectResponse])
async def list_subjects(user_id: str = Depends(get_current_user_id)):
    subjects = await repo.query_documents("subjects", {"userId": user_id})
    all_units = await repo.query_documents("units", {"userId": user_id})
    all_topics = await repo.query_documents("topics", {"userId": user_id})

    results = []
    for s in subjects:
        s_id = s["subjectId"]
        s_units = [u for u in all_units if u.get("subjectId") == s_id]
        s_topics = [t for t in all_topics if t.get("subjectId") == s_id]

        total_t = len(s_topics)
        completed_t = len([t for t in s_topics if t.get("status") == "completed"])
        pct = round((completed_t / total_t * 100), 1) if total_t > 0 else 0.0

        # Nest units and topics
        nested_units = []
        for u in s_units:
            u_topics = [TopicResponse(**t) for t in s_topics if t.get("unitId") == u.get("unitId")]
            u_dict = dict(u)
            u_dict["topics"] = u_topics
            nested_units.append(UnitResponse(**u_dict))

        s_copy = dict(s)
        s_copy["totalUnits"] = len(s_units)
        s_copy["totalTopics"] = total_t
        s_copy["completedTopics"] = completed_t
        s_copy["completionPercentage"] = pct
        s_copy["units"] = nested_units
        results.append(SubjectResponse(**s_copy))

    return results

@router.get("/{subject_id}", response_model=SubjectResponse)
async def get_subject(subject_id: str, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("subjects", subject_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    s_units = await repo.query_documents("units", {"subjectId": subject_id, "userId": user_id})
    s_topics = await repo.query_documents("topics", {"subjectId": subject_id, "userId": user_id})

    total_t = len(s_topics)
    completed_t = len([t for t in s_topics if t.get("status") == "completed"])
    pct = round((completed_t / total_t * 100), 1) if total_t > 0 else 0.0

    nested_units = []
    for u in s_units:
        u_topics = [TopicResponse(**t) for t in s_topics if t.get("unitId") == u.get("unitId")]
        u_dict = dict(u)
        u_dict["topics"] = u_topics
        nested_units.append(UnitResponse(**u_dict))

    s_copy = dict(s)
    s_copy["totalUnits"] = len(s_units)
    s_copy["totalTopics"] = total_t
    s_copy["completedTopics"] = completed_t
    s_copy["completionPercentage"] = pct
    s_copy["units"] = nested_units
    return SubjectResponse(**s_copy)

@router.put("/{subject_id}", response_model=SubjectResponse)
async def update_subject(subject_id: str, payload: SubjectUpdate, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("subjects", subject_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    update_dict = {k: v for k, v in payload.model_dump().items() if v is not None}
    updated = await repo.update_document("subjects", subject_id, update_dict)
    return await get_subject(subject_id, user_id)

@router.delete("/{subject_id}")
async def delete_subject(subject_id: str, user_id: str = Depends(get_current_user_id)):
    s = await repo.get_document("subjects", subject_id)
    if not s or s.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    # Cascading delete of units, topics, and sessions for this subject
    units = await repo.query_documents("units", {"subjectId": subject_id, "userId": user_id})
    for u in units:
        await repo.delete_document("units", u["unitId"])

    topics = await repo.query_documents("topics", {"subjectId": subject_id, "userId": user_id})
    for t in topics:
        await repo.delete_document("topics", t["topicId"])

    sessions = await repo.query_documents("study_sessions", {"subjectId": subject_id, "userId": user_id})
    for sess in sessions:
        await repo.delete_document("study_sessions", sess["sessionId"])

    await repo.delete_document("subjects", subject_id)
    return {"success": True, "message": "Subject deleted successfully"}
