package com.studymate.ai.controller;

import com.studymate.ai.model.User;
import com.studymate.ai.repository.DataStore;
import org.springframework.web.bind.annotation.*;

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
                              @RequestBody User updated) {
        String uid = getUserId(auth);
        User current = dataStore.getUser(uid);
        if (updated.getFullName() != null) current.setFullName(updated.getFullName());
        if (updated.getCollegeName() != null) current.setCollegeName(updated.getCollegeName());
        if (updated.getDegree() != null) current.setDegree(updated.getDegree());
        if (updated.getDepartment() != null) current.setDepartment(updated.getDepartment());
        if (updated.getSemester() != null) current.setSemester(updated.getSemester());
        if (updated.getAcademicYear() != null) current.setAcademicYear(updated.getAcademicYear());
        return dataStore.saveUser(current);
    }

    @PostMapping("/study-preferences")
    public User updatePreferences(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
                                  @RequestBody User prefs) {
        String uid = getUserId(auth);
        User current = dataStore.getUser(uid);
        if (prefs.getPreferredWakeUpTime() != null) current.setPreferredWakeUpTime(prefs.getPreferredWakeUpTime());
        if (prefs.getPreferredStudyStartTime() != null) current.setPreferredStudyStartTime(prefs.getPreferredStudyStartTime());
        if (prefs.getPreferredStudyEndTime() != null) current.setPreferredStudyEndTime(prefs.getPreferredStudyEndTime());
        if (prefs.getAvailableDailyStudyHours() != null) current.setAvailableDailyStudyHours(prefs.getAvailableDailyStudyHours());
        if (prefs.getPreferredSessionDurationMinutes() != null) current.setPreferredSessionDurationMinutes(prefs.getPreferredSessionDurationMinutes());
        if (prefs.getPreferredBreakDurationMinutes() != null) current.setPreferredBreakDurationMinutes(prefs.getPreferredBreakDurationMinutes());
        if (prefs.getStudyDays() != null) current.setStudyDays(prefs.getStudyDays());
        if (prefs.getLearningStyle() != null) current.setLearningStyle(prefs.getLearningStyle());
        return dataStore.saveUser(current);
    }
}
