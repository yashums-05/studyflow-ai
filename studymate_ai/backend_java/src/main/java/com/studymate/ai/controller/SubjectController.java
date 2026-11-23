package com.studymate.ai.controller;

import com.studymate.ai.model.Subject;
import com.studymate.ai.repository.DataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final DataStore dataStore;

    public SubjectController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private String getUserId(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @GetMapping
    public List<Subject> getSubjects(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return dataStore.getAllSubjects(getUserId(auth));
    }

    @PostMapping
    public Subject createSubject(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
                                 @RequestBody Subject subject) {
        subject.setUserId(getUserId(auth));
        return dataStore.saveSubject(subject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable String id) {
        Optional<Subject> s = dataStore.getSubject(id);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable String id, @RequestBody Subject updated) {
        Optional<Subject> opt = dataStore.getSubject(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Subject s = opt.get();
        if (updated.getName() != null) s.setName(updated.getName());
        if (updated.getCode() != null) s.setCode(updated.getCode());
        if (updated.getSemester() != null) s.setSemester(updated.getSemester());
        if (updated.getDifficultyLevel() != null) s.setDifficultyLevel(updated.getDifficultyLevel());
        if (updated.getExaminationDate() != null) s.setExaminationDate(updated.getExaminationDate());
        if (updated.getEstimatedStudyHours() != null) s.setEstimatedStudyHours(updated.getEstimatedStudyHours());
        if (updated.getColorHex() != null) s.setColorHex(updated.getColorHex());
        s.setUpdatedAt(Instant.now().toString());

        return ResponseEntity.ok(dataStore.saveSubject(s));
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteSubject(@PathVariable String id) {
        boolean deleted = dataStore.deleteSubject(id);
        return Map.of("success", deleted, "id", id);
    }
}
