
package com.recycling.service;

import com.recycling.database.DBConnection;
import com.recycling.exception.InsufficientPointsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RedemptionService {

    // FIND RESIDENT BY MEMBERSHIP ID
    public int findResidentId(String membershipId) throws SQLException {

        String query = "SELECT resident_id FROM residents WHERE membership_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, membershipId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("resident_id");
                }

                return -1;
            }
        }
    }

    // GET CURRENT REWARD BALANCE
    public int getRewardBalance(int residentId) throws SQLException {

        String query = "SELECT reward_balance FROM residents WHERE resident_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, residentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("reward_balance");
                }

                return -1;
            }
        }
    }

    // REDEEM POINTS
    public boolean redeemPoints(
            String transactionId,
            int residentId,
            int points) throws SQLException, InsufficientPointsException {

        if (points <= 0) {
            throw new IllegalArgumentException(
                    "Points to redeem must be greater than zero.");
        }

        String updateQuery =
                "UPDATE residents " +
                "SET reward_balance = reward_balance - ? " +
                "WHERE resident_id = ? AND reward_balance >= ?";

        String insertQuery =
                "INSERT INTO transactions " +
                "(transaction_id, resident_id, material_id, transaction_type, weight_kg, points) " +
                "VALUES (?, ?, NULL, 'REDEMPTION', 0, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // DEDUCT POINTS ONLY IF BALANCE IS SUFFICIENT
                try (PreparedStatement updateStatement =
                        connection.prepareStatement(updateQuery)) {

                    updateStatement.setInt(1, points);
                    updateStatement.setInt(2, residentId);
                    updateStatement.setInt(3, points);

                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new InsufficientPointsException(
                                "Insufficient points or resident not found.");
                    }
                }

                // INSERT REDEMPTION TRANSACTION
                try (PreparedStatement insertStatement =
                        connection.prepareStatement(insertQuery)) {

                    insertStatement.setString(1, transactionId);
                    insertStatement.setInt(2, residentId);
                    insertStatement.setInt(3, points);

                    insertStatement.executeUpdate();
                }

                // COMMIT
                connection.commit();

                return true;

            } catch (SQLException | InsufficientPointsException e) {

                // ROLLBACK BOTH OPERATIONS
                connection.rollback();

                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
