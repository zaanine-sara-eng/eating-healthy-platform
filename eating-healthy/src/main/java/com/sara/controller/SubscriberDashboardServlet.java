package com.sara.controller;

import com.sara.dao.MealDAO;
import com.sara.dao.SubscriberDAO;
import com.sara.model.Notification;
import com.sara.model.Meal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date; // We keep this for the utility method Date.valueOf()
import java.time.LocalDate;
import java.util.List;

@WebServlet("/dashboard_subscriber")
public class SubscriberDashboardServlet extends HttpServlet {
    
    private SubscriberDAO subscriberDAO = new SubscriberDAO();
    private MealDAO mealDAO = new MealDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Security Check: Ensure user is logged in as a subscriber
        HttpSession session = request.getSession(false);
        if (session == null || !"subscriber".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        Integer subscriberId = (Integer) session.getAttribute("userId");
        Integer coachId = (Integer) session.getAttribute("coachId"); 

        if (subscriberId == null || coachId == null) {
            // Essential session data is missing, redirect to login
            session.invalidate(); 
            response.sendRedirect("login");
            return;
        }
        
        // --- 1. Fetch Today's Meals (This is the critical block that re-enables meal display) ---
        try {
            LocalDate today = LocalDate.now();
            
            // Call the DAO method using the coachId and the LocalDate 'today'
            List<Meal> todayMeals = mealDAO.getMealsForSubscriberToday(coachId, today);
            
            // Set the meal data
            request.setAttribute("todayMeals", todayMeals);
            request.setAttribute("currentDate", today.toString());
            
        } catch (Exception e) {
            System.err.println("Error fetching meals for subscriber: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to load today's meal plan.");
        }
            
        // --- 2. Fetch Unread Notifications (This works already) ---
        List<Notification> notifications = subscriberDAO.getUnreadNotifications(subscriberId);
        
        // Set the notification data
        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadCount", notifications.size());
        
        // 3. Forward to the dashboard view
        request.getRequestDispatcher("/WEB-INF/view/dashboard_subscriber.jsp")
                .forward(request, response);
    }
}