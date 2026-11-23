
package com.recycling.service;

import com.recycling.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {

    // VIEW ALL TRANSACTIONS
    public void viewAllTransactions() throws SQLException {

        String query = "SELECT t.transaction_id, r.membership_id, r.name, " +
                "m.material_name, t.transaction_type, t.weight_kg, " +
                "t.points, t.transaction_date " +
                "FROM transactions t " +
                "JOIN residents r ON t.resident_id = r.resident_id " +
                "LEFT JOIN materials m ON t.material_id = m.material_id " +
                "ORDER BY t.transaction_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n========== ALL TRANSACTIONS ==========");

            while (resultSet.next()) {

                System.out.println("-----------------------------------");
                System.out.println("Transaction ID : " +
                        resultSet.getString("transaction_id"));

                System.out.println("Membership ID  : " +
                        resultSet.getString("membership_id"));

                System.out.println("Resident Name  : " +
                        resultSet.getString("name"));

                System.out.println("Material       : " +
                        resultSet.getString("material_name"));

                System.out.println("Type           : " +
                        resultSet.getString("transaction_type"));

                System.out.println("Weight         : " +
                        resultSet.getBigDecimal("weight_kg") + " kg");

                System.out.println("Points         : " +
                        resultSet.getInt("points"));

                System.out.println("Date           : " +
                        resultSet.getTimestamp("transaction_date"));
            }
        }
    }

    // VIEW TRANSACTIONS OF A RESIDENT
    public void viewResidentTransactions(String membershipId)
            throws SQLException {

        String query = "SELECT t.transaction_id, m.material_name, " +
                "t.transaction_type, t.weight_kg, t.points, " +
                "t.transaction_date " +
                "FROM transactions t " +
                "JOIN residents r ON t.resident_id = r.resident_id " +
                "LEFT JOIN materials m ON t.material_id = m.material_id " +
                "WHERE r.membership_id = ? " +
                "ORDER BY t.transaction_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, membershipId);

            try (ResultSet resultSet = statement.executeQuery()) {

                System.out.println("\n===== TRANSACTION HISTORY: " +
                        membershipId + " =====");

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    System.out.println("-----------------------------------");
                    System.out.println("Transaction ID : " +
                            resultSet.getString("transaction_id"));

                    System.out.println("Material       : " +
                            resultSet.getString("material_name"));

                    System.out.println("Type           : " +
                            resultSet.getString("transaction_type"));

                    System.out.println("Weight         : " +
                            resultSet.getBigDecimal("weight_kg") + " kg");

                    System.out.println("Points         : " +
                            resultSet.getInt("points"));

                    System.out.println("Date           : " +
                            resultSet.getTimestamp("transaction_date"));
                }

                if (!found) {
                    System.out.println("No transactions found.");
                }
            }
        }
    }

    // TRANSACTION SUMMARY
    public void transactionSummary() throws SQLException {

        String query = "SELECT " +
                "COUNT(*) AS total_transactions, " +
                "SUM(CASE WHEN transaction_type = 'COLLECTION' " +
                "THEN 1 ELSE 0 END) AS total_collections, " +
                "SUM(CASE WHEN transaction_type = 'REDEMPTION' " +
                "THEN 1 ELSE 0 END) AS total_redemptions, " +
                "SUM(CASE WHEN transaction_type = 'COLLECTION' " +
                "THEN points ELSE 0 END) AS points_earned, " +
                "SUM(CASE WHEN transaction_type = 'REDEMPTION' " +
                "THEN points ELSE 0 END) AS points_redeemed " +
                "FROM transactions";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                System.out.println("\n========== TRANSACTION SUMMARY ==========");

                System.out.println("Total Transactions : " +
                        resultSet.getInt("total_transactions"));

                System.out.println("Total Collections : " +
                        resultSet.getInt("total_collections"));

                System.out.println("Total Redemptions: " +
                        resultSet.getInt("total_redemptions"));

                System.out.println("Points Earned     : " +
                        resultSet.getInt("points_earned"));

                System.out.println("Points Redeemed   : " +
                        resultSet.getInt("points_redeemed"));
            }
        }
    }
}
