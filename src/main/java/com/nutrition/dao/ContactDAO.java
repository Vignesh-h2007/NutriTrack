package com.nutrition.dao;

import com.nutrition.model.ContactMessage;
import com.nutrition.util.DBConnection;

import java.sql.*;

public class ContactDAO {

    public boolean saveMessage(ContactMessage msg) throws SQLException {
        String sql = "INSERT INTO contact_messages (user_id, name, email, subject, message) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (msg.getUserId() != null) {
                stmt.setInt(1, msg.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, msg.getName());
            stmt.setString(3, msg.getEmail());
            stmt.setString(4, msg.getSubject());
            stmt.setString(5, msg.getMessage());
            return stmt.executeUpdate() > 0;
        }
    }
}
