package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit {
    private String id = UUID.randomUUID().toString();
    private String subjectId;
    private Integer unitNumber = 1;
    private String title;
    private String description = "";
    private Integer orderIndex = 0;
    private Integer topicsCount = 0;

    public Unit() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public Integer getUnitNumber() { return unitNumber; }
    public void setUnitNumber(Integer unitNumber) { this.unitNumber = unitNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Integer getTopicsCount() { return topicsCount; }
    public void setTopicsCount(Integer topicsCount) { this.topicsCount = topicsCount; }
}
