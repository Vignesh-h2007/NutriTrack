package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Food implements Serializable {
    private static final long serialVersionUID = 1L;

    private int foodId;
    private String foodName;
    private String foodCategory;
    private String mealType;
    private double servingSize;
    private String servingUnit;
    private int calories;
    private double proteinG;
    private double carbsG;
    private double fatG;
    private double fiberG;
    private double sugarG;
    private double sodiumMg;
    private boolean isVegetarian;
    private boolean isVegan;
    private String description;
    private String imagePath;
    private boolean isActive;
    private Timestamp createdAt;

    private List<Allergy> allergens = new ArrayList<>();
    private String healthRuleStatus;
    private String healthRuleReason;

    public Food() {}

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getName() { return foodName; }
    public void setName(String name) { this.foodName = name; }

    public String getFoodCategory() { return foodCategory; }
    public void setFoodCategory(String foodCategory) { this.foodCategory = foodCategory; }

    public String getCategory() { return foodCategory; }
    public void setCategory(String category) { this.foodCategory = category; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public double getServingSize() { return servingSize; }
    public void setServingSize(double servingSize) { this.servingSize = servingSize; }

    public double getServingSizeG() { return servingSize; }

    public String getServingUnit() { return servingUnit; }
    public void setServingUnit(String servingUnit) { this.servingUnit = servingUnit; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public double getCaloriesPer100g() { return calories; }

    public double getProteinG() { return proteinG; }
    public void setProteinG(double proteinG) { this.proteinG = proteinG; }

    public double getProtein() { return proteinG; }

    public double getCarbsG() { return carbsG; }
    public void setCarbsG(double carbsG) { this.carbsG = carbsG; }

    public double getCarbs() { return carbsG; }

    public double getFatG() { return fatG; }
    public void setFatG(double fatG) { this.fatG = fatG; }

    public double getFat() { return fatG; }

    public double getFiberG() { return fiberG; }
    public void setFiberG(double fiberG) { this.fiberG = fiberG; }

    public double getSugarG() { return sugarG; }
    public void setSugarG(double sugarG) { this.sugarG = sugarG; }

    public double getSodiumMg() { return sodiumMg; }
    public void setSodiumMg(double sodiumMg) { this.sodiumMg = sodiumMg; }

    public boolean isVegetarian() { return isVegetarian; }
    public void setVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }

    public boolean isVegan() { return isVegan; }
    public void setVegan(boolean vegan) { isVegan = vegan; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<Allergy> getAllergens() { return allergens; }
    public void setAllergens(List<Allergy> allergens) { this.allergens = allergens; }

    private String healthTags;
    public String getHealthTags() { return healthTags; }
    public void setHealthTags(String healthTags) { this.healthTags = healthTags; }

    public double getFiber() { return fiberG; }
    public void setFiber(double fiber) { this.fiberG = fiber; }

    public double getSugar() { return sugarG; }
    public void setSugar(double sugar) { this.sugarG = sugar; }

    public double getSodium() { return sodiumMg; }
    public void setSodium(double sodium) { this.sodiumMg = sodium; }

    public String getHealthRuleStatus() { return healthRuleStatus; }
    public void setHealthRuleStatus(String healthRuleStatus) { this.healthRuleStatus = healthRuleStatus; }

    public String getHealthRuleReason() { return healthRuleReason; }
    public void setHealthRuleReason(String healthRuleReason) { this.healthRuleReason = healthRuleReason; }
}
