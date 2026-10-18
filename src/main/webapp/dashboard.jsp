
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    if (username == null || role == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Management Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            text-align: center;
            padding: 40px;
        }
        .box {
            background: white;
            max-width: 550px;
            margin: auto;
            padding: 30px;
            border-radius: 12px;
        }
        a {
            display: inline-block;
            margin: 10px;
            padding: 12px 20px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }
    </style>
</head>
<body>
    <div class="box">
        <h1>Student Management System</h1>
        <h2>Welcome, <%= username %>!</h2>
        <p>Your role: <strong><%= role %></strong></p>

        <% if ("admin".equalsIgnoreCase(role)) { %>
            <h3>Admin Dashboard</h3>
            <a href="students.jsp">View Students</a>
        <% } else if ("staff".equalsIgnoreCase(role)) { %>
            <h3>Staff Dashboard</h3>
            <a href="students.jsp">View Students</a>
        <% } else if ("student".equalsIgnoreCase(role)) { %>
            <h3>Student Dashboard</h3>
            <a href="students.jsp">View Student Records</a>
        <% } %>

        <br><br>
        <a href="LogoutServlet">Logout</a>
    </div>
</body>
</html>
