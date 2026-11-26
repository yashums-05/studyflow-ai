package com.studymate.ai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit {
    private String id = UUID.randomUUID().toString();
    private String subjectId;
    private Integer unitNumber = 1;
    private String title = "Unit 1";
    private String description = "";
    private Integer orderIndex = 0;
    private Integer topicsCount = 0;
    private List<Topic> topics = new ArrayList<>();

    public Unit() {}

    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("unitId")
    public String getUnitId() { return id; }
    public void setUnitId(String unitId) { if (unitId != null && !unitId.trim().isEmpty()) this.id = unitId.trim(); }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public Integer getUnitNumber() { return unitNumber != null ? unitNumber : 1; }
    public void setUnitNumber(Integer unitNumber) { if (unitNumber != null) this.unitNumber = unitNumber; }

    @JsonProperty("title")
    public String getTitle() { return (title != null && !title.trim().isEmpty()) ? title : "Unit " + getUnitNumber(); }
    public void setTitle(String title) { if (title != null && !title.trim().isEmpty()) this.title = title.trim(); }

    @JsonProperty("unitName")
    public String getUnitName() { return getTitle(); }
    public void setUnitName(String unitName) { setTitle(unitName); }

    public String getDescription() { return description != null ? description : ""; }
    public void setDescription(String description) { this.description = description; }

    @JsonProperty("orderIndex")
    public Integer getOrderIndex() { return orderIndex != null ? orderIndex : getUnitNumber(); }
    public void setOrderIndex(Integer orderIndex) { if (orderIndex != null) this.orderIndex = orderIndex; }

    @JsonProperty("order")
    public Integer getOrder() { return getOrderIndex(); }
    public void setOrder(Integer order) { setOrderIndex(order); }

    public Integer getTopicsCount() {
        if (topics != null && !topics.isEmpty()) return topics.size();
        return topicsCount != null ? topicsCount : 0;
    }
    public void setTopicsCount(Integer topicsCount) { if (topicsCount != null) this.topicsCount = topicsCount; }

    @JsonProperty("topics")
    public List<Topic> getTopics() { return topics != null ? topics : new ArrayList<>(); }
    public void setTopics(List<Topic> topics) { this.topics = topics != null ? topics : new ArrayList<>(); }
}
