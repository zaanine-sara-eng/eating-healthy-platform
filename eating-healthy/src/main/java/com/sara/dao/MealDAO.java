package com.sara.dao;

import com.sara.model.Meal;
import com.sara.utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealDAO {

    // (Existing method: addMeal)
    public boolean addMeal(Meal meal) {
        String sql = "INSERT INTO Meal (coach_id, meal_type, meal_name, meal_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meal.getCoachId());
            stmt.setString(2, meal.getMealType());
            stmt.setString(3, meal.getMealName());
            stmt.setDate(4, meal.getMealDate());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // (Existing method: getMealsByCoach)
    public List<Meal> getMealsByCoach(int coachId) {
        List<Meal> meals = new ArrayList<>();
        // Ordering by date helps the coach see the latest plans
        String sql = "SELECT * FROM Meal WHERE coach_id = ? ORDER BY meal_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Meal m = new Meal();
                m.setMealId(rs.getInt("meal_id"));
                m.setCoachId(rs.getInt("coach_id"));
                m.setMealType(rs.getString("meal_type"));
                m.setMealName(rs.getString("meal_name"));
                m.setMealDate(rs.getDate("meal_date"));
                meals.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    // New method: Fetch a single meal by ID
    public Meal getMealById(int mealId) {
        String sql = "SELECT * FROM Meal WHERE meal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mealId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Meal m = new Meal();
                m.setMealId(rs.getInt("meal_id"));
                m.setCoachId(rs.getInt("coach_id"));
                m.setMealType(rs.getString("meal_type"));
                m.setMealName(rs.getString("meal_name"));
                m.setMealDate(rs.getDate("meal_date"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // New method: Update an existing meal
    public boolean updateMeal(Meal meal) {
        String sql = "UPDATE Meal SET meal_type = ?, meal_name = ?, meal_date = ? WHERE meal_id = ? AND coach_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, meal.getMealType());
            stmt.setString(2, meal.getMealName());
            stmt.setDate(3, meal.getMealDate());
            stmt.setInt(4, meal.getMealId());
            stmt.setInt(5, meal.getCoachId()); // Security check: ensure only the owner coach can update
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // New method: Delete an existing meal
    public boolean deleteMeal(int mealId, int coachId) {
        String sql = "DELETE FROM Meal WHERE meal_id = ? AND coach_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mealId);
            stmt.setInt(2, coachId); // Security check: ensure only the owner coach can delete
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ----------------------------------------------------------------------
    // ✨ NEW METHOD: getMealsForSubscriberToday
    // ----------------------------------------------------------------------

    /**
     * Retrieves all meals created by a specific coach for a specific date (today).
     * Used by the Subscriber Dashboard.
     */
    public List<Meal> getMealsForSubscriberToday(int coachId, LocalDate date) {
        List<Meal> meals = new ArrayList<>();
        
        String sql = "SELECT meal_id, coach_id, meal_type, meal_name, meal_date " +
                     "FROM Meal WHERE coach_id = ? AND meal_date = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coachId);
            // Convert LocalDate to java.sql.Date for the SQL statement
            ps.setDate(2, Date.valueOf(date)); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meal meal = new Meal();
                    meal.setMealId(rs.getInt("meal_id"));
                    meal.setCoachId(rs.getInt("coach_id"));
                    meal.setMealType(rs.getString("meal_type"));
                    meal.setMealName(rs.getString("meal_name"));
                    meal.setMealDate(rs.getDate("meal_date")); 
                    
                    meals.add(meal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching meals for subscriber: " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }
}