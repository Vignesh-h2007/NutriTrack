package com.nutrition.model;

import java.io.Serializable;

public class ExerciseSessionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionItemId;
    private int sessionId;
    private int exerciseId;
    private int plannedSets;
    private int completedSets;
    private int plannedRepetitions;
    private int completedRepetitions;
    private int plannedSeconds;
    private int completedSeconds;
    private String status;
    private int orderNumber;

    private Exercise exercise;

    public ExerciseSessionItem() {}

    public int getSessionItemId() { return sessionItemId; }
    public void setSessionItemId(int sessionItemId) { this.sessionItemId = sessionItemId; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }

    public int getPlannedSets() { return plannedSets; }
    public void setPlannedSets(int plannedSets) { this.plannedSets = plannedSets; }

    public int getCompletedSets() { return completedSets; }
    public void setCompletedSets(int completedSets) { this.completedSets = completedSets; }

    public int getPlannedRepetitions() { return plannedRepetitions; }
    public void setPlannedRepetitions(int plannedRepetitions) { this.plannedRepetitions = plannedRepetitions; }

    public int getCompletedRepetitions() { return completedRepetitions; }
    public void setCompletedRepetitions(int completedRepetitions) { this.completedRepetitions = completedRepetitions; }

    public int getPlannedSeconds() { return plannedSeconds; }
    public void setPlannedSeconds(int plannedSeconds) { this.plannedSeconds = plannedSeconds; }

    public int getCompletedSeconds() { return completedSeconds; }
    public void setCompletedSeconds(int completedSeconds) { this.completedSeconds = completedSeconds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
}
