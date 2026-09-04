package com.sara.controller;

import com.sara.dao.SubscriberDAO;
import com.sara.model.SubscriberProfile; // You'll need a new Profile model/table
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateProfile")
public class UpdateProfileServlet extends HttpServlet {
    
    private SubscriberDAO subscriberDAO = new SubscriberDAO();

    // GET: Display the form, pre-populating existing data
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"subscriber".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        Integer subscriberId = (Integer) session.getAttribute("userId");
        
        // Fetch existing profile data (assumed method)
        SubscriberProfile profile = subscriberDAO.getProfileById(subscriberId);
        
        request.setAttribute("profile", profile);

        request.getRequestDispatcher("/WEB-INF/view/update_profile.jsp")
                .forward(request, response);
    }

    // POST: Process form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"subscriber".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        Integer subscriberId = (Integer) session.getAttribute("userId");
        
        try {
            double weight = Double.parseDouble(request.getParameter("weight"));
            int height = Integer.parseInt(request.getParameter("height"));
            String allergies = request.getParameter("allergies");

            SubscriberProfile profile = new SubscriberProfile();
            profile.setSubscriberId(subscriberId);
            profile.setWeight(weight);
            profile.setHeight(height);
            profile.setAllergies(allergies);

            // Save or Update the profile (assumed method)
            if (subscriberDAO.saveOrUpdateProfile(profile)) {
                request.setAttribute("success", "Profile updated successfully!");
            } else {
                request.setAttribute("error", "Database error: Could not save profile.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Please enter valid numbers for weight and height.");
        }
        
        // Reload the page
        doGet(request, response);
    }
}