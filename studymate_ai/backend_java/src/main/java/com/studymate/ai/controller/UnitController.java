package com.studymate.ai.controller;

import com.studymate.ai.model.Unit;
import com.studymate.ai.repository.DataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/units")
public class UnitController {
    private final DataStore dataStore;

    public UnitController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/subject/{subjectId}")
    public List<Unit> getUnitsBySubject(@PathVariable String subjectId) {
        return dataStore.getUnitsBySubject(subjectId);
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        return dataStore.saveUnit(unit);
    }

    @PutMapping("/{id}")
    public Unit updateUnit(@PathVariable String id, @RequestBody Unit updated) {
        updated.setId(id);
        return dataStore.saveUnit(updated);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUnit(@PathVariable String id) {
        boolean ok = dataStore.deleteUnit(id);
        return Map.of("success", ok, "id", id);
    }
}
