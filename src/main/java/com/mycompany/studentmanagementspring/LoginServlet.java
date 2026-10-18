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

    private static final String JDBC_URL = "jdbc:mysql://mysql-d360566-student-management-system-9138.i.aivencloud.com:28261/defaultdb?sslMode=REQUIRED&enabledTLSProtocols=TLSv1.2,TLSv1.3&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "avnadmin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Read password exclusively from Render environment variable
        String dbPassword = System.getenv("DB_PASSWORD");
        
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            System.err.println("WARNING: DB_PASSWORD environment variable is missing!");
        } else {
            System.out.println("INFO: DB_PASSWORD successfully loaded from environment variable.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, dbPassword)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            HttpSession session = request.getSession();
                            session.setAttribute("user", username);
                            response.sendRedirect("dashboard.jsp");
                        } else {
                            request.setAttribute("errorMessage", "Invalid username or password.");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database login failed.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}