<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %> <%-- ADDED: JSTL Functions for fn:length --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Coach Dashboard</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_dashboard.css">
</head>
<body>

<div class="dashboard-container">

    <div class="side-menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="addMeal">Add Meal of the Day</a></li>
            <li><a href="editMeal">Edit Meal</a></li>
            <li><a href="viewSubscribers">View Subscribers</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </div>

    <div class="main-content">
        <h1>Welcome, Coach ${sessionScope.userName}!</h1>

        <div class="info-boxes">

            <div class="box">
                <img src="${pageContext.request.contextPath}/images/jeunesse.png" alt="Subscribers">
                <p>Total Subscribers: 
                    <c:choose>
                        <c:when test="${not empty subscribers}">
                            ${fn:length(subscribers)}
                        </c:when>
                        <c:otherwise>0</c:otherwise>
                    </c:choose>
                </p>
            </div>

            <div class="box meal-list-box">
                
                <div class="meal-content-wrapper">
                    <h2>🍽️ Recent Meal Plans</h2>
                
                    <c:choose>
                        <c:when test="${not empty meals}">
                            <div class="meal-items-container">
                            <%-- Loop through the first few meals (adjust 'end' as needed) --%>
                            <c:forEach var="m" items="${meals}" begin="0" end="2">
                                <div class="meal-item">
                                    <%-- Set dynamic icon based on meal type --%>
                                    <c:set var="icon" value="🍚"/>
                                    <c:if test="${m.mealType eq 'Breakfast'}"><c:set var="icon" value="🍳"/></c:if>
                                    <c:if test="${m.mealType eq 'Lunch'}"><c:set var="icon" value="🥗"/></c:if>
                                    <c:if test="${m.mealType eq 'Dinner'}"><c:set var="icon" value="🍲"/></c:if>
                                    <c:if test="${m.mealType eq 'Snack'}"><c:set var="icon" value="🍎"/></c:if>
                                    
                                    <span class="meal-icon">${icon}</span>
                                    <div class="meal-details">
                                        <p class="meal-name"><strong>${m.mealName}</strong></p>
                                        <p class="meal-type-date">${m.mealType} - ${m.mealDate}</p>
                                    </div>
                                </div>
                            </c:forEach>
                            </div>
                            
                           
                        </c:when>
                        <c:otherwise>
                            <p>No meals have been added yet.</p>
                            <p><a href="addMeal">Click here to add your first meal!</a></p>
                        </c:otherwise>
                    </c:choose>
                </div>
                </div>
            <%-- The original static Meal of the Day box was removed here --%>

        </div>
    </div>
</div>

</body>
</html>