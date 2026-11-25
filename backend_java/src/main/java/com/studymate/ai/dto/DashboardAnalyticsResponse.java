package com.studymate.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardAnalyticsResponse {
    private int totalSubjects = 0;
    private int totalTopics = 0;
    private int completedTopics = 0;
    private int pendingTopics = 0;
    private double overallCompletionPercentage = 0.0;
    private double plannedStudyHoursToday = 0.0;
    private double completedStudyHoursToday = 0.0;
    private int remainingSessionsToday = 0;
    private int studyStreakDays = 0;
    private double weeklyTotalStudyHours = 0.0;
    private int upcomingExamsCount = 0;
    private List<String> aiRecommendations = new ArrayList<>();

    public DashboardAnalyticsResponse() {}

    public int getTotalSubjects() { return totalSubjects; }
    public void setTotalSubjects(int totalSubjects) { this.totalSubjects = totalSubjects; }
    public int getTotalTopics() { return totalTopics; }
    public void setTotalTopics(int totalTopics) { this.totalTopics = totalTopics; }
    public int getCompletedTopics() { return completedTopics; }
    public void setCompletedTopics(int completedTopics) { this.completedTopics = completedTopics; }
    public int getPendingTopics() { return pendingTopics; }
    public void setPendingTopics(int pendingTopics) { this.pendingTopics = pendingTopics; }
    public double getOverallCompletionPercentage() { return overallCompletionPercentage; }
    public void setOverallCompletionPercentage(double overallCompletionPercentage) { this.overallCompletionPercentage = overallCompletionPercentage; }
    public double getPlannedStudyHoursToday() { return plannedStudyHoursToday; }
    public void setPlannedStudyHoursToday(double plannedStudyHoursToday) { this.plannedStudyHoursToday = plannedStudyHoursToday; }
    public double getCompletedStudyHoursToday() { return completedStudyHoursToday; }
    public void setCompletedStudyHoursToday(double completedStudyHoursToday) { this.completedStudyHoursToday = completedStudyHoursToday; }
    public int getRemainingSessionsToday() { return remainingSessionsToday; }
    public void setRemainingSessionsToday(int remainingSessionsToday) { this.remainingSessionsToday = remainingSessionsToday; }
    public int getStudyStreakDays() { return studyStreakDays; }
    public void setStudyStreakDays(int studyStreakDays) { this.studyStreakDays = studyStreakDays; }
    public double getWeeklyTotalStudyHours() { return weeklyTotalStudyHours; }
    public void setWeeklyTotalStudyHours(double weeklyTotalStudyHours) { this.weeklyTotalStudyHours = weeklyTotalStudyHours; }
    public int getUpcomingExamsCount() { return upcomingExamsCount; }
    public void setUpcomingExamsCount(int upcomingExamsCount) { this.upcomingExamsCount = upcomingExamsCount; }
    public List<String> getAiRecommendations() { return aiRecommendations; }
    public void setAiRecommendations(List<String> aiRecommendations) { this.aiRecommendations = aiRecommendations; }
}
