package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassSlot {
    private String id = UUID.randomUUID().toString();
    private String userId = "dev_user_student_1";
    private String dayOfWeek; // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    private String startTime; // "09:00"
    private String endTime;   // "10:00"
    private String subjectName;
    private String room = "Room 301";
    private String slotType = "Lecture"; // Lecture, Lab, Tutorial, Seminar, Break

    public ClassSlot() {}

    public ClassSlot(String dayOfWeek, String startTime, String endTime, String subjectName, String slotType) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectName = subjectName;
        this.slotType = slotType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getSlotType() { return slotType; }
    public void setSlotType(String slotType) { this.slotType = slotType; }
}
