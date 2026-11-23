import uuid
from typing import Dict, Any, List, Optional
from datetime import datetime, timezone
from app.core.config import settings

class DataRepository:
    """
    Data repository that handles Firestore operations when available,
    and maintains an in-memory store for dev/testing when Firestore credentials are not set.
    """
    def __init__(self):
        self._memory_store: Dict[str, Dict[str, Dict[str, Any]]] = {
            "users": {},
            "subjects": {},
            "units": {},
            "topics": {},
            "study_sessions": {},
            "exams": {},
            "study_preferences": {},
            "quizzes": {},
            "ai_conversations": {},
            "ai_messages": {},
            "uploaded_documents": {},
            "notifications": {},
        }
        self._firestore_db = None
        self._init_firestore()

    def _init_firestore(self):
        try:
            from firebase_admin import firestore
            self._firestore_db = firestore.client()
        except Exception:
            self._firestore_db = None

    def _now_iso(self) -> str:
        return datetime.now(timezone.utc).isoformat()

    # Generic CRUD
    async def create_document(self, collection: str, doc_id: Optional[str], data: Dict[str, Any]) -> Dict[str, Any]:
        if not doc_id:
            doc_id = str(uuid.uuid4())
        
        record = dict(data)
        record["id"] = doc_id
        if "createdAt" not in record:
            record["createdAt"] = datetime.now(timezone.utc)
        if "updatedAt" not in record:
            record["updatedAt"] = datetime.now(timezone.utc)
            
        if self._firestore_db:
            try:
                self._firestore_db.collection(collection).document(doc_id).set(record)
                return record
            except Exception:
                pass
        
        # Memory fallback
        if collection not in self._memory_store:
            self._memory_store[collection] = {}
        self._memory_store[collection][doc_id] = record
        return record

    async def get_document(self, collection: str, doc_id: str) -> Optional[Dict[str, Any]]:
        if self._firestore_db:
            try:
                doc = self._firestore_db.collection(collection).document(doc_id).get()
                if doc.exists:
                    return doc.to_dict()
                return None
            except Exception:
                pass
        
        return self._memory_store.get(collection, {}).get(doc_id)

    async def update_document(self, collection: str, doc_id: str, data: Dict[str, Any]) -> Optional[Dict[str, Any]]:
        record = await self.get_document(collection, doc_id)
        if not record:
            return None
            
        updated = dict(record)
        updated.update(data)
        updated["updatedAt"] = datetime.now(timezone.utc)
        
        if self._firestore_db:
            try:
                self._firestore_db.collection(collection).document(doc_id).update(updated)
                return updated
            except Exception:
                pass
                
        self._memory_store[collection][doc_id] = updated
        return updated

    async def delete_document(self, collection: str, doc_id: str) -> bool:
        if self._firestore_db:
            try:
                self._firestore_db.collection(collection).document(doc_id).delete()
            except Exception:
                pass
                
        if collection in self._memory_store and doc_id in self._memory_store[collection]:
            del self._memory_store[collection][doc_id]
            return True
        return False

    async def query_documents(self, collection: str, filters: Dict[str, Any]) -> List[Dict[str, Any]]:
        if self._firestore_db:
            try:
                query = self._firestore_db.collection(collection)
                for key, val in filters.items():
                    query = query.where(key, "==", val)
                docs = query.stream()
                return [d.to_dict() for d in docs]
            except Exception:
                pass
                
        # Memory query
        items = self._memory_store.get(collection, {}).values()
        results = []
        for item in items:
            match = True
            for k, v in filters.items():
                if item.get(k) != v:
                    match = False
                    break
            if match:
                results.append(item)
        return results

repo = DataRepository()
