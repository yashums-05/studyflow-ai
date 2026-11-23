package com.studymate.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final List<Map<String, Object>> documents = new ArrayList<>();

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subjectId", required = false) String subjectId) {
        String docId = "doc_" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", docId);
        doc.put("fileName", file.getOriginalFilename() != null ? file.getOriginalFilename() : "syllabus.pdf");
        doc.put("fileSize", file.getSize());
        doc.put("subjectId", subjectId);
        doc.put("uploadedAt", new Date().toString());
        doc.put("status", "processed");
        documents.add(doc);

        return ResponseEntity.ok(doc);
    }

    @GetMapping
    public List<Map<String, Object>> listDocuments() {
        return documents;
    }
}
