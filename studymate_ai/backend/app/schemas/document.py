from typing import Optional, List
from pydantic import BaseModel
from datetime import datetime

class DocumentResponse(BaseModel):
    documentId: str
    userId: str
    fileName: str
    fileSize: int
    pageCount: int
    extractedSnippet: str
    uploadedAt: datetime

class DocumentAskRequest(BaseModel):
    question: str

class DocumentAskResponse(BaseModel):
    documentId: str
    question: str
    answer: str
    sourceSnippets: List[str] = []
    isDirectQuote: bool = False
