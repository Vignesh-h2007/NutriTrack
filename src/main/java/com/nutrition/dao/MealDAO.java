package com.nutrition.dao;

import com.nutrition.model.MealPlan;
import com.nutrition.model.MealPlanItem;
import com.nutrition.model.MealLog;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface MealDAO {
    MealPlan findMealPlanByDate(int userId, Date planDate) throws SQLException;
    int createMealPlan(MealPlan plan) throws SQLException;
    MealPlan createMealPlan(int userId, String planName, Date planDate, List<Integer> foodIds, List<Double> quantities, List<String> mealTypes) throws SQLException;
    List<MealPlan> getMealPlansForDate(int userId, Date date) throws SQLException;
    List<MealPlan> getAllMealPlans(int userId) throws SQLException;
    boolean deleteMealPlan(int planId, int userId) throws SQLException;

    boolean updateMealPlanItemStatus(int itemId, String status) throws SQLException;
    boolean replaceMealPlanItem(int itemId, int newFoodId) throws SQLException;

    MealLog logMeal(int userId, Integer planItemId, int foodId, double quantityGrams, String mealType, Date logDate) throws SQLException;
    void logCompletedMeal(MealLog log) throws SQLException;
    List<MealLog> findMealLogsByDate(int userId, Date date) throws SQLException;
    List<MealLog> getMealLogsForDate(int userId, Date date) throws SQLException;
    boolean deleteMealLog(int logId, int userId) throws SQLException;
    boolean completeMealLog(int logId, int userId) throws SQLException;
}
