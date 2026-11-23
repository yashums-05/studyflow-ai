package com.studymate.ai.controller;

import com.studymate.ai.dto.AiRequests;
import com.studymate.ai.service.AiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    private String getUserId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return "dev_user_student_1";
    }

    @PostMapping("/chat")
    public AiRequests.ChatResponse chat(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody AiRequests.ChatRequest request) {
        return aiService.chat(getUserId(auth), request);
    }

    @PostMapping("/explain")
    public Map<String, Object> explain(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody AiRequests.ExplainRequest request) {
        return aiService.explainTopic(getUserId(auth), request);
    }

    @PostMapping("/generate-quiz")
    public List<AiRequests.QuizQuestion> generateQuiz(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth,
            @RequestBody AiRequests.QuizRequest request) {
        return aiService.generateQuiz(getUserId(auth), request);
    }

    @GetMapping("/recommendations")
    public Map<String, Object> recommendations(
            @RequestHeader(value = "Authorization", defaultValue = "Bearer dev_user_student_1") String auth) {
        List<String> recs = aiService.getRecommendations(getUserId(auth));
        return Map.of("recommendations", recs);
    }

    @PostMapping("/analyze-syllabus")
    public Map<String, Object> analyzeSyllabus(@RequestBody(required = false) Map<String, Object> payload) {
        return Map.of(
                "subjectName", "Software Engineering & Architecture",
                "units", List.of(
                        Map.of("unitNumber", 1, "title", "Software Requirements & Modeling", "topics", List.of("Functional vs Non-Functional Requirements", "Use Case Diagrams")),
                        Map.of("unitNumber", 2, "title", "Architectural Styles & Design Patterns", "topics", List.of("MVC & Microservices", "Creational & Structural Patterns"))
                )
        );
    }
}
