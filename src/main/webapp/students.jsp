
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext"%>
<%@page import="com.mycompany.studentmanagementspring.Student"%>
<%@page import="com.mycompany.studentmanagementspring.StudentService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    // Require login
    if (username == null || role == null) {
        response.sendRedirect(
            request.getContextPath() + "/login.jsp");
        return;
    }

    Object springObject =
        application.getAttribute("springContext");

    if (!(springObject instanceof
            AnnotationConfigApplicationContext)) {
        response.sendError(
            500, "Spring context is not available.");
        return;
    }

    AnnotationConfigApplicationContext context =
        (AnnotationConfigApplicationContext) springObject;

    StudentService service =
        context.getBean(StudentService.class);

    List<Student> students;

    if ("student".equalsIgnoreCase(role)) {

        Object id = session.getAttribute("student_id");

        if (id == null) {
            response.sendError(
                403, "Student ID is not linked to your account.");
            return;
        }

        int studentId = Integer.parseInt(id.toString());

        // A student receives only their own record
        students = service.getStudentByRollNo(studentId);

    } else if ("staff".equalsIgnoreCase(role)
            || "admin".equalsIgnoreCase(role)) {

        // Staff and admin can view all records
        students = service.getAllStudents();

    } else {
        response.sendError(403, "Access denied.");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Details</title>
</head>
<body>

    <h2>Student Details</h2>
    <p>Welcome, <%= username %>!</p>

    <% if (students.isEmpty()) { %>

        <p>No student record found for your account.</p>

    <% } else { %>

        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <th>Roll No</th>
                <th>Name</th>
                <th>Gender</th>
                <th>DOB</th>
                <th>Course</th>
                <th>Semester</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Address</th>
            </tr>

            <% for (Student s : students) { %>
                <tr>
                    <td><%= s.getRollNo() %></td>
                    <td><%= s.getName() %></td>
                    <td><%= s.getGender() %></td>
                    <td><%= s.getDob() %></td>
                    <td><%= s.getCourse() %></td>
                    <td><%= s.getSemester() %></td>
                    <td><%= s.getEmail() %></td>
                    <td><%= s.getPhone() %></td>
                    <td><%= s.getAddress() %></td>
                </tr>
            <% } %>
        </table>

    <% } %>

    <br>
    <a href="dashboard.jsp">Back to Dashboard</a>
    <br><br>
    <a href="LogoutServlet">Logout</a>

</body>
</html>
