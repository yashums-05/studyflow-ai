from fastapi import APIRouter, Depends, HTTPException, status
from app.core.dependencies import get_current_user_id
from app.schemas.user import UserProfileCreate, UserProfileUpdate, UserProfileResponse, StudyPreferences
from app.repositories.firestore_repository import repo
from datetime import datetime, timezone

router = APIRouter(prefix="/users", tags=["Users"])

@router.post("/profile", response_model=UserProfileResponse)
async def create_profile(profile: UserProfileCreate, user_id: str = Depends(get_current_user_id)):
    existing = await repo.get_document("users", user_id)
    now = datetime.now(timezone.utc)
    
    prefs = profile.preferences or StudyPreferences()
    doc_data = {
        "uid": user_id,
        "fullName": profile.fullName,
        "email": profile.email,
        "photoUrl": profile.photoUrl,
        "college": profile.college,
        "degree": profile.degree,
        "department": profile.department,
        "semester": profile.semester,
        "academicYear": profile.academicYear,
        "preferences": prefs.model_dump(),
        "createdAt": existing.get("createdAt") if existing else now,
        "updatedAt": now
    }
    saved = await repo.create_document("users", user_id, doc_data)
    return saved

@router.get("/profile", response_model=UserProfileResponse)
async def get_profile(user_id: str = Depends(get_current_user_id)):
    doc = await repo.get_document("users", user_id)
    if not doc:
        # Return default initialized profile for new user
        now = datetime.now(timezone.utc)
        default_profile = {
            "uid": user_id,
            "fullName": "New Student",
            "email": f"{user_id}@studymate.ai",
            "photoUrl": None,
            "college": "Engineering College",
            "degree": "B.Tech",
            "department": "Computer Science",
            "semester": 6,
            "academicYear": "2024-2025",
            "preferences": StudyPreferences().model_dump(),
            "createdAt": now,
            "updatedAt": now
        }
        await repo.create_document("users", user_id, default_profile)
        return default_profile
    return doc

@router.put("/profile", response_model=UserProfileResponse)
async def update_profile(update_data: UserProfileUpdate, user_id: str = Depends(get_current_user_id)):
    doc = await repo.get_document("users", user_id)
    if not doc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Profile not found")

    payload = {k: v for k, v in update_data.model_dump().items() if v is not None}
    if "preferences" in payload and payload["preferences"]:
        payload["preferences"] = payload["preferences"]

    updated = await repo.update_document("users", user_id, payload)
    return updated
