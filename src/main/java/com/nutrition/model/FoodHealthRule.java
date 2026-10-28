package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class FoodHealthRule implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ruleId;
    private int foodId;
    private int conditionId;
    private String ruleType;
    private String reason;
    private Timestamp createdAt;

    public FoodHealthRule() {}

    public int getRuleId() { return ruleId; }
    public void setRuleId(int ruleId) { this.ruleId = ruleId; }

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public int getConditionId() { return conditionId; }
    public void setConditionId(int conditionId) { this.conditionId = conditionId; }

    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
