<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Meal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_forms.css">
</head>
<body>

<div class="wrapper">
    <h1>Add Meal of the Day</h1>

    <form action="${pageContext.request.contextPath}/addMeal" method="post">
        
        <div class="input-box">
            <label for="mealType">Meal Type:</label>
            <select id="mealType" name="mealType" required>
                <option value="Breakfast">Breakfast</option>
                <option value="Lunch">Lunch</option>
                <option value="Dinner">Dinner</option>
                <option value="Snack">Snack</option>
            </select>
        </div>

        <div class="input-box">
            <label for="mealName">Meal Name/Description:</label>
            <input type="text" id="mealName" name="mealName" required placeholder="e.g., Chicken with brown rice">
        </div>

        <div class="input-box">
            <label for="mealDate">Date:</label>
            <input type="date" id="mealDate" name="mealDate" required value="${currentDate}"> 
        </div>

        <button type="submit" class="btn">Add Meal</button>
    </form>
    
    <p><a href="${pageContext.request.contextPath}/dashboard_coach">Back to Dashboard</a></p>

    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
    <c:if test="${not empty success}">
        <p style="color:green">${success}</p>
    </c:if>

</div>

</body>
</html>