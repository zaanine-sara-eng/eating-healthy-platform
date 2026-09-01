package com.sara.dao;

import com.sara.model.Coach;
import com.sara.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {

    // ✅ Get all coaches from the database
    public List<Coach> getAllCoaches() {
        List<Coach> coaches = new ArrayList<>();

        String sql = "SELECT coach_id, name, email, password FROM coach"; // adjust to your table
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coach coach = new Coach();
                coach.setCoachId(rs.getInt("coach_id"));
                coach.setName(rs.getString("name"));
                coach.setEmail(rs.getString("email"));
                coach.setPassword(rs.getString("password"));
                coaches.add(coach);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coaches;
    }
    
    public Coach findByEmailAndPassword(String email, String password) {
        String sql = "SELECT coach_id, name, email, password FROM coach WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Coach coach = new Coach();
                coach.setCoachId(rs.getInt("coach_id"));
                coach.setName(rs.getString("name"));
                coach.setEmail(rs.getString("email"));
                coach.setPassword(rs.getString("password"));
                return coach;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    
}
