package com.nutrition.service;

import com.nutrition.dao.*;
import com.nutrition.model.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Service for water intake and weight tracking.
 */
public class TrackingService {

    private final WaterDAO waterDAO = new WaterDAO();
    private final WeightDAO weightDAO = new WeightDAO();
    private final AuditDAO auditDAO = new AuditDAO();

    // ────── Water ──────

    public WaterLog logWater(int userId, int amountMl, String notes) throws SQLException {
        if (amountMl <= 0 || amountMl > 5000) throw new IllegalArgumentException("Water amount must be between 1 and 5000 ml.");
        WaterLog log = waterDAO.addWaterLog(userId, amountMl, notes);
        auditDAO.log(userId, "WATER_LOGGED", "water_logs", log.getLogId());
        return log;
    }

    public int getTodayWaterIntake(int userId) throws SQLException {
        return waterDAO.getTodayIntake(userId);
    }

    public List<WaterLog> getWaterLogsToday(int userId) throws SQLException {
        return waterDAO.getLogsForToday(userId);
    }

    public boolean deleteWaterLog(int userId, int logId) throws SQLException {
        return waterDAO.deleteLog(logId, userId);
    }

    /**
     * Daily water goal based on weight (approx 35 ml per kg).
     */
    public int recommendedDailyWaterMl(double weightKg) {
        return (int) Math.round(weightKg * 35);
    }

    // ────── Weight ──────

    public WeightHistory logWeight(int userId, double weightKg, String notes) throws SQLException {
        if (weightKg < 10 || weightKg > 600) throw new IllegalArgumentException("Weight must be between 10 and 600 kg.");
        WeightHistory record = weightDAO.addWeightRecord(userId, weightKg, notes);
        auditDAO.log(userId, "WEIGHT_LOGGED", "weight_history", record.getRecordId());
        return record;
    }

    public List<WeightHistory> getWeightHistory(int userId, int limit) throws SQLException {
        return weightDAO.getWeightHistory(userId, limit);
    }

    public WeightHistory getLatestWeight(int userId) throws SQLException {
        return weightDAO.getLatestWeight(userId);
    }

    /**
     * Calculate BMI (Body Mass Index).
     */
    public double calculateBMI(double weightKg, double heightCm) {
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    public String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal weight";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }
}
