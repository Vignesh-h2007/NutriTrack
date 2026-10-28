package com.nutrition.service;

import com.nutrition.dao.UserDAO;
import com.nutrition.dao.UserDAOImpl;
import com.nutrition.dao.AuditDAO;
import com.nutrition.model.User;
import com.nutrition.util.PasswordUtil;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();
    private final AuditDAO auditDAO = new AuditDAO();

    /**
     * Register a new user after validating inputs and hashing password.
     */
    public User register(String name, String email, String plainPassword,
                         String gender, int age, double heightCm, double weightKg,
                         String activityLevel, String healthGoal, java.util.List<Integer> medicalConditionIds) throws SQLException {

        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name is required.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Valid email is required.");
        if (plainPassword == null || plainPassword.length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters.");
        if (userDAO.findByEmail(email.trim().toLowerCase()) != null) throw new IllegalArgumentException("Email already registered.");

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(plainPassword, salt);

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        user.setGender(gender);
        user.setAge(age);
        user.setHeightCm(heightCm);
        user.setWeightKg(weightKg);
        user.setActivityLevel(activityLevel);
        user.setHealthGoal(healthGoal);

        int userId = userDAO.createUser(user);
        user.setUserId(userId);

        if (medicalConditionIds != null && !medicalConditionIds.isEmpty()) {
            userDAO.saveUserMedicalConditions(userId, medicalConditionIds);
        }

        if (weightKg > 0) {
            try {
                com.nutrition.dao.WeightDAO weightDAO = new com.nutrition.dao.WeightDAO();
                weightDAO.addWeightRecord(userId, weightKg, "Starting weight");
            } catch (Exception ignored) {}
        }

        auditDAO.log(userId, "USER_REGISTERED", "users", userId);
        return user;
    }

    public User register(String name, String email, String plainPassword,
                         String gender, int age, double heightCm, double weightKg,
                         String activityLevel, String healthGoal) throws SQLException {
        return register(name, email, plainPassword, gender, age, heightCm, weightKg, activityLevel, healthGoal, null);
    }

    /**
     * Authenticate user and return User object if valid.
     */
    public User login(String email, String plainPassword) throws SQLException {
        if (email == null || plainPassword == null) throw new IllegalArgumentException("Email and password required.");
        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) throw new IllegalArgumentException("Invalid email or password.");

        String hashed = PasswordUtil.hashPassword(plainPassword, user.getSalt());
        if (!hashed.equals(user.getPassword())) throw new IllegalArgumentException("Invalid email or password.");
        if (!user.isActive()) throw new IllegalArgumentException("Account is deactivated.");

        auditDAO.log(user.getUserId(), "USER_LOGIN", "users", user.getUserId());
        return user;
    }

    /**
     * Update user profile details including medical conditions and daily water target.
     */
    public boolean updateProfile(int userId, String name, int age, double heightCm,
                                 double weightKg, String activityLevel, String healthGoal,
                                 int dailyWaterTargetMl,
                                 java.util.List<Integer> medicalConditionIds) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found.");

        user.setName(name.trim());
        user.setAge(age);
        user.setHeightCm(heightCm);
        user.setWeightKg(weightKg);
        user.setActivityLevel(activityLevel);
        user.setHealthGoal(healthGoal);
        if (dailyWaterTargetMl > 0) {
            user.setDailyWaterTargetMl(dailyWaterTargetMl);
        }

        boolean ok = userDAO.updateUser(user);
        if (ok) {
            if (medicalConditionIds != null) {
                userDAO.saveUserMedicalConditions(userId, medicalConditionIds);
            }
            auditDAO.log(userId, "PROFILE_UPDATED", "users", userId);
        }
        return ok;
    }

    public boolean updateProfile(int userId, String name, int age, double heightCm,
                                 double weightKg, String activityLevel, String healthGoal,
                                 java.util.List<Integer> medicalConditionIds) throws SQLException {
        return updateProfile(userId, name, age, heightCm, weightKg, activityLevel, healthGoal, 0, medicalConditionIds);
    }

    public boolean updateProfile(int userId, String name, int age, double heightCm,
                                 double weightKg, String activityLevel, String healthGoal) throws SQLException {
        return updateProfile(userId, name, age, heightCm, weightKg, activityLevel, healthGoal, 0, null);
    }

    /**
     * Change password after verifying the old one.
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found.");

        String oldHashed = PasswordUtil.hashPassword(oldPassword, user.getSalt());
        if (!oldHashed.equals(user.getPassword())) throw new IllegalArgumentException("Current password is incorrect.");
        if (newPassword == null || newPassword.length() < 6) throw new IllegalArgumentException("New password must be at least 6 characters.");

        String newSalt = PasswordUtil.generateSalt();
        String newHashed = PasswordUtil.hashPassword(newPassword, newSalt);
        boolean ok = userDAO.updatePassword(userId, newHashed, newSalt);
        if (ok) auditDAO.log(userId, "PASSWORD_CHANGED", "users", userId);
        return ok;
    }

    public User getUserById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }
}
