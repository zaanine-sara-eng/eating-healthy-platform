<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_forms.css">
</head>
<body>

<div class="wrapper">
    <h1>Update Health Profile</h1>

    <form action="${pageContext.request.contextPath}/updateProfile" method="post">
        
        <p>Current User: <strong>${sessionScope.userName}</strong></p>

        <div class="input-box">
            <label for="weight">Weight (kg):</label>
            <%-- Pre-populate with existing data if available --%>
            <input type="number" id="weight" name="weight" value="${profile.weight}" step="0.1" required>
        </div>

        <div class="input-box">
            <label for="height">Height (cm):</label>
            <input type="number" id="height" name="height" value="${profile.height}" required>
        </div>

        <div class="input-box">
            <label for="allergies">Allergies/Dietary Restrictions:</label>
            <textarea id="allergies" name="allergies" rows="4" placeholder="e.g., Peanuts, Lactose Intolerant, Vegetarian">${profile.allergies}</textarea>
        </div>

        <button type="submit" class="btn">Save Profile</button>
    </form>
    
    <p><a href="dashboard_subscriber">← Back to Dashboard</a></p>

    <c:if test="${not empty success}">
        <p style="color:green; font-weight: bold;">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red; font-weight: bold;">${error}</p>
    </c:if>

</div>

</body>
</html>