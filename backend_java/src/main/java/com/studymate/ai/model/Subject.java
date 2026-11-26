package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subject {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String name = "Subject";
    private String code = "SUB101";
    private Integer semester = 6;
    private Integer difficultyLevel = 3;
    private String examinationDate;
    private Integer totalUnits = 0;
    private Double estimatedStudyHours = 30.0;
    private Double completionPercentage = 0.0;
    private String colorHex = "#7556D9";
    private String createdAt = Instant.now().toString();
    private String updatedAt = Instant.now().toString();

    private List<Unit> units = new ArrayList<>();
    private Integer totalTopics = 0;
    private Integer completedTopics = 0;

    public Subject() {}

    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("subjectId")
    public String getSubjectId() { return id; }
    public void setSubjectId(String subjectId) { if (subjectId != null && !subjectId.trim().isEmpty()) this.id = subjectId.trim(); }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @JsonProperty("name")
    public String getName() { return (name != null && !name.trim().isEmpty()) ? name : "Subject"; }
    public void setName(String name) { if (name != null && !name.trim().isEmpty()) this.name = name.trim(); }

    @JsonProperty("subjectName")
    public String getSubjectName() { return getName(); }
    public void setSubjectName(String subjectName) { setName(subjectName); }

    @JsonProperty("code")
    public String getCode() { return (code != null && !code.trim().isEmpty()) ? code : "SUB101"; }
    public void setCode(String code) { if (code != null && !code.trim().isEmpty()) this.code = code.trim(); }

    @JsonProperty("subjectCode")
    public String getSubjectCode() { return getCode(); }
    public void setSubjectCode(String subjectCode) { setCode(subjectCode); }

    public Integer getSemester() { return semester != null ? semester : 6; }
    public void setSemester(Integer semester) { if (semester != null) this.semester = semester; }

    @JsonProperty("difficultyLevel")
    public Integer getDifficultyLevel() { return difficultyLevel != null ? difficultyLevel : 3; }
    public void setDifficultyLevel(Integer difficultyLevel) { if (difficultyLevel != null) this.difficultyLevel = difficultyLevel; }

    @JsonProperty("difficulty")
    public Integer getDifficulty() { return getDifficultyLevel(); }
    public void setDifficulty(Integer difficulty) { setDifficultyLevel(difficulty); }

    @JsonProperty("examinationDate")
    public String getExaminationDate() { return examinationDate; }
    public void setExaminationDate(String examinationDate) { this.examinationDate = examinationDate; }

    @JsonProperty("examDate")
    public String getExamDate() { return examinationDate; }
    public void setExamDate(String examDate) { this.examinationDate = examDate; }

    public Integer getTotalUnits() {
        if (units != null && !units.isEmpty()) return units.size();
        return totalUnits != null ? totalUnits : 0;
    }
    public void setTotalUnits(Integer totalUnits) { if (totalUnits != null) this.totalUnits = totalUnits; }

    @JsonProperty("estimatedStudyHours")
    public Double getEstimatedStudyHours() { return estimatedStudyHours != null ? estimatedStudyHours : 30.0; }
    public void setEstimatedStudyHours(Double estimatedStudyHours) { if (estimatedStudyHours != null) this.estimatedStudyHours = estimatedStudyHours; }

    @JsonProperty("estimatedHours")
    public Double getEstimatedHours() { return getEstimatedStudyHours(); }
    public void setEstimatedHours(Double estimatedHours) { setEstimatedStudyHours(estimatedHours); }

    public Double getCompletionPercentage() { return completionPercentage != null ? completionPercentage : 0.0; }
    public void setCompletionPercentage(Double completionPercentage) { if (completionPercentage != null) this.completionPercentage = completionPercentage; }

    public String getColorHex() { return (colorHex != null && !colorHex.trim().isEmpty()) ? colorHex : "#7556D9"; }
    public void setColorHex(String colorHex) { if (colorHex != null && !colorHex.trim().isEmpty()) this.colorHex = colorHex.trim(); }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @JsonProperty("units")
    public List<Unit> getUnits() { return units != null ? units : new ArrayList<>(); }
    public void setUnits(List<Unit> units) { this.units = units != null ? units : new ArrayList<>(); }

    @JsonProperty("totalTopics")
    public Integer getTotalTopics() { return totalTopics != null ? totalTopics : 0; }
    public void setTotalTopics(Integer totalTopics) { if (totalTopics != null) this.totalTopics = totalTopics; }

    @JsonProperty("completedTopics")
    public Integer getCompletedTopics() { return completedTopics != null ? completedTopics : 0; }
    public void setCompletedTopics(Integer completedTopics) { if (completedTopics != null) this.completedTopics = completedTopics; }
}
