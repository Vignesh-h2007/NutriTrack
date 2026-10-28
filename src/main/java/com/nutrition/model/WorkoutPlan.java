package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class WorkoutPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private int planId;
    private String title;
    private String description;
    private String targetGoal;
    private int durationWeeks;
    private int estimatedCaloriesBurn;
    private boolean isActive = true;
    private Timestamp createdAt;

    public WorkoutPlan() {}

    public WorkoutPlan(String title, String description, String targetGoal, int durationWeeks, int estimatedCaloriesBurn) {
        this.title = title;
        this.description = description;
        this.targetGoal = targetGoal;
        this.durationWeeks = durationWeeks;
        this.estimatedCaloriesBurn = estimatedCaloriesBurn;
    }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTargetGoal() { return targetGoal; }
    public void setTargetGoal(String targetGoal) { this.targetGoal = targetGoal; }

    public int getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }

    public int getEstimatedCaloriesBurn() { return estimatedCaloriesBurn; }
    public void setEstimatedCaloriesBurn(int estimatedCaloriesBurn) { this.estimatedCaloriesBurn = estimatedCaloriesBurn; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
