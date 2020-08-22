package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String DB_url = "jdbc:mysql://localhost:3306/workshop2?serverTimezone=UTC&useSSL=false";
    private static String DB_name = "root";
    private static String DB_password = "coderslab";

    public static Connection getConnection()  throws SQLException {
        return DriverManager.getConnection(DB_url, DB_name, DB_password);
    }
}
