import logging
from typing import Optional, Dict, Any
from fastapi import Header, HTTPException, status
import firebase_admin
from firebase_admin import auth as firebase_auth, credentials
from app.core.config import settings

logger = logging.getLogger(__name__)

# Initialize Firebase Admin if credentials are provided
_firebase_initialized = False

def initialize_firebase():
    global _firebase_initialized
    if _firebase_initialized:
        return
    try:
        if settings.FIREBASE_CREDENTIALS_PATH:
            cred = credentials.Certificate(settings.FIREBASE_CREDENTIALS_PATH)
            firebase_admin.initialize_app(cred)
            _firebase_initialized = True
            logger.info("Firebase Admin initialized with certificate credentials.")
        else:
            # Try default app initialization if in GCP or environment
            try:
                firebase_admin.initialize_app()
                _firebase_initialized = True
                logger.info("Firebase Admin initialized with default credentials.")
            except Exception:
                logger.warning("Firebase Admin not configured with credentials; running with dev auth support.")
    except Exception as e:
        logger.warning(f"Failed to initialize Firebase Admin: {e}")

initialize_firebase()

async def verify_firebase_token(authorization: Optional[str] = Header(None)) -> Dict[str, Any]:
    """
    Verifies the Bearer token against Firebase Authentication.
    If dev mode is active and token starts with 'dev_token_', extracts test UID.
    """
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Missing or invalid Authorization header. Expected 'Bearer <token>'",
            headers={"WWW-Authenticate": "Bearer"},
        )
    
    token = authorization.split("Bearer ")[1].strip()
    
    # Dev / Test token support for local testing and CI
    if settings.ALLOW_DEV_AUTH and token.startswith("dev_user_"):
        uid = token
        return {
            "uid": uid,
            "email": f"{uid}@example.com",
            "name": f"Test Student ({uid})",
            "auth_time": 1700000000
        }
    
    if not _firebase_initialized:
        # If Firebase is not configured with live credentials, but dev mode is enabled
        if settings.ALLOW_DEV_AUTH:
            # Fallback dev user
            return {
                "uid": f"user_{token[:12]}",
                "email": "student@studymate.ai",
                "name": "StudyMate Student",
                "auth_time": 1700000000
            }
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Firebase Auth is not initialized and DEV_AUTH is disabled."
        )
    
    try:
        decoded_token = firebase_auth.verify_id_token(token)
        return decoded_token
    except Exception as e:
        logger.error(f"Token verification failed: {e}")
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=f"Invalid authentication token: {str(e)}",
            headers={"WWW-Authenticate": "Bearer"},
        )
