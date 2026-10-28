package com.nutrition.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ExerciseCompletion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int completionId;
    private int userId;
    private int exerciseId;
    private String exerciseName;
    private Date completionDate;
    private Time startTime;
    private Time endTime;
    private int durationSeconds;
    private int setsCompleted;
    private int repetitionsCompleted;
    private int caloriesBurned;
    private int completionPercentage = 100;
    private int dailyScore = 100;
    private Timestamp createdAt;

    public ExerciseCompletion() {}

    public ExerciseCompletion(int userId, int exerciseId, String exerciseName, Date completionDate,
                              int durationSeconds, int setsCompleted, int repetitionsCompleted,
                              int caloriesBurned, int completionPercentage, int dailyScore) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.completionDate = completionDate;
        this.durationSeconds = durationSeconds;
        this.setsCompleted = setsCompleted;
        this.repetitionsCompleted = repetitionsCompleted;
        this.caloriesBurned = caloriesBurned;
        this.completionPercentage = completionPercentage;
        this.dailyScore = dailyScore;
    }

    public int getCompletionId() { return completionId; }
    public void setCompletionId(int completionId) { this.completionId = completionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public Date getCompletionDate() { return completionDate; }
    public void setCompletionDate(Date completionDate) { this.completionDate = completionDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getDurationMinutes() { return Math.max(1, durationSeconds / 60); }

    public int getSetsCompleted() { return setsCompleted; }
    public void setSetsCompleted(int setsCompleted) { this.setsCompleted = setsCompleted; }

    public int getRepetitionsCompleted() { return repetitionsCompleted; }
    public void setRepetitionsCompleted(int repetitionsCompleted) { this.repetitionsCompleted = repetitionsCompleted; }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }

    public int getDailyScore() { return dailyScore; }
    public void setDailyScore(int dailyScore) { this.dailyScore = dailyScore; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
