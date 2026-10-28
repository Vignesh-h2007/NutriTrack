package com.nutrition.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/personalized_nutrition_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("[DBConnection] ERROR: MySQL JDBC Driver not found in classpath!");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", System.getenv().getOrDefault("DB_URL", DEFAULT_URL));
        String user = System.getProperty("db.user", System.getenv().getOrDefault("DB_USER", DEFAULT_USER));
        String pass = System.getProperty("db.pass", System.getenv().getOrDefault("DB_PASS", DEFAULT_PASS));

        return DriverManager.getConnection(url, user, pass);
    }
}
