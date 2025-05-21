package org.example.topkapihazinensi.untils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/topkapihazinensi?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";



    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }


public static List<Map<String, Object>> SelectExecute(String sql) {
    List<Map<String, Object>> resultList = new ArrayList<>();

    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultList.add(row);
        }

    } catch (Exception e) {
        System.out.println("Execute Error: " + e.getMessage());
    }
    return resultList;
}


    // Execute for CUD -> C = Create , U = Update , D = Delete .
    // Require SQL string
    // Returning a Boolean value
    // exp : "INSERT INTO users(name, email, password) VALUES ('Ahmed Bey', 'ahmed@gmail.com', 'pass123')"
    public static Boolean CUDExecute(String sql) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Execute successful!");
                return true;
            } else {
                System.out.println("No rows affected.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Execute Error: " + e.getMessage());
            return false;
        }
    }



}
