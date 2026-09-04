<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Eating Healthy - Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="wrapper">

    <h1>Login Page</h1>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <label>Email:</label>
        <div class="input-box">
            <input type="text" name="email" required>
        </div>

        <label>Password:</label>
        <div class="input-box">
            <input type="password" name="password" required>
        </div>

        <button type="submit" class="btn">Login</button>
    </form>

    <div class="register-link">
        <p>Don’t have an account?
            <a href="${pageContext.request.contextPath}/register">Register here</a>
        </p>
    </div>

    <!-- JSTL Error -->
    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>

</div>

</body>
</html>
