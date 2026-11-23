from datetime import datetime, timezone
from fastapi import APIRouter, Depends
from app.core.dependencies import get_current_user_id
from app.schemas.ai import (
    AIChatRequest,
    AIChatResponse,
    TopicExplainRequest,
    TopicExplainResponse,
    QuizGenerateRequest,
    QuizGenerateResponse,
    AIRecommendationsResponse,
    SyllabusAnalyzeRequest,
    SyllabusAnalyzeResponse
)
from app.services.ai_service import ai_service
from app.services.syllabus_service import syllabus_service
from app.services.analytics_service import analytics_service
from app.repositories.firestore_repository import repo

router = APIRouter(prefix="/ai", tags=["AI Tutor"])

@router.post("/chat", response_model=AIChatResponse)
async def chat_with_ai(req: AIChatRequest, user_id: str = Depends(get_current_user_id)):
    result = await ai_service.chat(
        message=req.message,
        conversation_id=req.conversationId,
        subject_context=req.subjectContext,
        topic_context=req.topicContext
    )
    # Save message to history
    await repo.create_document("ai_messages", None, {
        "userId": user_id,
        "conversationId": result["conversationId"],
        "userMessage": req.message,
        "aiResponse": result["response"],
        "timestamp": datetime.now(timezone.utc).isoformat()
    })
    return result

@router.post("/explain", response_model=TopicExplainResponse)
async def explain_topic(req: TopicExplainRequest, user_id: str = Depends(get_current_user_id)):
    result = await ai_service.explain_topic(
        subject=req.subject,
        topic=req.topic,
        student_level=req.studentLevel or "Engineering Undergraduate",
        concept_details=req.conceptDetails
    )
    return result

@router.post("/generate-quiz", response_model=QuizGenerateResponse)
async def generate_quiz(req: QuizGenerateRequest, user_id: str = Depends(get_current_user_id)):
    result = await ai_service.generate_quiz(
        subject=req.subject,
        topic=req.topic,
        num_questions=req.numQuestions,
        difficulty=req.difficulty
    )
    return result

@router.post("/recommendations", response_model=AIRecommendationsResponse)
async def get_study_recommendations(user_id: str = Depends(get_current_user_id)):
    dash = await analytics_service.get_dashboard_analytics(user_id)
    subj_analytics = await analytics_service.get_subject_analytics(user_id)
    
    return AIRecommendationsResponse(
        recommendations=dash["aiRecommendations"],
        weakSubjects=subj_analytics["subjectsNeedingImprovement"],
        generatedAt=datetime.now(timezone.utc).isoformat()
    )

@router.post("/analyze-syllabus", response_model=SyllabusAnalyzeResponse)
async def analyze_syllabus(req: SyllabusAnalyzeRequest, user_id: str = Depends(get_current_user_id)):
    parsed = await syllabus_service.parse_syllabus_text(
        raw_text=req.rawText,
        default_subject=req.defaultSubjectName or "Extracted Subject"
    )
    return parsed
