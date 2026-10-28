package com.nutrition.dao;

import com.nutrition.model.Exercise;
import com.nutrition.model.ExerciseCompletion;
import com.nutrition.model.ExerciseHealthRule;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ExerciseDAO {
    List<Exercise> findAllActive() throws SQLException;
    List<Exercise> getAllExercises() throws SQLException;
    Exercise findById(int exerciseId) throws SQLException;
    Exercise getExerciseById(int exerciseId) throws SQLException;
    List<Exercise> findByCategory(String category) throws SQLException;
    List<Exercise> findByDifficulty(String difficulty) throws SQLException;
    List<Exercise> getRecommendationsForUser(int userId, String healthGoal, String activityLevel) throws SQLException;
    List<Exercise> getPersonalizedRecommendations(int userId) throws SQLException;
    List<Exercise> searchExercises(String query, String muscleGroup) throws SQLException;
    List<ExerciseHealthRule> findHealthRulesForConditions(List<Integer> conditionIds) throws SQLException;
    List<ExerciseHealthRule> findHealthRulesForExercise(int exerciseId) throws SQLException;
    int logExerciseCompletion(ExerciseCompletion comp) throws SQLException;
    List<ExerciseCompletion> getCompletionsByDate(int userId, Date date) throws SQLException;
    List<ExerciseCompletion> getCompletionsByDateRange(int userId, Date startDate, Date endDate) throws SQLException;
    List<ExerciseCompletion> getRecentCompletions(int userId, int limit) throws SQLException;
}
