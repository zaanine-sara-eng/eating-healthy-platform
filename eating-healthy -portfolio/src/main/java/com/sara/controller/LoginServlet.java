package com.sara.controller;

import com.sara.dao.CoachDAO;
import com.sara.dao.SubscriberDAO;
import com.sara.model.Coach;
import com.sara.model.Subscriber;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private CoachDAO coachDAO = new CoachDAO();
    private SubscriberDAO subscriberDAO = new SubscriberDAO();

    // ✅ Show login page on GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

    // ✅ Handle login form submission on POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Try coach login
        Coach coach = coachDAO.findByEmailAndPassword(email, password);
        if (coach != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userType", "coach");
            session.setAttribute("userName", coach.getName());
            session.setAttribute("userEmail", coach.getEmail());
            session.setAttribute("userId", coach.getCoachId()); // Add userId

            response.sendRedirect("dashboard_coach");
            return;
        }


     // Try subscriber login
        Subscriber subscriber = subscriberDAO.findByEmailAndPassword(email, password);
        if (subscriber != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userType", "subscriber");
            session.setAttribute("userName", subscriber.getName());
            session.setAttribute("userEmail", subscriber.getEmail());
            session.setAttribute("goal", subscriber.getGoal());

            // ✅ CLEANER FIX: Get the int IDs directly from the model
            session.setAttribute("userId", subscriber.getSubscriberId()); 
            session.setAttribute("coachId", subscriber.getCoachId()); 
            
            // Check if coachId is valid (assuming 0 is not a valid ID)
            if (subscriber.getCoachId() <= 0) {
                // Handle case where subscriber is unassigned or error occurred
                session.setAttribute("coachId", null);
            }
            
            // ✅ Redirect and stop
            response.sendRedirect("dashboard_subscriber");
            return; 
        }
     
        // Failed login
        request.setAttribute("error", "Invalid email or password");
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }
}
