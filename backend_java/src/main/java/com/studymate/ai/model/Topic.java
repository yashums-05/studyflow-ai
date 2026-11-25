package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic {
    private String id = UUID.randomUUID().toString();
    private String unitId;
    private String subjectId;
    private String name;
    private Integer difficultyLevel = 3;
    private Integer estimatedDurationMinutes = 45;
    private String completionStatus = "not_started"; // not_started, in_progress, completed, needs_revision
    private Integer importanceScore = 3; // 1-5
    private Integer understandingRating = 0; // 0-5
    private String notes = "";
    private Integer orderIndex = 0;

    public Topic() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUnitId() { return unitId; }
    public void setUnitId(String unitId) { this.unitId = unitId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Integer difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public String getCompletionStatus() { return completionStatus; }
    public void setCompletionStatus(String completionStatus) { this.completionStatus = completionStatus; }
    public Integer getImportanceScore() { return importanceScore; }
    public void setImportanceScore(Integer importanceScore) { this.importanceScore = importanceScore; }
    public Integer getUnderstandingRating() { return understandingRating; }
    public void setUnderstandingRating(Integer understandingRating) { this.understandingRating = understandingRating; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}
