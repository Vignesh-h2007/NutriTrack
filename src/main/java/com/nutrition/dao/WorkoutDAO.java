package com.nutrition.dao;

import com.nutrition.model.ExerciseSession;
import com.nutrition.model.ExerciseSessionItem;
import com.nutrition.model.WorkoutPlan;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface WorkoutDAO {
    List<WorkoutPlan> getPlans() throws SQLException;
    WorkoutPlan createPlan(WorkoutPlan plan) throws SQLException;
    ExerciseSession findSessionByDate(int userId, Date sessionDate) throws SQLException;
    int createSession(ExerciseSession session) throws SQLException;
    ExerciseSession createSession(int userId, Date sessionDate, List<Integer> exerciseIds, List<Integer> plannedDurations) throws SQLException;
    boolean completeSession(int sessionId, int userId, int actualDurationSec, int actualCalories) throws SQLException;
    List<ExerciseSession> getSessionsByDate(int userId, Date date) throws SQLException;
    List<ExerciseSession> getRecentSessions(int userId, int limit) throws SQLException;
    boolean updateSessionProgress(int sessionId, int actualDuration, int actualCalories, int completionPercentage, String status) throws SQLException;
    boolean updateSessionItemProgress(int sessionItemId, int completedSets, int completedReps, int completedSeconds, String status) throws SQLException;
}
