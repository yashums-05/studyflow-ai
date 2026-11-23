import uuid
from typing import List
from datetime import datetime, date, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.exam import ExamCreate, ExamUpdate, ExamResponse
from app.repositories.firestore_repository import repo

router = APIRouter(prefix="/exams", tags=["Exams"])

@router.post("", response_model=ExamResponse)
async def create_exam(payload: ExamCreate, user_id: str = Depends(get_current_user_id)):
    subject = await repo.get_document("subjects", payload.subjectId)
    if not subject or subject.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Subject not found")

    exam_id = str(uuid.uuid4())
    now = datetime.now(timezone.utc)
    
    # Calculate days remaining
    today = date.today()
    try:
        e_date = datetime.strptime(payload.examDate, "%Y-%m-%d").date()
        days_rem = max(0, (e_date - today).days)
    except Exception:
        days_rem = 0

    data = payload.model_dump()
    data.update({
        "examId": exam_id,
        "userId": user_id,
        "createdAt": now,
        "updatedAt": now
    })
    created = await repo.create_document("exams", exam_id, data)

    # Sync exam date with subject if empty
    if not subject.get("examDate"):
        await repo.update_document("subjects", payload.subjectId, {"examDate": payload.examDate})

    return ExamResponse(
        **created,
        subjectName=subject.get("subjectName", ""),
        subjectCode=subject.get("subjectCode", ""),
        subjectColor=subject.get("colorHex", "#7556D9"),
        daysRemaining=days_rem,
        totalTopics=0,
        completedTopics=0,
        completionPercentage=0.0
    )

@router.get("", response_model=List[ExamResponse])
async def list_exams(user_id: str = Depends(get_current_user_id)):
    exams = await repo.query_documents("exams", {"userId": user_id})
    subjects = await repo.query_documents("subjects", {"userId": user_id})
    topics = await repo.query_documents("topics", {"userId": user_id})
    
    subj_map = {s["subjectId"]: s for s in subjects}
    today = date.today()

    results = []
    for e in exams:
        subj = subj_map.get(e.get("subjectId"), {})
        try:
            e_date = datetime.strptime(e.get("examDate"), "%Y-%m-%d").date()
            days_rem = max(0, (e_date - today).days)
        except Exception:
            days_rem = 0

        s_topics = [t for t in topics if t.get("subjectId") == e.get("subjectId")]
        total_t = len(s_topics)
        completed_t = len([t for t in s_topics if t.get("status") == "completed"])
        pct = round((completed_t / total_t * 100), 1) if total_t > 0 else 0.0

        results.append(ExamResponse(
            **e,
            subjectName=subj.get("subjectName", "Subject"),
            subjectCode=subj.get("subjectCode", ""),
            subjectColor=subj.get("colorHex", "#7556D9"),
            daysRemaining=days_rem,
            totalTopics=total_t,
            completedTopics=completed_t,
            completionPercentage=pct
        ))

    results.sort(key=lambda x: x.daysRemaining)
    return results

@router.put("/{exam_id}", response_model=ExamResponse)
async def update_exam(exam_id: str, payload: ExamUpdate, user_id: str = Depends(get_current_user_id)):
    e = await repo.get_document("exams", exam_id)
    if not e or e.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Exam not found")

    update_dict = {k: v for k, v in payload.model_dump().items() if v is not None}
    updated = await repo.update_document("exams", exam_id, update_dict)
    
    # Return refreshed exam
    all_exams = await list_exams(user_id)
    for exam in all_exams:
        if exam.examId == exam_id:
            return exam
    raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Exam not found")

@router.delete("/{exam_id}")
async def delete_exam(exam_id: str, user_id: str = Depends(get_current_user_id)):
    e = await repo.get_document("exams", exam_id)
    if not e or e.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Exam not found")

    await repo.delete_document("exams", exam_id)
    return {"success": True, "message": "Exam deleted successfully"}
