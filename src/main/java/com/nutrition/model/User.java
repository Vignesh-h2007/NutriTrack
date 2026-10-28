package com.nutrition.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String username;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String fullName;
    private Date dateOfBirth;
    private int age;
    private String gender;
    private double heightCm;
    private double weightKg;
    private String bloodGroup;
    private String occupation;
    private String incomeRange;
    private String activityLevel;
    private String exerciseLevel;
    private String foodPreference;
    private String goal;
    private double sleepHours;
    private int dailyWaterTargetMl;
    private int dailyCalorieTarget;
    private double dailyProteinTarget;
    private double dailyCarbsTarget;
    private double dailyFatTarget;
    private String accountStatus = "ACTIVE";
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private List<MedicalCondition> medicalConditions = new ArrayList<>();
    private List<Allergy> allergies = new ArrayList<>();

    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPassword() { return passwordHash; }
    public void setPassword(String password) { this.passwordHash = password; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public String getSalt() { return passwordSalt; }
    public void setSalt(String salt) { this.passwordSalt = salt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getName() { return fullName != null ? fullName : username; }
    public void setName(String name) { this.fullName = name; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getIncomeRange() { return incomeRange; }
    public void setIncomeRange(String incomeRange) { this.incomeRange = incomeRange; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getExerciseLevel() { return exerciseLevel; }
    public void setExerciseLevel(String exerciseLevel) { this.exerciseLevel = exerciseLevel; }

    public String getFoodPreference() { return foodPreference; }
    public void setFoodPreference(String foodPreference) { this.foodPreference = foodPreference; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getHealthGoal() { return goal; }
    public void setHealthGoal(String healthGoal) { this.goal = healthGoal; }

    public double getSleepHours() { return sleepHours; }
    public void setSleepHours(double sleepHours) { this.sleepHours = sleepHours; }

    public int getDailyWaterTargetMl() { return dailyWaterTargetMl; }
    public void setDailyWaterTargetMl(int dailyWaterTargetMl) { this.dailyWaterTargetMl = dailyWaterTargetMl; }

    public int getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(int dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }

    public double getDailyProteinTarget() { return dailyProteinTarget; }
    public void setDailyProteinTarget(double dailyProteinTarget) { this.dailyProteinTarget = dailyProteinTarget; }

    public double getDailyCarbsTarget() { return dailyCarbsTarget; }
    public void setDailyCarbsTarget(double dailyCarbsTarget) { this.dailyCarbsTarget = dailyCarbsTarget; }

    public double getDailyFatTarget() { return dailyFatTarget; }
    public void setDailyFatTarget(double dailyFatTarget) { this.dailyFatTarget = dailyFatTarget; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public boolean isActive() { return accountStatus == null || "ACTIVE".equalsIgnoreCase(accountStatus); }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public List<MedicalCondition> getMedicalConditions() { return medicalConditions; }
    public void setMedicalConditions(List<MedicalCondition> medicalConditions) { this.medicalConditions = medicalConditions; }

    public List<Allergy> getAllergies() { return allergies; }
    public void setAllergies(List<Allergy> allergies) { this.allergies = allergies; }

    public double getBmi() {
        if (heightCm <= 0) return 0;
        double heightM = heightCm / 100.0;
        return Math.round((weightKg / (heightM * heightM)) * 10.0) / 10.0;
    }
}
