package com.nhnacademy.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    public DbUtils(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            //todo connection.
            connection = DriverManager.getConnection("jdbc:mysql://220.67.216.14:13306/nhn_academy_215","nhn_academy_215","yQ4vfzap!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}