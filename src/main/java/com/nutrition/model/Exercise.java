package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    // ── Core Fields ────────────────────────────────────────────────────
    private int exerciseId;
    private String exerciseName;
    private String category;
    private String difficulty;
    private int durationSeconds;
    private int defaultSets;
    private int defaultRepetitions;
    private int restSeconds;
    private int estimatedCalories;
    private String targetBodyArea;
    private String instructions;
    private String imagePath;
    private boolean isActive;
    private Timestamp createdAt;

    // ── Health / Rule Fields ────────────────────────────────────────────
    private String healthTags;
    private String healthRuleStatus;
    private String healthRuleReason;

    public Exercise() {}

    // ── Core Getters / Setters ──────────────────────────────────────────
    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }

    /** Alias so code using getName() still works. */
    public String getName() { return exerciseName; }
    public void setName(String name) { this.exerciseName = name; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    /** Alias — some call sites use getDuration() */
    public int getDuration() { return durationSeconds; }
    public void setDuration(int duration) { this.durationSeconds = duration; }

    /** Convenience: duration in whole minutes (minimum 1). */
    public int getDefaultDurationMin() { return Math.max(1, durationSeconds / 60); }

    public int getDefaultSets() { return defaultSets; }
    public void setDefaultSets(int defaultSets) { this.defaultSets = defaultSets; }

    /** Alias — some call sites use getSets() */
    public int getSets() { return defaultSets; }
    public void setSets(int sets) { this.defaultSets = sets; }

    public int getDefaultRepetitions() { return defaultRepetitions; }
    public void setDefaultRepetitions(int defaultRepetitions) { this.defaultRepetitions = defaultRepetitions; }

    /** Alias — some call sites use getRepetitions() */
    public int getRepetitions() { return defaultRepetitions; }
    public void setRepetitions(int reps) { this.defaultRepetitions = reps; }

    public int getRestSeconds() { return restSeconds; }
    public void setRestSeconds(int restSeconds) { this.restSeconds = restSeconds; }

    public int getEstimatedCalories() { return estimatedCalories; }
    public void setEstimatedCalories(int estimatedCalories) { this.estimatedCalories = estimatedCalories; }

    /** Alias — some call sites use getCaloriesBurn() */
    public int getCaloriesBurn() { return estimatedCalories; }
    public void setCaloriesBurn(int cal) { this.estimatedCalories = cal; }

    /** Rough MET value derived from calorie estimate. */
    public double getMet() { return Math.max(3.0, estimatedCalories / 10.0); }

    public String getTargetBodyArea() { return targetBodyArea; }
    public void setTargetBodyArea(String targetBodyArea) { this.targetBodyArea = targetBodyArea; }

    /** Alias — some call sites use getMuscleGroup() */
    public String getMuscleGroup() { return targetBodyArea; }
    public void setMuscleGroup(String muscleGroup) { this.targetBodyArea = muscleGroup; }

    /** Alias — some call sites use getTargetMuscle() */
    public String getTargetMuscle() { return targetBodyArea; }
    public void setTargetMuscle(String muscle) { this.targetBodyArea = muscle; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // ── Health / Rule Getters / Setters ────────────────────────────────
    public String getHealthTags() { return healthTags; }
    public void setHealthTags(String healthTags) { this.healthTags = healthTags; }

    public String getHealthRuleStatus() { return healthRuleStatus; }
    public void setHealthRuleStatus(String healthRuleStatus) { this.healthRuleStatus = healthRuleStatus; }

    public String getHealthRuleReason() { return healthRuleReason; }
    public void setHealthRuleReason(String healthRuleReason) { this.healthRuleReason = healthRuleReason; }
}
