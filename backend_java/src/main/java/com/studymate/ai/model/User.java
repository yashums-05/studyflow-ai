package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;
    private String email;
    private String fullName;
    private String collegeName = "Engineering Institute";
    private String degree = "B.Tech";
    private String department = "Computer Science";
    private Integer semester = 6;
    private String academicYear = "2026-2027";
    private String preferredWakeUpTime = "06:30";
    private String preferredStudyStartTime = "09:00";
    private String preferredStudyEndTime = "22:00";
    private Double availableDailyStudyHours = 4.0;
    private Integer preferredSessionDurationMinutes = 45;
    private Integer preferredBreakDurationMinutes = 10;
    private List<String> studyDays = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
    private String learningStyle = "Visual & Practical";

    public User() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public String getPreferredWakeUpTime() { return preferredWakeUpTime; }
    public void setPreferredWakeUpTime(String preferredWakeUpTime) { this.preferredWakeUpTime = preferredWakeUpTime; }
    public String getPreferredStudyStartTime() { return preferredStudyStartTime; }
    public void setPreferredStudyStartTime(String preferredStudyStartTime) { this.preferredStudyStartTime = preferredStudyStartTime; }
    public String getPreferredStudyEndTime() { return preferredStudyEndTime; }
    public void setPreferredStudyEndTime(String preferredStudyEndTime) { this.preferredStudyEndTime = preferredStudyEndTime; }
    public Double getAvailableDailyStudyHours() { return availableDailyStudyHours; }
    public void setAvailableDailyStudyHours(Double availableDailyStudyHours) { this.availableDailyStudyHours = availableDailyStudyHours; }
    public Integer getPreferredSessionDurationMinutes() { return preferredSessionDurationMinutes; }
    public void setPreferredSessionDurationMinutes(Integer preferredSessionDurationMinutes) { this.preferredSessionDurationMinutes = preferredSessionDurationMinutes; }
    public Integer getPreferredBreakDurationMinutes() { return preferredBreakDurationMinutes; }
    public void setPreferredBreakDurationMinutes(Integer preferredBreakDurationMinutes) { this.preferredBreakDurationMinutes = preferredBreakDurationMinutes; }
    public List<String> getStudyDays() { return studyDays; }
    public void setStudyDays(List<String> studyDays) { this.studyDays = studyDays; }
    public String getLearningStyle() { return learningStyle; }
    public void setLearningStyle(String learningStyle) { this.learningStyle = learningStyle; }
}
