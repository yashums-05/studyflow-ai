package com.studymate.ai.controller;

import com.studymate.ai.model.Exam;
import com.studymate.ai.repository.DataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {
    private final DataStore dataStore;

    public ExamController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @GetMapping
    public List<Exam> getExams(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return dataStore.getAllExams(getUserId(auth));
    }

    @PostMapping
    public Exam createExam(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody Exam exam) {
        exam.setUserId(getUserId(auth));
        return dataStore.saveExam(exam);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteExam(@PathVariable String id) {
        boolean ok = dataStore.deleteExam(id);
        return Map.of("success", ok, "id", id);
    }
}
