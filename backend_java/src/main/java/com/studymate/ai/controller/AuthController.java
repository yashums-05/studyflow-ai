package com.studymate.ai.controller;

import com.studymate.ai.model.User;
import com.studymate.ai.repository.DataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final DataStore dataStore;

    public AuthController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> req) {
        String email = (String) req.get("email");
        String fullName = (String) req.get("fullName");
        
        String collegeName = (String) req.get("collegeName");
        if (collegeName == null || collegeName.trim().isEmpty()) {
            collegeName = (String) req.get("college");
        }
        if (collegeName == null || collegeName.trim().isEmpty()) {
            collegeName = (String) req.get("university");
        }

        String degree = (String) req.get("degree");
        String department = (String) req.get("department");
        String academicYear = (String) req.get("academicYear");
        Object semObj = req.get("semester");
        Integer semester = semObj instanceof Number ? ((Number) semObj).intValue() : 6;

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        email = email.trim().toLowerCase();

        Optional<User> existing = dataStore.findUserByEmail(email);
        User user;
        if (existing.isPresent()) {
            user = existing.get();
        } else {
            user = new User();
            user.setId("user_" + UUID.randomUUID().toString().substring(0, 8));
        }

        user.setEmail(email);
        if (fullName != null && !fullName.trim().isEmpty()) user.setFullName(fullName.trim());
        if (collegeName != null && !collegeName.trim().isEmpty()) user.setCollegeName(collegeName.trim());
        if (degree != null && !degree.trim().isEmpty()) user.setDegree(degree.trim());
        if (department != null && !department.trim().isEmpty()) user.setDepartment(department.trim());
        if (academicYear != null && !academicYear.trim().isEmpty()) user.setAcademicYear(academicYear.trim());
        if (semester != null) user.setSemester(semester);

        if (req.containsKey("preferences") && req.get("preferences") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> prefs = (Map<String, Object>) req.get("preferences");
            user.setPreferences(prefs);
        }

        dataStore.saveUser(user);

        // Pre-fetch or initialize subjects for this new account
        dataStore.getAllSubjects(user.getId());

        Map<String, Object> res = new HashMap<>();
        res.put("token", user.getId());
        res.put("user", user);
        res.put("message", "Account registered successfully");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> req) {
        String email = (String) req.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }
        email = email.trim().toLowerCase();

        Optional<User> existing = dataStore.findUserByEmail(email);
        User user;
        if (existing.isPresent()) {
            user = existing.get();
        } else {
            user = new User();
            user.setId("user_" + UUID.randomUUID().toString().substring(0, 8));
            user.setEmail(email);
            String name = email.split("@")[0];
            user.setFullName(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            
            String collegeName = (String) req.get("collegeName");
            if (collegeName == null || collegeName.trim().isEmpty()) collegeName = (String) req.get("college");
            if (collegeName == null || collegeName.trim().isEmpty()) collegeName = (String) req.get("university");
            if (collegeName != null && !collegeName.trim().isEmpty()) user.setCollegeName(collegeName.trim());
            else user.setCollegeName("Engineering Institute");

            String department = (String) req.get("department");
            if (department != null && !department.trim().isEmpty()) user.setDepartment(department.trim());
            else user.setDepartment("Computer Science");

            String degree = (String) req.get("degree");
            if (degree != null && !degree.trim().isEmpty()) user.setDegree(degree.trim());
            else user.setDegree("B.Tech");

            Object semObj = req.get("semester");
            user.setSemester(semObj instanceof Number ? ((Number) semObj).intValue() : 6);
            user.setAcademicYear("2026-2027");

            dataStore.saveUser(user);
        }

        // Initialize user subjects if needed
        dataStore.getAllSubjects(user.getId());

        Map<String, Object> res = new HashMap<>();
        res.put("token", user.getId());
        res.put("user", user);
        res.put("message", "Login successful");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
