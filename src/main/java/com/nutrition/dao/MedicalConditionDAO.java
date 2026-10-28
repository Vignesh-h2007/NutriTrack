package com.nutrition.dao;

import com.nutrition.model.MedicalCondition;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalConditionDAO {

    public List<MedicalCondition> findAllActive() throws SQLException {
        List<MedicalCondition> list = new ArrayList<>();
        String sql = "SELECT * FROM medical_conditions WHERE is_active = TRUE ORDER BY condition_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MedicalCondition mc = new MedicalCondition();
                mc.setConditionId(rs.getInt("condition_id"));
                mc.setConditionName(rs.getString("condition_name"));
                mc.setDescription(rs.getString("description"));
                list.add(mc);
            }
        }
        return list;
    }
}
