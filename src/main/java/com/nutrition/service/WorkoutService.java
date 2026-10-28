package com.nutrition.service;

import com.nutrition.dao.*;
import com.nutrition.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Service that handles exercise recommendations and workout session logic.
 */
public class WorkoutService {

    private final ExerciseDAO exerciseDAO = new ExerciseDAOImpl();
    private final WorkoutDAO workoutDAO = new WorkoutDAOImpl();
    private final AuditDAO auditDAO = new AuditDAO();

    // ────── Workout Plans ──────

    public List<WorkoutPlan> getPlans() throws SQLException {
        return workoutDAO.getPlans();
    }

    public WorkoutPlan createPlan(WorkoutPlan plan) throws SQLException {
        if (plan.getTitle() == null || plan.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Plan title is required");
        }
        return workoutDAO.createPlan(plan);
    }

    // ────── Exercises ──────

    public List<Exercise> getAllExercises() throws SQLException {
        return exerciseDAO.getAllExercises();
    }

    public List<Exercise> getRecommendedExercises(int userId) throws SQLException {
        return exerciseDAO.getPersonalizedRecommendations(userId);
    }

    public List<Exercise> getPersonalizedExercises(int userId) throws SQLException {
        return exerciseDAO.getPersonalizedRecommendations(userId);
    }

    public List<Exercise> searchExercises(String query, String muscleGroup) throws SQLException {
        return exerciseDAO.searchExercises(query, muscleGroup);
    }

    public Exercise getExerciseById(int exerciseId) throws SQLException {
        return exerciseDAO.getExerciseById(exerciseId);
    }

    // ────── Exercise Completions (Historical Tracking & Score) ──────

    public int logExerciseCompletion(ExerciseCompletion comp) throws SQLException {
        int id = exerciseDAO.logExerciseCompletion(comp);
        if (id > 0) {
            auditDAO.log(comp.getUserId(), "EXERCISE_COMPLETED: " + comp.getExerciseName(), "exercise_completions", id);
        }
        return id;
    }

    public List<ExerciseCompletion> getCompletionsByDate(int userId, Date date) throws SQLException {
        return exerciseDAO.getCompletionsByDate(userId, date);
    }

    public List<ExerciseCompletion> getRecentCompletions(int userId, int limit) throws SQLException {
        return exerciseDAO.getRecentCompletions(userId, limit);
    }

    // ────── Sessions ──────

    public ExerciseSession createSession(int userId, Date sessionDate,
                                         List<Integer> exerciseIds, List<Integer> plannedDurations) throws SQLException {
        ExerciseSession session = workoutDAO.createSession(userId, sessionDate, exerciseIds, plannedDurations);
        auditDAO.log(userId, "WORKOUT_SESSION_STARTED", "exercise_sessions", session.getSessionId());
        return session;
    }

    public boolean completeSession(int userId, int sessionId, int actualDurationSec, int actualCalories) throws SQLException {
        boolean ok = workoutDAO.completeSession(sessionId, userId, actualDurationSec, actualCalories);
        if (ok) auditDAO.log(userId, "WORKOUT_SESSION_COMPLETED", "exercise_sessions", sessionId);
        return ok;
    }

    public List<ExerciseSession> getSessionsByDate(int userId, Date date) throws SQLException {
        return workoutDAO.getSessionsByDate(userId, date);
    }

    public List<ExerciseSession> getRecentSessions(int userId, int limit) throws SQLException {
        return workoutDAO.getRecentSessions(userId, limit);
    }

    /**
     * Estimate calories burned using MET formula.
     * calories = MET * weightKg * durationHours
     */
    public int estimateCaloriesBurned(double metValue, double weightKg, int durationSeconds) {
        double hours = durationSeconds / 3600.0;
        return (int) Math.round(metValue * weightKg * hours);
    }
}
