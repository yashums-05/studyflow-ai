from typing import Dict, Any
from fastapi import Depends
from app.core.security import verify_firebase_token

async def get_current_user(token_payload: Dict[str, Any] = Depends(verify_firebase_token)) -> Dict[str, Any]:
    """
    Returns current authenticated user payload.
    Guarantees user ownership by extracting user ID directly from token.
    """
    return token_payload

async def get_current_user_id(user: Dict[str, Any] = Depends(get_current_user)) -> str:
    return user["uid"]
