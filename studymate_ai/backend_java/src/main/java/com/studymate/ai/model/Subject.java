package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subject {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String name;
    private String code;
    private Integer semester = 6;
    private Integer difficultyLevel = 3;
    private String examinationDate;
    private Integer totalUnits = 0;
    private Double estimatedStudyHours = 30.0;
    private Double completionPercentage = 0.0;
    private String colorHex = "#7556D9";
    private String createdAt = Instant.now().toString();
    private String updatedAt = Instant.now().toString();

    public Subject() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public Integer getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Integer difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getExaminationDate() { return examinationDate; }
    public void setExaminationDate(String examinationDate) { this.examinationDate = examinationDate; }
    public Integer getTotalUnits() { return totalUnits; }
    public void setTotalUnits(Integer totalUnits) { this.totalUnits = totalUnits; }
    public Double getEstimatedStudyHours() { return estimatedStudyHours; }
    public void setEstimatedStudyHours(Double estimatedStudyHours) { this.estimatedStudyHours = estimatedStudyHours; }
    public Double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Double completionPercentage) { this.completionPercentage = completionPercentage; }
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
