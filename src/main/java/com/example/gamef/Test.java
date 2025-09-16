package com.example.gamef;

import javafx.application.Application;

import java.sql.*;

public class Test {
    public static void main(String[] args) {
        String sql1 =  "SELECT firstname FROM user_account WHERE  account_id =1";
        String sql2 =  "SELECT firstname FROM user_account WHERE  account_id =1";

        String url = "jdbc:mysql://localhost/shakib";

        String username ="root";
        String password= "Shakib123@";

        {
            try {
                Connection con = DriverManager.getConnection(url,username,password);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
