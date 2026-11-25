
package com.recycling.service;

import com.recycling.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {

    // Stores all dashboard statistics
    public static class DashboardStats {

        public int totalResidents;
        public int totalMaterials;
        public double totalCollected;
        public int pointsAwarded;
    }

    // Fetch dashboard statistics from database
    public DashboardStats getDashboardStats() throws SQLException {

        DashboardStats stats = new DashboardStats();

        String query = "SELECT " +

                "(SELECT COUNT(*) FROM residents) AS total_residents, " +

                "(SELECT COUNT(*) FROM materials) AS total_materials, " +

                "(SELECT COALESCE(SUM(weight_kg), 0) " +
                "FROM transactions " +
                "WHERE transaction_type = 'COLLECTION') AS total_collected, " +

                "(SELECT COALESCE(SUM(points), 0) " +
                "FROM transactions " +
                "WHERE transaction_type = 'COLLECTION') AS points_awarded";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()
        ) {

            if (resultSet.next()) {

                stats.totalResidents =
                        resultSet.getInt("total_residents");

                stats.totalMaterials =
                        resultSet.getInt("total_materials");

                stats.totalCollected =
                        resultSet.getDouble("total_collected");

                stats.pointsAwarded =
                        resultSet.getInt("points_awarded");
            }
        }

        return stats;
    }
}
