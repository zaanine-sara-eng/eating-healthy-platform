package com.sara.controller;

import com.sara.dao.SubscriberDAO;
import com.sara.dao.CoachDAO;
import com.sara.model.Coach;
import com.sara.model.Subscriber;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SubscriberDAO subscriberDAO = new SubscriberDAO();
    private CoachDAO coachDAO = new CoachDAO();

    // ✅ Show registration page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Coach> coaches = coachDAO.getAllCoaches(); // fetch coaches
        request.setAttribute("coaches", coaches);
        request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
    }

    // ✅ Handle registration form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        int age = 0;
        try { age = Integer.parseInt(request.getParameter("age")); } catch (Exception ignored) {}

        String goal = request.getParameter("goal");

        int coachId = 0;
        try { coachId = Integer.parseInt(request.getParameter("coach")); } catch (Exception ignored) {}

        // Check if email already exists
        if (subscriberDAO.emailExists(email)) {
            request.setAttribute("error", "Email already registered.");
            request.setAttribute("coaches", coachDAO.getAllCoaches()); // reload coaches
            request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
            return;
        }

        Subscriber s = new Subscriber(name, email, password, age, goal, String.valueOf(coachId));
        int newId = subscriberDAO.addSubscriber(s, coachId);

        if (newId > 0) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Registration failed. Try again.");
            request.setAttribute("coaches", coachDAO.getAllCoaches()); // reload coaches
            request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
        }
    }
}
