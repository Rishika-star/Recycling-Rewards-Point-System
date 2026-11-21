
package com.recycling.service;

import com.recycling.database.DBConnection;
import com.recycling.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MaterialService {

    // ADD MATERIAL
    public boolean addMaterial(String materialName, BigDecimal pointsPerKg)
            throws SQLException {

        String query = "INSERT INTO materials (material_name, points_per_kg) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, materialName);
            statement.setBigDecimal(2, pointsPerKg);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        }
    }

    // VIEW ALL MATERIALS
    public List<Material> getAllMaterials() throws SQLException {

        List<Material> materials = new ArrayList<>();

        String query = "SELECT * FROM materials ORDER BY material_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int materialId = resultSet.getInt("material_id");
                String materialName = resultSet.getString("material_name");
                BigDecimal pointsPerKg = resultSet.getBigDecimal("points_per_kg");

                Material material = new Material(
                        materialId,
                        materialName,
                        pointsPerKg
                );

                materials.add(material);
            }
        }

        return materials;
    }

    // SEARCH MATERIAL BY NAME
    public Material searchMaterial(String materialName) throws SQLException {

        String query = "SELECT * FROM materials WHERE material_name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, materialName);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    int materialId = resultSet.getInt("material_id");
                    String name = resultSet.getString("material_name");
                    BigDecimal pointsPerKg = resultSet.getBigDecimal("points_per_kg");

                    return new Material(materialId, name, pointsPerKg);
                }
            }
        }

        return null;
    }
}
