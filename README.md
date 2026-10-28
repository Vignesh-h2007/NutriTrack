# Personalized Nutrition Management System 🥗💪

> **Second Year Semester 3 B.Tech Computer Science & Engineering (Data Science)**  
> **KTU Mini Project**

A complete, production-grade full-stack Java web application for personalized nutrition tracking, automated calorie & macronutrient calculation (BMR & TDEE formulas), workout session tracking, hydration logs, weight monitoring, and AI-driven food recommendations based on health goals.

---

## 🌟 Technologies Used

- **Frontend**: HTML5, Vanilla CSS3 (Custom Dark Mode Design System), Vanilla JavaScript ES6+, Chart.js v4.4.0 (for interactive data visualizers).
- **Backend**: Java OOP, Java Servlets (JAX-RS / Jakarta Servlet API), JDBC (Java Database Connectivity).
- **Database**: MySQL Server (8.0+).
- **Application Server**: Apache Tomcat 9 / 10.
- **Build Tool**: Apache Maven (`pom.xml`).
- **Architecture**: Classic Model-View-Controller (MVC) & Data Access Object (DAO) Pattern.

---

## 📁 Project Directory Structure

```
PersonalizedNutritionManagement/
├── pom.xml
├── README.md
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── reset.sql
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── nutrition/
        │           ├── controller/
        │           │   ├── AuthServlet.java
        │           │   ├── FoodServlet.java
        │           │   ├── MealServlet.java
        │           │   ├── ReportServlet.java
        │           │   ├── TrackingServlet.java
        │           │   ├── UserServlet.java
        │           │   └── WorkoutServlet.java
        │           ├── dao/
        │           │   ├── AuditDAO.java
        │           │   ├── ContactDAO.java
        │           │   ├── FoodDAO.java
        │           │   ├── FoodDAOImpl.java
        │           │   ├── MealDAO.java
        │           │   ├── MealDAOImpl.java
        │           │   ├── ReportDAO.java
        │           │   ├── ReportDAOImpl.java
        │           │   ├── TrackingDAO.java
        │           │   ├── TrackingDAOImpl.java
        │           │   ├── UserDAO.java
        │           │   ├── UserDAOImpl.java
        │           │   ├── WorkoutDAO.java
        │           │   └── WorkoutDAOImpl.java
        │           ├── filter/
        │           │   ├── AuthFilter.java
        │           │   └── EncodingCorsFilter.java
        │           ├── model/
        │           │   ├── AuditLog.java
        │           │   ├── ContactMessage.java
        │           │   ├── DailyNutritionalReport.java
        │           │   ├── ExerciseLog.java
        │           │   ├── FoodItem.java
        │           │   ├── MealLog.java
        │           │   ├── User.java
        │           │   ├── WaterLog.java
        │           │   ├── WeightLog.java
        │           │   ├── WorkoutExercise.java
        │           │   ├── WorkoutPlan.java
        │           │   └── WorkoutSession.java
        │           ├── service/
        │           │   ├── FoodRecommendationService.java
        │           │   ├── MealService.java
        │           │   ├── ReportService.java
        │           │   ├── TrackingService.java
        │           │   ├── UserService.java
        │           │   └── WorkoutService.java
        │           └── util/
        │               ├── DBConnection.java
        │               ├── JsonUtil.java
        │               └── PasswordUtil.java
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml
            ├── css/
            │   └── style.css
            ├── js/
            │   └── api.js
            ├── dashboard.html
            ├── food.html
            ├── index.html
            ├── login.html
            ├── profile.html
            ├── register.html
            ├── reports.html
            ├── water.html
            ├── weight.html
            └── workout.html
```

---

## ⚡ Core Features

1. **User Authentication & Authorization**:
   - Secure login & registration with SHA-256 password hashing & salt.
   - HTTP Session management protected via custom servlet filter (`AuthFilter`).
   - Profile management with customizable BMR/TDEE health parameters (Age, Height, Weight, Activity Level, Health Goals).

2. **Personalized Nutrition Engine**:
   - Automated Mifflin-St Jeor BMR calculation.
   - Dynamic target calorie calculation based on activity level multiplier & goal offsets (+500 for Muscle Gain, -500 for Weight Loss).
   - Food Item Catalog with searching, category filtering, and custom food creation.
   - Intelligent AI recommendation engine prioritizing high-protein items for muscle gain, low-calorie items for weight loss, and balanced items for maintenance.

3. **Meal Tracking & Log Analytics**:
   - Meal logging by type (Breakfast, Lunch, Dinner, Snack).
   - Real-time macronutrient breakdown (Protein, Carbohydrates, Fats) displayed in interactive Chart.js doughnut charts.
   - Calorie consumption history timeline with edit/delete capabilities.

4. **Workout & Fitness Manager**:
   - Preset workout plans tailored to user goals (Weight Loss Blitz, Hypertrophy Muscle Builder, Endurance Cardio).
   - Live workout session runner with set, rep, weight, and duration tracker.
   - Calories burned estimator integrated into daily net calorie equations.

5. **Hydration & Weight Tracking**:
   - Daily water intake logging with quick-add presets (+250ml glass, +500ml bottle, +750ml flask, +1L pitcher).
   - Weight logging with BMI category classification (Underweight, Normal, Overweight, Obese) and historical trend lines.

6. **Comprehensive Analytics & Reporting**:
   - Custom date-range query generator.
   - Comparative Bar Chart (Consumed vs. Burned Calories).
   - Average Macro Distribution Pie Chart.
   - Daily summary report table with print-friendly layout.

---

## 🚀 Setup & Execution Guide

### Prerequisites
- Java JDK 11 or higher
- MySQL Server 8.0+
- Apache Tomcat 9.0+ or 10.0+
- Maven 3.8+ (optional, or standard IDE build)

### Step 1: Database Setup
1. Log into your MySQL instance via MySQL Workbench, Command Line, or phpMyAdmin:
   ```bash
   mysql -u root -p
   ```
2. Execute the schema script:
   ```sql
   SOURCE database/schema.sql;
   ```
3. Populate seed data:
   ```sql
   SOURCE database/seed.sql;
   ```

### Step 2: Configure Database Credentials
Open `src/main/java/com/nutrition/util/DBConnection.java` and set your MySQL username and password if different from default:
```java
private static final String URL = "jdbc:mysql://localhost:3306/nutrition_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### Step 3: Deployment on Apache Tomcat
1. Build the WAR artifact:
   ```bash
   mvn clean package
   ```
2. Copy the resulting `PersonalizedNutritionManagement.war` from the `target/` directory to Apache Tomcat's `webapps/` folder.
3. Start Tomcat:
   - **Windows**: `bin\startup.bat`
   - **Linux/Mac**: `bin/startup.sh`
4. Access the web app in your browser at:
   `http://localhost:8080/PersonalizedNutritionManagement/`

---

## 🔒 Test Credentials (Seed Data)

- **Default User**: `john@example.com`
- **Default Password**: `password123`
- **Demo User 2**: `jane@example.com` / `password123`

---

## 📜 KTU Academic Details

- **Course**: B.Tech Computer Science & Engineering (Data Science)
- **Semester**: Semester 3 (Second Year)
- **Project Type**: Full-Stack Web Development Mini Project

=========================================================================================
                              study......

# Personalized Nutrition Management System 🥗💪

> **Second Year Semester 3 B.Tech Computer Science & Engineering (Data Science)**  
> **KTU Mini Project**

A complete, production-grade full-stack Java web application for personalized nutrition tracking, automated calorie & macronutrient calculation (BMR & TDEE formulas), workout session tracking, hydration logs (with dynamic 3D glass animations), weight monitoring, and AI-driven food recommendations based on health goals.

---

## 🌟 Technologies Used

- **Frontend**: HTML5, Vanilla CSS3 (Custom Dark Glassmorphism Theme), Vanilla JavaScript ES6+, Chart.js v4.4.0 (for interactive data visualizers). No external frameworks like Tailwind are used to ensure maximum control.
- **Backend**: Java OOP, Java Servlets (JAX-RS / Jakarta Servlet API), JDBC (Java Database Connectivity).
- **Database**: MySQL Server (8.0+).
- **Application Server**: Apache Tomcat 9 / 10.
- **Build Tool**: Apache Maven (`pom.xml`) or standard IDE build (`build.bat`).
- **Architecture**: Classic Model-View-Controller (MVC) & Data Access Object (DAO) Pattern.

---

## ⚡ Core Features (Updated!)

1. **User Authentication & Authorization**:
   - Secure login & registration with SHA-256 password hashing & salt.
   - HTTP Session management protected via custom servlet filter (`AuthFilter`).
   - Profile management with customizable BMR/TDEE health parameters (Age, Height, Weight, Activity Level, Health Goals).

2. **Personalized Nutrition Engine**:
   - Automated Mifflin-St Jeor BMR calculation.
   - Dynamic target calorie calculation based on activity level multiplier & goal offsets.
   - Intelligent AI recommendation engine prioritizing high-protein items for muscle gain, low-calorie items for weight loss, and balanced items for maintenance.

3. **Meal Tracking & Log Analytics**:
   - Real-time macronutrient breakdown (Protein, Carbohydrates, Fats) displayed in interactive Chart.js doughnut charts.
   - Calorie consumption history timeline with edit/delete capabilities.

4. **Hydration Engine & UI**:
   - Immersive 3D water glass animation for tracking daily water intake.
   - Default goal set to an ambitious **100,000 ml** (400 glasses of 250ml).
   - Dynamic batched rendering and Web Audio API integration for engaging drinking sound effects.

5. **Advanced Settings & Customization**:
   - Multiple Theme Modes (Dark, AMOLED, Cyber Neon, Emerald, Light) and Accent Colors.
   - Background Particle and Aurora effects toggles.
   - Built-in Water Reminder chimes and data backup/restore capabilities (JSON export/import).

6. **Comprehensive Analytics & Reporting**:
   - Visual BMI color spectrum bar with pin indicator.
   - Live digital clock and glowing stat cards for calories and water.

---

## 🧠 How It Works / Teach Me

This project follows a classic **Client-Server Architecture** heavily reliant on Java Servlets and JavaScript `fetch` API.

1. **The Client (Frontend)**: The HTML files (e.g., `dashboard.html`, `water.html`) load structural layouts. The `style.css` provides the visual glassmorphism aesthetic.
2. **JavaScript APIs (`api.js`)**: The frontend uses asynchronous `fetch()` requests to communicate with the Java backend. When you click a water glass or save settings, a JSON payload is sent via a POST or PUT request to a specific URL endpoint (e.g., `/api/user/water`).
3. **The Server (Backend Servlets)**: Tomcat intercepts these requests and routes them to the appropriate Java Servlet (like `TrackingServlet.java` or `UserServlet.java`). The servlet parses the incoming JSON, applies business logic (like updating scores or verifying authentication via session cookies), and interacts with the DAO (Data Access Object) layer.
4. **The DAO (Data Access Object)**: These Java classes execute the actual SQL queries to read or modify the MySQL database. 
5. **The Response**: Once the database operation finishes, the servlet sends a JSON response back to the frontend, which then dynamically updates the DOM (HTML elements) without reloading the page.

---

## 🗄️ Database & SQL Connectivity

The application persists all user data, logs, and settings in a **MySQL** relational database (`personalized_nutrition_db` or `nutrition_db`).

### How the Database Works
- **Schema Design**: The database (`database/schema.sql`) contains over 19 normalized tables. Core tables include `users`, `foods`, `meal_logs`, `water_logs`, and `weight_history`.
- **Relationships**: It uses Foreign Keys (e.g., `user_id` in `water_logs` references `users.user_id`) with `ON DELETE CASCADE` to ensure data integrity (if a user is deleted, their logs are automatically removed).

### How Java Connects to SQL (JDBC)
The connection is established using **JDBC (Java Database Connectivity)**.

1. **`DBConnection.java`**: Located in `src/main/java/com/nutrition/util/DBConnection.java`, this utility class manages the connection pool. 
2. **The Driver**: It uses the `mysql-connector-j` driver to establish a TCP connection to `localhost:3306`.
3. **Connection String**: 
   ```java
   String URL = "jdbc:mysql://localhost:3306/personalized_nutrition_db"; // Note: verify exact DB name in util class
   Connection conn = DriverManager.getConnection(URL, "root", "your_password");
   ```
4. **Execution**: The DAO layers (e.g., `TrackingDAOImpl.java`) call `DBConnection.getConnection()`, create `PreparedStatement`s to prevent SQL injection, and execute `ResultSet` queries to fetch or insert data.

---

## 🚀 Setup & Execution Guide

### Prerequisites
- Java JDK 11 or higher
- MySQL Server 8.0+
- Apache Tomcat 9.0+ or 10.0+
- Maven 3.8+ (optional, or use standard IDE build)

### Step 1: Database Setup
1. Log into your MySQL instance via Command Line or MySQL Workbench:
   ```bash
   mysql -u root -p
   ```
2. Execute the schema script:
   ```sql
   SOURCE database/schema.sql;
   ```
3. Populate seed data:
   ```sql
   SOURCE database/seed.sql;
   ```

### Step 2: Configure Database Credentials
Open `src/main/java/com/nutrition/util/DBConnection.java` and set your MySQL username and password if different from default:
```java
private static final String URL = "jdbc:mysql://localhost:3306/personalized_nutrition_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### Step 3: Compilation and Deployment

**Option A: Using Maven (Recommended)**
1. Build the WAR artifact:
   ```bash
   mvn clean package
   ```
2. Copy the resulting `PersonalizedNutritionManagement.war` from the `target/` directory to Apache Tomcat's `webapps/` folder.
3. Start Tomcat:
   - **Windows**: `bin\startup.bat`
   - **Linux/Mac**: `bin/startup.sh`

**Option B: Using the provided batch script (Windows)**
1. Run the `build.bat` file in the root directory. This will compile all `.java` files using `javac` into the `target/classes` folder.
2. Ensure you have the `mysql-connector-j` and `servlet-api` jar files in the `lib` directory for it to compile successfully.
3. Configure your Tomcat server to point to the `src/main/webapp` directory.

**Access the web app at:**
`http://localhost:8080/PersonalizedNutritionManagement/`

---

## 🔒 Test Credentials (Seed Data)

- **Default User**: `john@example.com`
- **Default Password**: `password123`
- **Demo User 2**: `jane@example.com` / `password123`

---

## 📜 KTU Academic Details & Credits

- **Course**: B.Tech Computer Science & Engineering (Data Science)
- **Institution**: College of Engineering Adoor (CEA)
- **Faculty Guide & Mentor**: Asst. Prof. Krishnenthu P Mani (Professor of Object Oriented Programming Language)
- **Project Type**: Full-Stack Web Development & OOP Mini Project

### Project Development Team
- **Vignesh H** — Frontend UI/UX Developer (KTU ID: ADR25CD052)
- **Chris** — Lead AI & Data Architect (KTU ID: ADR25CD016)
- **Jinson Biju** — Recommendation Model Dev (KTU ID: ADR25CD026)
- **Abhijith S** — Database Developer (KTU ID: ADR25CD001)
- **Athul** — API & Servlet Developer (KTU ID: ADR25CD013)
- **Visakh B** — Recommendation Model Dev (KTU ID: ADR25CD053)
