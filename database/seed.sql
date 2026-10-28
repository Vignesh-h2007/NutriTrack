-- ====================================================================
-- PERSONALIZED NUTRITION MANAGEMENT SYSTEM
-- Seed Data Script (MySQL 8.0+)
-- Includes Master Health Rules, Real Foods, Workouts, & Multi-Date Logs
-- ====================================================================

USE `personalized_nutrition_db`;

-- 1. SEED MEDICAL CONDITIONS
INSERT INTO `medical_conditions` (`condition_id`, `condition_name`, `description`, `is_active`) VALUES
(1, 'Heart-related condition', 'Cardiovascular conditions requiring low-sodium and low saturated fat diet with controlled aerobic activity', TRUE),
(2, 'High blood pressure', 'Hypertension requiring strict sodium control (<1500mg/day) and regular mild-to-moderate aerobic exercise', TRUE),
(3, 'Diabetes', 'Type 1/Type 2 diabetes requiring low glycemic index foods, complex carbohydrates, and sugar restriction', TRUE),
(4, 'Asthma', 'Respiratory condition requiring allergen avoidance and controlled-intensity workouts with proper warmup', TRUE),
(5, 'Kidney-related condition', 'Renal conditions requiring sodium, potassium, and phosphorus management with regulated protein intake', TRUE),
(6, 'Thyroid condition', 'Metabolic rate adjustments affecting energy expenditure and macronutrient requirements', TRUE),
(7, 'Joint problems', 'Arthritis or knee joint issues requiring non-weight-bearing, low-impact exercises', TRUE),
(8, 'Back problems', 'Lumbar or spinal sensitivity requiring safe core workouts and avoidance of high load spinal flexion', TRUE),
(9, 'Obesity', 'High Body Mass Index requiring gradual calorie deficit and structured joint-safe workout progression', TRUE),
(10, 'Other', 'Other physician-monitored health condition requiring personalized dietary adjustments', TRUE)
ON DUPLICATE KEY UPDATE `description` = VALUES(`description`);

-- 2. SEED ALLERGIES
INSERT INTO `allergies` (`allergy_id`, `allergy_name`) VALUES
(1, 'Peanuts'),
(2, 'Tree Nuts'),
(3, 'Lactose / Dairy'),
(4, 'Gluten'),
(5, 'Soy'),
(6, 'Eggs'),
(7, 'Fish / Seafood'),
(8, 'Shellfish')
ON DUPLICATE KEY UPDATE `allergy_name` = VALUES(`allergy_name`);

-- 3. SEED FOODS CATALOG
INSERT INTO `foods` (`food_id`, `food_name`, `food_category`, `category`, `meal_type`, `serving_size`, `serving_unit`, `calories`, `protein_g`, `protein`, `carbs_g`, `carbs`, `fat_g`, `fat`, `fiber_g`, `fiber`, `sugar_g`, `sugar`, `sodium_mg`, `sodium`, `health_tags`, `is_vegetarian`, `is_vegan`, `description`, `image_path`) VALUES
(1, 'Oatmeal with Berries & Chia Seeds', 'Grains', 'Grains', 'Breakfast', 250, 'g', 280, 8.5, 8.5, 52.0, 52.0, 4.5, 4.5, 7.0, 7.0, 6.0, 6.0, 15, 15, 'Heart-Healthy, Diabetic-Friendly, High-Fiber, Low-Sodium', TRUE, TRUE, 'Whole rolled oats simmered in water topped with antioxidant fresh berries, chia seeds, and cinnamon.', 'assets/images/oatmeal.jpg'),
(2, 'Vegetable Egg White Scramble', 'Protein', 'Protein', 'Breakfast', 200, 'g', 170, 18.0, 18.0, 4.0, 4.0, 8.0, 8.0, 2.0, 2.0, 2.0, 2.0, 180, 180, 'High-Protein, Low-Carb, Muscle-Gain', FALSE, FALSE, 'Fluffy egg whites scrambled with baby spinach, diced tomatoes, bell peppers, and olive oil.', 'assets/images/omelette.jpg'),
(3, 'Greek Yogurt Berry Parfait', 'Dairy', 'Dairy', 'Breakfast', 200, 'g', 210, 16.0, 16.0, 24.0, 24.0, 3.5, 3.5, 3.0, 3.0, 12.0, 12.0, 65, 65, 'Probiotic, High-Protein, Bone-Health', TRUE, FALSE, 'Creamy plain Greek yogurt layered with blueberries, flaxseeds, and a dash of raw honey.', 'assets/images/yogurt_parfait.jpg'),
(4, 'Avocado Whole Wheat Sourdough', 'Grains', 'Grains', 'Breakfast', 160, 'g', 245, 6.5, 6.5, 27.0, 27.0, 12.5, 12.5, 7.0, 7.0, 1.5, 1.5, 160, 160, 'Heart-Healthy, Healthy-Fats, High-Fiber', TRUE, TRUE, 'Crusty whole grain sourdough toasted and spread with ripe avocado, hemp seeds, and extra virgin olive oil.', 'assets/images/avocado_toast.jpg'),
(5, 'Steamed Idli with Vegetable Sambar', 'Grains', 'Grains', 'Breakfast', 250, 'g', 210, 7.0, 7.0, 42.0, 42.0, 2.0, 2.0, 5.0, 5.0, 2.0, 2.0, 140, 140, 'Fermented, Easy-Digest, Low-Fat, Heart-Healthy', TRUE, TRUE, 'Steamed fermented rice and urad dal cakes paired with vegetable and lentil sambar stew.', 'assets/images/idli.jpg'),
(6, 'Moong Dal Chilla (Savory Pancake)', 'Protein', 'Protein', 'Breakfast', 180, 'g', 220, 12.0, 12.0, 32.0, 32.0, 5.0, 5.0, 6.0, 6.0, 2.0, 2.0, 120, 120, 'Diabetic-Friendly, Gluten-Free, High-Protein', TRUE, TRUE, 'Nutritious yellow moong dal batter pan-cooked with grated carrots, ginger, and coriander.', 'assets/images/chilla.jpg'),
(7, 'Fresh Apple with Natural Peanut Butter', 'Fruits', 'Fruits', 'Morning Snack', 160, 'g', 185, 5.0, 5.0, 22.0, 22.0, 9.0, 9.0, 4.5, 4.5, 14.0, 14.0, 75, 75, 'Energy-Booster, Fiber-Rich', TRUE, TRUE, 'Crisp crisp apple slices served with pure roasted peanut butter.', 'assets/images/apple_pb.jpg'),
(8, 'Raw Mixed Nuts & Seeds', 'Snacks', 'Snacks', 'Morning Snack', 40, 'g', 225, 6.5, 6.5, 14.0, 14.0, 17.0, 17.0, 3.5, 3.5, 4.0, 4.0, 5, 5, 'Heart-Healthy, Brain-Health, Mineral-Dense', TRUE, TRUE, 'Raw almonds, walnuts, pumpkin seeds, and dried cranberries rich in omega-3 fats.', 'assets/images/nuts.jpg'),
(9, 'Green Alkalizing Smoothie', 'Beverages', 'Beverages', 'Morning Snack', 300, 'ml', 140, 4.0, 4.0, 28.0, 28.0, 1.5, 1.5, 5.0, 5.0, 14.0, 14.0, 35, 35, 'Detox, Vitamin-Rich, Hydrating', TRUE, TRUE, 'Nutrient-packed smoothie blending baby spinach, cucumber, green apple, ginger, and coconut water.', 'assets/images/green_smoothie.jpg'),
(10, 'Grilled Lemon Herb Chicken Bowl', 'Protein', 'Protein', 'Lunch', 350, 'g', 420, 38.0, 38.0, 38.0, 38.0, 10.0, 10.0, 6.0, 6.0, 2.5, 2.5, 260, 260, 'High-Protein, Lean-Muscle, Balanced-Meal', FALSE, FALSE, 'Skinless lean chicken breast grilled with rosemary served over steamed quinoa and broccoli.', 'assets/images/chicken_quinoa.jpg'),
(11, 'Brown Basmati Rice & Yellow Dal', 'Grains', 'Grains', 'Lunch', 350, 'g', 360, 15.0, 15.0, 64.0, 64.0, 5.0, 5.0, 9.0, 9.0, 3.0, 3.0, 220, 220, 'Diabetic-Friendly, Heart-Healthy, Comfort-Food', TRUE, TRUE, 'Aromatic steamed brown rice served with cumin-tempered yellow lentil tadka dal.', 'assets/images/dal_rice.jpg'),
(12, 'Grilled Paneer Tikka Salad', 'Protein', 'Protein', 'Lunch', 300, 'g', 330, 19.0, 19.0, 14.0, 14.0, 22.0, 22.0, 4.0, 4.0, 4.0, 4.0, 280, 280, 'Vegetarian-Protein, Calcium-Dense', TRUE, FALSE, 'Spiced grilled cottage cheese cubes tossed with cucumber, cherry tomatoes, and bell peppers.', 'assets/images/paneer_salad.jpg'),
(13, 'Mediterranean Chickpea & Olive Salad', 'Protein', 'Protein', 'Lunch', 320, 'g', 310, 14.0, 14.0, 44.0, 44.0, 9.0, 9.0, 11.0, 11.0, 5.0, 5.0, 240, 240, 'Heart-Healthy, High-Fiber, Vegan-Protein', TRUE, TRUE, 'Tender boiled chickpeas tossed with diced Persian cucumber, kalamata olives, parsley, and lemon.', 'assets/images/chickpea_salad.jpg'),
(14, 'Roasted Makhana (Fox Nuts)', 'Snacks', 'Snacks', 'Evening Snack', 35, 'g', 130, 3.5, 3.5, 22.0, 22.0, 3.5, 3.5, 3.0, 3.0, 0.5, 0.5, 80, 80, 'Low-Calorie, Low-Sodium, Kidney-Safe, Weight-Loss', TRUE, TRUE, 'Crunchy popped lotus seeds lightly roasted in cold-pressed olive oil with turmeric and black pepper.', 'assets/images/makhana.jpg'),
(15, 'Hummus Dip with Crisp Baby Carrots', 'Snacks', 'Snacks', 'Evening Snack', 150, 'g', 160, 6.0, 6.0, 20.0, 20.0, 7.5, 7.5, 6.0, 6.0, 5.0, 5.0, 190, 190, 'Fiber-Dense, Prebiotic, Heart-Healthy', TRUE, TRUE, 'Smooth homemade tahini chickpea hummus served alongside crunchy organic carrot batons.', 'assets/images/hummus.jpg'),
(16, 'Boiled Sprouted Moong Chaat', 'Snacks', 'Snacks', 'Evening Snack', 150, 'g', 140, 9.5, 9.5, 23.0, 23.0, 1.5, 1.5, 6.5, 6.5, 3.0, 3.0, 110, 110, 'Live-Enzymes, High-Protein, Low-Fat', TRUE, TRUE, 'Steam-softened sprouted green mung beans mixed with pomegranate arils and chaat herbs.', 'assets/images/sprouts.jpg'),
(17, 'Baked Wild Salmon with Asparagus', 'Protein', 'Protein', 'Dinner', 320, 'g', 390, 35.0, 35.0, 7.0, 7.0, 24.0, 24.0, 3.5, 3.5, 2.0, 2.0, 180, 180, 'Omega-3, Heart-Healthy, Joint-Friendly', FALSE, FALSE, 'Atlantic wild salmon fillet baked with lemon slices, fresh dill, and roasted tender asparagus spears.', 'assets/images/salmon.jpg'),
(18, 'Organic Tofu & Bok Choy Stir-Fry', 'Protein', 'Protein', 'Dinner', 320, 'g', 270, 21.0, 21.0, 16.0, 16.0, 13.0, 13.0, 7.0, 7.0, 3.5, 3.5, 210, 210, 'Plant-Protein, Bone-Health, Diabetic-Friendly', TRUE, TRUE, 'Pan-seared firm organic tofu cubes tossed with baby bok choy, shiitake mushrooms, and sesame oil.', 'assets/images/tofu_stirfry.jpg'),
(19, 'Herb-Steamed White Fish & Sweet Potato', 'Protein', 'Protein', 'Dinner', 320, 'g', 340, 31.0, 31.0, 36.0, 36.0, 6.0, 6.0, 5.5, 5.5, 8.0, 8.0, 160, 160, 'Lean-Protein, Clean-Carbs, Easy-Digest', FALSE, FALSE, 'Delicate white sea bass fillet steamed with thyme, paired with roasted sweet potato wedges.', 'assets/images/fish_sweetpotato.jpg'),
(20, 'Garden Vegetable Soup & Multi-Grain Roll', 'Vegetables', 'Vegetables', 'Dinner', 350, 'g', 220, 7.5, 7.5, 38.0, 38.0, 4.0, 4.0, 8.5, 8.5, 6.0, 6.0, 220, 220, 'Low-Calorie, Hydrating, High-Fiber', TRUE, TRUE, 'Slow-simmered vegetable broth packed with zucchini, celery, tomatoes, beans, and fresh herbs.', 'assets/images/veg_soup.jpg')
ON DUPLICATE KEY UPDATE `food_name` = VALUES(`food_name`), `calories` = VALUES(`calories`);

-- 4. SEED FOOD ALLERGENS
INSERT INTO `food_allergens` (`food_id`, `allergy_id`) VALUES
(3, 3), -- Greek yogurt contains dairy
(4, 4), -- Whole wheat sourdough contains gluten
(5, 4), -- Sambar/idli trace gluten
(7, 1), -- Peanut butter contains peanuts
(8, 2), -- Mixed nuts contains tree nuts
(12, 3), -- Paneer contains dairy
(17, 7), -- Salmon contains fish
(18, 5), -- Tofu contains soy
(19, 7)  -- White fish contains fish
ON DUPLICATE KEY UPDATE `allergy_id` = VALUES(`allergy_id`);

-- 5. SEED FOOD HEALTH RULES (Database-driven Safety Rules)
INSERT INTO `food_health_rules` (`food_id`, `condition_name`, `rule_type`, `reason`) VALUES
-- Heart-related condition
(1, 'Heart-related condition', 'ALLOWED', 'Soluble beta-glucan fiber in oats actively reduces LDL blood cholesterol.'),
(4, 'Heart-related condition', 'ALLOWED', 'Monounsaturated fats from avocado improve cardiovascular lipid profiles.'),
(10, 'Heart-related condition', 'ALLOWED', 'Lean chicken breast contains low saturated fats and promotes arterial health.'),
(11, 'Heart-related condition', 'ALLOWED', 'Lentils provide dietary potassium and soluble fiber without cholesterol.'),
(13, 'Heart-related condition', 'ALLOWED', 'Olive oil and legumes support endothelial vascular compliance.'),
(17, 'Heart-related condition', 'ALLOWED', 'High omega-3 fatty acids lower blood triglycerides and stabilize heart rhythm.'),
(8, 'Heart-related condition', 'RESTRICTED', 'Healthy fats, but consume unsalted portions to protect arterial elasticity.'),
(12, 'Heart-related condition', 'RESTRICTED', 'High saturated dairy fat in paneer requires moderate consumption.'),
-- High blood pressure
(1, 'High blood pressure', 'ALLOWED', 'Very low sodium content (15mg) and high potassium support healthy blood pressure.'),
(10, 'High blood pressure', 'ALLOWED', 'Unprocessed lean protein seasoned with herbs instead of table salt.'),
(11, 'High blood pressure', 'ALLOWED', 'Natural potassium in lentils counteracts excess sodium.'),
(14, 'High blood pressure', 'ALLOWED', 'Low sodium snack alternative to processed crisps.'),
(17, 'High blood pressure', 'ALLOWED', 'Eicosapentaenoic acid (EPA) in salmon promotes blood vessel relaxation.'),
(12, 'High blood pressure', 'RESTRICTED', 'Avoid salted seasonings when preparing paneer.'),
-- Diabetes
(1, 'Diabetes', 'ALLOWED', 'Low glycemic index whole oats produce gentle, prolonged glucose response.'),
(2, 'Diabetes', 'ALLOWED', 'Zero glycemic impact from pure egg whites and leafy greens.'),
(6, 'Diabetes', 'ALLOWED', 'Moong dal has low glycemic load and prevents postprandial blood sugar spikes.'),
(11, 'Diabetes', 'ALLOWED', 'Brown basmati and dal combination contains complex carbs with high fiber.'),
(18, 'Diabetes', 'ALLOWED', 'Soy isoflavones and protein in tofu enhance insulin sensitivity.'),
(7, 'Diabetes', 'RESTRICTED', 'Contains natural fructose; monitor total carbohydrate intake per serving.'),
(8, 'Diabetes', 'ALLOWED', 'Dietary fiber and healthy fats slow carbohydrate absorption.'),
-- Kidney-related condition
(14, 'Kidney-related condition', 'ALLOWED', 'Fox nuts are low in phosphorus and potassium, making them ideal for renal health.'),
(1, 'Kidney-related condition', 'RESTRICTED', 'Requires monitoring phosphorus and potassium as prescribed by nephrologist.'),
(8, 'Kidney-related condition', 'AVOID', 'High potassium and phosphorus content in mixed nuts places burden on filtration.'),
(10, 'Kidney-related condition', 'RESTRICTED', 'Protein portion must be precisely regulated to avoid renal hyperfiltration.'),
-- Obesity
(1, 'Obesity', 'ALLOWED', 'High dietary fiber provides prolonged satiety and prevents snacking.'),
(14, 'Obesity', 'ALLOWED', 'Low-calorie density snack (130 kcal) satisfies crunch cravings safely.'),
(18, 'Obesity', 'ALLOWED', 'High protein to calorie ratio accelerates fat loss while preserving lean mass.'),
(20, 'Obesity', 'ALLOWED', 'High volume vegetable broth expands gastric capacity with low calories (220 kcal).'),
(8, 'Obesity', 'RESTRICTED', 'Calorie-dense; restrict to 20g measured portions to maintain deficit.'),
-- Joint problems
(17, 'Joint problems', 'ALLOWED', 'Omega-3 fatty acids dramatically inhibit joint inflammatory pathways.'),
(1, 'Joint problems', 'ALLOWED', 'Anti-inflammatory whole grains reduce systemic C-reactive protein levels.');

-- 6. SEED EXERCISES MASTER
INSERT INTO `exercises` (`exercise_id`, `exercise_name`, `category`, `difficulty`, `duration`, `duration_seconds`, `sets`, `default_sets`, `repetitions`, `default_repetitions`, `rest_seconds`, `calories_burn`, `estimated_calories`, `target_muscle`, `target_body_area`, `instructions`, `health_tags`) VALUES
(1, 'Brisk Walking', 'Cardio', 'Beginner', 1200, 1200, 1, 1, 1, 1, 0, 110, 110, 'Cardiovascular & Legs', 'Cardiovascular & Legs', 'Walk at an energetic, steady pace keeping spine tall, shoulders relaxed, and swinging arms rhythmically.', 'Low-Impact, Heart-Safe, Joint-Friendly, Blood-Pressure-Safe'),
(2, 'Stationary Bicycle Ride', 'Cardio', 'Beginner', 900, 900, 1, 1, 1, 1, 0, 125, 125, 'Quads & Cardiovascular', 'Lower Body & Heart', 'Pedal smoothly with moderate resistance keeping chest open and breathing deeply in through nose.', 'Zero-Impact, Knee-Friendly, Heart-Safe, Endurance'),
(3, 'Bodyweight Squats', 'Strength', 'Beginner', 45, 45, 3, 3, 12, 12, 30, 35, 35, 'Quads & Glutes', 'Legs & Glutes', 'Feet shoulder-width apart. Push hips backward and descend as if sitting on a sturdy chair, then push up.', 'Functional, Leg-Strength, Calorie-Burn'),
(4, 'Standard Push-Ups', 'Strength', 'Intermediate', 45, 45, 3, 3, 10, 10, 30, 40, 40, 'Chest & Triceps', 'Upper Body', 'Hands slightly wider than shoulders. Lower chest until 2 inches above floor, maintaining rigid core line.', 'Upper-Body, Core-Engagement, Muscle-Gain'),
(5, 'Knee Push-Ups (Modified)', 'Strength', 'Beginner', 40, 40, 3, 3, 8, 8, 30, 25, 25, 'Chest & Shoulders', 'Upper Body', 'Resting on knees, lower upper torso with flat spine. Ideal for beginners or joint relief.', 'Low-Load, Spine-Safe, Joint-Friendly'),
(6, 'Isometric Plank Hold', 'Strength', 'Intermediate', 30, 30, 3, 3, 1, 1, 30, 25, 25, 'Core Abdominals', 'Core', 'Hold forearm plank position with hips level and abdominal wall braced tight.', 'Spine-Stabilizing, Core-Strength, Posture'),
(7, 'Cat-Cow Spinal Mobility', 'Mobility', 'Beginner', 60, 60, 2, 2, 8, 8, 15, 15, 15, 'Spine & Lumbar', 'Spine & Core', 'On hands and knees, gently arch spine upward toward ceiling (Cat) then curve downward opening chest (Cow).', 'Back-Friendly, Spine-Decompression, Gentle'),
(8, 'Supine Glute Bridges', 'Strength', 'Beginner', 45, 45, 3, 3, 15, 15, 20, 30, 30, 'Glutes & Hamstrings', 'Lower Body', 'Lie on back with knees bent. Squeeze glutes and press hips upward until knees, hips, and shoulders align.', 'Zero-Knee-Impact, Lumbar-Support, Posture'),
(9, 'Shoulder & Neck Mobility', 'Mobility', 'Beginner', 60, 60, 2, 2, 10, 10, 15, 10, 10, 'Neck & Deltoids', 'Upper Body', 'Slow, smooth circular shoulder rolls backwards and controlled gentle neck rotations.', 'Stress-Relief, Cervical-Safe, Gentle'),
(10, 'Sun Salutation (Surya Namaskar)', 'Yoga', 'Intermediate', 300, 300, 3, 3, 4, 4, 30, 50, 50, 'Full Body', 'Full Body', 'Flow smoothly through the 12 dynamic classical yoga postures synchronized with rhythmic diaphragmatic breath.', 'Flexibility, Mind-Body, Total-Body'),
(11, 'Jumping Jacks Warm-Up', 'Cardio', 'Beginner', 60, 60, 2, 2, 25, 25, 20, 30, 30, 'Full Body', 'Full Body', 'Jump feet apart while swinging arms overhead, then jump back to start position in rhythm.', 'High-Impact, Cardio-Booster, Warmup'),
(12, 'High Knees Cardio Sprint', 'Cardio', 'Intermediate', 45, 45, 3, 3, 20, 20, 30, 45, 45, 'Hip Flexors & Core', 'Cardiovascular', 'Rapidly alternate driving knees high toward chest while pumping arms vigorously.', 'High-Intensity, Calorie-Torch, High-Impact'),
(13, 'Full Body Burpees', 'Strength', 'Advanced', 60, 60, 3, 3, 10, 10, 45, 60, 60, 'Full Body', 'Full Body', 'Drop into squat, kick feet back to plank, complete push-up, jump feet forward, and explode upward.', 'Extreme-Cardio, High-Load, High-Intensity'),
(14, 'Seated Quad Leg Extensions', 'Strength', 'Beginner', 45, 45, 3, 3, 12, 12, 20, 20, 20, 'Quadriceps', 'Legs', 'Seated on bench or chair, smoothly extend lower leg straight out, pause to contract quad, then lower.', 'Joint-Friendly, Knee-Rehab, Low-Risk'),
(15, 'Full Body Static Cooldown', 'Cooldown', 'Beginner', 180, 180, 1, 1, 1, 1, 0, 15, 15, 'Full Body', 'Full Body', 'Slow relaxing stretches holding hamstrings, quads, chest, and lats to safely reduce heart rate.', 'Heart-Recovery, Flexibility, Calming')
ON DUPLICATE KEY UPDATE `exercise_name` = VALUES(`exercise_name`);

-- 7. SEED EXERCISE HEALTH RULES (Database-driven Safety Rules)
INSERT INTO `exercise_health_rules` (`exercise_id`, `condition_name`, `rule_type`, `reason`) VALUES
-- Heart-related condition
(1, 'Heart-related condition', 'ALLOWED', 'Brisk walking maintains heart rate in safe aerobic zone without hypertensive spikes.'),
(2, 'Heart-related condition', 'ALLOWED', 'Stationary cycling provides controlled aerobic conditioning without sudden cardiovascular shocks.'),
(7, 'Heart-related condition', 'ALLOWED', 'Gentle mobility decompresses thoracic spine with calming parasympathetic tone.'),
(15, 'Heart-related condition', 'ALLOWED', 'Crucial cooldown to prevent post-exercise venous pooling and sudden blood pressure drops.'),
(11, 'Heart-related condition', 'AVOID', 'Sudden rapid ballistic jumping spikes systolic heart rate dangerously.'),
(12, 'Heart-related condition', 'AVOID', 'Extreme high-knee cardio exceeds clinical aerobic safety threshold for heart patients.'),
(13, 'Heart-related condition', 'AVOID', 'Extreme intensity burpees combine rapid postural shifts with high cardiac pressure.'),
(3, 'Heart-related condition', 'RESTRICTED', 'Limit resistance load; perform with controlled breathing without Valsalva maneuver.'),
-- High blood pressure
(1, 'High blood pressure', 'ALLOWED', 'Continuous aerobic walking enhances arterial vascular compliance and lowers baseline BP.'),
(2, 'High blood pressure', 'ALLOWED', 'Cycle at steady resistance to avoid sharp blood pressure spikes.'),
(13, 'High blood pressure', 'AVOID', 'Repeated head-down to jumping transitions induce extreme intracranial pressure spikes.'),
(12, 'High blood pressure', 'AVOID', 'High intensity anaerobic sprints are contraindicated in untreated hypertension.'),
-- Joint problems (Arthritis / Knee issues)
(1, 'Joint problems', 'ALLOWED', 'Low-impact walking stimulates synovial fluid lubrication without knee shear.'),
(2, 'Joint problems', 'ALLOWED', 'Zero-impact closed-chain stationary cycling protects knee cartilage.'),
(8, 'Joint problems', 'ALLOWED', 'Glute bridges strengthen posterior chain with zero patellofemoral compressive force.'),
(14, 'Joint problems', 'ALLOWED', 'Non-weight-bearing seated extensions rebuild quadriceps safely around joint.'),
(11, 'Joint problems', 'AVOID', 'Ballistic jumping impacts knee and ankle joints with 3x body weight force.'),
(12, 'Joint problems', 'AVOID', 'High impact running gait aggravates osteoarthritis and joint wear.'),
(13, 'Joint problems', 'AVOID', 'Landing force from jump causes significant cartilage shock.'),
(3, 'Joint problems', 'RESTRICTED', 'Limit squat depth to 45 degrees; do not squat past parallel.'),
-- Back problems (Spinal / Lumbar sensitivity)
(1, 'Back problems', 'ALLOWED', 'Walking decompresses lumbar vertebrae naturally through pelvic movement.'),
(6, 'Back problems', 'ALLOWED', 'Isometric plank trains core stabilization to protect lumbar spine alignment.'),
(7, 'Back problems', 'ALLOWED', 'Cat-cow gently restores spinal mobility and decompresses intervertebral discs.'),
(8, 'Back problems', 'ALLOWED', 'Glute bridge strengthens lower back support without spinal flexion.'),
(13, 'Back problems', 'AVOID', 'Rapid spinal flexion and extension in burpees severely stresses lumbar discs.'),
(4, 'Back problems', 'RESTRICTED', 'Perform modified knee push-ups to prevent lumbar hyperextension (sagging).'),
-- Obesity
(1, 'Obesity', 'ALLOWED', 'Safe energy expenditure with low joint wear.'),
(2, 'Obesity', 'ALLOWED', 'Non-weight-bearing cycling prevents knee overload.'),
(11, 'Obesity', 'RESTRICTED', 'Perform low impact step-jacks rather than full jumping jacks to protect ankles.');

-- 8. SEED WORKOUT PLANS
INSERT INTO `workout_plans` (`plan_id`, `title`, `description`, `target_goal`, `duration_weeks`, `estimated_calories_burn`, `is_active`) VALUES
(1, 'Weight Loss & Heart-Safe Cardio', 'Low-impact brisk walking, stationary cycling, and gentle mobility designed for safe, steady fat burn.', 'WEIGHT_LOSS', 4, 320, TRUE),
(2, 'Hypertrophy Muscle Builder', 'Progressive bodyweight and resistance exercises targeting chest, legs, arms, and core development.', 'MUSCLE_GAIN', 6, 450, TRUE),
(3, 'Joint-Friendly Total Fitness', 'Non-weight-bearing cycling, glute bridges, and seated extensions for pain-free stamina and tone.', 'MAINTENANCE', 4, 280, TRUE),
(4, 'Spine & Core Strength Routine', 'Planks, cat-cow mobility, and posterior chain stabilization to eradicate back tension.', 'IMPROVED_FITNESS', 3, 220, TRUE)
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`);

-- 9. SEED PRE-REGISTERED TEST USERS (PBKDF2 Password: 'password123')
-- Salt: 9DnzLoWfTjN7TN0EBDGGcQ==
-- Hash: tgoFXtBrsC2d6DfX1eIDa9rMRVbJ6p+IwiCq8SfqXVE=
INSERT INTO `users` (`user_id`, `username`, `password_hash`, `password_salt`, `email`, `full_name`, `age`, `gender`, `height_cm`, `weight_kg`, `blood_group`, `occupation`, `income_range`, `activity_level`, `exercise_level`, `food_preference`, `goal`, `daily_water_target_ml`, `daily_calorie_target`, `daily_protein_target`, `daily_carbs_target`, `daily_fat_target`, `account_status`) VALUES
(1, 'john_doe', 'tgoFXtBrsC2d6DfX1eIDa9rMRVbJ6p+IwiCq8SfqXVE=', '9DnzLoWfTjN7TN0EBDGGcQ==', 'john@example.com', 'John Doe', 28, 'Male', 178.0, 81.5, 'O+', 'Accountant', 'Medium', 'LIGHT', 'Beginner', 'Non-Vegetarian', 'WEIGHT_LOSS', 100000, 1950, 115.0, 220.0, 60.0, 'ACTIVE'),
(2, 'jane_smith', 'tgoFXtBrsC2d6DfX1eIDa9rMRVbJ6p+IwiCq8SfqXVE=', '9DnzLoWfTjN7TN0EBDGGcQ==', 'jane@example.com', 'Jane Smith', 26, 'Female', 165.0, 61.5, 'A+', 'Teacher', 'Medium', 'MODERATE', 'Intermediate', 'Vegetarian', 'MAINTENANCE', 100000, 1850, 95.0, 230.0, 55.0, 'ACTIVE'),
(3, 'vignesh_h', 'tgoFXtBrsC2d6DfX1eIDa9rMRVbJ6p+IwiCq8SfqXVE=', '9DnzLoWfTjN7TN0EBDGGcQ==', 'vigneshharikumar2007@gmail.com', 'Vignesh H', 22, 'Male', 170.0, 71.0, 'O+', 'Software Engineer', 'High', 'MODERATE', 'Intermediate', 'Non-Vegetarian', 'MUSCLE_GAIN', 100000, 2450, 140.0, 290.0, 75.0, 'ACTIVE')
ON DUPLICATE KEY UPDATE `full_name` = VALUES(`full_name`), `password_hash` = VALUES(`password_hash`);

-- 10. SEED USER MEDICAL CONDITIONS
-- John has Heart Condition (1) + High Blood Pressure (2)
INSERT INTO `user_medical_conditions` (`user_id`, `condition_id`, `additional_notes`) VALUES
(1, 1, 'Mild cardiovascular sensitivity, requires low sodium and low impact workouts'),
(1, 2, 'Doctor advised sodium under 1500mg and daily brisk walking'),
-- Jane has Diabetes (3)
(2, 3, 'Type 2 diabetes managed with low GI diet and carbohydrate monitoring')
ON DUPLICATE KEY UPDATE `additional_notes` = VALUES(`additional_notes`);

-- 11. SEED HISTORICAL LOGS ACROSS REAL DATES (14 Days: 2026-09-24 to 2026-10-07)
-- User 1 (John Doe): Consistent logging for weight loss
INSERT INTO `weight_history` (`user_id`, `weight_kg`, `weight`, `record_date`) VALUES
(1, 84.5, 84.5, '2026-09-24'),
(1, 84.0, 84.0, '2026-09-26'),
(1, 83.4, 83.4, '2026-09-28'),
(1, 82.8, 82.8, '2026-09-30'),
(1, 82.3, 82.3, '2026-10-02'),
(1, 81.9, 81.9, '2026-10-04'),
(1, 81.5, 81.5, '2026-10-07'),
-- User 2 (Jane Smith): Stable weight maintenance
(2, 62.2, 62.2, '2026-09-24'),
(2, 62.0, 62.0, '2026-09-28'),
(2, 61.8, 61.8, '2026-10-02'),
(2, 61.5, 61.5, '2026-10-07'),
-- User 3 (Vignesh H): Steady lean muscle gain
(3, 68.5, 68.5, '2026-09-24'),
(3, 69.2, 69.2, '2026-09-28'),
(3, 70.1, 70.1, '2026-10-02'),
(3, 71.0, 71.0, '2026-10-07');

-- Meal Logs for User 1 (John Doe - Heart-Safe, Low Sodium meals)
INSERT INTO `meal_logs` (`user_id`, `food_id`, `food_name`, `meal_type`, `quantity_grams`, `calories`, `protein`, `carbs`, `fat`, `logged_date`, `logged_time`) VALUES
-- 2026-09-24
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-09-24', '08:15:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-09-24', '13:00:00'),
(1, 14, 'Roasted Makhana (Fox Nuts)', 'Snack', 35, 130, 3.5, 22.0, 3.5, '2026-09-24', '17:30:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-09-24', '20:00:00'),
-- 2026-09-25
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-09-25', '08:30:00'),
(1, 11, 'Brown Basmati Rice & Yellow Dal', 'Lunch', 350, 360, 15.0, 64.0, 5.0, '2026-09-25', '13:15:00'),
(1, 20, 'Garden Vegetable Soup & Roll', 'Dinner', 350, 220, 7.5, 38.0, 4.0, '2026-09-25', '19:45:00'),
-- 2026-09-26
(1, 2, 'Vegetable Egg White Scramble', 'Breakfast', 200, 170, 18.0, 4.0, 8.0, '2026-09-26', '08:00:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-09-26', '13:00:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-09-26', '20:15:00'),
-- 2026-09-27
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-09-27', '08:20:00'),
(1, 13, 'Mediterranean Chickpea Salad', 'Lunch', 320, 310, 14.0, 44.0, 9.0, '2026-09-27', '12:45:00'),
(1, 19, 'Steamed White Fish & Sweet Potato', 'Dinner', 320, 340, 31.0, 36.0, 6.0, '2026-09-27', '19:30:00'),
-- 2026-09-28
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-09-28', '08:00:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-09-28', '13:10:00'),
(1, 14, 'Roasted Makhana (Fox Nuts)', 'Snack', 35, 130, 3.5, 22.0, 3.5, '2026-09-28', '17:00:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-09-28', '20:00:00'),
-- 2026-09-29
(1, 2, 'Vegetable Egg White Scramble', 'Breakfast', 200, 170, 18.0, 4.0, 8.0, '2026-09-29', '08:15:00'),
(1, 11, 'Brown Basmati Rice & Yellow Dal', 'Lunch', 350, 360, 15.0, 64.0, 5.0, '2026-09-29', '13:00:00'),
(1, 20, 'Garden Vegetable Soup & Roll', 'Dinner', 350, 220, 7.5, 38.0, 4.0, '2026-09-29', '19:40:00'),
-- 2026-09-30
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-09-30', '08:30:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-09-30', '13:20:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-09-30', '20:10:00'),
-- 2026-10-01
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-10-01', '08:15:00'),
(1, 13, 'Mediterranean Chickpea Salad', 'Lunch', 320, 310, 14.0, 44.0, 9.0, '2026-10-01', '13:00:00'),
(1, 19, 'Steamed White Fish & Sweet Potato', 'Dinner', 320, 340, 31.0, 36.0, 6.0, '2026-10-01', '19:50:00'),
-- 2026-10-02
(1, 2, 'Vegetable Egg White Scramble', 'Breakfast', 200, 170, 18.0, 4.0, 8.0, '2026-10-02', '08:00:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-10-02', '13:15:00'),
(1, 14, 'Roasted Makhana (Fox Nuts)', 'Snack', 35, 130, 3.5, 22.0, 3.5, '2026-10-02', '17:15:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-10-02', '20:00:00'),
-- 2026-10-03
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-10-03', '08:20:00'),
(1, 11, 'Brown Basmati Rice & Yellow Dal', 'Lunch', 350, 360, 15.0, 64.0, 5.0, '2026-10-03', '13:00:00'),
(1, 20, 'Garden Vegetable Soup & Roll', 'Dinner', 350, 220, 7.5, 38.0, 4.0, '2026-10-03', '19:30:00'),
-- 2026-10-04
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-10-04', '08:10:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-10-04', '13:00:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-10-04', '20:15:00'),
-- 2026-10-05
(1, 2, 'Vegetable Egg White Scramble', 'Breakfast', 200, 170, 18.0, 4.0, 8.0, '2026-10-05', '08:25:00'),
(1, 13, 'Mediterranean Chickpea Salad', 'Lunch', 320, 310, 14.0, 44.0, 9.0, '2026-10-05', '13:10:00'),
(1, 19, 'Steamed White Fish & Sweet Potato', 'Dinner', 320, 340, 31.0, 36.0, 6.0, '2026-10-05', '19:40:00'),
-- 2026-10-06
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-10-06', '08:30:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-10-06', '13:00:00'),
(1, 14, 'Roasted Makhana (Fox Nuts)', 'Snack', 35, 130, 3.5, 22.0, 3.5, '2026-10-06', '17:00:00'),
(1, 17, 'Baked Wild Salmon with Asparagus', 'Dinner', 320, 390, 35.0, 7.0, 24.0, '2026-10-06', '20:00:00'),
-- 2026-10-07 (Today)
(1, 1, 'Oatmeal with Berries & Chia Seeds', 'Breakfast', 250, 280, 8.5, 52.0, 4.5, '2026-10-07', '08:15:00'),
(1, 10, 'Grilled Lemon Herb Chicken Bowl', 'Lunch', 350, 420, 38.0, 38.0, 10.0, '2026-10-07', '13:00:00'),
(1, 14, 'Roasted Makhana (Fox Nuts)', 'Snack', 35, 130, 3.5, 22.0, 3.5, '2026-10-07', '17:20:00');

-- Water Logs for User 1 (2200 - 2800ml daily)
INSERT INTO `water_logs` (`user_id`, `amount_ml`, `logged_date`, `logged_time`, `notes`) VALUES
(1, 2500, '2026-09-24', '18:00:00', 'Daily goal met'),
(1, 2750, '2026-09-25', '19:00:00', 'Hydrated throughout walk'),
(1, 2250, '2026-09-26', '18:30:00', 'Water intake tracked'),
(1, 2600, '2026-09-27', '19:00:00', 'Good hydration'),
(1, 2800, '2026-09-28', '19:30:00', 'Drank warm water'),
(1, 2400, '2026-09-29', '18:15:00', 'Target reached'),
(1, 2700, '2026-09-30', '19:00:00', 'Clean hydration'),
(1, 2500, '2026-10-01', '18:45:00', 'Daily hydration'),
(1, 2900, '2026-10-02', '19:20:00', 'Active day hydration'),
(1, 2600, '2026-10-03', '18:30:00', 'Electrolytes included'),
(1, 2450, '2026-10-04', '18:00:00', 'Water logged'),
(1, 2800, '2026-10-05', '19:15:00', 'Pre & post workout'),
(1, 2750, '2026-10-06', '19:00:00', 'Steady intake'),
(1, 2250, '2026-10-07', '17:30:00', 'Today so far');

-- Exercise Completions for User 1 (Heart-safe: Walking, Cycling, Mobility)
INSERT INTO `exercise_completions` (`user_id`, `exercise_id`, `exercise_name`, `completion_date`, `start_time`, `end_time`, `duration_seconds`, `sets_completed`, `repetitions_completed`, `calories_burned`, `completion_percentage`, `daily_score`) VALUES
(1, 1, 'Brisk Walking', '2026-09-24', '07:00:00', '07:25:00', 1500, 1, 1, 140, 100, 100),
(1, 7, 'Cat-Cow Spinal Mobility', '2026-09-24', '07:26:00', '07:31:00', 300, 2, 8, 25, 100, 50),
(1, 2, 'Stationary Bicycle Ride', '2026-09-25', '18:00:00', '18:20:00', 1200, 1, 1, 160, 100, 120),
(1, 1, 'Brisk Walking', '2026-09-26', '07:00:00', '07:25:00', 1500, 1, 1, 140, 100, 100),
(1, 2, 'Stationary Bicycle Ride', '2026-09-27', '18:00:00', '18:25:00', 1500, 1, 1, 190, 100, 130),
(1, 1, 'Brisk Walking', '2026-09-28', '07:00:00', '07:30:00', 1800, 1, 1, 170, 100, 110),
(1, 8, 'Supine Glute Bridges', '2026-09-28', '07:32:00', '07:42:00', 600, 3, 15, 45, 100, 75),
(1, 2, 'Stationary Bicycle Ride', '2026-09-29', '18:00:00', '18:20:00', 1200, 1, 1, 150, 100, 100),
(1, 1, 'Brisk Walking', '2026-09-30', '07:00:00', '07:25:00', 1500, 1, 1, 140, 100, 100),
(1, 2, 'Stationary Bicycle Ride', '2026-10-01', '18:00:00', '18:25:00', 1500, 1, 1, 185, 100, 125),
(1, 1, 'Brisk Walking', '2026-10-02', '07:00:00', '07:30:00', 1800, 1, 1, 175, 100, 115),
(1, 1, 'Brisk Walking', '2026-10-03', '07:00:00', '07:25:00', 1500, 1, 1, 140, 100, 100),
(1, 2, 'Stationary Bicycle Ride', '2026-10-04', '18:00:00', '18:20:00', 1200, 1, 1, 160, 100, 120),
(1, 1, 'Brisk Walking', '2026-10-05', '07:00:00', '07:30:00', 1800, 1, 1, 170, 100, 110),
(1, 2, 'Stationary Bicycle Ride', '2026-10-06', '18:00:00', '18:25:00', 1500, 1, 1, 190, 100, 130),
(1, 1, 'Brisk Walking', '2026-10-07', '07:00:00', '07:25:00', 1500, 1, 1, 145, 100, 100);

-- Exercise Sessions for User 1 (To match exercise_sessions table)
INSERT INTO `exercise_sessions` (`user_id`, `session_date`, `planned_duration`, `actual_duration`, `estimated_calories`, `actual_calories`, `completion_percentage`, `daily_score`, `status`) VALUES
(1, '2026-09-24', 1800, 1800, 160, 165, 100, 150, 'COMPLETED'),
(1, '2026-09-25', 1200, 1200, 150, 160, 100, 120, 'COMPLETED'),
(1, '2026-09-26', 1500, 1500, 140, 140, 100, 100, 'COMPLETED'),
(1, '2026-09-27', 1500, 1500, 180, 190, 100, 130, 'COMPLETED'),
(1, '2026-09-28', 2400, 2400, 210, 215, 100, 185, 'COMPLETED'),
(1, '2026-09-29', 1200, 1200, 150, 150, 100, 100, 'COMPLETED'),
(1, '2026-09-30', 1500, 1500, 140, 140, 100, 100, 'COMPLETED'),
(1, '2026-10-01', 1500, 1500, 180, 185, 100, 125, 'COMPLETED'),
(1, '2026-10-02', 1800, 1800, 170, 175, 100, 115, 'COMPLETED'),
(1, '2026-10-03', 1500, 1500, 140, 140, 100, 100, 'COMPLETED'),
(1, '2026-10-04', 1200, 1200, 150, 160, 100, 120, 'COMPLETED'),
(1, '2026-10-05', 1800, 1800, 170, 170, 100, 110, 'COMPLETED'),
(1, '2026-10-06', 1500, 1500, 180, 190, 100, 130, 'COMPLETED'),
(1, '2026-10-07', 1500, 1500, 140, 145, 100, 100, 'COMPLETED');

-- Seed Daily Goals for Users
INSERT INTO `daily_goals` (`user_id`, `target_calories`, `target_protein`, `target_carbs`, `target_fat`, `target_water_ml`, `target_exercise_calories`) VALUES
(1, 1950, 115.0, 220.0, 60.0, 3000, 300),
(2, 1850, 95.0, 230.0, 55.0, 2500, 250),
(3, 2450, 140.0, 290.0, 75.0, 3200, 450)
ON DUPLICATE KEY UPDATE `target_calories` = VALUES(`target_calories`);

-- Meal Plans for User 1 (Planned vs Completed)
INSERT INTO `meal_plans` (`user_id`, `plan_date`, `calorie_target`, `status`) VALUES
(1, '2026-10-07', 1950, 'ACTIVE')
ON DUPLICATE KEY UPDATE `calorie_target` = VALUES(`calorie_target`);
