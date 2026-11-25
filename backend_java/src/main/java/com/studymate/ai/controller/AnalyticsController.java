package com.studymate.ai.controller;

import com.studymate.ai.dto.DashboardAnalyticsResponse;
import com.studymate.ai.dto.WeeklyAnalyticsResponse;
import com.studymate.ai.model.Subject;
import com.studymate.ai.repository.DataStore;
import com.studymate.ai.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final DataStore dataStore;

    public AnalyticsController(AnalyticsService analyticsService, DataStore dataStore) {
        this.analyticsService = analyticsService;
        this.dataStore = dataStore;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @GetMapping("/dashboard")
    public DashboardAnalyticsResponse getDashboard(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return analyticsService.getDashboardAnalytics(getUserId(auth));
    }

    @GetMapping("/weekly")
    public WeeklyAnalyticsResponse getWeekly(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return analyticsService.getWeeklyAnalytics(getUserId(auth));
    }

    @GetMapping("/subjects")
    public Map<String, Object> getSubjectAnalytics(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        List<Subject> subjects = dataStore.getAllSubjects(getUserId(auth));
        return Map.of(
                "subjects", subjects,
                "averageCompletion", 37.5,
                "highRiskSubjectsCount", 0
        );
    }
}
