-- ====================================================================
-- PERSONALIZED NUTRITION MANAGEMENT SYSTEM
-- Complete Database Schema Script (MySQL 8.0+)
-- ====================================================================

CREATE DATABASE IF NOT EXISTS `personalized_nutrition_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `personalized_nutrition_db`;

-- 1. USERS TABLE
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `password_salt` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `full_name` VARCHAR(100) NOT NULL,
    `date_of_birth` DATE NULL,
    `age` INT NOT NULL DEFAULT 22,
    `gender` VARCHAR(20) NOT NULL DEFAULT 'Male',
    `height_cm` DOUBLE NOT NULL DEFAULT 170.0,
    `weight_kg` DOUBLE NOT NULL DEFAULT 70.0,
    `blood_group` VARCHAR(10) DEFAULT 'O+',
    `occupation` VARCHAR(50) DEFAULT 'Software Engineer',
    `income_range` VARCHAR(50) DEFAULT 'Medium',
    `activity_level` VARCHAR(50) NOT NULL DEFAULT 'MODERATE',
    `exercise_level` VARCHAR(50) NOT NULL DEFAULT 'Beginner',
    `food_preference` VARCHAR(50) NOT NULL DEFAULT 'Non-Vegetarian',
    `goal` VARCHAR(50) NOT NULL DEFAULT 'WEIGHT_LOSS',
    `sleep_hours` DOUBLE DEFAULT 7.5,
    `daily_water_target_ml` INT DEFAULT 100000,
    `daily_calorie_target` INT DEFAULT 2100,
    `daily_protein_target` DOUBLE DEFAULT 120.0,
    `daily_carbs_target` DOUBLE DEFAULT 240.0,
    `daily_fat_target` DOUBLE DEFAULT 65.0,
    `account_status` VARCHAR(20) DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. MEDICAL CONDITIONS MASTER
CREATE TABLE IF NOT EXISTS `medical_conditions` (
    `condition_id` INT AUTO_INCREMENT PRIMARY KEY,
    `condition_name` VARCHAR(100) NOT NULL UNIQUE,
    `description` TEXT,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. USER MEDICAL CONDITIONS (Normalized Many-to-Many)
CREATE TABLE IF NOT EXISTS `user_medical_conditions` (
    `user_condition_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `condition_id` INT NOT NULL,
    `additional_notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`condition_id`) REFERENCES `medical_conditions`(`condition_id`) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_condition` (`user_id`, `condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. ALLERGIES MASTER
CREATE TABLE IF NOT EXISTS `allergies` (
    `allergy_id` INT AUTO_INCREMENT PRIMARY KEY,
    `allergy_name` VARCHAR(100) NOT NULL UNIQUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. USER ALLERGIES
CREATE TABLE IF NOT EXISTS `user_allergies` (
    `user_id` INT NOT NULL,
    `allergy_id` INT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`, `allergy_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`allergy_id`) REFERENCES `allergies`(`allergy_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. FOODS TABLE
CREATE TABLE IF NOT EXISTS `foods` (
    `food_id` INT AUTO_INCREMENT PRIMARY KEY,
    `food_name` VARCHAR(100) NOT NULL,
    `food_category` VARCHAR(50) NOT NULL,
    `category` VARCHAR(50) NULL,
    `meal_type` VARCHAR(50) NOT NULL,
    `serving_size` DOUBLE NOT NULL DEFAULT 100.0,
    `serving_unit` VARCHAR(20) DEFAULT 'g',
    `calories` INT NOT NULL,
    `protein_g` DOUBLE NOT NULL DEFAULT 0.0,
    `protein` DOUBLE NOT NULL DEFAULT 0.0,
    `carbs_g` DOUBLE NOT NULL DEFAULT 0.0,
    `carbs` DOUBLE NOT NULL DEFAULT 0.0,
    `fat_g` DOUBLE NOT NULL DEFAULT 0.0,
    `fat` DOUBLE NOT NULL DEFAULT 0.0,
    `fiber_g` DOUBLE DEFAULT 0.0,
    `fiber` DOUBLE DEFAULT 0.0,
    `sugar_g` DOUBLE DEFAULT 0.0,
    `sugar` DOUBLE DEFAULT 0.0,
    `sodium_mg` DOUBLE DEFAULT 0.0,
    `sodium` DOUBLE DEFAULT 0.0,
    `health_tags` VARCHAR(255) DEFAULT '',
    `is_vegetarian` BOOLEAN DEFAULT TRUE,
    `is_vegan` BOOLEAN DEFAULT FALSE,
    `description` TEXT,
    `image_path` VARCHAR(255) DEFAULT 'assets/images/food_default.png',
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_meal_type` (`meal_type`),
    INDEX `idx_food_category` (`food_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. FOOD ALLERGENS
CREATE TABLE IF NOT EXISTS `food_allergens` (
    `food_id` INT NOT NULL,
    `allergy_id` INT NOT NULL,
    PRIMARY KEY (`food_id`, `allergy_id`),
    FOREIGN KEY (`food_id`) REFERENCES `foods`(`food_id`) ON DELETE CASCADE,
    FOREIGN KEY (`allergy_id`) REFERENCES `allergies`(`allergy_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. FOOD HEALTH RULES (Database-driven safety system)
CREATE TABLE IF NOT EXISTS `food_health_rules` (
    `rule_id` INT AUTO_INCREMENT PRIMARY KEY,
    `food_id` INT NOT NULL,
    `condition_id` INT NULL,
    `condition_name` VARCHAR(100) NOT NULL,
    `rule_type` VARCHAR(20) NOT NULL DEFAULT 'ALLOWED', -- 'ALLOWED', 'RESTRICTED', 'AVOID'
    `reason` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`food_id`) REFERENCES `foods`(`food_id`) ON DELETE CASCADE,
    INDEX `idx_food_rule_cond` (`food_id`, `condition_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. EXERCISES TABLE
CREATE TABLE IF NOT EXISTS `exercises` (
    `exercise_id` INT AUTO_INCREMENT PRIMARY KEY,
    `exercise_name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `difficulty` VARCHAR(20) NOT NULL DEFAULT 'Beginner',
    `duration` INT DEFAULT 60,
    `duration_seconds` INT DEFAULT 60,
    `sets` INT DEFAULT 3,
    `default_sets` INT DEFAULT 3,
    `repetitions` INT DEFAULT 10,
    `default_repetitions` INT DEFAULT 10,
    `rest_seconds` INT DEFAULT 30,
    `calories_burn` INT DEFAULT 30,
    `estimated_calories` INT DEFAULT 30,
    `target_muscle` VARCHAR(100) DEFAULT 'Full Body',
    `target_body_area` VARCHAR(100) DEFAULT 'Full Body',
    `instructions` TEXT,
    `health_tags` VARCHAR(255) DEFAULT '',
    `image_path` VARCHAR(255) DEFAULT 'assets/images/exercise_default.png',
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_exercise_cat` (`category`),
    INDEX `idx_exercise_diff` (`difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. EXERCISE HEALTH RULES (Database-driven safety system)
CREATE TABLE IF NOT EXISTS `exercise_health_rules` (
    `rule_id` INT AUTO_INCREMENT PRIMARY KEY,
    `exercise_id` INT NOT NULL,
    `condition_id` INT NULL,
    `condition_name` VARCHAR(100) NOT NULL,
    `rule_type` VARCHAR(20) NOT NULL DEFAULT 'ALLOWED', -- 'ALLOWED', 'RESTRICTED', 'AVOID'
    `reason` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercises`(`exercise_id`) ON DELETE CASCADE,
    INDEX `idx_ex_rule_cond` (`exercise_id`, `condition_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. EXERCISE COMPLETIONS (Historical Workout Log & Score Tracker)
CREATE TABLE IF NOT EXISTS `exercise_completions` (
    `completion_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `exercise_id` INT NOT NULL,
    `exercise_name` VARCHAR(100) NOT NULL,
    `completion_date` DATE NOT NULL,
    `start_time` TIME NULL,
    `end_time` TIME NULL,
    `duration_seconds` INT DEFAULT 0,
    `sets_completed` INT DEFAULT 0,
    `repetitions_completed` INT DEFAULT 0,
    `calories_burned` INT DEFAULT 0,
    `completion_percentage` INT DEFAULT 100,
    `daily_score` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercises`(`exercise_id`) ON DELETE CASCADE,
    INDEX `idx_user_date_comp` (`user_id`, `completion_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. EXERCISE SESSIONS & ITEMS (Routines & Multi-Exercise Circuits)
CREATE TABLE IF NOT EXISTS `exercise_sessions` (
    `session_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `session_date` DATE NOT NULL,
    `started_at` TIMESTAMP NULL,
    `completed_at` TIMESTAMP NULL,
    `planned_duration` INT DEFAULT 1800,
    `actual_duration` INT DEFAULT 0,
    `estimated_calories` INT DEFAULT 200,
    `actual_calories` INT DEFAULT 0,
    `completion_percentage` INT DEFAULT 0,
    `daily_score` INT DEFAULT 0,
    `status` VARCHAR(20) DEFAULT 'PLANNED',
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    INDEX `idx_user_session_date` (`user_id`, `session_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `exercise_session_items` (
    `session_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `session_id` INT NOT NULL,
    `exercise_id` INT NOT NULL,
    `planned_sets` INT DEFAULT 3,
    `completed_sets` INT DEFAULT 0,
    `planned_repetitions` INT DEFAULT 10,
    `completed_repetitions` INT DEFAULT 0,
    `planned_seconds` INT DEFAULT 60,
    `completed_seconds` INT DEFAULT 0,
    `status` VARCHAR(20) DEFAULT 'PENDING',
    `order_number` INT DEFAULT 1,
    FOREIGN KEY (`session_id`) REFERENCES `exercise_sessions`(`session_id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercises`(`exercise_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 13. WORKOUT PLANS
CREATE TABLE IF NOT EXISTS `workout_plans` (
    `plan_id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `target_goal` VARCHAR(50) DEFAULT 'FITNESS',
    `duration_weeks` INT DEFAULT 4,
    `estimated_calories_burn` INT DEFAULT 350,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 14. MEAL PLANS & ITEMS
CREATE TABLE IF NOT EXISTS `meal_plans` (
    `meal_plan_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `plan_date` DATE NOT NULL,
    `calorie_target` INT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_plan_date` (`user_id`, `plan_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `meal_plan_items` (
    `item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `meal_plan_id` INT NOT NULL,
    `food_id` INT NOT NULL,
    `meal_type` VARCHAR(50) NOT NULL,
    `scheduled_time` VARCHAR(20) NOT NULL DEFAULT '08:00 AM',
    `serving_quantity` DOUBLE DEFAULT 1.0,
    `status` VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    `completed_at` TIMESTAMP NULL,
    FOREIGN KEY (`meal_plan_id`) REFERENCES `meal_plans`(`meal_plan_id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_id`) REFERENCES `foods`(`food_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 15. MEAL LOGS (User consumed foods)
CREATE TABLE IF NOT EXISTS `meal_logs` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `food_id` INT NOT NULL,
    `food_name` VARCHAR(100) NULL,
    `meal_type` VARCHAR(50) NOT NULL,
    `quantity_grams` DOUBLE DEFAULT 100.0,
    `calories` INT NOT NULL,
    `protein` DOUBLE NOT NULL DEFAULT 0.0,
    `carbs` DOUBLE NOT NULL DEFAULT 0.0,
    `fat` DOUBLE NOT NULL DEFAULT 0.0,
    `logged_date` DATE NOT NULL,
    `logged_time` TIME NOT NULL,
    `source` VARCHAR(20) DEFAULT 'MANUAL',
    `status` VARCHAR(20) DEFAULT 'COMPLETED',
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_id`) REFERENCES `foods`(`food_id`) ON DELETE CASCADE,
    INDEX `idx_user_logged_date` (`user_id`, `logged_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 16. WATER LOGS
CREATE TABLE IF NOT EXISTS `water_logs` (
    `water_log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `amount_ml` INT NOT NULL,
    `logged_date` DATE NOT NULL,
    `logged_time` TIME NOT NULL,
    `notes` VARCHAR(255) DEFAULT '',
    `logged_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    INDEX `idx_water_user_date` (`user_id`, `logged_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 17. WEIGHT HISTORY
CREATE TABLE IF NOT EXISTS `weight_history` (
    `weight_history_id` INT AUTO_INCREMENT PRIMARY KEY,
    `weight_id` INT NULL,
    `user_id` INT NOT NULL,
    `weight_kg` DOUBLE NOT NULL,
    `weight` DOUBLE NULL,
    `record_date` DATE NOT NULL,
    `recorded_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    INDEX `idx_weight_user_date` (`user_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 18. DAILY / USER GOALS
CREATE TABLE IF NOT EXISTS `daily_goals` (
    `goal_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `target_calories` INT DEFAULT 2000,
    `target_protein` DOUBLE DEFAULT 120.0,
    `target_carbs` DOUBLE DEFAULT 250.0,
    `target_fat` DOUBLE DEFAULT 65.0,
    `target_water_ml` INT DEFAULT 2500,
    `target_exercise_calories` INT DEFAULT 400,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_goal` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 19. AUDIT LOGS
CREATE TABLE IF NOT EXISTS `audit_logs` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `action` VARCHAR(100) NOT NULL,
    `entity_type` VARCHAR(50) DEFAULT NULL,
    `entity_id` INT DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
