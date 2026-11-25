package com.studymate.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleSuggestion {
    private String id = UUID.randomUUID().toString();
    private String category; // exam_urgency, class_gap, holiday_boost, workload_balance, weak_topic
    private String title;
    private String suggestion;
    private String priority = "MEDIUM"; // HIGH, MEDIUM, LOW
    private String actionableRoute = "/timetable";
    private String relatedSubject;
    private String timeSlot;

    public ScheduleSuggestion() {}

    public ScheduleSuggestion(String category, String title, String suggestion, String priority, String relatedSubject, String timeSlot) {
        this.category = category;
        this.title = title;
        this.suggestion = suggestion;
        this.priority = priority;
        this.relatedSubject = relatedSubject;
        this.timeSlot = timeSlot;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getActionableRoute() { return actionableRoute; }
    public void setActionableRoute(String actionableRoute) { this.actionableRoute = actionableRoute; }
    public String getRelatedSubject() { return relatedSubject; }
    public void setRelatedSubject(String relatedSubject) { this.relatedSubject = relatedSubject; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
}
