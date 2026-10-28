package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MealPlanItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemId;
    private int mealPlanId;
    private int foodId;
    private String mealType;
    private String scheduledTime;
    private double servingQuantity;
    private String status;
    private Timestamp completedAt;

    private Food food;

    public MealPlanItem() {}

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getMealPlanId() { return mealPlanId; }
    public void setMealPlanId(int mealPlanId) { this.mealPlanId = mealPlanId; }

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public double getServingQuantity() { return servingQuantity; }
    public void setServingQuantity(double servingQuantity) { this.servingQuantity = servingQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }

    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
}
