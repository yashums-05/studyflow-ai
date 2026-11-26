package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic {
    private String id = UUID.randomUUID().toString();
    private String unitId;
    private String subjectId;
    private String name = "Topic";
    private Integer difficultyLevel = 3;
    private Integer estimatedDurationMinutes = 45;
    private String completionStatus = "not_started"; // not_started, in_progress, completed, needs_revision
    private Integer importanceScore = 3; // 1-5
    private Integer understandingRating = 0; // 0-5
    private String notes = "";
    private Integer orderIndex = 0;

    public Topic() {}

    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("topicId")
    public String getTopicId() { return id; }
    public void setTopicId(String topicId) { if (topicId != null && !topicId.trim().isEmpty()) this.id = topicId.trim(); }

    public String getUnitId() { return unitId; }
    public void setUnitId(String unitId) { this.unitId = unitId; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    @JsonProperty("name")
    public String getName() { return (name != null && !name.trim().isEmpty()) ? name : "Topic"; }
    public void setName(String name) { if (name != null && !name.trim().isEmpty()) this.name = name.trim(); }

    @JsonProperty("topicName")
    public String getTopicName() { return getName(); }
    public void setTopicName(String topicName) { setName(topicName); }

    @JsonProperty("difficultyLevel")
    public Integer getDifficultyLevel() { return difficultyLevel != null ? difficultyLevel : 3; }
    public void setDifficultyLevel(Integer difficultyLevel) { if (difficultyLevel != null) this.difficultyLevel = difficultyLevel; }

    @JsonProperty("difficulty")
    public Integer getDifficulty() { return getDifficultyLevel(); }
    public void setDifficulty(Integer difficulty) { setDifficultyLevel(difficulty); }

    @JsonProperty("estimatedDurationMinutes")
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes != null ? estimatedDurationMinutes : 45; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { if (estimatedDurationMinutes != null) this.estimatedDurationMinutes = estimatedDurationMinutes; }

    @JsonProperty("estimatedMinutes")
    public Integer getEstimatedMinutes() { return getEstimatedDurationMinutes(); }
    public void setEstimatedMinutes(Integer estimatedMinutes) { setEstimatedDurationMinutes(estimatedMinutes); }

    @JsonProperty("completionStatus")
    public String getCompletionStatus() { return completionStatus != null ? completionStatus : "not_started"; }
    public void setCompletionStatus(String completionStatus) { if (completionStatus != null) this.completionStatus = completionStatus; }

    @JsonProperty("status")
    public String getStatus() { return getCompletionStatus(); }
    public void setStatus(String status) { setCompletionStatus(status); }

    @JsonProperty("importanceScore")
    public Integer getImportanceScore() { return importanceScore != null ? importanceScore : 3; }
    public void setImportanceScore(Integer importanceScore) { if (importanceScore != null) this.importanceScore = importanceScore; }

    @JsonProperty("importance")
    public Integer getImportance() { return getImportanceScore(); }
    public void setImportance(Integer importance) { setImportanceScore(importance); }

    public Integer getUnderstandingRating() { return understandingRating != null ? understandingRating : 0; }
    public void setUnderstandingRating(Integer understandingRating) { if (understandingRating != null) this.understandingRating = understandingRating; }

    public String getNotes() { return notes != null ? notes : ""; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getOrderIndex() { return orderIndex != null ? orderIndex : 0; }
    public void setOrderIndex(Integer orderIndex) { if (orderIndex != null) this.orderIndex = orderIndex; }

    @JsonProperty("priorityScore")
    public Double getPriorityScore() {
        int imp = importanceScore != null ? importanceScore : 3;
        int diff = difficultyLevel != null ? difficultyLevel : 3;
        return (double) (imp * diff);
    }
}
