package com.studymate.ai.controller;

import com.studymate.ai.dto.TimetableRequests;
import com.studymate.ai.model.StudySession;
import com.studymate.ai.repository.DataStore;
import com.studymate.ai.service.TimetableService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/timetable")
public class TimetableController {
    private final TimetableService timetableService;
    private final DataStore dataStore;

    public TimetableController(TimetableService timetableService, DataStore dataStore) {
        this.timetableService = timetableService;
        this.dataStore = dataStore;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @PostMapping("/generate")
    public List<StudySession> generateTimetable(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody(required = false) TimetableRequests.Generate request) {
        if (request == null) request = new TimetableRequests.Generate();
        return timetableService.generateTimetable(getUserId(auth), request);
    }

    @GetMapping("/daily")
    public List<StudySession> getDailyTimetable(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestParam(required = false) String date) {
        String targetDate = (date != null && !date.isEmpty()) ? date : LocalDate.now().toString();
        List<StudySession> sessions = dataStore.getSessionsByDate(getUserId(auth), targetDate);
        if (sessions.isEmpty()) {
            // Auto generate if none exists for user
            TimetableRequests.Generate req = new TimetableRequests.Generate();
            req.setStartDate(targetDate);
            req.setDays(7);
            timetableService.generateTimetable(getUserId(auth), req);
            sessions = dataStore.getSessionsByDate(getUserId(auth), targetDate);
        }
        return sessions;
    }

    @GetMapping("/weekly")
    public List<StudySession> getWeeklyTimetable(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        List<StudySession> all = dataStore.getAllSessions(getUserId(auth));
        if (all.isEmpty()) {
            TimetableRequests.Generate req = new TimetableRequests.Generate();
            req.setStartDate(LocalDate.now().toString());
            req.setDays(7);
            return timetableService.generateTimetable(getUserId(auth), req);
        }
        return all;
    }

    @PostMapping("/reschedule")
    public List<StudySession> rescheduleSession(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody TimetableRequests.Reschedule request) {
        return timetableService.rescheduleSession(getUserId(auth), request);
    }
}
