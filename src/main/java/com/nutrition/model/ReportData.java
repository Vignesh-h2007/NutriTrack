package com.nutrition.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String filterRange;
    private String startDate;
    private String endDate;

    private int totalCaloriesConsumed;
    private double totalProteinG;
    private double totalCarbsG;
    private double totalFatG;
    private int totalWaterMl;
    private int totalCaloriesBurned;
    private int totalExerciseMinutes;
    private int completedMealsCount;
    private int completedExercisesCount;
    private double latestWeightKg;

    private List<DailyMetric> dateSeries = new ArrayList<>();
    private List<String> activityLog = new ArrayList<>();

    public ReportData() {}

    public String getFilterRange() { return filterRange; }
    public void setFilterRange(String filterRange) { this.filterRange = filterRange; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getTotalCaloriesConsumed() { return totalCaloriesConsumed; }
    public void setTotalCaloriesConsumed(int totalCaloriesConsumed) { this.totalCaloriesConsumed = totalCaloriesConsumed; }

    public double getTotalProteinG() { return totalProteinG; }
    public void setTotalProteinG(double totalProteinG) { this.totalProteinG = totalProteinG; }

    public double getTotalCarbsG() { return totalCarbsG; }
    public void setTotalCarbsG(double totalCarbsG) { this.totalCarbsG = totalCarbsG; }

    public double getTotalFatG() { return totalFatG; }
    public void setTotalFatG(double totalFatG) { this.totalFatG = totalFatG; }

    public int getTotalWaterMl() { return totalWaterMl; }
    public void setTotalWaterMl(int totalWaterMl) { this.totalWaterMl = totalWaterMl; }

    public int getTotalCaloriesBurned() { return totalCaloriesBurned; }
    public void setTotalCaloriesBurned(int totalCaloriesBurned) { this.totalCaloriesBurned = totalCaloriesBurned; }

    public int getTotalExerciseMinutes() { return totalExerciseMinutes; }
    public void setTotalExerciseMinutes(int totalExerciseMinutes) { this.totalExerciseMinutes = totalExerciseMinutes; }

    public int getCompletedMealsCount() { return completedMealsCount; }
    public void setCompletedMealsCount(int completedMealsCount) { this.completedMealsCount = completedMealsCount; }

    public int getCompletedExercisesCount() { return completedExercisesCount; }
    public void setCompletedExercisesCount(int completedExercisesCount) { this.completedExercisesCount = completedExercisesCount; }

    public double getLatestWeightKg() { return latestWeightKg; }
    public void setLatestWeightKg(double latestWeightKg) { this.latestWeightKg = latestWeightKg; }

    public List<DailyMetric> getDateSeries() { return dateSeries; }
    public void setDateSeries(List<DailyMetric> dateSeries) { this.dateSeries = dateSeries; }

    public List<String> getActivityLog() { return activityLog; }
    public void setActivityLog(List<String> activityLog) { this.activityLog = activityLog; }

    public static class DailyMetric implements Serializable {
        private static final long serialVersionUID = 1L;
        private String date;
        private int caloriesConsumed;
        private double proteinG;
        private double carbsG;
        private double fatG;
        private int waterMl;
        private int exerciseMinutes;
        private int caloriesBurned;
        private int mealsCompleted;
        private double weightKg;

        public DailyMetric() {}
        public DailyMetric(String date) { this.date = date; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public int getCaloriesConsumed() { return caloriesConsumed; }
        public void setCaloriesConsumed(int caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }

        public double getProteinG() { return proteinG; }
        public void setProteinG(double proteinG) { this.proteinG = proteinG; }

        public double getCarbsG() { return carbsG; }
        public void setCarbsG(double carbsG) { this.carbsG = carbsG; }

        public double getFatG() { return fatG; }
        public void setFatG(double fatG) { this.fatG = fatG; }

        public int getWaterMl() { return waterMl; }
        public void setWaterMl(int waterMl) { this.waterMl = waterMl; }

        public int getExerciseMinutes() { return exerciseMinutes; }
        public void setExerciseMinutes(int exerciseMinutes) { this.exerciseMinutes = exerciseMinutes; }

        public int getCaloriesBurned() { return caloriesBurned; }
        public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

        public int getMealsCompleted() { return mealsCompleted; }
        public void setMealsCompleted(int mealsCompleted) { this.mealsCompleted = mealsCompleted; }

        public double getWeightKg() { return weightKg; }
        public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    }
}
