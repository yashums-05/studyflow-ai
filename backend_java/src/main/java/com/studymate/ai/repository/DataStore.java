package com.studymate.ai.repository;

import com.studymate.ai.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class DataStore {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Subject> subjects = new ConcurrentHashMap<>();
    private final Map<String, Unit> units = new ConcurrentHashMap<>();
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final Map<String, StudySession> studySessions = new ConcurrentHashMap<>();
    private final Map<String, Exam> exams = new ConcurrentHashMap<>();
    private final Map<String, ClassSlot> classSlots = new ConcurrentHashMap<>();
    private final Map<String, AcademicEvent> academicEvents = new ConcurrentHashMap<>();

    public DataStore() {
        seedInitialData();
    }

    private void seedInitialData() {
        // Default User
        User user = new User();
        user.setId("dev_user_student_1");
        user.setEmail("student@univ.edu");
        user.setFullName("Alex Morgan");
        user.setCollegeName("MIT College of Engineering");
        user.setDegree("B.Tech Computer Science");
        user.setDepartment("Computer Science & Engineering");
        user.setSemester(6);
        users.put(user.getId(), user);

        // Seed Subject 1: Computer Networks
        Subject s1 = new Subject();
        s1.setId("sub_cn_1");
        s1.setName("Computer Networks");
        s1.setCode("CS601");
        s1.setSemester(6);
        s1.setDifficultyLevel(4);
        s1.setColorHex("#7556D9");
        s1.setEstimatedStudyHours(45.0);
        s1.setExaminationDate(LocalDate.now().plusDays(14).toString());
        s1.setTotalUnits(3);
        s1.setCompletionPercentage(25.0);
        subjects.put(s1.getId(), s1);

        Unit u1 = new Unit();
        u1.setId("unit_cn_1");
        u1.setSubjectId(s1.getId());
        u1.setUnitNumber(1);
        u1.setTitle("Physical & Data Link Layers");
        u1.setTopicsCount(2);
        units.put(u1.getId(), u1);

        Topic t1 = new Topic();
        t1.setId("topic_cn_1");
        t1.setSubjectId(s1.getId());
        t1.setUnitId(u1.getId());
        t1.setName("Framing and Error Detection (CRC)");
        t1.setDifficultyLevel(3);
        t1.setEstimatedDurationMinutes(45);
        t1.setCompletionStatus("completed");
        t1.setImportanceScore(4);
        t1.setUnderstandingRating(4);
        topics.put(t1.getId(), t1);

        Topic t2 = new Topic();
        t2.setId("topic_cn_2");
        t2.setSubjectId(s1.getId());
        t2.setUnitId(u1.getId());
        t2.setName("Flow Control (Sliding Window Protocols)");
        t2.setDifficultyLevel(4);
        t2.setEstimatedDurationMinutes(60);
        t2.setCompletionStatus("in_progress");
        t2.setImportanceScore(5);
        t2.setUnderstandingRating(2);
        topics.put(t2.getId(), t2);

        // Seed Subject 2: Operating Systems
        Subject s2 = new Subject();
        s2.setId("sub_os_2");
        s2.setName("Operating Systems");
        s2.setCode("CS602");
        s2.setSemester(6);
        s2.setDifficultyLevel(4);
        s2.setColorHex("#22A06B");
        s2.setEstimatedStudyHours(40.0);
        s2.setExaminationDate(LocalDate.now().plusDays(21).toString());
        s2.setTotalUnits(2);
        s2.setCompletionPercentage(50.0);
        subjects.put(s2.getId(), s2);

        Unit u2 = new Unit();
        u2.setId("unit_os_1");
        u2.setSubjectId(s2.getId());
        u2.setUnitNumber(1);
        u2.setTitle("Process Scheduling & Synchronization");
        u2.setTopicsCount(2);
        units.put(u2.getId(), u2);

        Topic t3 = new Topic();
        t3.setId("topic_os_1");
        t3.setSubjectId(s2.getId());
        t3.setUnitId(u2.getId());
        t3.setName("Semaphores and Mutex Locks");
        t3.setDifficultyLevel(4);
        t3.setEstimatedDurationMinutes(50);
        t3.setCompletionStatus("completed");
        t3.setImportanceScore(5);
        t3.setUnderstandingRating(4);
        topics.put(t3.getId(), t3);

        Topic t4 = new Topic();
        t4.setId("topic_os_2");
        t4.setSubjectId(s2.getId());
        t4.setUnitId(u2.getId());
        t4.setName("Deadlock Avoidance (Banker's Algorithm)");
        t4.setDifficultyLevel(5);
        t4.setEstimatedDurationMinutes(60);
        t4.setCompletionStatus("needs_revision");
        t4.setImportanceScore(5);
        t4.setUnderstandingRating(2);
        topics.put(t4.getId(), t4);

        // Seed Exam
        Exam exam1 = new Exam();
        exam1.setId("exam_1");
        exam1.setSubjectId(s1.getId());
        exam1.setSubjectName(s1.getName());
        exam1.setExamDate(LocalDate.now().plusDays(14).toString());
        exam1.setExamType("Semester End Examination");
        exam1.setVenue("Hall B-204");
        exam1.setTargetScore(90);
        exams.put(exam1.getId(), exam1);

        // Seed Sample Completed Study Session
        StudySession sess1 = new StudySession();
        sess1.setId("sess_seed_1");
        sess1.setSubjectId(s1.getId());
        sess1.setSubjectName(s1.getName());
        sess1.setTopicId(t1.getId());
        sess1.setTopicName(t1.getName());
        sess1.setSessionDate(LocalDate.now().toString());
        sess1.setStartTime("09:00");
        sess1.setEndTime("09:45");
        sess1.setEstimatedDurationMinutes(45);
        sess1.setActualDurationMinutes(45);
        sess1.setSessionType("learning");
        sess1.setStatus("completed");
        sess1.setUnderstandingRating(4);
        sess1.setNotes("Understood CRC checksum computation using polynomial division.");
        studySessions.put(sess1.getId(), sess1);

        // Seed Class Timetable Slots (Typical Engineering Schedule)
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            ClassSlot cs1 = new ClassSlot(day, "09:00", "10:00", "Operating Systems", "Lecture");
            cs1.setRoom("LH-204");
            classSlots.put(cs1.getId(), cs1);

            ClassSlot cs2 = new ClassSlot(day, "10:00", "11:00", "Computer Networks", "Lecture");
            cs2.setRoom("LH-205");
            classSlots.put(cs2.getId(), cs2);

            if ("Wednesday".equals(day) || "Friday".equals(day)) {
                ClassSlot csLab = new ClassSlot(day, "14:00", "16:00", "Systems Programming Lab", "Lab");
                csLab.setRoom("Lab-B (Comp Dept)");
                classSlots.put(csLab.getId(), csLab);
            }
        }

        // Seed Academic Calendar Events
        AcademicEvent ae1 = new AcademicEvent("Internal Assessment (IA-1)", LocalDate.now().plusDays(5).toString(), "internal_assessment", false);
        ae1.setDescription("Syllabus: Units 1 & 2 for Operating Systems and Networks.");
        academicEvents.put(ae1.getId(), ae1);

        AcademicEvent ae2 = new AcademicEvent("College Founders Day (Holiday)", LocalDate.now().plusDays(8).toString(), "holiday", true);
        ae2.setDescription("Full-day campus holiday. All regular classes suspended.");
        academicEvents.put(ae2.getId(), ae2);

        AcademicEvent ae3 = new AcademicEvent("Mini Project Architecture Submission", LocalDate.now().plusDays(12).toString(), "deadline", false);
        ae3.setDescription("Submit GitHub repository link and architecture documentation.");
        academicEvents.put(ae3.getId(), ae3);

        AcademicEvent ae4 = new AcademicEvent("Semester Final Examinations", LocalDate.now().plusDays(20).toString(), "semester_exam", false);
        ae4.setDescription("University board finals across all 6 registered subjects.");
        academicEvents.put(ae4.getId(), ae4);
    }

    // User CRUD
    public User getUser(String id) {
        return users.computeIfAbsent(id, k -> {
            User u = new User();
            u.setId(k);
            if ("dev_user_student_1".equals(k)) {
                u.setEmail("student@univ.edu");
                u.setFullName("Alex Morgan");
                u.setCollegeName("MIT College of Engineering");
            } else {
                String clean = k.replace("dev_user_", "").replace("user_", "");
                if (clean.contains("@")) {
                    u.setEmail(clean);
                    String name = clean.split("@")[0];
                    u.setFullName(Character.toUpperCase(name.charAt(0)) + name.substring(1));
                } else if (!clean.isEmpty()) {
                    u.setFullName(Character.toUpperCase(clean.charAt(0)) + clean.substring(1));
                    u.setEmail(clean + "@studyflow.ai");
                } else {
                    u.setFullName("New Student");
                    u.setEmail("student@studyflow.ai");
                }
            }
            return u;
        });
    }

    public Optional<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return Optional.empty();
        return users.values().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();
    }

    public User saveUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    // Subject CRUD
    public List<Subject> getAllSubjects(String userId) {
        List<Subject> list = subjects.values().stream()
                .filter(s -> userId != null && userId.equals(s.getUserId()))
                .collect(Collectors.toList());

        if (list.isEmpty() && userId != null && !"dev_user_student_1".equals(userId)) {
            initializeStarterSubjectsForUser(userId);
            list = subjects.values().stream()
                    .filter(s -> userId.equals(s.getUserId()))
                    .collect(Collectors.toList());
        }
        list.forEach(this::populateSubjectHierarchy);
        return list;
    }

    public void populateSubjectHierarchy(Subject s) {
        if (s == null) return;
        List<Unit> unitList = getUnitsBySubject(s.getId());
        int totalTop = 0;
        int completedTop = 0;
        for (Unit u : unitList) {
            List<Topic> topicList = getTopicsByUnit(u.getId());
            u.setTopics(topicList);
            u.setTopicsCount(topicList.size());
            totalTop += topicList.size();
            for (Topic t : topicList) {
                if ("completed".equalsIgnoreCase(t.getCompletionStatus())) {
                    completedTop++;
                }
            }
        }
        s.setUnits(unitList);
        s.setTotalUnits(unitList.size());
        s.setTotalTopics(totalTop);
        s.setCompletedTopics(completedTop);
        if (totalTop > 0) {
            s.setCompletionPercentage(Math.round((completedTop * 100.0 / totalTop) * 10.0) / 10.0);
        }
    }

    private void initializeStarterSubjectsForUser(String userId) {
        Subject s1 = new Subject();
        s1.setId("sub_" + UUID.randomUUID().toString().substring(0, 8));
        s1.setUserId(userId);
        s1.setName("Computer Networks");
        s1.setCode("CS601");
        s1.setSemester(6);
        s1.setDifficultyLevel(4);
        s1.setColorHex("#7556D9");
        s1.setEstimatedStudyHours(45.0);
        s1.setExaminationDate(LocalDate.now().plusDays(14).toString());
        s1.setTotalUnits(2);
        s1.setCompletionPercentage(20.0);
        subjects.put(s1.getId(), s1);

        Unit u1 = new Unit();
        u1.setId("unit_" + UUID.randomUUID().toString().substring(0, 8));
        u1.setSubjectId(s1.getId());
        u1.setUnitNumber(1);
        u1.setTitle("Physical & Data Link Layers");
        units.put(u1.getId(), u1);

        Topic t1 = new Topic();
        t1.setId("top_" + UUID.randomUUID().toString().substring(0, 8));
        t1.setSubjectId(s1.getId());
        t1.setUnitId(u1.getId());
        t1.setName("Framing and Error Detection (CRC)");
        t1.setDifficultyLevel(3);
        t1.setCompletionStatus("in_progress");
        t1.setUnderstandingRating(4);
        topics.put(t1.getId(), t1);

        Subject s2 = new Subject();
        s2.setId("sub_" + UUID.randomUUID().toString().substring(0, 8));
        s2.setUserId(userId);
        s2.setName("Operating Systems");
        s2.setCode("CS602");
        s2.setSemester(6);
        s2.setDifficultyLevel(4);
        s2.setColorHex("#22C55E");
        s2.setEstimatedStudyHours(50.0);
        s2.setExaminationDate(LocalDate.now().plusDays(10).toString());
        s2.setTotalUnits(2);
        s2.setCompletionPercentage(35.0);
        subjects.put(s2.getId(), s2);

        Unit u2 = new Unit();
        u2.setId("unit_" + UUID.randomUUID().toString().substring(0, 8));
        u2.setSubjectId(s2.getId());
        u2.setUnitNumber(1);
        u2.setTitle("Process & Memory Management");
        units.put(u2.getId(), u2);

        Topic t2 = new Topic();
        t2.setId("top_" + UUID.randomUUID().toString().substring(0, 8));
        t2.setSubjectId(s2.getId());
        t2.setUnitId(u2.getId());
        t2.setName("Deadlock Avoidance (Banker's Algorithm)");
        t2.setDifficultyLevel(5);
        t2.setCompletionStatus("needs_revision");
        t2.setUnderstandingRating(2);
        topics.put(t2.getId(), t2);

        // Starter exam for this user
        Exam ex = new Exam();
        ex.setUserId(userId);
        ex.setSubjectId(s2.getId());
        ex.setSubjectName("Operating Systems");
        ex.setExamDate(LocalDate.now().plusDays(10).toString());
        ex.setVenue("Hall 3");
        ex.setTargetScore(90);
        exams.put(ex.getId(), ex);
    }

    public Optional<Subject> getSubject(String id) {
        Optional<Subject> opt = Optional.ofNullable(subjects.get(id));
        opt.ifPresent(this::populateSubjectHierarchy);
        return opt;
    }

    public Subject saveSubject(Subject subject) {
        if (subject.getId() == null || subject.getId().isEmpty()) {
            subject.setId("sub_" + UUID.randomUUID().toString().substring(0, 8));
        }
        subjects.put(subject.getId(), subject);
        populateSubjectHierarchy(subject);
        return subject;
    }

    public boolean deleteSubject(String id) {
        units.values().removeIf(u -> id.equals(u.getSubjectId()));
        topics.values().removeIf(t -> id.equals(t.getSubjectId()));
        studySessions.values().removeIf(s -> id.equals(s.getSubjectId()));
        exams.values().removeIf(e -> id.equals(e.getSubjectId()));
        return subjects.remove(id) != null;
    }

    // Unit CRUD
    public List<Unit> getUnitsBySubject(String subjectId) {
        return units.values().stream()
                .filter(u -> subjectId.equals(u.getSubjectId()))
                .sorted(Comparator.comparingInt(Unit::getOrderIndex))
                .collect(Collectors.toList());
    }

    public Unit saveUnit(Unit unit) {
        if (unit.getId() == null || unit.getId().isEmpty()) {
            unit.setId(UUID.randomUUID().toString());
        }
        units.put(unit.getId(), unit);
        // update subject totalUnits
        Optional<Subject> sub = getSubject(unit.getSubjectId());
        sub.ifPresent(s -> {
            s.setTotalUnits(getUnitsBySubject(s.getId()).size());
        });
        return unit;
    }

    public boolean deleteUnit(String id) {
        Unit u = units.remove(id);
        if (u != null) {
            topics.values().removeIf(t -> id.equals(t.getUnitId()));
            Optional<Subject> sub = getSubject(u.getSubjectId());
            sub.ifPresent(s -> s.setTotalUnits(getUnitsBySubject(s.getId()).size()));
            return true;
        }
        return false;
    }

    // Topic CRUD
    public List<Topic> getTopicsByUnit(String unitId) {
        return topics.values().stream()
                .filter(t -> unitId.equals(t.getUnitId()))
                .sorted(Comparator.comparingInt(Topic::getOrderIndex))
                .collect(Collectors.toList());
    }

    public List<Topic> getTopicsBySubject(String subjectId) {
        return topics.values().stream()
                .filter(t -> subjectId.equals(t.getSubjectId()))
                .collect(Collectors.toList());
    }

    public List<Topic> getAllTopics(String userId) {
        Set<String> userSubjectIds = subjects.values().stream()
                .filter(s -> userId.equals(s.getUserId()))
                .map(Subject::getId)
                .collect(Collectors.toSet());

        return topics.values().stream()
                .filter(t -> userSubjectIds.contains(t.getSubjectId()))
                .collect(Collectors.toList());
    }

    public Optional<Topic> getTopic(String id) {
        return Optional.ofNullable(topics.get(id));
    }

    public Topic saveTopic(Topic topic) {
        if (topic.getId() == null || topic.getId().isEmpty()) {
            topic.setId(UUID.randomUUID().toString());
        }
        topics.put(topic.getId(), topic);
        return topic;
    }

    public boolean deleteTopic(String id) {
        return topics.remove(id) != null;
    }

    // StudySession CRUD
    public List<StudySession> getAllSessions(String userId) {
        return studySessions.values().stream()
                .filter(s -> userId.equals(s.getUserId()))
                .sorted(Comparator.comparing(StudySession::getSessionDate)
                        .thenComparing(StudySession::getStartTime))
                .collect(Collectors.toList());
    }

    public List<StudySession> getSessionsByDate(String userId, String date) {
        return studySessions.values().stream()
                .filter(s -> userId.equals(s.getUserId()) && date.equals(s.getSessionDate()))
                .sorted(Comparator.comparing(StudySession::getStartTime))
                .collect(Collectors.toList());
    }

    public Optional<StudySession> getSession(String id) {
        return Optional.ofNullable(studySessions.get(id));
    }

    public StudySession saveSession(StudySession session) {
        if (session.getId() == null || session.getId().isEmpty()) {
            session.setId(UUID.randomUUID().toString());
        }
        studySessions.put(session.getId(), session);
        return session;
    }

    public void saveSessions(List<StudySession> sessions) {
        for (StudySession s : sessions) {
            saveSession(s);
        }
    }

    public void clearScheduledSessions(String userId, String fromDate) {
        studySessions.values().removeIf(s ->
                userId.equals(s.getUserId()) &&
                "scheduled".equals(s.getStatus()) &&
                s.getSessionDate().compareTo(fromDate) >= 0
        );
    }

    // Exam CRUD
    public List<Exam> getAllExams(String userId) {
        return exams.values().stream()
                .filter(e -> userId.equals(e.getUserId()))
                .sorted(Comparator.comparing(Exam::getExamDate))
                .collect(Collectors.toList());
    }

    public Exam saveExam(Exam exam) {
        if (exam.getId() == null || exam.getId().isEmpty()) {
            exam.setId(UUID.randomUUID().toString());
        }
        exams.put(exam.getId(), exam);
        return exam;
    }

    public boolean deleteExam(String id) {
        return exams.remove(id) != null;
    }

    // ClassSlot CRUD
    public List<ClassSlot> getAllClassSlots(String userId) {
        return classSlots.values().stream()
                .filter(cs -> userId.equals(cs.getUserId()))
                .sorted(Comparator.comparing(ClassSlot::getDayOfWeek)
                        .thenComparing(ClassSlot::getStartTime))
                .collect(Collectors.toList());
    }

    public List<ClassSlot> getClassSlotsByDay(String userId, String dayOfWeek) {
        return classSlots.values().stream()
                .filter(cs -> userId.equals(cs.getUserId()) && dayOfWeek.equalsIgnoreCase(cs.getDayOfWeek()))
                .sorted(Comparator.comparing(ClassSlot::getStartTime))
                .collect(Collectors.toList());
    }

    public ClassSlot saveClassSlot(ClassSlot slot) {
        if (slot.getId() == null || slot.getId().isEmpty()) {
            slot.setId(UUID.randomUUID().toString());
        }
        classSlots.put(slot.getId(), slot);
        return slot;
    }

    public void saveClassSlots(List<ClassSlot> slots) {
        for (ClassSlot s : slots) {
            saveClassSlot(s);
        }
    }

    public void clearClassSlots(String userId) {
        classSlots.values().removeIf(cs -> userId.equals(cs.getUserId()));
    }

    public boolean deleteClassSlot(String id) {
        return classSlots.remove(id) != null;
    }

    // AcademicEvent CRUD
    public List<AcademicEvent> getAllAcademicEvents(String userId) {
        return academicEvents.values().stream()
                .filter(ae -> userId.equals(ae.getUserId()))
                .sorted(Comparator.comparing(AcademicEvent::getEventDate))
                .collect(Collectors.toList());
    }

    public AcademicEvent saveAcademicEvent(AcademicEvent event) {
        if (event.getId() == null || event.getId().isEmpty()) {
            event.setId(UUID.randomUUID().toString());
        }
        academicEvents.put(event.getId(), event);
        return event;
    }

    public void saveAcademicEvents(List<AcademicEvent> events) {
        for (AcademicEvent ae : events) {
            saveAcademicEvent(ae);
        }
    }

    public boolean deleteAcademicEvent(String id) {
        return academicEvents.remove(id) != null;
    }
}
