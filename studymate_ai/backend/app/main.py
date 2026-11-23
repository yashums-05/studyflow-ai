import logging
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from app.core.config import settings

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# Create FastAPI application
app = FastAPI(
    title=settings.PROJECT_NAME,
    description="Intelligent Study Planner Backend powered by FastAPI, Firebase, and Google Gemini AI.",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url="/openapi.json"
)

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.BACKEND_CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global Exception Handler
@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    logger.error(f"Global error on {request.method} {request.url}: {exc}", exc_info=True)
    return JSONResponse(
        status_code=500,
        content={"detail": "An internal server error occurred. Please try again later."}
    )

# Include Routers under API_V1_STR
from app.api.routes import (
    users,
    subjects,
    units,
    topics,
    timetable,
    study_sessions,
    ai,
    exams,
    analytics,
    documents
)

prefix = settings.API_V1_STR
app.include_router(users.router, prefix=prefix)
app.include_router(subjects.router, prefix=prefix)
app.include_router(units.router, prefix=prefix)
app.include_router(topics.router, prefix=prefix)
app.include_router(timetable.router, prefix=prefix)
app.include_router(study_sessions.router, prefix=prefix)
app.include_router(ai.router, prefix=prefix)
app.include_router(exams.router, prefix=prefix)
app.include_router(analytics.router, prefix=prefix)
app.include_router(documents.router, prefix=prefix)

import os
from fastapi.responses import FileResponse, JSONResponse

@app.get("/download/app.apk")
async def download_apk():
    apk_paths = [
        r"D:\AI app\studymate_ai\mobile\build\app\outputs\flutter-apk\app-debug.apk",
        r"D:\AI app\studymate_ai\mobile\build\app\outputs\flutter-apk\app-release.apk",
    ]
    for p in apk_paths:
        if os.path.exists(p):
            return FileResponse(
                p,
                media_type="application/vnd.android.package-archive",
                filename="StudyMateAI.apk"
            )
    return JSONResponse(status_code=404, content={"message": "APK is currently being compiled. Please try again shortly."})

@app.get("/")
async def root():
    return {
        "app": settings.PROJECT_NAME,
        "status": "online",
        "docs": "/docs",
        "version": "1.0.0"
    }

@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "api": "v1",
        "geminiConfigured": bool(settings.GEMINI_API_KEY)
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
