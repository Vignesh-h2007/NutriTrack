package com.nutrition.dao;

import com.nutrition.model.WeightHistory;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeightDAO {

    public WeightHistory addWeightRecord(int userId, double weightKg, String notes) throws SQLException {
        String sql = "INSERT INTO weight_history (user_id, weight_kg, record_date, recorded_at) VALUES (?, ?, CURRENT_DATE(), CURRENT_TIMESTAMP())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, weightKg);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                WeightHistory history = new WeightHistory(userId, weightKg, notes);
                if (rs.next()) {
                    history.setWeightHistoryId(rs.getInt(1));
                }
                history.setRecordedAt(new Timestamp(System.currentTimeMillis()));
                return history;
            }
        }
    }

    public void addWeightRecord(WeightHistory history) throws SQLException {
        addWeightRecord(history.getUserId(), history.getWeightKg(), history.getNotes());
    }

    public List<WeightHistory> getWeightHistory(int userId, int limit) throws SQLException {
        List<WeightHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM weight_history WHERE user_id = ? ORDER BY recorded_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit > 0 ? limit : 50);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WeightHistory wh = new WeightHistory();
                    wh.setWeightHistoryId(rs.getInt("weight_history_id"));
                    wh.setUserId(rs.getInt("user_id"));
                    wh.setWeightKg(rs.getDouble("weight_kg"));
                    wh.setRecordedAt(rs.getTimestamp("recorded_at"));
                    list.add(wh);
                }
            }
        }
        return list;
    }

    public WeightHistory getLatestWeight(int userId) throws SQLException {
        List<WeightHistory> list = getWeightHistory(userId, 1);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<WeightHistory> findWeightHistory(int userId, Date startDate, Date endDate) throws SQLException {
        List<WeightHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM weight_history WHERE user_id = ? AND DATE(recorded_at) BETWEEN ? AND ? ORDER BY recorded_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WeightHistory wh = new WeightHistory();
                    wh.setWeightHistoryId(rs.getInt("weight_history_id"));
                    wh.setUserId(rs.getInt("user_id"));
                    wh.setWeightKg(rs.getDouble("weight_kg"));
                    wh.setRecordedAt(rs.getTimestamp("recorded_at"));
                    list.add(wh);
                }
            }
        }
        return list;
    }
}
