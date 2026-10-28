package com.nutrition.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ExerciseSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;
    private int userId;
    private Date sessionDate;
    private Timestamp startedAt;
    private Timestamp completedAt;
    private int plannedDuration;
    private int actualDuration;
    private int estimatedCalories;
    private int actualCalories;
    private int completionPercentage;
    private String status;

    private List<ExerciseSessionItem> items = new ArrayList<>();

    public ExerciseSession() {}

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getSessionDate() { return sessionDate; }
    public void setSessionDate(Date sessionDate) { this.sessionDate = sessionDate; }

    public Timestamp getStartedAt() { return startedAt; }
    public void setStartedAt(Timestamp startedAt) { this.startedAt = startedAt; }

    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }

    public int getPlannedDuration() { return plannedDuration; }
    public void setPlannedDuration(int plannedDuration) { this.plannedDuration = plannedDuration; }

    public int getPlannedDurationMin() { return Math.max(1, plannedDuration / 60); }

    public int getActualDuration() { return actualDuration; }
    public void setActualDuration(int actualDuration) { this.actualDuration = actualDuration; }

    public int getActualDurationMin() { return Math.max(0, actualDuration / 60); }

    public int getEstimatedCalories() { return estimatedCalories; }
    public void setEstimatedCalories(int estimatedCalories) { this.estimatedCalories = estimatedCalories; }

    public int getActualCalories() { return actualCalories; }
    public void setActualCalories(int actualCalories) { this.actualCalories = actualCalories; }

    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<ExerciseSessionItem> getItems() { return items; }
    public void setItems(List<ExerciseSessionItem> items) { this.items = items; }
}
