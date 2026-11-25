package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Exam {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String subjectId;
    private String subjectName;
    private String examDate;
    private String examType = "Semester End";
    private String venue = "Main Exam Hall";
    private Integer targetScore = 85;
    private String notes = "";

    public Exam() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getExamDate() { return examDate; }
    public void setExamDate(String examDate) { this.examDate = examDate; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getTargetScore() { return targetScore; }
    public void setTargetScore(Integer targetScore) { this.targetScore = targetScore; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
