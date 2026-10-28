package com.nutrition.dao;

import com.nutrition.model.Food;
import com.nutrition.model.FoodHealthRule;
import java.sql.SQLException;
import java.util.List;

public interface FoodDAO {
    List<Food> findAllActive() throws SQLException;
    List<Food> getAllFoods() throws SQLException;
    Food findById(int foodId) throws SQLException;
    Food getFoodById(int foodId) throws SQLException;
    Food findFoodById(int foodId) throws SQLException;
    List<Food> findByMealType(String mealType) throws SQLException;
    List<Food> searchFoods(String query, String category, String mealType, Boolean isVegetarian, Boolean isVegan) throws SQLException;
    List<Food> searchFoods(String query, String category) throws SQLException;
    List<Food> getRecommendationsForUser(int userId, String healthGoal, String activityLevel) throws SQLException;
    List<FoodHealthRule> findHealthRulesForFood(int foodId) throws SQLException;
    List<FoodHealthRule> findHealthRulesForConditions(List<Integer> conditionIds) throws SQLException;
    List<Integer> findFoodAllergyIds(int foodId) throws SQLException;
}
