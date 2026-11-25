package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudySession {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String subjectId;
    private String subjectName;
    private String unitId;
    private String topicId;
    private String topicName;
    private String sessionDate;
    private String startTime;
    private String endTime;
    private Integer estimatedDurationMinutes = 45;
    private Integer actualDurationMinutes = 0;
    private String sessionType = "learning"; // learning, revision, practice, mock_test
    private String status = "scheduled"; // scheduled, in_progress, completed, missed, rescheduled
    private Double priorityScore = 0.0;
    private Integer understandingRating = 0;
    private String notes = "";

    public StudySession() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getUnitId() { return unitId; }
    public void setUnitId(String unitId) { this.unitId = unitId; }
    public String getTopicId() { return topicId; }
    public void setTopicId(String topicId) { this.topicId = topicId; }
    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }
    public String getSessionDate() { return sessionDate; }
    public void setSessionDate(String sessionDate) { this.sessionDate = sessionDate; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public Integer getActualDurationMinutes() { return actualDurationMinutes; }
    public void setActualDurationMinutes(Integer actualDurationMinutes) { this.actualDurationMinutes = actualDurationMinutes; }
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }
    public Integer getUnderstandingRating() { return understandingRating; }
    public void setUnderstandingRating(Integer understandingRating) { this.understandingRating = understandingRating; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
