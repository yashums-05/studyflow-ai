package com.studymate.ai.controller;

import com.studymate.ai.dto.ScheduleSuggestion;
import com.studymate.ai.model.*;
import com.studymate.ai.repository.DataStore;
import com.studymate.ai.service.DocumentParserService;
import com.studymate.ai.service.ScheduleSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/academic")
public class AcademicHubController {

    private final DataStore dataStore;
    private final ScheduleSuggestionService suggestionService;
    private final DocumentParserService documentParserService;

    public AcademicHubController(DataStore dataStore, ScheduleSuggestionService suggestionService, DocumentParserService documentParserService) {
        this.dataStore = dataStore;
        this.suggestionService = suggestionService;
        this.documentParserService = documentParserService;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    // --- 1. Class Timetable Endpoints ---
    @GetMapping("/class-timetable")
    public List<ClassSlot> getClassTimetable(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return dataStore.getAllClassSlots(getUserId(auth));
    }

    @PostMapping("/class-timetable")
    public ClassSlot addClassSlot(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody ClassSlot slot) {
        slot.setUserId(getUserId(auth));
        return dataStore.saveClassSlot(slot);
    }

    @DeleteMapping("/class-timetable/{id}")
    public Map<String, Object> deleteClassSlot(@PathVariable String id) {
        boolean ok = dataStore.deleteClassSlot(id);
        return Map.of("success", ok, "id", id);
    }

    // --- 2. Calendar of Events Endpoints ---
    @GetMapping("/calendar-events")
    public List<AcademicEvent> getCalendarEvents(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return dataStore.getAllAcademicEvents(getUserId(auth));
    }

    @PostMapping("/calendar-events")
    public AcademicEvent addCalendarEvent(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody AcademicEvent event) {
        event.setUserId(getUserId(auth));
        return dataStore.saveAcademicEvent(event);
    }

    @DeleteMapping("/calendar-events/{id}")
    public Map<String, Object> deleteCalendarEvent(@PathVariable String id) {
        boolean ok = dataStore.deleteAcademicEvent(id);
        return Map.of("success", ok, "id", id);
    }

    // --- 3. AI Smart Suggestions Endpoint ---
    @GetMapping("/suggestions")
    public List<ScheduleSuggestion> getSuggestions(@RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        return suggestionService.generateSuggestions(getUserId(auth));
    }

    // --- 4. Upload & AI Parser Endpoint ---
    @PostMapping("/upload-and-parse")
    public ResponseEntity<Map<String, Object>> uploadAndParse(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestParam("type") String type, // CLASS_TIMETABLE, CALENDAR_OF_EVENTS, EXAM_TIMETABLE, SYLLABUS
            @RequestParam(value = "text", required = false) String rawText,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        String userId = getUserId(auth);
        String textToParse = rawText != null ? rawText.trim() : "";
        String filename = "Direct Input";

        if (file != null && !file.isEmpty()) {
            filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
            String extracted = documentParserService.extractTextFromFile(file);
            if (extracted != null && !extracted.trim().isEmpty()) {
                if (!textToParse.isEmpty()) {
                    textToParse = textToParse + "\n\n" + extracted.trim();
                } else {
                    textToParse = extracted.trim();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("filename", filename);
        result.put("extractedText", textToParse);
        result.put("status", "success");

        if ("CLASS_TIMETABLE".equalsIgnoreCase(type)) {
            List<ClassSlot> parsedSlots = documentParserService.parseClassSlotsFromText(userId, textToParse);
            dataStore.saveClassSlots(parsedSlots);
            result.put("count", parsedSlots.size());
            result.put("items", parsedSlots);
            result.put("message", "Extracted " + parsedSlots.size() + " class routine slots from " + filename + ".");
        } else if ("CALENDAR_OF_EVENTS".equalsIgnoreCase(type)) {
            List<AcademicEvent> parsedEvents = documentParserService.parseEventsFromText(userId, textToParse);
            dataStore.saveAcademicEvents(parsedEvents);
            result.put("count", parsedEvents.size());
            result.put("items", parsedEvents);
            result.put("message", "Extracted " + parsedEvents.size() + " calendar events/holidays from " + filename + ".");
        } else if ("EXAM_TIMETABLE".equalsIgnoreCase(type)) {
            List<Exam> parsedExams = documentParserService.parseExamsFromText(userId, textToParse);
            for (Exam e : parsedExams) {
                dataStore.saveExam(e);
            }
            result.put("count", parsedExams.size());
            result.put("items", parsedExams);
            result.put("message", "Extracted " + parsedExams.size() + " examinations from " + filename + ".");
        } else if ("SYLLABUS".equalsIgnoreCase(type)) {
            Map<String, Object> syllabus = documentParserService.parseSyllabusStructureFromText(textToParse);
            result.put("syllabus", syllabus);
            result.put("count", 1);
            result.put("message", "Syllabus extracted and parsed successfully from " + filename + ".");
        } else {
            result.put("count", 1);
            result.put("message", "Document text extracted successfully from " + filename + ".");
        }

        return ResponseEntity.ok(result);
    }

    private List<ClassSlot> parseClassTimetable(String userId, String text) {
        List<ClassSlot> list = new ArrayList<>();
        // Simple heuristic line parser
        String[] lines = text.split("\\r?\\n");
        String currentDay = "Monday";
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String lower = line.toLowerCase();
            if (lower.contains("monday")) currentDay = "Monday";
            else if (lower.contains("tuesday")) currentDay = "Tuesday";
            else if (lower.contains("wednesday")) currentDay = "Wednesday";
            else if (lower.contains("thursday")) currentDay = "Thursday";
            else if (lower.contains("friday")) currentDay = "Friday";
            else if (lower.contains("saturday")) currentDay = "Saturday";
            else {
                // Parse a class slot: e.g. "09:00 - 10:00: Machine Learning (LH-101)"
                String time = "11:00";
                String end = "12:00";
                String subject = line;
                String type = lower.contains("lab") ? "Lab" : "Lecture";

                if (line.contains("-")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        String timePart = parts[0].trim();
                        subject = parts[1].trim();
                        String[] times = timePart.split("-");
                        if (times.length == 2) {
                            time = times[0].trim();
                            end = times[1].trim();
                        }
                    }
                }
                ClassSlot cs = new ClassSlot(currentDay, time, end, subject, type);
                cs.setUserId(userId);
                list.add(cs);
            }
        }
        if (list.isEmpty()) {
            // Default sample parsed slots if text was unstructured
            list.add(new ClassSlot("Monday", "11:00", "12:00", "Software Engineering", "Lecture"));
            list.add(new ClassSlot("Tuesday", "14:00", "16:00", "Web Development Lab", "Lab"));
            list.add(new ClassSlot("Thursday", "10:00", "11:00", "Database Management Systems", "Lecture"));
            for (ClassSlot s : list) s.setUserId(userId);
        }
        return list;
    }

    private List<AcademicEvent> parseCalendarEvents(String userId, String text) {
        List<AcademicEvent> list = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String lower = line.toLowerCase();
            boolean isHoliday = lower.contains("holiday") || lower.contains("vacation") || lower.contains("break");
            String type = isHoliday ? "holiday" : (lower.contains("exam") || lower.contains("test") ? "internal_assessment" : "event");
            AcademicEvent ae = new AcademicEvent(line, LocalDate.now().plusDays(list.size() * 3 + 3).toString(), type, isHoliday);
            ae.setUserId(userId);
            list.add(ae);
        }
        if (list.isEmpty()) {
            list.add(new AcademicEvent("Internal Test 2", LocalDate.now().plusDays(10).toString(), "internal_assessment", false));
            list.add(new AcademicEvent("National Holiday", LocalDate.now().plusDays(15).toString(), "holiday", true));
            for (AcademicEvent ae : list) ae.setUserId(userId);
        }
        return list;
    }

    private List<Exam> parseExamTimetable(String userId, String text) {
        List<Exam> list = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");
        int offset = 12;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            Exam ex = new Exam();
            ex.setUserId(userId);
            ex.setSubjectName(line);
            ex.setExamDate(LocalDate.now().plusDays(offset).toString());
            ex.setTargetScore(85);
            list.add(ex);
            offset += 3;
        }
        if (list.isEmpty()) {
            Exam ex1 = new Exam();
            ex1.setUserId(userId);
            ex1.setSubjectName("Distributed Systems");
            ex1.setExamDate(LocalDate.now().plusDays(14).toString());
            list.add(ex1);
        }
        return list;
    }
}
