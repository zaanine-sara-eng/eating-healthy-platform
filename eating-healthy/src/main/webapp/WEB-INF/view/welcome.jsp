<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <%@ page import="jakarta.servlet.http.HttpSession" %> 
 <html> 
 <head>
 <title>Welcome</title>
 </head> 
 <body> 
 <% if (session == null || session.getAttribute("userType") == null) { %> 
 <h3>You are not logged in. <a href="login.jsp">Go to login</a></h3>
  <% } else { String userType = (String) session.getAttribute("userType"); %>
   <h2>Welcome, <%= userType %>!</h2>
    <p>This is your dashboard. (Later we’ll show different pages for coach and subscriber.)</p> 
    <a href="logout">Logout</a> <% } %> 
    </body> 
    </html>