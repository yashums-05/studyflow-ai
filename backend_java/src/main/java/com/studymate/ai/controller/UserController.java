package com.studymate.ai.controller;

import com.studymate.ai.model.User;
import com.studymate.ai.repository.DataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final DataStore dataStore;

    public UserController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private String getUserId(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @GetMapping("/profile")
    public User getProfile(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return dataStore.getUser(getUserId(auth));
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
                              @RequestBody Map<String, Object> req) {
        String uid = getUserId(auth);
        User current = dataStore.getUser(uid);

        if (req.containsKey("fullName") && req.get("fullName") != null) {
            current.setFullName(req.get("fullName").toString().trim());
        }

        // Support collegeName, college, university
        String col = null;
        if (req.containsKey("collegeName") && req.get("collegeName") != null) {
            col = req.get("collegeName").toString();
        } else if (req.containsKey("college") && req.get("college") != null) {
            col = req.get("college").toString();
        } else if (req.containsKey("university") && req.get("university") != null) {
            col = req.get("university").toString();
        }
        if (col != null && !col.trim().isEmpty()) {
            current.setCollegeName(col.trim());
        }

        if (req.containsKey("degree") && req.get("degree") != null) {
            current.setDegree(req.get("degree").toString().trim());
        }
        if (req.containsKey("department") && req.get("department") != null) {
            current.setDepartment(req.get("department").toString().trim());
        }
        if (req.containsKey("semester") && req.get("semester") != null) {
            Object s = req.get("semester");
            current.setSemester(s instanceof Number ? ((Number) s).intValue() : Integer.parseInt(s.toString()));
        }
        if (req.containsKey("academicYear") && req.get("academicYear") != null) {
            current.setAcademicYear(req.get("academicYear").toString().trim());
        }

        // Support nested preferences object from Flutter onboarding
        if (req.containsKey("preferences") && req.get("preferences") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> p = (Map<String, Object>) req.get("preferences");
            current.setPreferences(p);
        }

        // Also support flat preference fields
        if (req.containsKey("availableDailyStudyHours") && req.get("availableDailyStudyHours") != null) {
            Object v = req.get("availableDailyStudyHours");
            current.setAvailableDailyStudyHours(v instanceof Number ? ((Number) v).doubleValue() : Double.parseDouble(v.toString()));
        }
        if (req.containsKey("maximumDailyStudyHours") && req.get("maximumDailyStudyHours") != null) {
            Object v = req.get("maximumDailyStudyHours");
            current.setAvailableDailyStudyHours(v instanceof Number ? ((Number) v).doubleValue() : Double.parseDouble(v.toString()));
        }
        if (req.containsKey("preferredSessionDurationMinutes") && req.get("preferredSessionDurationMinutes") != null) {
            Object v = req.get("preferredSessionDurationMinutes");
            current.setPreferredSessionDurationMinutes(v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString()));
        }
        if (req.containsKey("preferredSessionDuration") && req.get("preferredSessionDuration") != null) {
            Object v = req.get("preferredSessionDuration");
            current.setPreferredSessionDurationMinutes(v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString()));
        }
        if (req.containsKey("preferredBreakDurationMinutes") && req.get("preferredBreakDurationMinutes") != null) {
            Object v = req.get("preferredBreakDurationMinutes");
            current.setPreferredBreakDurationMinutes(v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString()));
        }
        if (req.containsKey("shortBreakDuration") && req.get("shortBreakDuration") != null) {
            Object v = req.get("shortBreakDuration");
            current.setPreferredBreakDurationMinutes(v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(v.toString()));
        }
        if (req.containsKey("studyStartTime") && req.get("studyStartTime") != null) {
            current.setPreferredStudyStartTime(req.get("studyStartTime").toString());
        }
        if (req.containsKey("studyEndTime") && req.get("studyEndTime") != null) {
            current.setPreferredStudyEndTime(req.get("studyEndTime").toString());
        }

        return dataStore.saveUser(current);
    }

    @PostMapping("/study-preferences")
    public User updatePreferences(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
                                  @RequestBody Map<String, Object> prefs) {
        String uid = getUserId(auth);
        User current = dataStore.getUser(uid);
        current.setPreferences(prefs);
        return dataStore.saveUser(current);
    }
}
