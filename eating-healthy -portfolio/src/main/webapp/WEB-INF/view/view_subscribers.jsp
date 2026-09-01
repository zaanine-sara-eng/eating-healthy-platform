<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>View Subscribers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <%-- Make sure this matches your actual CSS file name --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_dashboard.css"> 
</head>
<body class="subscribers-body">

<div class="main-content-wrapper">
    <h1>Subscribers List for Coach ${sessionScope.userName}</h1>

    <p class="back-link"><a href="dashboard_coach">← Back to Dashboard</a></p>

    <c:choose>
        <c:when test="${not empty subscribers}">
            <table class="subscribers-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Age</th>
                        <th>Weight (kg)</th>
                        <th>Height (cm)</th>
                        <th>Allergies</th>
                        <th>Goal</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${subscribers}">
                        <tr>
                            <td>${s.name}</td>
                            <td>${s.email}</td>
                            <td>${s.age}</td>
                            
                            <%-- Display Weight --%>
                            <td>
                                <c:choose>
                                    <c:when test="${s.weight > 0}">${s.weight}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>

                            <%-- Display Height --%>
                            <td>
                                <c:choose>
                                    <c:when test="${s.height > 0}">${s.height}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>

                            <%-- Display Allergies --%>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty s.allergies}">
                                        <span style="color: #ff6b6b;">${s.allergies}</span>
                                    </c:when>
                                    <c:otherwise>None</c:otherwise>
                                </c:choose>
                            </td>

                            <td>${s.goal}</td>
                            <td>
                                <a href="#" class="action-link">View Progress</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="no-subscribers">You currently do not have any registered subscribers.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>