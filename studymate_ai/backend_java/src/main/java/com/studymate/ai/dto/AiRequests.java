package com.studymate.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

public class AiRequests {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatRequest {
        private String message;
        private String subjectContext;
        private List<ChatMessage> conversationHistory = new ArrayList<>();

        public ChatRequest() {}

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSubjectContext() { return subjectContext; }
        public void setSubjectContext(String subjectContext) { this.subjectContext = subjectContext; }
        public List<ChatMessage> getConversationHistory() { return conversationHistory; }
        public void setConversationHistory(List<ChatMessage> conversationHistory) { this.conversationHistory = conversationHistory; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatMessage {
        private String role; // "user" or "assistant"
        private String content;

        public ChatMessage() {}
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatResponse {
        private String response;
        private List<String> suggestedFollowUps = new ArrayList<>();
        private List<String> relatedTopics = new ArrayList<>();

        public ChatResponse() {}
        public ChatResponse(String response) { this.response = response; }

        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
        public List<String> getSuggestedFollowUps() { return suggestedFollowUps; }
        public void setSuggestedFollowUps(List<String> suggestedFollowUps) { this.suggestedFollowUps = suggestedFollowUps; }
        public List<String> getRelatedTopics() { return relatedTopics; }
        public void setRelatedTopics(List<String> relatedTopics) { this.relatedTopics = relatedTopics; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ExplainRequest {
        private String topicName;
        private String subjectName;
        private int difficultyLevel = 3;
        private String learningStyle = "Visual & Practical";

        public ExplainRequest() {}

        public String getTopicName() { return topicName; }
        public void setTopicName(String topicName) { this.topicName = topicName; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public int getDifficultyLevel() { return difficultyLevel; }
        public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
        public String getLearningStyle() { return learningStyle; }
        public void setLearningStyle(String learningStyle) { this.learningStyle = learningStyle; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuizRequest {
        private String subjectName;
        private String topicName;
        private int questionCount = 5;
        private int difficultyLevel = 3;

        public QuizRequest() {}

        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getTopicName() { return topicName; }
        public void setTopicName(String topicName) { this.topicName = topicName; }
        public int getQuestionCount() { return questionCount; }
        public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
        public int getDifficultyLevel() { return difficultyLevel; }
        public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuizQuestion {
        private String question;
        private List<String> options = new ArrayList<>();
        private int correctAnswerIndex;
        private String explanation;

        public QuizQuestion() {}

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        public int getCorrectAnswerIndex() { return correctAnswerIndex; }
        public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }
}
