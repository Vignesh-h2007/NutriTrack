package com.nutrition.dao;

import com.nutrition.model.WaterLog;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaterDAO {

    public WaterLog addWaterLog(int userId, int amountMl, String notes) throws SQLException {
        String sql = "INSERT INTO water_logs (user_id, amount_ml, logged_date, logged_at) VALUES (?, ?, CURRENT_DATE(), CURRENT_TIMESTAMP())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, amountMl);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                WaterLog log = new WaterLog(userId, amountMl, notes);
                if (rs.next()) {
                    log.setWaterLogId(rs.getInt(1));
                }
                log.setLoggedAt(new Timestamp(System.currentTimeMillis()));
                return log;
            }
        }
    }

    public void addWaterLog(WaterLog log) throws SQLException {
        addWaterLog(log.getUserId(), log.getAmountMl(), log.getNotes());
    }

    public int getTodayIntake(int userId) throws SQLException {
        return getTodayWaterIntakeMl(userId);
    }

    public int getTodayWaterIntakeMl(int userId) throws SQLException {
        String sql = "SELECT SUM(amount_ml) FROM water_logs WHERE user_id = ? AND (DATE(logged_at) = CURRENT_DATE() OR logged_date = CURRENT_DATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<WaterLog> getLogsForToday(int userId) throws SQLException {
        List<WaterLog> list = new ArrayList<>();
        String sql = "SELECT * FROM water_logs WHERE user_id = ? AND (DATE(logged_at) = CURRENT_DATE() OR logged_date = CURRENT_DATE()) ORDER BY logged_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WaterLog log = new WaterLog();
                    log.setWaterLogId(rs.getInt("water_log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setAmountMl(rs.getInt("amount_ml"));
                    log.setLoggedAt(rs.getTimestamp("logged_at"));
                    list.add(log);
                }
            }
        }
        return list;
    }

    public boolean deleteLog(int logId, int userId) throws SQLException {
        String sql = "DELETE FROM water_logs WHERE water_log_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }
}
