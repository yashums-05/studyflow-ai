from fastapi import APIRouter, Depends
from app.core.dependencies import get_current_user_id
from app.schemas.analytics import (
    DashboardAnalyticsResponse,
    WeeklyAnalyticsResponse,
    SubjectAnalyticsResponse
)
from app.services.analytics_service import analytics_service

router = APIRouter(prefix="/analytics", tags=["Analytics"])

@router.get("/dashboard", response_model=DashboardAnalyticsResponse)
async def get_dashboard(user_id: str = Depends(get_current_user_id)):
    data = await analytics_service.get_dashboard_analytics(user_id)
    return data

@router.get("/weekly", response_model=WeeklyAnalyticsResponse)
async def get_weekly(user_id: str = Depends(get_current_user_id)):
    data = await analytics_service.get_weekly_analytics(user_id)
    return data

@router.get("/subjects", response_model=SubjectAnalyticsResponse)
async def get_subject_analytics(user_id: str = Depends(get_current_user_id)):
    data = await analytics_service.get_subject_analytics(user_id)
    return data
