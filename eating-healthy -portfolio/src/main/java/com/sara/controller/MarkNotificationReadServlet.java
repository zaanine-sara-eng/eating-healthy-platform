// File: com.sara.controller.MarkNotificationReadServlet.java

package com.sara.controller;

import com.sara.dao.SubscriberDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/markNotificationRead")
public class MarkNotificationReadServlet extends HttpServlet {
    
    private SubscriberDAO subscriberDAO = new SubscriberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check session here for security if needed
        
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int notificationId = Integer.parseInt(idStr);
                // 1. Mark notification as read in the database
                subscriberDAO.markNotificationAsRead(notificationId);
                
                // 2. Send success response back to the client
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } catch (NumberFormatException e) {
                // Ignore and return error status
            }
        }
        // Send error status
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}