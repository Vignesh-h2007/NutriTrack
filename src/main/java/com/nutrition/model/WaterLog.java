package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class WaterLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private int waterLogId;
    private int userId;
    private int amountMl;
    private String notes = "";
    private Timestamp loggedAt;

    public WaterLog() {}

    public WaterLog(int userId, int amountMl) {
        this.userId = userId;
        this.amountMl = amountMl;
    }

    public WaterLog(int userId, int amountMl, String notes) {
        this.userId = userId;
        this.amountMl = amountMl;
        this.notes = notes;
    }

    public int getWaterLogId() { return waterLogId; }
    public void setWaterLogId(int waterLogId) { this.waterLogId = waterLogId; }

    public int getLogId() { return waterLogId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAmountMl() { return amountMl; }
    public void setAmountMl(int amountMl) { this.amountMl = amountMl; }

    public String getNotes() { return notes != null ? notes : ""; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getLoggedAt() { return loggedAt; }
    public void setLoggedAt(Timestamp loggedAt) { this.loggedAt = loggedAt; }
}
