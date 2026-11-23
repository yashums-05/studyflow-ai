from typing import Optional, List, Dict, Any
from pydantic import BaseModel

class ChatMessage(BaseModel):
    role: str # user, assistant, system
    content: str

class AIChatRequest(BaseModel):
    message: str
    conversationId: Optional[str] = None
    subjectContext: Optional[str] = None
    topicContext: Optional[str] = None

class AIChatResponse(BaseModel):
    conversationId: str
    response: str
    suggestedFollowups: List[str] = []

class TopicExplainRequest(BaseModel):
    subject: str
    topic: str
    studentLevel: Optional[str] = "Engineering Undergraduate"
    conceptDetails: Optional[str] = None

class TopicExplainResponse(BaseModel):
    topic: str
    simpleExplanation: str
    detailedExplanation: str
    example: str
    importantPoints: List[str]
    practiceQuestions: List[str]

class QuizQuestion(BaseModel):
    questionId: str
    question: str
    options: List[str]
    correctAnswer: str
    explanation: str
    difficulty: str

class QuizGenerateRequest(BaseModel):
    subject: str
    topic: str
    numQuestions: int = 5
    difficulty: str = "Medium"

class QuizGenerateResponse(BaseModel):
    subject: str
    topic: str
    questions: List[QuizQuestion]

class AIRecommendationsResponse(BaseModel):
    recommendations: List[str]
    weakSubjects: List[Dict[str, Any]] = []
    generatedAt: str

class SyllabusAnalyzeRequest(BaseModel):
    rawText: str
    defaultSubjectName: Optional[str] = "New Subject"

class SyllabusUnitExtracted(BaseModel):
    unitName: str
    topics: List[str]

class SyllabusAnalyzeResponse(BaseModel):
    subjectName: str
    subjectCode: Optional[str] = None
    units: List[SyllabusUnitExtracted]
    totalEstimatedHours: float = 30.0
