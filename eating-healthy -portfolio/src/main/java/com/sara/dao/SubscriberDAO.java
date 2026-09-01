package com.sara.dao;

import com.sara.utils.DBConnection;
import com.sara.model.Subscriber;
import com.sara.model.SubscriberProfile; // <-- This is now correctly imported
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import com.sara.model.Notification;
public class SubscriberDAO {

    // Insert a new subscriber and return the generated id (or -1 on error)
    public int addSubscriber(Subscriber s, int coachId) {
        String sql = "INSERT INTO Subscriber (name, email, password, age, goal, coach_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPassword()); // TODO: hash passwords in production
            ps.setObject(4, s.getAge() == 0 ? null : s.getAge(), java.sql.Types.INTEGER);
            ps.setString(5, s.getGoal());
            if (coachId > 0) ps.setInt(6, coachId); else ps.setNull(6, Types.INTEGER);

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }


 // In SubscriberDAO.java

 // Find subscriber by email/password (for login)
 public Subscriber findByEmailAndPassword(String email, String password) {
     String sql = "SELECT subscriber_id, name, email, password, age, goal, coach_id FROM Subscriber WHERE email = ? AND password = ?";
     try (Connection c = DBConnection.getConnection();
          PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, email);
         ps.setString(2, password);
         try (ResultSet rs = ps.executeQuery()) {
             if (rs.next()) {
                 Subscriber s = new Subscriber();
                 // ✅ CRITICAL FIX: Set the Subscriber ID (int)
                 s.setSubscriberId(rs.getInt("subscriber_id")); 
                 // ✅ CRITICAL FIX: Set the Coach ID (int)
                 s.setCoachId(rs.getInt("coach_id"));
                 
                 s.setName(rs.getString("name"));
                 s.setEmail(rs.getString("email"));
                 s.setPassword(rs.getString("password"));
                 s.setAge(rs.getInt("age"));
                 s.setGoal(rs.getString("goal"));
                 
                 // You no longer need to use s.setCoach(String.valueOf(...)) 
                 // but we keep the old field set for compatibility if needed.
                 s.setCoach(String.valueOf(rs.getInt("coach_id")));
                 
                 return s;
             }
         }
     } catch (SQLException e) { e.printStackTrace(); }
     return null;
 }
    // Optional: check if email already exists
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM Subscriber WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
 // Get subscribers managed by a coach (with Profile Data)
    public List<Subscriber> getSubscribersByCoach(int coachId) {
        List<Subscriber> subscribers = new ArrayList<>();
        
        // 💥 UPDATED SQL: Uses LEFT JOIN to get profile data if it exists
        String sql = "SELECT s.subscriber_id, s.name, s.email, s.age, s.goal, " +
                     "p.weight, p.height, p.allergies " +
                     "FROM Subscriber s " +
                     "LEFT JOIN SubscriberProfile p ON s.subscriber_id = p.subscriber_id " +
                     "WHERE s.coach_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Subscriber s = new Subscriber();
                s.setSubscriberId(rs.getInt("subscriber_id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setAge(rs.getInt("age"));
                s.setGoal(rs.getString("goal"));
                
                // 💥 NEW: Populate profile data
                // Note: If no profile exists, numbers will be 0 and string will be null
                s.setWeight(rs.getDouble("weight"));
                s.setHeight(rs.getInt("height"));
                s.setAllergies(rs.getString("allergies"));
                
                subscribers.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }
    // ----------------------------------------------------------------------
    // ✨ NEW PROFILE METHODS
    // ----------------------------------------------------------------------

    /**
     * Retrieves the SubscriberProfile data for the given subscriber ID.
     */
    public SubscriberProfile getProfileById(int subscriberId) {
        SubscriberProfile profile = new SubscriberProfile();
        profile.setSubscriberId(subscriberId); 

        String sql = "SELECT weight, height, allergies FROM SubscriberProfile WHERE subscriber_id = ?";

        // FIX: Use DBConnection.getConnection() instead of the undefined getConnection()
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subscriberId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    profile.setWeight(rs.getDouble("weight"));
                    profile.setHeight(rs.getInt("height"));
                    profile.setAllergies(rs.getString("allergies"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }
    
    /**
     * Inserts or updates the SubscriberProfile data (UPSERT logic).
     */
    public boolean saveOrUpdateProfile(SubscriberProfile profile) {
      
      // 1. Try to UPDATE first
      String updateSql = "UPDATE SubscriberProfile SET weight=?, height=?, allergies=? WHERE subscriber_id=?";
      int updatedRows = 0;
      
      // FIX: Use DBConnection.getConnection() instead of the undefined getConnection()
      try (Connection conn = DBConnection.getConnection(); 
           PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

          psUpdate.setDouble(1, profile.getWeight());
          psUpdate.setInt(2, profile.getHeight());
          psUpdate.setString(3, profile.getAllergies());
          psUpdate.setInt(4, profile.getSubscriberId());

          updatedRows = psUpdate.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
      
      // 2. If no row was updated, the profile doesn't exist, so INSERT it
      if (updatedRows == 0) {
          String insertSql = "INSERT INTO SubscriberProfile (subscriber_id, weight, height, allergies) VALUES (?, ?, ?, ?)";
          
          // FIX: Use DBConnection.getConnection() instead of the undefined getConnection()
          try (Connection conn = DBConnection.getConnection(); 
               PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

              psInsert.setInt(1, profile.getSubscriberId());
              psInsert.setDouble(2, profile.getWeight());
              psInsert.setInt(3, profile.getHeight());
              psInsert.setString(4, profile.getAllergies());

              return psInsert.executeUpdate() > 0;
          } catch (SQLException e) {
              e.printStackTrace();
              return false;
          }
      }
      
      return updatedRows > 0; // Return true if the update was successful
    }
    
    /**
     * Saves a new notification record to the database.
     */
    public boolean addNotification(int subscriberId, String message) {
        // Assume you have a Notification table: (notification_id, subscriber_id, message, is_read, created_at)
        String sql = "INSERT INTO Notification (subscriber_id, message, is_read) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, subscriberId);
            ps.setString(2, message);
            ps.setBoolean(3, false); // Always mark as unread upon creation
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all UNREAD notifications for a given subscriber.
     */
    public List<Notification> getUnreadNotifications(int subscriberId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT notification_id, message, created_at FROM Notification WHERE subscriber_id = ? AND is_read = 0 ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subscriberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setMessage(rs.getString("message"));
                    n.setCreatedAt(rs.getTimestamp("created_at"));
                    notifications.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    /**
     * Marks a specific notification as read.
     */
    public boolean markNotificationAsRead(int notificationId) {
        String sql = "UPDATE Notification SET is_read = 1 WHERE notification_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
}