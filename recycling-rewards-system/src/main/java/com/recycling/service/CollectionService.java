
package com.recycling.service;

import com.recycling.database.DBConnection;
import com.recycling.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollectionService {

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

    // FIND MATERIAL BY NAME
    public Material findMaterial(String materialName) throws SQLException {

        String query = "SELECT material_id, material_name, points_per_kg " +
                      "FROM materials WHERE LOWER(material_name) = LOWER(?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, materialName.trim());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Material(
                            resultSet.getInt("material_id"),
                            resultSet.getString("material_name"),
                            resultSet.getBigDecimal("points_per_kg")
                    );
                }

                return null;
            }
        }
    }

    // CALCULATE POINTS
    public int calculatePoints(BigDecimal weight, Material material) {

        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero.");
        }

        if (material == null) {
            throw new IllegalArgumentException("Material not found.");
        }

        BigDecimal points = weight.multiply(material.getPointsPerKg());

        return points.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    // RECORD COLLECTION AND UPDATE REWARD BALANCE
    public boolean recordCollection(
            String transactionId,
            int residentId,
            int materialId,
            BigDecimal weight,
            int points) throws SQLException {

        String insertQuery = "INSERT INTO transactions " +
                "(transaction_id, resident_id, material_id, transaction_type, weight_kg, points) " +
                "VALUES (?, ?, ?, 'COLLECTION', ?, ?)";

        String updateQuery = "UPDATE residents " +
                "SET reward_balance = reward_balance + ? " +
                "WHERE resident_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // INSERT COLLECTION TRANSACTION
                try (PreparedStatement insertStatement =
                        connection.prepareStatement(insertQuery)) {

                    insertStatement.setString(1, transactionId);
                    insertStatement.setInt(2, residentId);
                    insertStatement.setInt(3, materialId);
                    insertStatement.setBigDecimal(4, weight);
                    insertStatement.setInt(5, points);

                    insertStatement.executeUpdate();
                }

                // UPDATE RESIDENT BALANCE
                try (PreparedStatement updateStatement =
                        connection.prepareStatement(updateQuery)) {

                    updateStatement.setInt(1, points);
                    updateStatement.setInt(2, residentId);

                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Resident not found.");
                    }
                }

                // COMMIT BOTH OPERATIONS
                connection.commit();

                return true;

            } catch (SQLException e) {

                // ROLLBACK IF ANY OPERATION FAILS
                connection.rollback();

                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
