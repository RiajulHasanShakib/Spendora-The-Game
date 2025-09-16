package com.example.gamef;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "user_info";
        String databaseUser = "root";
        String databasePassword = "Shakib123@";

       String url = "jdbc:mysql://localhost:3306/user_info?useSSL=false&serverTimezone=UTC";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
