
package com.mycompany.studentmanagementspring;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL =
        "jdbc:mysql://mysql-d360566-student-management-system-9138.i.aivencloud.com:28261/defaultdb?sslMode=REQUIRED";

    private static final String JDBC_USER = "avnadmin";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String dbPassword = System.getenv("DB_PASSWORD");

        if (username == null || password == null
                || username.trim().isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage",
                    "Please enter username and password.");
            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);
            return;
        }

        if (dbPassword == null || dbPassword.isEmpty()) {
            throw new ServletException(
                    "DB_PASSWORD is missing in Render environment variables.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(
                    JDBC_URL, JDBC_USER, dbPassword)) {

                String sql =
                    "SELECT username FROM users WHERE username = ? AND password = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, username.trim());
                    stmt.setString(2, password);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (rs.next()) {
                            String loggedInUser = rs.getString("username");

                            // Match the attributes expected by dashboard.jsp
                            String role = "admin".equalsIgnoreCase(loggedInUser)
                                    ? "Admin" : "Student";

                            HttpSession session = request.getSession();
                            session.setAttribute("username", loggedInUser);
                            session.setAttribute("role", role);

                            response.sendRedirect(
                                    request.getContextPath() + "/dashboard.jsp");
                        } else {
                            request.setAttribute("errorMessage",
                                    "Invalid username or password.");
                            request.getRequestDispatcher("/login.jsp")
                                   .forward(request, response);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new ServletException("Database login failed.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/login.jsp");
    }
}
