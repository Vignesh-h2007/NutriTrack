package com.nutrition.dao;

import com.nutrition.model.Exercise;
import com.nutrition.model.ExerciseHealthRule;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAOImpl implements ExerciseDAO {

    @Override
    public List<Exercise> findAllActive() throws SQLException {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE is_active = TRUE ORDER BY category ASC, exercise_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractExercise(rs));
            }
        }
        return list;
    }

    @Override
    public List<Exercise> getAllExercises() throws SQLException {
        return findAllActive();
    }

    @Override
    public Exercise findById(int exerciseId) throws SQLException {
        String sql = "SELECT * FROM exercises WHERE exercise_id = ? AND is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exerciseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractExercise(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Exercise getExerciseById(int exerciseId) throws SQLException {
        return findById(exerciseId);
    }

    @Override
    public List<Exercise> findByCategory(String category) throws SQLException {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE category = ? AND is_active = TRUE ORDER BY exercise_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractExercise(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Exercise> findByDifficulty(String difficulty) throws SQLException {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE difficulty = ? AND is_active = TRUE ORDER BY exercise_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, difficulty);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractExercise(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Exercise> getRecommendationsForUser(int userId, String healthGoal, String activityLevel) throws SQLException {
        List<Exercise> all = findAllActive();
        if (all.isEmpty()) return all;

        UserDAO userDAO = new UserDAOImpl();
        com.nutrition.model.User user = userDAO.findById(userId);
        List<Integer> condIds = new ArrayList<>();
        if (user != null && user.getMedicalConditions() != null) {
            for (com.nutrition.model.MedicalCondition mc : user.getMedicalConditions()) {
                condIds.add(mc.getConditionId());
            }
        }

        List<ExerciseHealthRule> healthRules = findHealthRulesForConditions(condIds);
        List<Integer> avoidExerciseIds = new ArrayList<>();
        for (ExerciseHealthRule r : healthRules) {
            if ("AVOID".equalsIgnoreCase(r.getRuleType())) {
                avoidExerciseIds.add(r.getExerciseId());
            }
        }

        List<Exercise> recs = new ArrayList<>();
        for (Exercise e : all) {
            if (avoidExerciseIds.contains(e.getExerciseId())) continue;
            recs.add(e);
        }
        return recs.isEmpty() ? all : recs;
    }

    @Override
    public List<Exercise> searchExercises(String query, String muscleGroup) throws SQLException {
        List<Exercise> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM exercises WHERE is_active = TRUE");
        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (LOWER(exercise_name) LIKE LOWER(?) OR LOWER(instructions) LIKE LOWER(?))");
        }
        if (muscleGroup != null && !muscleGroup.trim().isEmpty()) {
            sql.append(" AND LOWER(target_body_area) LIKE LOWER(?)");
        }
        sql.append(" ORDER BY exercise_name ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (query != null && !query.trim().isEmpty()) {
                stmt.setString(idx++, "%" + query.trim() + "%");
                stmt.setString(idx++, "%" + query.trim() + "%");
            }
            if (muscleGroup != null && !muscleGroup.trim().isEmpty()) {
                stmt.setString(idx, "%" + muscleGroup.trim() + "%");
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractExercise(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ExerciseHealthRule> findHealthRulesForConditions(List<Integer> conditionIds) throws SQLException {
        List<ExerciseHealthRule> rules = new ArrayList<>();
        if (conditionIds == null || conditionIds.isEmpty()) return rules;

        StringBuilder sql = new StringBuilder("SELECT * FROM exercise_health_rules WHERE condition_id IN (");
        for (int i = 0; i < conditionIds.size(); i++) {
            sql.append("?");
            if (i < conditionIds.size() - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < conditionIds.size(); i++) {
                stmt.setInt(i + 1, conditionIds.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExerciseHealthRule r = new ExerciseHealthRule();
                    r.setRuleId(rs.getInt("rule_id"));
                    r.setExerciseId(rs.getInt("exercise_id"));
                    r.setConditionId(rs.getInt("condition_id"));
                    r.setRuleType(rs.getString("rule_type"));
                    r.setReason(rs.getString("reason"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    rules.add(r);
                }
            }
        }
        return rules;
    }

    @Override
    public List<ExerciseHealthRule> findHealthRulesForExercise(int exerciseId) throws SQLException {
        List<ExerciseHealthRule> rules = new ArrayList<>();
        String sql = "SELECT * FROM exercise_health_rules WHERE exercise_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exerciseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExerciseHealthRule r = new ExerciseHealthRule();
                    r.setRuleId(rs.getInt("rule_id"));
                    r.setExerciseId(rs.getInt("exercise_id"));
                    r.setConditionId(rs.getInt("condition_id"));
                    r.setRuleType(rs.getString("rule_type"));
                    r.setReason(rs.getString("reason"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    rules.add(r);
                }
            }
        }
        return rules;
    }

    @Override
    public List<Exercise> getPersonalizedRecommendations(int userId) throws SQLException {
        UserDAO userDAO = new UserDAOImpl();
        com.nutrition.model.User user = userDAO.findById(userId);
        if (user == null) return findAllActive();

        List<Exercise> all = findAllActive();
        List<Integer> condIds = new ArrayList<>();
        List<String> condNames = new ArrayList<>();

        if (user.getMedicalConditions() != null) {
            for (com.nutrition.model.MedicalCondition mc : user.getMedicalConditions()) {
                condIds.add(mc.getConditionId());
                if (mc.getConditionName() != null) condNames.add(mc.getConditionName().toLowerCase());
            }
        }

        // Fetch health rules from database for conditions
        List<Integer> avoidIds = new ArrayList<>();
        List<Integer> allowedIds = new ArrayList<>();
        java.util.Map<Integer, String> ruleReasons = new java.util.HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {
            StringBuilder ruleSql = new StringBuilder("SELECT ehr.*, mc.condition_name FROM exercise_health_rules ehr ");
            ruleSql.append("LEFT JOIN medical_conditions mc ON ehr.condition_id = mc.condition_id WHERE 1=1 ");

            if (!condIds.isEmpty()) {
                ruleSql.append("AND (ehr.condition_id IN (");
                for (int i = 0; i < condIds.size(); i++) {
                    ruleSql.append("?");
                    if (i < condIds.size() - 1) ruleSql.append(",");
                }
                ruleSql.append(")");
                if (!condNames.isEmpty()) {
                    ruleSql.append(" OR LOWER(ehr.condition_name) IN (");
                    for (int i = 0; i < condNames.size(); i++) {
                        ruleSql.append("?");
                        if (i < condNames.size() - 1) ruleSql.append(",");
                    }
                    ruleSql.append(")");
                }
                ruleSql.append(")");

                try (PreparedStatement stmt = conn.prepareStatement(ruleSql.toString())) {
                    int pIndex = 1;
                    for (Integer cId : condIds) stmt.setInt(pIndex++, cId);
                    for (String cName : condNames) stmt.setString(pIndex++, cName);

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int exId = rs.getInt("exercise_id");
                            String rType = rs.getString("rule_type");
                            String reason = rs.getString("reason");
                            ruleReasons.put(exId, reason);

                            if ("AVOID".equalsIgnoreCase(rType)) {
                                avoidIds.add(exId);
                            } else if ("ALLOWED".equalsIgnoreCase(rType) || "ALLOW".equalsIgnoreCase(rType)) {
                                allowedIds.add(exId);
                            }
                        }
                    }
                }
            }
        }

        // Health condition specific safety checks — computed once, outside the loop
        boolean isHeartPatient = condNames.stream().anyMatch(c -> c.contains("heart") || c.contains("cardio"));
        boolean isJointPatient = condNames.stream().anyMatch(c -> c.contains("joint") || c.contains("arthritis") || c.contains("knee"));
        boolean isBackPatient  = condNames.stream().anyMatch(c -> c.contains("back") || c.contains("spine") || c.contains("lumbar"));

        List<Exercise> recommendations = new ArrayList<>();
        for (Exercise ex : all) {
            // Strict health safety: skip any exercise with AVOID rule
            if (avoidIds.contains(ex.getExerciseId())) {
                continue;
            }

            if (ruleReasons.containsKey(ex.getExerciseId())) {
                ex.setHealthRuleReason(ruleReasons.get(ex.getExerciseId()));
                ex.setHealthRuleStatus(allowedIds.contains(ex.getExerciseId()) ? "ALLOWED" : "RESTRICTED");
            } else {
                ex.setHealthRuleStatus("ALLOWED");
            }

            if (isHeartPatient) {
                // Heart condition: DO NOT recommend high intensity jumping or extreme anaerobic cardio
                String name = ex.getName().toLowerCase();
                if (name.contains("jumping jack") || name.contains("high knee") || name.contains("burpee") || "advanced".equalsIgnoreCase(ex.getDifficulty())) {
                    continue;
                }
            }

            if (isJointPatient) {
                // Joint issues: exclude high-impact jumping
                String name = ex.getName().toLowerCase();
                if (name.contains("jumping jack") || name.contains("high knee") || name.contains("burpee")) {
                    continue;
                }
            }

            if (isBackPatient) {
                String name = ex.getName().toLowerCase();
                if (name.contains("burpee") || name.contains("heavy deadlift")) {
                    continue;
                }
            }
            recommendations.add(ex);
        }
        return recommendations.isEmpty() ? all : recommendations;
    }

    @Override
    public int logExerciseCompletion(com.nutrition.model.ExerciseCompletion comp) throws SQLException {
        String sql = "INSERT INTO exercise_completions (user_id, exercise_id, exercise_name, completion_date, start_time, end_time, duration_seconds, sets_completed, repetitions_completed, calories_burned, completion_percentage, daily_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, comp.getUserId());
            stmt.setInt(2, comp.getExerciseId());
            stmt.setString(3, comp.getExerciseName() != null ? comp.getExerciseName() : "Exercise");
            stmt.setDate(4, comp.getCompletionDate() != null ? comp.getCompletionDate() : new Date(System.currentTimeMillis()));
            stmt.setTime(5, comp.getStartTime());
            stmt.setTime(6, comp.getEndTime());
            stmt.setInt(7, comp.getDurationSeconds());
            stmt.setInt(8, comp.getSetsCompleted());
            stmt.setInt(9, comp.getRepetitionsCompleted());
            stmt.setInt(10, comp.getCaloriesBurned());
            stmt.setInt(11, comp.getCompletionPercentage());
            stmt.setInt(12, comp.getDailyScore());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    comp.setCompletionId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public List<com.nutrition.model.ExerciseCompletion> getCompletionsByDate(int userId, Date date) throws SQLException {
        List<com.nutrition.model.ExerciseCompletion> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_completions WHERE user_id = ? AND completion_date = ? ORDER BY completion_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCompletion(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<com.nutrition.model.ExerciseCompletion> getCompletionsByDateRange(int userId, Date startDate, Date endDate) throws SQLException {
        List<com.nutrition.model.ExerciseCompletion> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_completions WHERE user_id = ? AND completion_date BETWEEN ? AND ? ORDER BY completion_date ASC, completion_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCompletion(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<com.nutrition.model.ExerciseCompletion> getRecentCompletions(int userId, int limit) throws SQLException {
        List<com.nutrition.model.ExerciseCompletion> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_completions WHERE user_id = ? ORDER BY completion_date DESC, completion_id DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit > 0 ? limit : 20);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCompletion(rs));
                }
            }
        }
        return list;
    }

    private com.nutrition.model.ExerciseCompletion extractCompletion(ResultSet rs) throws SQLException {
        com.nutrition.model.ExerciseCompletion c = new com.nutrition.model.ExerciseCompletion();
        c.setCompletionId(rs.getInt("completion_id"));
        c.setUserId(rs.getInt("user_id"));
        c.setExerciseId(rs.getInt("exercise_id"));
        c.setExerciseName(rs.getString("exercise_name"));
        c.setCompletionDate(rs.getDate("completion_date"));
        c.setStartTime(rs.getTime("start_time"));
        c.setEndTime(rs.getTime("end_time"));
        c.setDurationSeconds(rs.getInt("duration_seconds"));
        c.setSetsCompleted(rs.getInt("sets_completed"));
        c.setRepetitionsCompleted(rs.getInt("repetitions_completed"));
        c.setCaloriesBurned(rs.getInt("calories_burned"));
        c.setCompletionPercentage(rs.getInt("completion_percentage"));
        c.setDailyScore(rs.getInt("daily_score"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }

    private Exercise extractExercise(ResultSet rs) throws SQLException {
        Exercise e = new Exercise();
        e.setExerciseId(rs.getInt("exercise_id"));
        e.setExerciseName(rs.getString("exercise_name"));
        e.setCategory(rs.getString("category"));
        e.setDifficulty(rs.getString("difficulty"));
        e.setDurationSeconds(rs.getInt("duration_seconds"));
        e.setDefaultSets(rs.getInt("default_sets"));
        e.setDefaultRepetitions(rs.getInt("default_repetitions"));
        e.setRestSeconds(rs.getInt("rest_seconds"));
        e.setEstimatedCalories(rs.getInt("estimated_calories"));
        e.setTargetBodyArea(rs.getString("target_body_area"));
        e.setInstructions(rs.getString("instructions"));
        e.setImagePath(rs.getString("image_path"));
        e.setActive(rs.getBoolean("is_active"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        try { e.setHealthTags(rs.getString("health_tags")); } catch (Exception ignored) {}
        return e;
    }
}
