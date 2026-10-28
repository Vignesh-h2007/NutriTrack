package com.nutrition.dao;

import com.nutrition.model.AuditLog;
import com.nutrition.util.DBConnection;

import java.sql.*;

public class AuditDAO {

    public void log(int userId, String action, String entityType, Integer entityId) {
        String sql = "INSERT INTO audit_logs (user_id, action, entity_type, entity_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, entityType);
            if (entityId != null) {
                stmt.setInt(4, entityId);
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[AuditDAO] Failed to log audit: " + e.getMessage());
        }
    }
}
