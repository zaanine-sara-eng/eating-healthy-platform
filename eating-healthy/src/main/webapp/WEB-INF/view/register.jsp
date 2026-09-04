<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Eating Healthy - Register</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="register-wrapper">
    <h1>Register</h1>

    <form action="${pageContext.request.contextPath}/register" method="post" class="register-form">

        <div class="input-box">
            <label>Full Name:</label>
            <input type="text" name="name" required>
        </div>

        <div class="input-box">
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>

        <div class="input-box">
            <label>Password:</label>
            <input type="password" name="password" required>
        </div>

        <div class="input-box">
            <label>Age:</label>
            <input type="number" name="age" min="10" max="99">
        </div>

        <div class="input-box">
            <label>Goal:</label>
            <input type="text" name="goal" placeholder="Lose weight / Gain muscle">
        </div>

        <div class="input-box">
            <label>Choose your coach:</label>
            <select name="coach">
                <c:forEach var="c" items="${coaches}">
                    <option value="${c.coachId}">${c.name}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn">Register</button>
    </form>

    <div class="register-link">
        <p>Already have an account? 
           <a href="${pageContext.request.contextPath}/login">Login here</a></p>
    </div>

    <!-- Replace scriptlet error with JSTL -->
    <c:if test="${not empty error}">
        <p style="color: red">${error}</p>
    </c:if>

</div>
</body>
</html>
