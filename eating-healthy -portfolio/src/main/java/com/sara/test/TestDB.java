package com.sara.test;

import com.sara.utils.DBConnection;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected to SQL Server successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

