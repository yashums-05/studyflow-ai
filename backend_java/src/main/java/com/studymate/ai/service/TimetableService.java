package com.studymate.ai.service;

import com.studymate.ai.dto.TimetableRequests;
import com.studymate.ai.model.*;
import com.studymate.ai.repository.DataStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TimetableService {
    private final DataStore dataStore;

    // Configurable weights: P = 0.35D + 0.30U + 0.20W + 0.15I
    private static final double WEIGHT_DIFFICULTY = 0.35;
    private static final double WEIGHT_URGENCY = 0.30;
    private static final double WEIGHT_WEAKNESS = 0.20;
    private static final double WEIGHT_IMPORTANCE = 0.15;

    public TimetableService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<StudySession> generateTimetable(String userId, TimetableRequests.Generate request) {
        User user = dataStore.getUser(userId);
        List<Subject> subjects = dataStore.getAllSubjects(userId);
        List<Exam> exams = dataStore.getAllExams(userId);
        List<Topic> allTopics = dataStore.getAllTopics(userId);

        LocalDate startDate = request.getStartDate() != null && !request.getStartDate().isEmpty()
                ? LocalDate.parse(request.getStartDate())
                : LocalDate.now();

        int daysCount = request.getDays() > 0 ? request.getDays() : 7;
        double maxDailyHours = request.getMaxDailyHours() != null
                ? request.getMaxDailyHours()
                : (user.getAvailableDailyStudyHours() != null ? user.getAvailableDailyStudyHours() : 4.0);

        int sessionDuration = user.getPreferredSessionDurationMinutes() != null ? user.getPreferredSessionDurationMinutes() : 45;
        int breakDuration = user.getPreferredBreakDurationMinutes() != null ? user.getPreferredBreakDurationMinutes() : 10;

        // Clear existing scheduled sessions from startDate
        dataStore.clearScheduledSessions(userId, startDate.toString());

        // Map subject exam dates for urgency
        Map<String, LocalDate> subjectExamDates = new HashMap<>();
        for (Exam e : exams) {
            if (e.getExamDate() != null) {
                try {
                    subjectExamDates.put(e.getSubjectId(), LocalDate.parse(e.getExamDate()));
                } catch (Exception ignored) {}
            }
        }
        for (Subject s : subjects) {
            if (s.getExaminationDate() != null && !subjectExamDates.containsKey(s.getId())) {
                try {
                    subjectExamDates.put(s.getId(), LocalDate.parse(s.getExaminationDate()));
                } catch (Exception ignored) {}
            }
        }

        // Map subject names
        Map<String, String> subjectNames = new HashMap<>();
        for (Subject s : subjects) {
            subjectNames.put(s.getId(), s.getName());
        }

        // Calculate Priority Score for each topic
        List<ScoredTopic> scoredTopics = new ArrayList<>();
        for (Topic t : allTopics) {
            if ("completed".equalsIgnoreCase(t.getCompletionStatus()) && !request.isIncludeRevision()) {
                continue;
            }
            double score = calculatePriorityScore(t, subjectExamDates.get(t.getSubjectId()), startDate);
            scoredTopics.add(new ScoredTopic(t, score));
        }

        // Sort descending by priority score
        scoredTopics.sort((a, b) -> Double.compare(b.score, a.score));

        // Generate sessions across the days
        List<StudySession> generatedSessions = new ArrayList<>();
        int maxSessionsPerDay = Math.max(1, (int) Math.floor((maxDailyHours * 60) / (sessionDuration + breakDuration)));
        LocalTime dayStartTime = LocalTime.of(9, 0);
        try {
            if (user.getPreferredStudyStartTime() != null) {
                dayStartTime = LocalTime.parse(user.getPreferredStudyStartTime());
            }
        } catch (Exception ignored) {}

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        int topicIdx = 0;

        for (int d = 0; d < daysCount; d++) {
            LocalDate currentDate = startDate.plusDays(d);
            LocalTime currentSlotTime = dayStartTime;

            for (int s = 0; s < maxSessionsPerDay; s++) {
                if (scoredTopics.isEmpty()) break;

                ScoredTopic chosen = scoredTopics.get(topicIdx % scoredTopics.size());
                topicIdx++;

                Topic topic = chosen.topic;
                LocalTime slotEndTime = currentSlotTime.plusMinutes(sessionDuration);

                StudySession session = new StudySession();
                session.setId("sess_" + UUID.randomUUID().toString().substring(0, 8));
                session.setUserId(userId);
                session.setSubjectId(topic.getSubjectId());
                session.setSubjectName(subjectNames.getOrDefault(topic.getSubjectId(), "General Subject"));
                session.setUnitId(topic.getUnitId());
                session.setTopicId(topic.getId());
                session.setTopicName(topic.getName());
                session.setSessionDate(currentDate.toString());
                session.setStartTime(currentSlotTime.format(timeFmt));
                session.setEndTime(slotEndTime.format(timeFmt));
                session.setEstimatedDurationMinutes(sessionDuration);
                session.setPriorityScore(Math.round(chosen.score * 10.0) / 10.0);
                session.setStatus("scheduled");

                if ("completed".equalsIgnoreCase(topic.getCompletionStatus()) || "needs_revision".equalsIgnoreCase(topic.getCompletionStatus())) {
                    session.setSessionType("revision");
                } else {
                    session.setSessionType("learning");
                }

                generatedSessions.add(session);

                // Advance by session + break
                currentSlotTime = slotEndTime.plusMinutes(breakDuration);
            }
        }

        // Persist generated sessions
        dataStore.saveSessions(generatedSessions);
        return generatedSessions;
    }

    public double calculatePriorityScore(Topic topic, LocalDate examDate, LocalDate currentDate) {
        // D: Difficulty (0-100)
        double d = (topic.getDifficultyLevel() != null ? topic.getDifficultyLevel() : 3) * 20.0;

        // U: Urgency (0-100)
        double u = 30.0;
        if (examDate != null) {
            long daysUntilExam = ChronoUnit.DAYS.between(currentDate, examDate);
            if (daysUntilExam <= 3) u = 100.0;
            else if (daysUntilExam <= 7) u = 85.0;
            else if (daysUntilExam <= 14) u = 70.0;
            else if (daysUntilExam <= 30) u = 50.0;
            else u = 25.0;
        }

        // W: Weakness (0-100)
        double w;
        if ("needs_revision".equalsIgnoreCase(topic.getCompletionStatus())) {
            w = 90.0;
        } else if ("not_started".equalsIgnoreCase(topic.getCompletionStatus())) {
            w = 70.0;
        } else if ("in_progress".equalsIgnoreCase(topic.getCompletionStatus())) {
            w = 50.0;
        } else {
            int rating = topic.getUnderstandingRating() != null ? topic.getUnderstandingRating() : 0;
            w = Math.max(10.0, (5 - rating) * 20.0);
        }

        // I: Importance (0-100)
        double i = (topic.getImportanceScore() != null ? topic.getImportanceScore() : 3) * 20.0;

        // Final Priority Score: P = 0.35D + 0.30U + 0.20W + 0.15I
        return (WEIGHT_DIFFICULTY * d) + (WEIGHT_URGENCY * u) + (WEIGHT_WEAKNESS * w) + (WEIGHT_IMPORTANCE * i);
    }

    public List<StudySession> rescheduleSession(String userId, TimetableRequests.Reschedule request) {
        Optional<StudySession> missedOpt = dataStore.getSession(request.getMissedSessionId());
        if (missedOpt.isEmpty()) {
            return Collections.emptyList();
        }

        StudySession missed = missedOpt.get();
        missed.setStatus("rescheduled");
        missed.setNotes("Rescheduled: " + request.getReason());
        dataStore.saveSession(missed);

        LocalDate targetDate = request.getTargetDate() != null && !request.getTargetDate().isEmpty()
                ? LocalDate.parse(request.getTargetDate())
                : LocalDate.now().plusDays(1);

        StudySession replacement = new StudySession();
        replacement.setId("sess_" + UUID.randomUUID().toString().substring(0, 8));
        replacement.setUserId(userId);
        replacement.setSubjectId(missed.getSubjectId());
        replacement.setSubjectName(missed.getSubjectName());
        replacement.setUnitId(missed.getUnitId());
        replacement.setTopicId(missed.getTopicId());
        replacement.setTopicName(missed.getTopicName());
        replacement.setSessionDate(targetDate.toString());
        replacement.setStartTime("17:00");
        replacement.setEndTime("17:45");
        replacement.setEstimatedDurationMinutes(missed.getEstimatedDurationMinutes());
        replacement.setPriorityScore(missed.getPriorityScore() + 5.0);
        replacement.setSessionType("learning");
        replacement.setStatus("scheduled");
        replacement.setNotes("Adapted from missed session (" + missed.getSessionDate() + ")");

        dataStore.saveSession(replacement);
        return dataStore.getSessionsByDate(userId, targetDate.toString());
    }

    private static class ScoredTopic {
        final Topic topic;
        final double score;

        ScoredTopic(Topic topic, double score) {
            this.topic = topic;
            this.score = score;
        }
    }
}
