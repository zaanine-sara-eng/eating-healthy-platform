<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Meals</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_subscribers.css">
</head>
<body class="subscribers-body">

<div class="main-content-wrapper">
    <h1>Manage Meal Plans</h1>

    <p class="back-link"><a href="dashboard_coach">← Back to Dashboard</a></p>

    <!-- Display Success or Error Message from Session -->
    <c:if test="${not empty sessionScope.success}">
        <p style="color:green; font-weight: bold; margin-bottom: 15px;">${sessionScope.success}</p>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p style="color:red; font-weight: bold; margin-bottom: 15px;">${sessionScope.error}</p>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${not empty meals}">
            <p style="color: #ccc; margin-bottom: 20px;">You have ${fn:length(meals)} meals planned.</p>
            
            <table class="subscribers-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Name/Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${meals}">
                        <tr>
                            <td>${m.mealDate}</td>
                            <td>${m.mealType}</td>
                            <td>${m.mealName}</td>
                            <td>
                                <!-- Link to show edit form -->
                                <a href="editMeal?action=edit&mealId=${m.mealId}" class="action-link" style="margin-right: 15px;">Edit</a>
                                
                                <!-- Form to submit deletion -->
                                <form action="editMeal" method="post" style="display:inline-block; margin:0;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="mealId" value="${m.mealId}">
                                    <button type="submit" class="delete-btn" onclick="return confirm('Are you sure you want to delete this meal?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-subscribers">You have not added any meals yet.</p>
            <p style="text-align: center;"><a href="addMeal">Add your first meal now!</a></p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>