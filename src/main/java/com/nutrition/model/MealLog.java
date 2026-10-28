package com.nutrition.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class MealLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private int logId;
    private int userId;
    private int foodId;
    private String mealType;
    private double quantity;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private Date loggedDate;
    private Time loggedTime;
    private String source;

    private String foodName;

    public MealLog() {}

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getQuantityGrams() { return quantity; }
    public void setQuantityGrams(double quantityGrams) { this.quantity = quantityGrams; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public Date getLoggedDate() { return loggedDate; }
    public void setLoggedDate(Date loggedDate) { this.loggedDate = loggedDate; }

    public Time getLoggedTime() { return loggedTime; }
    public void setLoggedTime(Time loggedTime) { this.loggedTime = loggedTime; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    private String status;
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
