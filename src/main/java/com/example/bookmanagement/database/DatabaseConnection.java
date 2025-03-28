package com.example.bookmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cuoiki";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Quang@1209";

    // Phương thức để tạo kết nối với cơ sở dữ liệu
    public static Connection getConnection() throws SQLException {
        try {
            // Đảm bảo driver JDBC được load
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Không thể tải driver JDBC: " + e.getMessage());
            throw new SQLException("Không tìm thấy driver JDBC", e);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
            throw e;
        }
    }

    // Phương thức để đóng kết nối
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Không thể đóng kết nối: " + e.getMessage());
            }
        }
    }

}
