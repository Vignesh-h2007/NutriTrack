package com.nutrition.dao;

import com.nutrition.model.ExerciseSession;
import com.nutrition.model.ExerciseSessionItem;
import com.nutrition.model.Exercise;
import com.nutrition.model.WorkoutPlan;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAOImpl implements WorkoutDAO {

    @Override
    public List<WorkoutPlan> getPlans() throws SQLException {
        List<WorkoutPlan> list = new ArrayList<>();
        String sql = "SELECT * FROM workout_plans WHERE is_active = TRUE ORDER BY plan_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                WorkoutPlan p = new WorkoutPlan();
                p.setPlanId(rs.getInt("plan_id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setTargetGoal(rs.getString("target_goal"));
                p.setDurationWeeks(rs.getInt("duration_weeks"));
                p.setEstimatedCaloriesBurn(rs.getInt("estimated_calories_burn"));
                p.setActive(rs.getBoolean("is_active"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public WorkoutPlan createPlan(WorkoutPlan plan) throws SQLException {
        String sql = "INSERT INTO workout_plans (title, description, target_goal, duration_weeks, estimated_calories_burn) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, plan.getTitle());
            stmt.setString(2, plan.getDescription() != null ? plan.getDescription() : "");
            stmt.setString(3, plan.getTargetGoal() != null ? plan.getTargetGoal() : "FITNESS");
            stmt.setInt(4, plan.getDurationWeeks() > 0 ? plan.getDurationWeeks() : 4);
            stmt.setInt(5, plan.getEstimatedCaloriesBurn() > 0 ? plan.getEstimatedCaloriesBurn() : 350);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    plan.setPlanId(rs.getInt(1));
                }
            }
        }
        return plan;
    }

    @Override
    public ExerciseSession findSessionByDate(int userId, Date sessionDate) throws SQLException {
        String sql = "SELECT * FROM exercise_sessions WHERE user_id = ? AND session_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, sessionDate);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSession(conn, rs);
                }
            }
        }
        return null;
    }

    @Override
    public int createSession(ExerciseSession session) throws SQLException {
        String sql = "INSERT INTO exercise_sessions (user_id, session_date, planned_duration, estimated_calories, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int sessionId;
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, session.getUserId());
                    stmt.setDate(2, session.getSessionDate());
                    stmt.setInt(3, session.getPlannedDuration());
                    stmt.setInt(4, session.getEstimatedCalories());
                    stmt.setString(5, session.getStatus() != null ? session.getStatus() : "PLANNED");
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            sessionId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to obtain session_id.");
                        }
                    }
                }

                String itemSql = "INSERT INTO exercise_session_items (session_id, exercise_id, planned_sets, planned_repetitions, planned_seconds, status, order_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                    int order = 1;
                    for (ExerciseSessionItem item : session.getItems()) {
                        itemStmt.setInt(1, sessionId);
                        itemStmt.setInt(2, item.getExerciseId());
                        itemStmt.setInt(3, item.getPlannedSets());
                        itemStmt.setInt(4, item.getPlannedRepetitions());
                        itemStmt.setInt(5, item.getPlannedSeconds());
                        itemStmt.setString(6, item.getStatus() != null ? item.getStatus() : "PENDING");
                        itemStmt.setInt(7, order++);
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }

                conn.commit();
                return sessionId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public ExerciseSession createSession(int userId, Date sessionDate, List<Integer> exerciseIds, List<Integer> plannedDurations) throws SQLException {
        ExerciseSession session = new ExerciseSession();
        session.setUserId(userId);
        session.setSessionDate(sessionDate != null ? sessionDate : new Date(System.currentTimeMillis()));
        session.setStatus("IN_PROGRESS");

        int totalDuration = 0;
        List<ExerciseSessionItem> items = new ArrayList<>();
        if (exerciseIds != null) {
            for (int i = 0; i < exerciseIds.size(); i++) {
                int exId = exerciseIds.get(i);
                int dur = (plannedDurations != null && i < plannedDurations.size()) ? plannedDurations.get(i) : 900;
                totalDuration += dur;

                ExerciseSessionItem item = new ExerciseSessionItem();
                item.setExerciseId(exId);
                item.setPlannedSets(3);
                item.setPlannedRepetitions(10);
                item.setPlannedSeconds(dur);
                item.setStatus("PENDING");
                items.add(item);
            }
        }
        session.setPlannedDuration(totalDuration);
        session.setEstimatedCalories((int) Math.round(totalDuration * 0.15));
        session.setItems(items);

        int sessionId = createSession(session);
        session.setSessionId(sessionId);
        return session;
    }

    @Override
    public boolean completeSession(int sessionId, int userId, int actualDurationSec, int actualCalories) throws SQLException {
        return updateSessionProgress(sessionId, actualDurationSec, actualCalories, 100, "COMPLETED");
    }

    @Override
    public List<ExerciseSession> getSessionsByDate(int userId, Date date) throws SQLException {
        List<ExerciseSession> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_sessions WHERE user_id = ? AND session_date = ? ORDER BY session_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSession(conn, rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ExerciseSession> getRecentSessions(int userId, int limit) throws SQLException {
        List<ExerciseSession> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_sessions WHERE user_id = ? ORDER BY session_date DESC, session_id DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit > 0 ? limit : 20);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSession(conn, rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateSessionProgress(int sessionId, int actualDuration, int actualCalories, int completionPercentage, String status) throws SQLException {
        String sql = "UPDATE exercise_sessions SET actual_duration = ?, actual_calories = ?, completion_percentage = ?, status = ?, completed_at = IF(? = 'COMPLETED', CURRENT_TIMESTAMP, completed_at) WHERE session_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actualDuration);
            stmt.setInt(2, actualCalories);
            stmt.setInt(3, completionPercentage);
            stmt.setString(4, status);
            stmt.setString(5, status);
            stmt.setInt(6, sessionId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateSessionItemProgress(int sessionItemId, int completedSets, int completedReps, int completedSeconds, String status) throws SQLException {
        String sql = "UPDATE exercise_session_items SET completed_sets = ?, completed_repetitions = ?, completed_seconds = ?, status = ? WHERE session_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, completedSets);
            stmt.setInt(2, completedReps);
            stmt.setInt(3, completedSeconds);
            stmt.setString(4, status);
            stmt.setInt(5, sessionItemId);
            return stmt.executeUpdate() > 0;
        }
    }

    private ExerciseSession mapSession(Connection conn, ResultSet rs) throws SQLException {
        ExerciseSession s = new ExerciseSession();
        s.setSessionId(rs.getInt("session_id"));
        s.setUserId(rs.getInt("user_id"));
        s.setSessionDate(rs.getDate("session_date"));
        s.setStartedAt(rs.getTimestamp("started_at"));
        s.setCompletedAt(rs.getTimestamp("completed_at"));
        s.setPlannedDuration(rs.getInt("planned_duration"));
        s.setActualDuration(rs.getInt("actual_duration"));
        s.setEstimatedCalories(rs.getInt("estimated_calories"));
        s.setActualCalories(rs.getInt("actual_calories"));
        s.setCompletionPercentage(rs.getInt("completion_percentage"));
        s.setStatus(rs.getString("status"));
        s.setItems(findItemsBySessionId(conn, s.getSessionId()));
        return s;
    }

    private List<ExerciseSessionItem> findItemsBySessionId(Connection conn, int sessionId) throws SQLException {
        List<ExerciseSessionItem> list = new ArrayList<>();
        String sql = "SELECT esi.*, e.* FROM exercise_session_items esi JOIN exercises e ON esi.exercise_id = e.exercise_id WHERE esi.session_id = ? ORDER BY esi.order_number ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExerciseSessionItem item = new ExerciseSessionItem();
                    item.setSessionItemId(rs.getInt("session_item_id"));
                    item.setSessionId(rs.getInt("session_id"));
                    item.setExerciseId(rs.getInt("exercise_id"));
                    item.setPlannedSets(rs.getInt("planned_sets"));
                    item.setCompletedSets(rs.getInt("completed_sets"));
                    item.setPlannedRepetitions(rs.getInt("planned_repetitions"));
                    item.setCompletedRepetitions(rs.getInt("completed_repetitions"));
                    item.setPlannedSeconds(rs.getInt("planned_seconds"));
                    item.setCompletedSeconds(rs.getInt("completed_seconds"));
                    item.setStatus(rs.getString("status"));
                    item.setOrderNumber(rs.getInt("order_number"));

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

                    item.setExercise(e);
                    list.add(item);
                }
            }
        }
        return list;
    }
}
