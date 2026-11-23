import uuid
from typing import List
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.subject import UnitCreate, UnitUpdate, UnitResponse, TopicResponse
from app.repositories.firestore_repository import repo

router = APIRouter(tags=["Units"])

@router.post("/units", response_model=UnitResponse)
async def create_unit(payload: UnitCreate, user_id: str = Depends(get_current_user_id)):
    subject = await repo.get_document("subjects", payload.subjectId)
    if not subject or subject.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    unit_id = str(uuid.uuid4())
    now = datetime.now(timezone.utc)
    data = payload.model_dump()
    data.update({
        "unitId": unit_id,
        "userId": user_id,
        "createdAt": now,
        "updatedAt": now
    })
    created = await repo.create_document("units", unit_id, data)
    created["topics"] = []
    return created

@router.get("/subjects/{subject_id}/units", response_model=List[UnitResponse])
async def list_subject_units(subject_id: str, user_id: str = Depends(get_current_user_id)):
    units = await repo.query_documents("units", {"subjectId": subject_id, "userId": user_id})
    all_topics = await repo.query_documents("topics", {"subjectId": subject_id, "userId": user_id})

    results = []
    for u in units:
        u_topics = [TopicResponse(**t) for t in all_topics if t.get("unitId") == u.get("unitId")]
        u_copy = dict(u)
        u_copy["topics"] = u_topics
        results.append(UnitResponse(**u_copy))
    return results

@router.put("/units/{unit_id}", response_model=UnitResponse)
async def update_unit(unit_id: str, payload: UnitUpdate, user_id: str = Depends(get_current_user_id)):
    u = await repo.get_document("units", unit_id)
    if not u or u.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Unit not found")

    update_dict = {k: v for k, v in payload.model_dump().items() if v is not None}
    updated = await repo.update_document("units", unit_id, update_dict)
    
    topics = await repo.query_documents("topics", {"unitId": unit_id, "userId": user_id})
    updated["topics"] = [TopicResponse(**t) for t in topics]
    return updated

@router.delete("/units/{unit_id}")
async def delete_unit(unit_id: str, user_id: str = Depends(get_current_user_id)):
    u = await repo.get_document("units", unit_id)
    if not u or u.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Unit not found")

    # Cascade delete topics under this unit
    topics = await repo.query_documents("topics", {"unitId": unit_id, "userId": user_id})
    for t in topics:
        await repo.delete_document("topics", t["topicId"])

    await repo.delete_document("units", unit_id)
    return {"success": True, "message": "Unit deleted successfully"}
