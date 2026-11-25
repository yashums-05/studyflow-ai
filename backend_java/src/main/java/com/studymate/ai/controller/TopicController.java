package com.studymate.ai.controller;

import com.studymate.ai.model.Topic;
import com.studymate.ai.repository.DataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {
    private final DataStore dataStore;

    public TopicController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/unit/{unitId}")
    public List<Topic> getTopicsByUnit(@PathVariable String unitId) {
        return dataStore.getTopicsByUnit(unitId);
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return dataStore.saveTopic(topic);
    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable String id, @RequestBody Topic updated) {
        updated.setId(id);
        return dataStore.saveTopic(updated);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTopic(@PathVariable String id) {
        boolean ok = dataStore.deleteTopic(id);
        return Map.of("success", ok, "id", id);
    }
}
