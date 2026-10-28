package com.nutrition.dao;

import com.nutrition.model.Allergy;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllergyDAO {

    public List<Allergy> findAll() throws SQLException {
        List<Allergy> list = new ArrayList<>();
        String sql = "SELECT * FROM allergies ORDER BY allergy_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Allergy a = new Allergy();
                a.setAllergyId(rs.getInt("allergy_id"));
                a.setAllergyName(rs.getString("allergy_name"));
                list.add(a);
            }
        }
        return list;
    }
}
