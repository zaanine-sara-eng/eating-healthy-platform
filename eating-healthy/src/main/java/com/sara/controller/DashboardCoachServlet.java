package com.sara.controller;

import com.sara.dao.MealDAO;
import com.sara.dao.SubscriberDAO;
import com.sara.model.Meal;
import com.sara.model.Subscriber;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard_coach")
public class DashboardCoachServlet extends HttpServlet {

    private MealDAO mealDAO = new MealDAO();
    private SubscriberDAO subscriberDAO = new SubscriberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        List<Meal> meals = mealDAO.getMealsByCoach(coachId);
        List<Subscriber> subscribers = subscriberDAO.getSubscribersByCoach(coachId);

        request.setAttribute("meals", meals);
        request.setAttribute("subscribers", subscribers);

        request.getRequestDispatcher("/WEB-INF/view/dashboard_coach.jsp")
                .forward(request, response);
    }
}
