package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MedicalCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    private int conditionId;
    private String conditionName;
    private String description;
    private boolean isActive;
    private Timestamp createdAt;

    public MedicalCondition() {}

    public MedicalCondition(int conditionId, String conditionName, String description) {
        this.conditionId = conditionId;
        this.conditionName = conditionName;
        this.description = description;
        this.isActive = true;
    }

    public int getConditionId() { return conditionId; }
    public void setConditionId(int conditionId) { this.conditionId = conditionId; }

    public String getConditionName() { return conditionName; }
    public void setConditionName(String conditionName) { this.conditionName = conditionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
