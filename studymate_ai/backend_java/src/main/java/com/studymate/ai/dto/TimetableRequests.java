package com.studymate.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TimetableRequests {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Generate {
        private String startDate;
        private int days = 7;
        private boolean includeRevision = true;
        private Double maxDailyHours = 4.0;

        public Generate() {}

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public int getDays() { return days; }
        public void setDays(int days) { this.days = days; }
        public boolean isIncludeRevision() { return includeRevision; }
        public void setIncludeRevision(boolean includeRevision) { this.includeRevision = includeRevision; }
        public Double getMaxDailyHours() { return maxDailyHours; }
        public void setMaxDailyHours(Double maxDailyHours) { this.maxDailyHours = maxDailyHours; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Reschedule {
        private String missedSessionId;
        private String targetDate;
        private String reason = "missed";

        public Reschedule() {}

        public String getMissedSessionId() { return missedSessionId; }
        public void setMissedSessionId(String missedSessionId) { this.missedSessionId = missedSessionId; }
        public String getTargetDate() { return targetDate; }
        public void setTargetDate(String targetDate) { this.targetDate = targetDate; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CompleteSession {
        private int actualMinutes = 45;
        private int understandingRating = 4;
        private String notes = "";
        private String topicStatus = "completed";

        public CompleteSession() {}

        public int getActualMinutes() { return actualMinutes; }
        public void setActualMinutes(int actualMinutes) { this.actualMinutes = actualMinutes; }
        public int getUnderstandingRating() { return understandingRating; }
        public void setUnderstandingRating(int understandingRating) { this.understandingRating = understandingRating; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getTopicStatus() { return topicStatus; }
        public void setTopicStatus(String topicStatus) { this.topicStatus = topicStatus; }
    }
}
