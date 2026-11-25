package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcademicEvent {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String title;
    private String eventDate; // "2026-11-28"
    private String endDate;   // optional
    private String eventType = "event"; // "holiday", "internal_assessment", "deadline", "semester_exam", "event"
    private String description = "";
    private boolean holiday = false;

    public AcademicEvent() {}

    public AcademicEvent(String title, String eventDate, String eventType, boolean isHoliday) {
        this.title = title;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.holiday = isHoliday;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isHoliday() { return holiday; }
    public void setHoliday(boolean holiday) { this.holiday = holiday; }
}
