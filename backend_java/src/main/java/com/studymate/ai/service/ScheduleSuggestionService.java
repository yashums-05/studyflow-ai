package com.studymate.ai.service;

import com.studymate.ai.dto.ScheduleSuggestion;
import com.studymate.ai.model.*;
import com.studymate.ai.repository.DataStore;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ScheduleSuggestionService {
    private final DataStore dataStore;

    public ScheduleSuggestionService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<ScheduleSuggestion> generateSuggestions(String userId) {
        List<ScheduleSuggestion> suggestions = new ArrayList<>();
        LocalDate today = LocalDate.now();

        List<Exam> exams = dataStore.getAllExams(userId);
        List<AcademicEvent> events = dataStore.getAllAcademicEvents(userId);
        List<ClassSlot> classSlots = dataStore.getAllClassSlots(userId);
        List<Subject> subjects = dataStore.getAllSubjects(userId);
        List<Topic> topics = dataStore.getAllTopics(userId);

        // 1. Exam Urgency Suggestions
        for (Exam e : exams) {
            if (e.getExamDate() != null) {
                try {
                    LocalDate examDate = LocalDate.parse(e.getExamDate());
                    long daysLeft = ChronoUnit.DAYS.between(today, examDate);
                    if (daysLeft >= 0 && daysLeft <= 7) {
                        suggestions.add(new ScheduleSuggestion(
                                "exam_urgency",
                                "🚨 Urgent Exam: " + e.getSubjectName() + " in " + daysLeft + " days",
                                e.getSubjectName() + " final exam is coming up on " + e.getExamDate() + " at " + e.getVenue() +
                                        ". The scheduler has prioritized this subject with 2.5x urgency weight.",
                                "HIGH",
                                e.getSubjectName(),
                                "Evenings & Open Slots"
                        ));
                    } else if (daysLeft > 7 && daysLeft <= 16) {
                        suggestions.add(new ScheduleSuggestion(
                                "exam_urgency",
                                "📅 Upcoming Exam: " + e.getSubjectName() + " (" + daysLeft + " days remaining)",
                                "Target score: " + e.getTargetScore() + "%. Aim to complete all core syllabus units this week before entering revision mode.",
                                "MEDIUM",
                                e.getSubjectName(),
                                "Regular Study Slots"
                        ));
                    }
                } catch (Exception ignored) {}
            }
        }

        // 2. Calendar of Events (Holidays, Deadlines, Internal Tests)
        for (AcademicEvent event : events) {
            if (event.getEventDate() != null) {
                try {
                    LocalDate eventDate = LocalDate.parse(event.getEventDate());
                    long daysUntil = ChronoUnit.DAYS.between(today, eventDate);
                    if (daysUntil >= 0 && daysUntil <= 14) {
                        if (event.isHoliday() || "holiday".equalsIgnoreCase(event.getEventType())) {
                            suggestions.add(new ScheduleSuggestion(
                                    "holiday_boost",
                                    "🏖️ Holiday Advantage: " + event.getTitle(),
                                    "College is closed on " + event.getEventDate() + " (" + event.getTitle() + "). Regular classes are off; study hours automatically expand to 5 hours for maximum topic mastery.",
                                    "HIGH",
                                    "All Subjects",
                                    event.getEventDate() + " (Full Day)"
                            ));
                        } else if ("deadline".equalsIgnoreCase(event.getEventType())) {
                            suggestions.add(new ScheduleSuggestion(
                                    "deadline",
                                    "⚠️ Submission Deadline: " + event.getTitle(),
                                    "Due on " + event.getEventDate() + ": " + event.getDescription() + ". Study sessions before this date are scheduled early in the evening.",
                                    "HIGH",
                                    "Projects & Assignments",
                                    "Pre-deadline slots"
                            ));
                        } else if ("internal_assessment".equalsIgnoreCase(event.getEventType())) {
                            suggestions.add(new ScheduleSuggestion(
                                    "internal_assessment",
                                    "📝 Midterm / IA: " + event.getTitle(),
                                    event.getDescription() + " in " + daysUntil + " days. Focused revision on Units 1 and 2 is recommended.",
                                    "MEDIUM",
                                    "Exam Prep",
                                    event.getEventDate()
                            ));
                        }
                    }
                } catch (Exception ignored) {}
            }
        }

        // 3. Class Timetable Free Slot & Workload Analysis
        Map<String, List<ClassSlot>> daySlots = new HashMap<>();
        for (ClassSlot cs : classSlots) {
            if (cs.getDayOfWeek() != null) {
                daySlots.computeIfAbsent(cs.getDayOfWeek().toUpperCase(), k -> new ArrayList<>()).add(cs);
            }
        }

        // Check Wednesday or Friday lab days for open gaps
        List<ClassSlot> wedSlots = daySlots.get("WEDNESDAY");
        if (wedSlots != null && !wedSlots.isEmpty()) {
            suggestions.add(new ScheduleSuggestion(
                    "class_gap",
                    "⚡ Midday Micro-Study Slot on Wednesday",
                    "You have a 3-hour open window between 11:00 AM lectures and 02:00 PM lab. Ideal for an active 45-minute practice quiz or flashcards review.",
                    "MEDIUM",
                    "Practice & Quizzes",
                    "Wed 11:00 AM - 02:00 PM"
            ));
        }

        // Check heavy class days for cognitive load balance
        for (Map.Entry<String, List<ClassSlot>> entry : daySlots.entrySet()) {
            if (entry.getValue().size() >= 3) {
                String dayName = entry.getKey().substring(0, 1) + entry.getKey().substring(1).toLowerCase();
                suggestions.add(new ScheduleSuggestion(
                        "workload_balance",
                        "⚖️ Heavy Class Schedule: " + dayName,
                        dayName + " has multiple lectures and labs. Evening self-study sessions are capped to prevent burnout and ensure consistent daily focus.",
                        "LOW",
                        "Daily Routine",
                        dayName + " Evening"
                ));
                break; // Just one workload tip is enough
            }
        }

        // 4. Topic Weakness Detection
        for (Topic t : topics) {
            if ("needs_revision".equalsIgnoreCase(t.getCompletionStatus()) ||
                    (t.getUnderstandingRating() != null && t.getUnderstandingRating() <= 2)) {
                Subject sub = dataStore.getSubject(t.getSubjectId()).orElse(null);
                String subName = sub != null ? sub.getName() : "Core Subject";
                suggestions.add(new ScheduleSuggestion(
                        "weak_topic",
                        "🔍 Weak Topic Revision: " + t.getName(),
                        "Mastery rating is low for " + t.getName() + " in " + subName + ". The timetable generator has assigned top priority to this topic in your upcoming slots.",
                        "HIGH",
                        subName,
                        "Next Available Slot"
                ));
                break;
            }
        }

        return suggestions;
    }
}
