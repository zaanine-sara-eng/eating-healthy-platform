package com.sara.controller;

import com.sara.dao.MealDAO;
import com.sara.dao.SubscriberDAO; 
import com.sara.model.Meal;
import com.sara.model.Subscriber; 
import com.sara.utils.EmailService; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List; 

@WebServlet("/addMeal")
public class AddMealServlet extends HttpServlet {
    
    private MealDAO mealDAO = new MealDAO();
    private SubscriberDAO subscriberDAO = new SubscriberDAO(); 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Security Check: Only coaches can access this
        HttpSession session = request.getSession(false);
        if (session == null || !"coach".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        // Set today's date for the form default value
        request.setAttribute("currentDate", LocalDate.now().toString());  
        
        request.getRequestDispatcher("/WEB-INF/view/add_meal.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Security Check (re-check on POST)
        HttpSession session = request.getSession(false);
        if (session == null || !"coach".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        // 1. Get Coach ID from session
        Integer coachId = (Integer) session.getAttribute("userId");
        if (coachId == null) {
            request.setAttribute("error", "Session error. Please log in again.");
            doGet(request, response);
            return;
        }

        // 2. Get form parameters
        String mealType = request.getParameter("mealType");
        String mealName = request.getParameter("mealName");
        String mealDateStr = request.getParameter("mealDate");

        Date mealDate = null;
        try {
            mealDate = Date.valueOf(mealDateStr);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid date format.");
            doGet(request, response);
            return;
        }

        // 3. Create Meal object and save
        Meal newMeal = new Meal();
        newMeal.setCoachId(coachId);
        newMeal.setMealType(mealType);
        newMeal.setMealName(mealName);
        newMeal.setMealDate(mealDate);

        if (mealDAO.addMeal(newMeal)) {
            request.setAttribute("success", "Meal successfully added!");

            // ---------------------------------------------------------
            // 🔔 NEW: DB NOTIFICATION LOGIC (Replaces old email-only block)
            // ---------------------------------------------------------
            
            // 1. Fetch all subscribers managed by this coach
            List<Subscriber> subscribers = subscriberDAO.getSubscribersByCoach(coachId);
            
            // 2. Construct the message for the notification modal
            String notificationMessage = String.format(
                "Your coach added a new %s meal: %s for %s.", 
                mealType.toLowerCase(), 
                mealName, 
                mealDate.toString()
            );

            if (subscribers != null && !subscribers.isEmpty()) {
                
                // 3. Loop through all subscribers and save the notification to the DB
                for (Subscriber s : subscribers) {
                    subscriberDAO.addNotification(s.getSubscriberId(), notificationMessage);
                    
                    // 📧 OPTIONAL: Keep the Mailjet email notification running as well
                    // This gives the user both the email and the in-app modal notification!
                    new Thread(() -> {
                         EmailService.sendNotification(s.getEmail(), s.getName(), mealName);
                    }).start();
                }
                
                System.out.println("✅ Notifications saved to DB and emails queued for " + subscribers.size() + " subscribers.");
            } else {
                 System.out.println("No subscribers found for coach. Notification skipped.");
            }
            // ---------------------------------------------------------

        } else {
            request.setAttribute("error", "Failed to add meal due to a database error.");
        }
        
        // Reload the page with the status message
        doGet(request, response);
    }
}