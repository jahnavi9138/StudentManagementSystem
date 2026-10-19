

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.studentmanagementspring.Student"%>
<%@page import="com.mycompany.studentmanagementspring.StudentService"%>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    if (username == null || role == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Object springObject = application.getAttribute("springContext");

    if (springObject == null) {
        throw new javax.servlet.ServletException(
            "Spring context was not found in application scope."
        );
    }

    AnnotationConfigApplicationContext context =
        (AnnotationConfigApplicationContext) springObject;

    StudentService service = context.getBean(StudentService.class);

    List<Student> students = new ArrayList<>();

    if ("student".equalsIgnoreCase(role)) {
        Object id = session.getAttribute("roll_no");

        if (id != null) {
            int studentId = Integer.parseInt(id.toString());
            Student student = service.getStudentByRollNo(studentId);

            if (student != null) {
                students.add(student);
            }
        }
    } else if ("staff".equalsIgnoreCase(role)
            || "admin".equalsIgnoreCase(role)) {
        students = service.getAllStudents();
    } else {
        response.sendError(
            HttpServletResponse.SC_FORBIDDEN,
            "You do not have permission to view students."
        );
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f4f6f9;
        }

        h2 {
            color: #23395d;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background: #23395d;
            color: white;
        }

        .message {
            padding: 12px;
            background: white;
            border: 1px solid #ddd;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            color: #174ea6;
        }
    </style>
</head>
<body>

<h2>Student Management System</h2>
<p>Welcome, <%= username %>!</p>
<p>Role: <%= role %></p>

<h3>Student Details</h3>

<% if (students.isEmpty()) { %>
    <p class="message">No student records found.</p>
<% } else { %>

<table>
    <thead>
        <tr>
            <th>Roll No</th>
            <th>Name</th>
            <th>Gender</th>
            <th>Date of Birth</th>
            <th>Course</th>
            <th>Semester</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
        </tr>
    </thead>
    <tbody>
    <% for (Student student : students) { %>
        <tr>
            <td><%= student.getRollNo() %></td>
            <td><%= student.getName() %></td>
            <td><%= student.getGender() %></td>
            <td><%= student.getDob() %></td>
            <td><%= student.getCourse() %></td>
            <td><%= student.getSemester() %></td>
            <td><%= student.getEmail() %></td>
            <td><%= student.getPhone() %></td>
            <td><%= student.getAddress() %></td>
        </tr>
    <% } %>
    </tbody>
</table>

<% } %>

<a href="dashboard.jsp">Back to Dashboard</a>

</body>
</html>

