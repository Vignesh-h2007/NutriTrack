package com.nutrition.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MealPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private int mealPlanId;
    private int userId;
    private Date planDate;
    private int calorieTarget;
    private String planName = "Daily Meal Plan";
    private String status;
    private Timestamp createdAt;

    private List<MealPlanItem> items = new ArrayList<>();

    public MealPlan() {}

    public int getMealPlanId() { return mealPlanId; }
    public void setMealPlanId(int mealPlanId) { this.mealPlanId = mealPlanId; }

    public int getPlanId() { return mealPlanId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getPlanDate() { return planDate; }
    public void setPlanDate(Date planDate) { this.planDate = planDate; }

    public String getPlanName() { return planName != null ? planName : "Daily Meal Plan"; }
    public void setPlanName(String planName) { this.planName = planName; }

    public int getCalorieTarget() { return calorieTarget; }
    public void setCalorieTarget(int calorieTarget) { this.calorieTarget = calorieTarget; }

    public int getTotalCalories() { return calorieTarget; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<MealPlanItem> getItems() { return items; }
    public void setItems(List<MealPlanItem> items) { this.items = items; }
}
