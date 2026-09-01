package com.sara.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = System.getenv("EATING_HEALTHY_DB_URL");
	private static final String USER = System.getenv("EATING_HEALTHY_DB_USER");
	private static final String PASSWORD = System.getenv("EATING_HEALTHY_DB_PASSWORD");

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load SQL Server driver explicitly
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to SQL Server successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQL Server JDBC Driver not found. Make sure the JAR is in Tomcat's lib folder.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Connection failed! Check URL, username, or password.");
            e.printStackTrace();
        }
        return conn;
    }
}
