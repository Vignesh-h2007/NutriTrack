package com.nutrition.dao;

import com.nutrition.model.Food;
import com.nutrition.model.FoodHealthRule;
import com.nutrition.model.Allergy;
import com.nutrition.model.User;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAOImpl implements FoodDAO {

    @Override
    public List<Food> findAllActive() throws SQLException {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods WHERE is_active = TRUE ORDER BY food_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Food f = extractFood(rs);
                populateFoodAllergens(conn, f);
                foods.add(f);
            }
        }
        return foods;
    }

    @Override
    public List<Food> getAllFoods() throws SQLException {
        return findAllActive();
    }

    @Override
    public Food findById(int foodId) throws SQLException {
        String sql = "SELECT * FROM foods WHERE food_id = ? AND is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Food f = extractFood(rs);
                    populateFoodAllergens(conn, f);
                    return f;
                }
            }
        }
        return null;
    }

    @Override
    public Food getFoodById(int foodId) throws SQLException {
        return findById(foodId);
    }

    @Override
    public Food findFoodById(int foodId) throws SQLException {
        return findById(foodId);
    }

    @Override
    public List<Food> findByMealType(String mealType) throws SQLException {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods WHERE meal_type = ? AND is_active = TRUE ORDER BY food_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Food f = extractFood(rs);
                    populateFoodAllergens(conn, f);
                    foods.add(f);
                }
            }
        }
        return foods;
    }

    @Override
    public List<Food> searchFoods(String query, String category) throws SQLException {
        return searchFoods(query, category, null, null, null);
    }

    @Override
    public List<Food> getRecommendationsForUser(int userId, String healthGoal, String activityLevel) throws SQLException {
        List<Food> all = findAllActive();
        if (all.isEmpty()) return all;

        // Fetch user medical conditions & health rules
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.findById(userId);
        List<Integer> condIds = new ArrayList<>();
        if (user != null && user.getMedicalConditions() != null) {
            for (com.nutrition.model.MedicalCondition mc : user.getMedicalConditions()) {
                condIds.add(mc.getConditionId());
            }
        }

        List<FoodHealthRule> healthRules = findHealthRulesForConditions(condIds);
        List<Integer> avoidFoodIds = new ArrayList<>();
        for (FoodHealthRule r : healthRules) {
            if ("AVOID".equalsIgnoreCase(r.getRuleType())) {
                avoidFoodIds.add(r.getFoodId());
            }
        }

        List<Food> recs = new ArrayList<>();
        for (Food f : all) {
            if (avoidFoodIds.contains(f.getFoodId())) continue;

            // Apply food preference filter if applicable
            if (user != null && "Vegetarian".equalsIgnoreCase(user.getFoodPreference()) && !f.isVegetarian()) continue;
            if (user != null && "Vegan".equalsIgnoreCase(user.getFoodPreference()) && !f.isVegan()) continue;

            // Goal filtering
            if ("WEIGHT_LOSS".equalsIgnoreCase(healthGoal) && f.getCalories() <= 350) {
                recs.add(f);
            } else if ("MUSCLE_GAIN".equalsIgnoreCase(healthGoal) && f.getProteinG() >= 12) {
                recs.add(f);
            } else if (!"WEIGHT_LOSS".equalsIgnoreCase(healthGoal) && !"MUSCLE_GAIN".equalsIgnoreCase(healthGoal)) {
                recs.add(f);
            }
        }
        return recs.isEmpty() ? all : recs;
    }

    @Override
    public List<Food> searchFoods(String query, String category, String mealType, Boolean isVegetarian, Boolean isVegan) throws SQLException {
        List<Food> foods = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM foods WHERE is_active = TRUE ");

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (food_name LIKE ? OR description LIKE ?) ");
        }
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append("AND food_category = ? ");
        }
        if (mealType != null && !mealType.trim().isEmpty() && !"ALL".equalsIgnoreCase(mealType)) {
            sql.append("AND meal_type = ? ");
        }
        if (Boolean.TRUE.equals(isVegetarian)) {
            sql.append("AND is_vegetarian = TRUE ");
        }
        if (Boolean.TRUE.equals(isVegan)) {
            sql.append("AND is_vegan = TRUE ");
        }

        sql.append("ORDER BY food_name ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (query != null && !query.trim().isEmpty()) {
                String q = "%" + query.trim() + "%";
                stmt.setString(paramIndex++, q);
                stmt.setString(paramIndex++, q);
            }
            if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
                stmt.setString(paramIndex++, category.trim());
            }
            if (mealType != null && !mealType.trim().isEmpty() && !"ALL".equalsIgnoreCase(mealType)) {
                stmt.setString(paramIndex, mealType.trim());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Food f = extractFood(rs);
                    populateFoodAllergens(conn, f);
                    foods.add(f);
                }
            }
        }
        return foods;
    }

    @Override
    public List<FoodHealthRule> findHealthRulesForFood(int foodId) throws SQLException {
        List<FoodHealthRule> rules = new ArrayList<>();
        String sql = "SELECT * FROM food_health_rules WHERE food_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodHealthRule r = new FoodHealthRule();
                    r.setRuleId(rs.getInt("rule_id"));
                    r.setFoodId(rs.getInt("food_id"));
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
    public List<FoodHealthRule> findHealthRulesForConditions(List<Integer> conditionIds) throws SQLException {
        List<FoodHealthRule> rules = new ArrayList<>();
        if (conditionIds == null || conditionIds.isEmpty()) return rules;

        StringBuilder sql = new StringBuilder("SELECT * FROM food_health_rules WHERE condition_id IN (");
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
                    FoodHealthRule r = new FoodHealthRule();
                    r.setRuleId(rs.getInt("rule_id"));
                    r.setFoodId(rs.getInt("food_id"));
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
    public List<Integer> findFoodAllergyIds(int foodId) throws SQLException {
        List<Integer> allergyIds = new ArrayList<>();
        String sql = "SELECT allergy_id FROM food_allergens WHERE food_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    allergyIds.add(rs.getInt("allergy_id"));
                }
            }
        }
        return allergyIds;
    }

    private Food extractFood(ResultSet rs) throws SQLException {
        Food f = new Food();
        f.setFoodId(rs.getInt("food_id"));
        f.setFoodName(rs.getString("food_name"));
        f.setFoodCategory(rs.getString("food_category"));
        f.setMealType(rs.getString("meal_type"));
        f.setServingSize(rs.getDouble("serving_size"));
        f.setServingUnit(rs.getString("serving_unit"));
        f.setCalories(rs.getInt("calories"));
        f.setProteinG(rs.getDouble("protein_g"));
        f.setCarbsG(rs.getDouble("carbs_g"));
        f.setFatG(rs.getDouble("fat_g"));
        f.setFiberG(rs.getDouble("fiber_g"));
        f.setSugarG(rs.getDouble("sugar_g"));
        f.setSodiumMg(rs.getDouble("sodium_mg"));
        f.setVegetarian(rs.getBoolean("is_vegetarian"));
        f.setVegan(rs.getBoolean("is_vegan"));
        f.setDescription(rs.getString("description"));
        f.setImagePath(rs.getString("image_path"));
        f.setActive(rs.getBoolean("is_active"));
        f.setCreatedAt(rs.getTimestamp("created_at"));
        return f;
    }

    private void populateFoodAllergens(Connection conn, Food f) throws SQLException {
        String sql = "SELECT a.* FROM allergies a JOIN food_allergens fa ON a.allergy_id = fa.allergy_id WHERE fa.food_id = ?";
        List<Allergy> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, f.getFoodId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Allergy a = new Allergy();
                    a.setAllergyId(rs.getInt("allergy_id"));
                    a.setAllergyName(rs.getString("allergy_name"));
                    list.add(a);
                }
            }
        }
        f.setAllergens(list);
    }
}
