<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Meal: ${meal.mealName}</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_forms.css"> 
</head>
<body>

<div class="wrapper">
    <h1>Edit Meal Plan</h1>
    <h2 style="font-size: 20px; text-align: center; color: #4CAF50;">Meal ID: ${meal.mealId}</h2>

    <form action="${pageContext.request.contextPath}/editMeal" method="post">
        
        <!-- Hidden inputs for POST action -->
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="mealId" value="${meal.mealId}">

        <div class="input-box">
            <label for="mealType">Meal Type:</label>
            <select id="mealType" name="mealType" required>
                <!-- Pre-select the current value -->
                <option value="Breakfast" <c:if test="${meal.mealType eq 'Breakfast'}">selected</c:if>>Breakfast</option>
                <option value="Lunch" <c:if test="${meal.mealType eq 'Lunch'}">selected</c:if>>Lunch</option>
                <option value="Dinner" <c:if test="${meal.mealType eq 'Dinner'}">selected</c:if>>Dinner</option>
                <option value="Snack" <c:if test="${meal.mealType eq 'Snack'}">selected</c:if>>Snack</option>
            </select>
        </div>

        <div class="input-box">
            <label for="mealName">Meal Name/Description:</label>
            <input type="text" id="mealName" name="mealName" required value="${meal.mealName}">
        </div>

        <div class="input-box">
            <label for="mealDate">Date:</label>
            <!-- Date input requires a string format for value, which java.sql.Date provides -->
            <input type="date" id="mealDate" name="mealDate" required value="${meal.mealDate}"> 
        </div>

        <button type="submit" class="btn">Save Changes</button>
    </form>
    
    <p style="text-align: center; margin-top: 20px;"><a href="${pageContext.request.contextPath}/editMeal">Cancel and View All Meals</a></p>

    <!-- Display Error Message -->
    <c:if test="${not empty error}">
        <p style="color: red; text-align: center;">${error}</p>
    </c:if>

</div>

</body>
</html>