import uuid
from typing import List
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, UploadFile, File, status
from app.core.dependencies import get_current_user_id
from app.schemas.document import DocumentResponse, DocumentAskRequest, DocumentAskResponse
from app.repositories.firestore_repository import repo
from app.services.syllabus_service import syllabus_service
from app.services.ai_service import ai_service
from app.core.config import settings

router = APIRouter(prefix="/documents", tags=["Documents"])

@router.post("/upload", response_model=DocumentResponse)
async def upload_document(
    file: UploadFile = File(...),
    user_id: str = Depends(get_current_user_id)
):
    if not file.filename.lower().endswith(".pdf"):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Only PDF documents are supported."
        )

    content = await file.read()
    if len(content) > 15 * 1024 * 1024: # 15MB limit
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="File size exceeds the 15 MB limit."
        )

    try:
        extracted_text = syllabus_service.extract_text_from_pdf(content)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail=f"Failed to process and extract text from PDF: {str(e)}"
        )

    doc_id = str(uuid.uuid4())
    now = datetime.now(timezone.utc)
    snippet = extracted_text[:300] + ("..." if len(extracted_text) > 300 else "")

    record = {
        "documentId": doc_id,
        "userId": user_id,
        "fileName": file.filename,
        "fileSize": len(content),
        "pageCount": max(1, len(extracted_text) // 2000),
        "extractedSnippet": snippet,
        "fullText": extracted_text,
        "uploadedAt": now
    }
    await repo.create_document("uploaded_documents", doc_id, record)
    
    return DocumentResponse(
        documentId=doc_id,
        userId=user_id,
        fileName=file.filename,
        fileSize=len(content),
        pageCount=record["pageCount"],
        extractedSnippet=snippet,
        uploadedAt=now
    )

@router.get("", response_model=List[DocumentResponse])
async def list_documents(user_id: str = Depends(get_current_user_id)):
    docs = await repo.query_documents("uploaded_documents", {"userId": user_id})
    return [
        DocumentResponse(
            documentId=d["documentId"],
            userId=d["userId"],
            fileName=d["fileName"],
            fileSize=d.get("fileSize", 0),
            pageCount=d.get("pageCount", 1),
            extractedSnippet=d.get("extractedSnippet", ""),
            uploadedAt=d.get("uploadedAt", datetime.now(timezone.utc))
        )
        for d in docs
    ]

@router.post("/{document_id}/ask", response_model=DocumentAskResponse)
async def ask_document(
    document_id: str,
    req: DocumentAskRequest,
    user_id: str = Depends(get_current_user_id)
):
    doc = await repo.get_document("uploaded_documents", document_id)
    if not doc or doc.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Document not found")

    text = doc.get("fullText", "")
    # Retrieve relevant chunks matching query keywords
    keywords = [w.lower() for w in req.question.split() if len(w) > 3]
    paragraphs = text.split("\n\n")
    relevant = []
    for p in paragraphs:
        if any(k in p.lower() for k in keywords):
            relevant.append(p.strip())
            if len(relevant) >= 3:
                break

    context_str = "\n---\n".join(relevant) if relevant else text[:2000]

    prompt = (
        f"You are StudyMate AI Document Assistant.\n"
        f"Answer the student's question using ONLY the provided document context where possible.\n"
        f"Distinguish clearly between information directly found in the document and general explanations.\n\n"
        f"Document Excerpt:\n{context_str}\n\n"
        f"Question: {req.question}"
    )

    # Use AI service or smart fallback
    reply = await ai_service.chat(prompt)

    return DocumentAskResponse(
        documentId=document_id,
        question=req.question,
        answer=reply["response"],
        sourceSnippets=relevant[:2] if relevant else [context_str[:200]],
        isDirectQuote=bool(relevant)
    )

@router.delete("/{document_id}")
async def delete_document(document_id: str, user_id: str = Depends(get_current_user_id)):
    doc = await repo.get_document("uploaded_documents", document_id)
    if not doc or doc.get("userId") != user_id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Document not found")

    await repo.delete_document("uploaded_documents", document_id)
    return {"success": True, "message": "Document deleted successfully"}
