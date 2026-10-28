package com.nutrition.dao;

import com.nutrition.model.User;
import com.nutrition.model.MedicalCondition;
import com.nutrition.model.Allergy;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND account_status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = extractUser(rs);
                    populateUserAssociations(conn, u);
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ? AND account_status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = extractUser(rs);
                    populateUserAssociations(conn, u);
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND account_status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = extractUser(rs);
                    populateUserAssociations(conn, u);
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public int createUser(User u) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, password_salt, email, full_name, date_of_birth, age, gender, " +
                "height_cm, weight_kg, blood_group, occupation, income_range, activity_level, exercise_level, food_preference, " +
                "goal, sleep_hours, daily_water_target_ml, daily_calorie_target, daily_protein_target, daily_carbs_target, daily_fat_target) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPasswordHash());
            stmt.setString(3, u.getPasswordSalt());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getFullName());
            stmt.setDate(6, u.getDateOfBirth());
            stmt.setInt(7, u.getAge());
            stmt.setString(8, u.getGender());
            stmt.setDouble(9, u.getHeightCm());
            stmt.setDouble(10, u.getWeightKg());
            stmt.setString(11, u.getBloodGroup());
            stmt.setString(12, u.getOccupation());
            stmt.setString(13, u.getIncomeRange());
            stmt.setString(14, u.getActivityLevel());
            stmt.setString(15, u.getExerciseLevel());
            stmt.setString(16, u.getFoodPreference());
            stmt.setString(17, u.getGoal());
            stmt.setDouble(18, u.getSleepHours());
            stmt.setInt(19, u.getDailyWaterTargetMl());
            stmt.setInt(20, u.getDailyCalorieTarget());
            stmt.setDouble(21, u.getDailyProteinTarget());
            stmt.setDouble(22, u.getDailyCarbsTarget());
            stmt.setDouble(23, u.getDailyFatTarget());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userId = rs.getInt(1);
                        u.setUserId(userId);

                        saveWeightHistory(conn, userId, u.getWeightKg());

                        return userId;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public boolean updateUser(User u) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, age = ?, gender = ?, height_cm = ?, weight_kg = ?, " +
                "blood_group = ?, occupation = ?, income_range = ?, activity_level = ?, exercise_level = ?, " +
                "food_preference = ?, goal = ?, sleep_hours = ?, daily_water_target_ml = ?, daily_calorie_target = ?, " +
                "daily_protein_target = ?, daily_carbs_target = ?, daily_fat_target = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getFullName());
            stmt.setInt(2, u.getAge());
            stmt.setString(3, u.getGender());
            stmt.setDouble(4, u.getHeightCm());
            stmt.setDouble(5, u.getWeightKg());
            stmt.setString(6, u.getBloodGroup());
            stmt.setString(7, u.getOccupation());
            stmt.setString(8, u.getIncomeRange());
            stmt.setString(9, u.getActivityLevel());
            stmt.setString(10, u.getExerciseLevel());
            stmt.setString(11, u.getFoodPreference());
            stmt.setString(12, u.getGoal());
            stmt.setDouble(13, u.getSleepHours());
            stmt.setInt(14, u.getDailyWaterTargetMl());
            stmt.setInt(15, u.getDailyCalorieTarget());
            stmt.setDouble(16, u.getDailyProteinTarget());
            stmt.setDouble(17, u.getDailyCarbsTarget());
            stmt.setDouble(18, u.getDailyFatTarget());
            stmt.setInt(19, u.getUserId());

            boolean updated = stmt.executeUpdate() > 0;
            if (updated) {
                saveWeightHistory(conn, u.getUserId(), u.getWeightKg());
            }
            return updated;
        }
    }

    @Override
    public boolean updatePassword(int userId, String newHash, String newSalt) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, password_salt = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newHash);
            stmt.setString(2, newSalt);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "UPDATE users SET account_status = 'DELETED' WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public void saveUserMedicalConditions(int userId, List<Integer> conditionIds) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String delSql = "DELETE FROM user_medical_conditions WHERE user_id = ?";
                try (PreparedStatement delStmt = conn.prepareStatement(delSql)) {
                    delStmt.setInt(1, userId);
                    delStmt.executeUpdate();
                }

                if (conditionIds != null && !conditionIds.isEmpty()) {
                    String insSql = "INSERT INTO user_medical_conditions (user_id, condition_id) VALUES (?, ?)";
                    try (PreparedStatement insStmt = conn.prepareStatement(insSql)) {
                        for (Integer cId : conditionIds) {
                            insStmt.setInt(1, userId);
                            insStmt.setInt(2, cId);
                            insStmt.addBatch();
                        }
                        insStmt.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void saveUserAllergies(int userId, List<Integer> allergyIds) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String delSql = "DELETE FROM user_allergies WHERE user_id = ?";
                try (PreparedStatement delStmt = conn.prepareStatement(delSql)) {
                    delStmt.setInt(1, userId);
                    delStmt.executeUpdate();
                }

                if (allergyIds != null && !allergyIds.isEmpty()) {
                    String insSql = "INSERT INTO user_allergies (user_id, allergy_id) VALUES (?, ?)";
                    try (PreparedStatement insStmt = conn.prepareStatement(insSql)) {
                        for (Integer aId : allergyIds) {
                            insStmt.setInt(1, userId);
                            insStmt.setInt(2, aId);
                            insStmt.addBatch();
                        }
                        insStmt.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void saveWeightHistory(Connection conn, int userId, double weightKg) throws SQLException {
        String sql = "INSERT INTO weight_history (user_id, weight_kg) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, weightKg);
            stmt.executeUpdate();
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setPasswordSalt(rs.getString("password_salt"));
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setDateOfBirth(rs.getDate("date_of_birth"));
        u.setAge(rs.getInt("age"));
        u.setGender(rs.getString("gender"));
        u.setHeightCm(rs.getDouble("height_cm"));
        u.setWeightKg(rs.getDouble("weight_kg"));
        u.setBloodGroup(rs.getString("blood_group"));
        u.setOccupation(rs.getString("occupation"));
        u.setIncomeRange(rs.getString("income_range"));
        u.setActivityLevel(rs.getString("activity_level"));
        u.setExerciseLevel(rs.getString("exercise_level"));
        u.setFoodPreference(rs.getString("food_preference"));
        u.setGoal(rs.getString("goal"));
        u.setSleepHours(rs.getDouble("sleep_hours"));
        u.setDailyWaterTargetMl(rs.getInt("daily_water_target_ml"));
        u.setDailyCalorieTarget(rs.getInt("daily_calorie_target"));
        u.setDailyProteinTarget(rs.getDouble("daily_protein_target"));
        u.setDailyCarbsTarget(rs.getDouble("daily_carbs_target"));
        u.setDailyFatTarget(rs.getDouble("daily_fat_target"));
        u.setAccountStatus(rs.getString("account_status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }

    private void populateUserAssociations(Connection conn, User u) throws SQLException {
        String condSql = "SELECT mc.* FROM medical_conditions mc JOIN user_medical_conditions umc ON mc.condition_id = umc.condition_id WHERE umc.user_id = ?";
        List<MedicalCondition> conditions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(condSql)) {
            stmt.setInt(1, u.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MedicalCondition mc = new MedicalCondition();
                    mc.setConditionId(rs.getInt("condition_id"));
                    mc.setConditionName(rs.getString("condition_name"));
                    mc.setDescription(rs.getString("description"));
                    conditions.add(mc);
                }
            }
        }
        u.setMedicalConditions(conditions);

        String algSql = "SELECT a.* FROM allergies a JOIN user_allergies ua ON a.allergy_id = ua.allergy_id WHERE ua.user_id = ?";
        List<Allergy> allergies = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(algSql)) {
            stmt.setInt(1, u.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Allergy a = new Allergy();
                    a.setAllergyId(rs.getInt("allergy_id"));
                    a.setAllergyName(rs.getString("allergy_name"));
                    allergies.add(a);
                }
            }
        }
        u.setAllergies(allergies);
    }
}
