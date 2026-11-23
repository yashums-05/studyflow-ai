import os
from typing import List
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", case_sensitive=True, extra="ignore")
    
    PROJECT_NAME: str = "StudyMate AI Backend"
    API_V1_STR: str = "/api/v1"
    DEBUG: bool = True
    
    # CORS
    BACKEND_CORS_ORIGINS: List[str] = ["*"]
    
    # Gemini AI API Configuration
    GEMINI_API_KEY: str = os.getenv("GEMINI_API_KEY", "")
    GEMINI_MODEL: str = os.getenv("GEMINI_MODEL", "gemini-1.5-flash")
    
    # Firebase Configuration
    FIREBASE_CREDENTIALS_PATH: str = os.getenv("FIREBASE_CREDENTIALS_PATH", "")
    FIREBASE_PROJECT_ID: str = os.getenv("FIREBASE_PROJECT_ID", "studymate-ai")
    
    # Scheduling Priority Weights
    # Priority Score: P = 0.35D + 0.30U + 0.20W + 0.15I
    WEIGHT_DIFFICULTY: float = 0.35
    WEIGHT_URGENCY: float = 0.30
    WEIGHT_WEAKNESS: float = 0.20
    WEIGHT_IMPORTANCE: float = 0.15
    
    # Default Pomodoro Settings
    DEFAULT_FOCUS_MINUTES: int = 25
    DEFAULT_SHORT_BREAK_MINUTES: int = 5
    DEFAULT_LONG_BREAK_MINUTES: int = 15
    DEFAULT_LONG_BREAK_INTERVAL: int = 4
    
    # Allow mock auth in testing or offline development mode
    ALLOW_DEV_AUTH: bool = True

settings = Settings()
