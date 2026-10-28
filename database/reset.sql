-- ====================================================================
-- PERSONALIZED NUTRITION MANAGEMENT SYSTEM
-- Reset Database Script (Development Utility)
-- ====================================================================

DROP DATABASE IF EXISTS `personalized_nutrition_db`;

CREATE DATABASE `personalized_nutrition_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `personalized_nutrition_db`;

SELECT 'Database personalized_nutrition_db cleanly reset.' AS `Status`;
