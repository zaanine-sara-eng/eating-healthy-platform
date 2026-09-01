package com.sara.controller;

import com.sara.dao.MealDAO;
import com.sara.model.Meal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/editMeal")
public class EditMealServlet extends HttpServlet {

    private MealDAO mealDAO = new MealDAO();

    private Integer checkAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"coach".equals(session.getAttribute("userType"))) {
            response.sendRedirect("login");
            return null;
        }
        Integer coachId = (Integer) session.getAttribute("userId");
        if (coachId == null) {
            response.sendRedirect("login");
            return null;
        }
        return coachId;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Integer coachId = checkAuth(request, response);
        if (coachId == null) return;

        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            showEditForm(request, response, coachId);
        } else {
            showMealList(request, response, coachId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Integer coachId = checkAuth(request, response);
        if (coachId == null) return;
        
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            handleUpdate(request, response, coachId);
        } else if ("delete".equals(action)) {
            handleDelete(request, response, coachId);
        } else {
            // If post has no action, redirect to list
            response.sendRedirect("editMeal");
        }
    }

    private void showMealList(HttpServletRequest request, HttpServletResponse response, int coachId)
            throws ServletException, IOException {
        
        List<Meal> meals = mealDAO.getMealsByCoach(coachId);
        request.setAttribute("meals", meals);
        
        // Use a new JSP for the list view
        request.getRequestDispatcher("/WEB-INF/view/edit_meals_list.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, int coachId)
            throws ServletException, IOException {
        
        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealDAO.getMealById(mealId);
            
            // Critical Security Check: Ensure the meal belongs to the logged-in coach
            if (meal != null && meal.getCoachId() == coachId) {
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/WEB-INF/view/edit_meal_form.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Meal not found or you do not have permission to edit it.");
                showMealList(request, response, coachId); // Re-show the list with an error
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid meal ID.");
            showMealList(request, response, coachId);
        }
    }
    
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, int coachId)
            throws ServletException, IOException {
        
        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            String mealType = request.getParameter("mealType");
            String mealName = request.getParameter("mealName");
            Date mealDate = Date.valueOf(request.getParameter("mealDate"));

            Meal meal = new Meal(mealId, coachId, mealType, mealName, mealDate);
            
            if (mealDAO.updateMeal(meal)) {
                request.getSession().setAttribute("success", "Meal updated successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to update meal. Check meal ID.");
            }
            // Redirect back to the list page
            response.sendRedirect("editMeal"); 
            
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Update failed: Invalid data submitted.");
            response.sendRedirect("editMeal");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, int coachId)
            throws ServletException, IOException {
        
        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            
            if (mealDAO.deleteMeal(mealId, coachId)) {
                request.getSession().setAttribute("success", "Meal deleted successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to delete meal. Check meal ID.");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid meal ID for deletion.");
        }
        // Redirect back to the list page
        response.sendRedirect("editMeal");
    }
}