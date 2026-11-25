package com.studymate.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeeklyAnalyticsResponse {
    private double totalHoursThisWeek = 0.0;
    private Map<String, Double> dailyHours = new LinkedHashMap<>();
    private double completionRate = 0.0;
    private double efficiencyScore = 85.0;

    public WeeklyAnalyticsResponse() {
        dailyHours.put("Mon", 0.0);
        dailyHours.put("Tue", 0.0);
        dailyHours.put("Wed", 0.0);
        dailyHours.put("Thu", 0.0);
        dailyHours.put("Fri", 0.0);
        dailyHours.put("Sat", 0.0);
        dailyHours.put("Sun", 0.0);
    }

    public double getTotalHoursThisWeek() { return totalHoursThisWeek; }
    public void setTotalHoursThisWeek(double totalHoursThisWeek) { this.totalHoursThisWeek = totalHoursThisWeek; }
    public Map<String, Double> getDailyHours() { return dailyHours; }
    public void setDailyHours(Map<String, Double> dailyHours) { this.dailyHours = dailyHours; }
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public double getEfficiencyScore() { return efficiencyScore; }
    public void setEfficiencyScore(double efficiencyScore) { this.efficiencyScore = efficiencyScore; }
}
