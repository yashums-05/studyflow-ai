import uuid
from datetime import datetime, timezone
from typing import Dict, Any, List
from app.repositories.firestore_repository import repo

class NotificationService:
    """
    Manages study reminders, exam alerts, and streak notifications.
    """

    async def create_notification(
        self,
        user_id: str,
        title: str,
        message: str,
        notif_type: str = "study_reminder",
        scheduled_at: str = ""
    ) -> Dict[str, Any]:
        notif_id = str(uuid.uuid4())
        record = {
            "notificationId": notif_id,
            "userId": user_id,
            "title": title,
            "message": message,
            "type": notif_type,
            "scheduledAt": scheduled_at or datetime.now(timezone.utc).isoformat(),
            "read": False,
            "createdAt": datetime.now(timezone.utc)
        }
        await repo.create_document("notifications", notif_id, record)
        return record

    async def get_user_notifications(self, user_id: str) -> List[Dict[str, Any]]:
        return await repo.query_documents("notifications", {"userId": user_id})

    async def mark_as_read(self, notification_id: str) -> bool:
        doc = await repo.update_document("notifications", notification_id, {"read": True})
        return doc is not None

notification_service = NotificationService()
