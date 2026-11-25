package com.studymate.ai.controller;

import com.studymate.ai.dto.TimetableRequests;
import com.studymate.ai.model.StudySession;
import com.studymate.ai.model.Topic;
import com.studymate.ai.repository.DataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/study-sessions")
public class StudySessionController {
    private final DataStore dataStore;

    public StudySessionController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody Map<String, String> body) {
        String sessionId = body.get("sessionId");
        if (sessionId == null) return ResponseEntity.badRequest().body(Map.of("error", "sessionId is required"));

        Optional<StudySession> opt = dataStore.getSession(sessionId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        StudySession s = opt.get();
        s.setStatus("in_progress");
        dataStore.saveSession(s);
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeSession(@PathVariable String id, @RequestBody TimetableRequests.CompleteSession body) {
        Optional<StudySession> opt = dataStore.getSession(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        StudySession s = opt.get();
        s.setStatus("completed");
        s.setActualDurationMinutes(body.getActualMinutes());
        s.setUnderstandingRating(body.getUnderstandingRating());
        s.setNotes(body.getNotes());
        dataStore.saveSession(s);

        // Update associated topic status and rating
        if (s.getTopicId() != null) {
            Optional<Topic> tOpt = dataStore.getTopic(s.getTopicId());
            tOpt.ifPresent(t -> {
                t.setUnderstandingRating(body.getUnderstandingRating());
                t.setCompletionStatus(body.getTopicStatus() != null ? body.getTopicStatus() : "completed");
                dataStore.saveTopic(t);
            });
        }

        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<?> pauseSession(@PathVariable String id) {
        Optional<StudySession> opt = dataStore.getSession(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        StudySession s = opt.get();
        s.setStatus("scheduled");
        dataStore.saveSession(s);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/history")
    public List<StudySession> getHistory(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestParam(defaultValue = "50") int limit) {
        List<StudySession> all = dataStore.getAllSessions(getUserId(auth));
        return all.stream()
                .filter(s -> "completed".equalsIgnoreCase(s.getStatus()) || "missed".equalsIgnoreCase(s.getStatus()))
                .limit(limit)
                .toList();
    }
}
