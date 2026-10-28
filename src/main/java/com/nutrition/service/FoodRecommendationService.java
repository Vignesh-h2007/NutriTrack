package com.nutrition.service;

import com.nutrition.dao.*;
import com.nutrition.model.Food;
import com.nutrition.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that recommends foods to a user based on their health profile,
 * goal, and database-driven health rules.
 */
public class FoodRecommendationService {

    private final FoodDAO foodDAO = new FoodDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    /**
     * Returns a list of recommended foods based on health profile.
     * Rules stored in food_health_rules table are applied.
     */
    public List<Food> getRecommendations(int userId) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) return new ArrayList<>();
        return foodDAO.getRecommendationsForUser(userId, user.getHealthGoal(), user.getActivityLevel());
    }

    /**
     * Search foods by name or category.
     */
    public List<Food> searchFoods(String query, String category) throws SQLException {
        return foodDAO.searchFoods(query, category);
    }

    /**
     * Get full food list.
     */
    public List<Food> getAllFoods() throws SQLException {
        return foodDAO.getAllFoods();
    }

    /**
     * Get food by ID.
     */
    public Food getFoodById(int foodId) throws SQLException {
        return foodDAO.getFoodById(foodId);
    }

    /**
     * Calculate nutritional totals for a list of foods with given quantities (in grams).
     */
    public NutritionTotals calculateNutrition(List<Food> foods, List<Double> quantitiesGrams) {
        NutritionTotals totals = new NutritionTotals();
        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            double factor = quantitiesGrams.get(i) / 100.0;
            totals.calories += f.getCaloriesPer100g() * factor;
            totals.proteinG += f.getProteinG() * factor;
            totals.carbsG += f.getCarbsG() * factor;
            totals.fatG += f.getFatG() * factor;
            totals.fiberG += f.getFiberG() * factor;
        }
        return totals;
    }

    /** Simple POJO to hold totals. */
    public static class NutritionTotals {
        public double calories, proteinG, carbsG, fatG, fiberG;
    }
}
