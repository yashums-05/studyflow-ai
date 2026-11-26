package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String preferredStudyStartTime = "08:00";
    private String preferredStudyEndTime = "22:00";
    private Double availableDailyStudyHours = 6.0;
    private Integer preferredSessionDurationMinutes = 45;
    private Integer preferredBreakDurationMinutes = 10;
    private List<String> studyDays = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    private String learningStyle = "Visual & Practice";

    public User() {}

    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("uid")
    public String getUid() { return id; }
    public void setUid(String uid) { if (this.id == null || this.id.isEmpty()) this.id = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @JsonProperty("collegeName")
    public String getCollegeName() {
        return (collegeName != null && !collegeName.trim().isEmpty()) ? collegeName : "Engineering Institute";
    }
    public void setCollegeName(String collegeName) {
        if (collegeName != null && !collegeName.trim().isEmpty()) {
            this.collegeName = collegeName.trim();
        }
    }

    @JsonProperty("college")
    public String getCollege() {
        return getCollegeName();
    }
    public void setCollege(String college) {
        setCollegeName(college);
    }

    @JsonProperty("university")
    public String getUniversity() {
        return getCollegeName();
    }
    public void setUniversity(String university) {
        setCollegeName(university);
    }

    public String getDegree() {
        return (degree != null && !degree.trim().isEmpty()) ? degree : "B.Tech";
    }
    public void setDegree(String degree) {
        if (degree != null && !degree.trim().isEmpty()) {
            this.degree = degree.trim();
        }
    }

    public String getDepartment() {
        return (department != null && !department.trim().isEmpty()) ? department : "Computer Science";
    }
    public void setDepartment(String department) {
        if (department != null && !department.trim().isEmpty()) {
            this.department = department.trim();
        }
    }

    public Integer getSemester() { return semester != null ? semester : 6; }
    public void setSemester(Integer semester) { if (semester != null) this.semester = semester; }

    public String getAcademicYear() {
        return (academicYear != null && !academicYear.trim().isEmpty()) ? academicYear : "2026-2027";
    }
    public void setAcademicYear(String academicYear) {
        if (academicYear != null && !academicYear.trim().isEmpty()) {
            this.academicYear = academicYear.trim();
        }
    }

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
    public void setStudyDays(List<String> studyDays) { if (studyDays != null) this.studyDays = studyDays; }

    public String getLearningStyle() { return learningStyle; }
    public void setLearningStyle(String learningStyle) { this.learningStyle = learningStyle; }

    @JsonProperty("preferences")
    public Map<String, Object> getPreferences() {
        Map<String, Object> map = new HashMap<>();
        map.put("availableDays", studyDays != null ? studyDays : List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        map.put("studyStartTime", preferredStudyStartTime != null ? preferredStudyStartTime : "08:00");
        map.put("studyEndTime", preferredStudyEndTime != null ? preferredStudyEndTime : "22:00");
        map.put("preferredSessionDuration", preferredSessionDurationMinutes != null ? preferredSessionDurationMinutes : 45);
        map.put("shortBreakDuration", preferredBreakDurationMinutes != null ? preferredBreakDurationMinutes : 10);
        map.put("longBreakDuration", 30);
        map.put("maximumDailyStudyHours", availableDailyStudyHours != null ? availableDailyStudyHours : 6.0);
        map.put("preferredLearningStyle", learningStyle != null ? learningStyle : "Visual & Practice");
        return map;
    }

    @JsonProperty("preferences")
    public void setPreferences(Map<String, Object> map) {
        if (map == null) return;
        if (map.containsKey("studyStartTime") && map.get("studyStartTime") != null) {
            this.preferredStudyStartTime = map.get("studyStartTime").toString();
        }
        if (map.containsKey("studyEndTime") && map.get("studyEndTime") != null) {
            this.preferredStudyEndTime = map.get("studyEndTime").toString();
        }
        if (map.containsKey("preferredSessionDuration") && map.get("preferredSessionDuration") != null) {
            Object v = map.get("preferredSessionDuration");
            this.preferredSessionDurationMinutes = v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString());
        }
        if (map.containsKey("shortBreakDuration") && map.get("shortBreakDuration") != null) {
            Object v = map.get("shortBreakDuration");
            this.preferredBreakDurationMinutes = v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString());
        }
        if (map.containsKey("maximumDailyStudyHours") && map.get("maximumDailyStudyHours") != null) {
            Object v = map.get("maximumDailyStudyHours");
            this.availableDailyStudyHours = v instanceof Number ? ((Number) v).doubleValue() : Double.parseDouble(v.toString());
        }
        if (map.containsKey("preferredLearningStyle") && map.get("preferredLearningStyle") != null) {
            this.learningStyle = map.get("preferredLearningStyle").toString();
        }
        if (map.containsKey("availableDays") && map.get("availableDays") instanceof List) {
            this.studyDays = new ArrayList<>((List<String>) map.get("availableDays"));
        }
    }
}
