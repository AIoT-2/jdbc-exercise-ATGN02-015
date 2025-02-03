package com.nhnacademy.jdbc.jndi;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet
public class JdbcConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        DataSource dataSource = getDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            ResultSet rs = statement.executeQuery("select * from Students");

            while (rs.next()) {
                resp.getWriter().println("==>>" + rs.getInt(1));
                resp.getWriter().println("==>>" + rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.flushBuffer();
    }

    private DataSource getDataSource() {
        try {
            InitialContext initialContext = new InitialContext();
            return (DataSource) initialContext.lookup("java:comp/env/jdbc/test");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
