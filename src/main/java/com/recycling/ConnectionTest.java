package com.recycling;

import com.recycling.database.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null) {
                System.out.println("Connection Successful!");
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}