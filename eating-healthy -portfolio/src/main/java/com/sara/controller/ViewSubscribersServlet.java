package com.sara.controller;

import com.sara.dao.SubscriberDAO;
import com.sara.model.Subscriber;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/viewSubscribers")
public class ViewSubscribersServlet extends HttpServlet {

    private SubscriberDAO subscriberDAO = new SubscriberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Security Check: Only coaches can access this
        HttpSession session = request.getSession(false);
        if (session == null || !"coach".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return;
        }

        Integer coachId = (Integer) session.getAttribute("userId");
        if (coachId == null) {
            response.sendRedirect("login");
            return;
        }

        // 2. Fetch the list of subscribers
        List<Subscriber> subscribers = subscriberDAO.getSubscribersByCoach(coachId);

        // 3. Set the data and forward to the new JSP
        request.setAttribute("subscribers", subscribers);

        request.getRequestDispatcher("/WEB-INF/view/view_subscribers.jsp")
                .forward(request, response);
    }
}