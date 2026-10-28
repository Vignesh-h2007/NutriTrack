package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class WeightHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int weightHistoryId;
    private int userId;
    private double weightKg;
    private String notes = "";
    private Timestamp recordedAt;

    public WeightHistory() {}

    public WeightHistory(int userId, double weightKg) {
        this.userId = userId;
        this.weightKg = weightKg;
    }

    public WeightHistory(int userId, double weightKg, String notes) {
        this.userId = userId;
        this.weightKg = weightKg;
        this.notes = notes;
    }

    public int getWeightHistoryId() { return weightHistoryId; }
    public void setWeightHistoryId(int weightHistoryId) { this.weightHistoryId = weightHistoryId; }

    public int getRecordId() { return weightHistoryId; }
    public int getLogId() { return weightHistoryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public String getNotes() { return notes != null ? notes : ""; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Timestamp recordedAt) { this.recordedAt = recordedAt; }
}
