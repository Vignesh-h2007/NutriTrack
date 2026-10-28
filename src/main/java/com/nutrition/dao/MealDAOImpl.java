package com.nutrition.dao;

import com.nutrition.model.MealPlan;
import com.nutrition.model.MealPlanItem;
import com.nutrition.model.MealLog;
import com.nutrition.model.Food;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDAOImpl implements MealDAO {

    @Override
    public MealPlan findMealPlanByDate(int userId, Date planDate) throws SQLException {
        String sql = "SELECT * FROM meal_plans WHERE user_id = ? AND plan_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, planDate);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MealPlan mp = new MealPlan();
                    mp.setMealPlanId(rs.getInt("meal_plan_id"));
                    mp.setUserId(rs.getInt("user_id"));
                    mp.setPlanDate(rs.getDate("plan_date"));
                    mp.setCalorieTarget(rs.getInt("calorie_target"));
                    mp.setStatus(rs.getString("status"));
                    mp.setCreatedAt(rs.getTimestamp("created_at"));
                    mp.setItems(findItemsByMealPlanId(conn, mp.getMealPlanId()));
                    return mp;
                }
            }
        }
        return null;
    }

    @Override
    public int createMealPlan(MealPlan plan) throws SQLException {
        String sql = "INSERT INTO meal_plans (user_id, plan_date, calorie_target, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int mealPlanId;
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, plan.getUserId());
                    stmt.setDate(2, plan.getPlanDate());
                    stmt.setInt(3, plan.getCalorieTarget());
                    stmt.setString(4, plan.getStatus() != null ? plan.getStatus() : "ACTIVE");
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            mealPlanId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to obtain meal_plan_id.");
                        }
                    }
                }

                String itemSql = "INSERT INTO meal_plan_items (meal_plan_id, food_id, meal_type, scheduled_time, serving_quantity, status) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                    for (MealPlanItem item : plan.getItems()) {
                        itemStmt.setInt(1, mealPlanId);
                        itemStmt.setInt(2, item.getFoodId());
                        itemStmt.setString(3, item.getMealType());
                        itemStmt.setString(4, item.getScheduledTime());
                        itemStmt.setDouble(5, item.getServingQuantity());
                        itemStmt.setString(6, item.getStatus() != null ? item.getStatus() : "PLANNED");
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }

                conn.commit();
                return mealPlanId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public MealPlan createMealPlan(int userId, String planName, Date planDate, List<Integer> foodIds, List<Double> quantities, List<String> mealTypes) throws SQLException {
        MealPlan plan = new MealPlan();
        plan.setUserId(userId);
        plan.setPlanName(planName);
        plan.setPlanDate(planDate != null ? planDate : new Date(System.currentTimeMillis()));
        plan.setStatus("ACTIVE");

        List<MealPlanItem> items = new ArrayList<>();
        if (foodIds != null) {
            for (int i = 0; i < foodIds.size(); i++) {
                MealPlanItem item = new MealPlanItem();
                item.setFoodId(foodIds.get(i));
                item.setServingQuantity(quantities != null && i < quantities.size() ? quantities.get(i) : 100.0);
                item.setMealType(mealTypes != null && i < mealTypes.size() ? mealTypes.get(i) : "LUNCH");
                item.setScheduledTime("12:00");
                item.setStatus("PLANNED");
                items.add(item);
            }
        }
        plan.setItems(items);
        int planId = createMealPlan(plan);
        plan.setMealPlanId(planId);
        return plan;
    }

    @Override
    public List<MealPlan> getMealPlansForDate(int userId, Date date) throws SQLException {
        List<MealPlan> list = new ArrayList<>();
        MealPlan plan = findMealPlanByDate(userId, date);
        if (plan != null) list.add(plan);
        return list;
    }

    @Override
    public List<MealPlan> getAllMealPlans(int userId) throws SQLException {
        List<MealPlan> list = new ArrayList<>();
        String sql = "SELECT * FROM meal_plans WHERE user_id = ? ORDER BY plan_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealPlan mp = new MealPlan();
                    mp.setMealPlanId(rs.getInt("meal_plan_id"));
                    mp.setUserId(rs.getInt("user_id"));
                    mp.setPlanDate(rs.getDate("plan_date"));
                    mp.setCalorieTarget(rs.getInt("calorie_target"));
                    mp.setStatus(rs.getString("status"));
                    mp.setCreatedAt(rs.getTimestamp("created_at"));
                    mp.setItems(findItemsByMealPlanId(conn, mp.getMealPlanId()));
                    list.add(mp);
                }
            }
        }
        return list;
    }

    @Override
    public boolean deleteMealPlan(int planId, int userId) throws SQLException {
        String sql = "DELETE FROM meal_plans WHERE meal_plan_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateMealPlanItemStatus(int itemId, String status) throws SQLException {
        String sql = "UPDATE meal_plan_items SET status = ?, completed_at = CURRENT_TIMESTAMP WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, itemId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean replaceMealPlanItem(int itemId, int newFoodId) throws SQLException {
        String sql = "UPDATE meal_plan_items SET food_id = ?, status = 'REPLACED' WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newFoodId);
            stmt.setInt(2, itemId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public MealLog logMeal(int userId, Integer planItemId, int foodId, double quantityGrams, String mealType, Date logDate) throws SQLException {
        MealLog log = new MealLog();
        log.setUserId(userId);
        log.setFoodId(foodId);
        log.setMealType(mealType != null ? mealType : "LUNCH");
        log.setQuantity(quantityGrams);
        log.setLoggedDate(logDate != null ? logDate : new Date(System.currentTimeMillis()));
        log.setSource("MANUAL");
        log.setStatus("PENDING");

        // Calculate macros from food item
        FoodDAO foodDAO = new FoodDAOImpl();
        Food food = foodDAO.findFoodById(foodId);
        if (food != null) {
            double ratio = quantityGrams / 100.0;
            log.setCalories((int) Math.round(food.getCalories() * ratio));
            log.setProtein(food.getProteinG() * ratio);
            log.setCarbs(food.getCarbsG() * ratio);
            log.setFat(food.getFatG() * ratio);
            log.setFoodName(food.getFoodName());
        }

        logCompletedMeal(log);
        return log;
    }

    @Override
    public void logCompletedMeal(MealLog log) throws SQLException {
        String sql = "INSERT INTO meal_logs (user_id, food_id, meal_type, quantity_grams, calories, protein, carbs, fat, logged_date, logged_time, source, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIME(), ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, log.getUserId());
            stmt.setInt(2, log.getFoodId());
            stmt.setString(3, log.getMealType());
            stmt.setDouble(4, log.getQuantity());
            stmt.setInt(5, log.getCalories());
            stmt.setDouble(6, log.getProtein());
            stmt.setDouble(7, log.getCarbs());
            stmt.setDouble(8, log.getFat());
            stmt.setDate(9, log.getLoggedDate());
            stmt.setString(10, log.getSource() != null ? log.getSource() : "MANUAL");
            stmt.setString(11, log.getStatus() != null ? log.getStatus() : "PENDING");
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    log.setLogId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<MealLog> findMealLogsByDate(int userId, Date date) throws SQLException {
        List<MealLog> list = new ArrayList<>();
        String sql = "SELECT ml.*, f.food_name FROM meal_logs ml JOIN foods f ON ml.food_id = f.food_id WHERE ml.user_id = ? AND ml.logged_date = ? ORDER BY ml.logged_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealLog log = new MealLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setFoodId(rs.getInt("food_id"));
                    log.setMealType(rs.getString("meal_type"));
                    log.setQuantity(rs.getDouble("quantity_grams"));
                    log.setCalories(rs.getInt("calories"));
                    log.setProtein(rs.getDouble("protein"));
                    log.setCarbs(rs.getDouble("carbs"));
                    log.setFat(rs.getDouble("fat"));
                    log.setLoggedDate(rs.getDate("logged_date"));
                    log.setLoggedTime(rs.getTime("logged_time"));
                    log.setSource(rs.getString("source"));
                    log.setStatus(rs.getString("status"));
                    log.setFoodName(rs.getString("food_name"));
                    list.add(log);
                }
            }
        }
        return list;
    }

    @Override
    public List<MealLog> getMealLogsForDate(int userId, Date date) throws SQLException {
        return findMealLogsByDate(userId, date);
    }

    @Override
    public boolean deleteMealLog(int logId, int userId) throws SQLException {
        String sql = "DELETE FROM meal_logs WHERE log_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean completeMealLog(int logId, int userId) throws SQLException {
        String sql = "UPDATE meal_logs SET status = 'COMPLETED' WHERE log_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    private List<MealPlanItem> findItemsByMealPlanId(Connection conn, int mealPlanId) throws SQLException {
        List<MealPlanItem> items = new ArrayList<>();
        String sql = "SELECT mpi.*, f.* FROM meal_plan_items mpi JOIN foods f ON mpi.food_id = f.food_id WHERE mpi.meal_plan_id = ? ORDER BY mpi.item_id ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mealPlanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealPlanItem item = new MealPlanItem();
                    item.setItemId(rs.getInt("item_id"));
                    item.setMealPlanId(rs.getInt("meal_plan_id"));
                    item.setFoodId(rs.getInt("food_id"));
                    item.setMealType(rs.getString("meal_type"));
                    item.setScheduledTime(rs.getString("scheduled_time"));
                    item.setServingQuantity(rs.getDouble("serving_quantity"));
                    item.setStatus(rs.getString("status"));
                    item.setCompletedAt(rs.getTimestamp("completed_at"));

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

                    item.setFood(f);
                    items.add(item);
                }
            }
        }
        return items;
    }
}
