package com.nutrition.service;

import com.nutrition.dao.*;
import com.nutrition.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Service that handles all meal-plan and meal-log business logic.
 */
public class MealService {

    private final MealDAO mealDAO = new MealDAOImpl();
    private final AuditDAO auditDAO = new AuditDAO();

    // ────── Meal Plan ──────

    public MealPlan createMealPlan(int userId, String planName, Date planDate,
                                   List<Integer> foodIds, List<Double> quantities,
                                   List<String> mealTypes) throws SQLException {
        if (planName == null || planName.trim().isEmpty()) throw new IllegalArgumentException("Plan name required.");
        MealPlan plan = mealDAO.createMealPlan(userId, planName.trim(), planDate, foodIds, quantities, mealTypes);
        auditDAO.log(userId, "MEAL_PLAN_CREATED", "meal_plans", plan.getPlanId());
        return plan;
    }

    public List<MealPlan> getMealPlansForDate(int userId, Date date) throws SQLException {
        return mealDAO.getMealPlansForDate(userId, date);
    }

    public List<MealPlan> getAllMealPlans(int userId) throws SQLException {
        return mealDAO.getAllMealPlans(userId);
    }

    public boolean deleteMealPlan(int userId, int planId) throws SQLException {
        boolean ok = mealDAO.deleteMealPlan(planId, userId);
        if (ok) auditDAO.log(userId, "MEAL_PLAN_DELETED", "meal_plans", planId);
        return ok;
    }

    // ────── Meal Log ──────

    public MealLog logMeal(int userId, int planItemId, int foodId,
                           double quantityGrams, String mealType, Date logDate) throws SQLException {
        MealLog log = mealDAO.logMeal(userId, planItemId, foodId, quantityGrams, mealType, logDate);
        auditDAO.log(userId, "MEAL_LOGGED", "meal_logs", log.getLogId());
        return log;
    }

    public List<MealLog> getMealLogsForDate(int userId, Date date) throws SQLException {
        return mealDAO.getMealLogsForDate(userId, date);
    }

    public boolean deleteMealLog(int userId, int logId) throws SQLException {
        boolean ok = mealDAO.deleteMealLog(logId, userId);
        if (ok) auditDAO.log(userId, "MEAL_LOG_DELETED", "meal_logs", logId);
        return ok;
    }

    public boolean completeMealLog(int userId, int logId) throws SQLException {
        boolean ok = mealDAO.completeMealLog(logId, userId);
        if (ok) auditDAO.log(userId, "MEAL_LOG_COMPLETED", "meal_logs", logId);
        return ok;
    }

    /**
     * Compute daily calorie target based on BMR + activity level (Mifflin-St Jeor).
     */
    public int computeDailyCalorieTarget(double weightKg, double heightCm, int age, String gender, String activityLevel) {
        double bmr;
        if ("Male".equalsIgnoreCase(gender)) {
            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;
        } else {
            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
        }
        double tdee;
        switch (activityLevel == null ? "" : activityLevel.toLowerCase()) {
            case "lightly_active":   tdee = bmr * 1.375; break;
            case "moderately_active":tdee = bmr * 1.55;  break;
            case "very_active":      tdee = bmr * 1.725; break;
            case "extremely_active": tdee = bmr * 1.9;   break;
            default:                 tdee = bmr * 1.2;   break; // sedentary
        }
        return (int) Math.round(tdee);
    }
}
