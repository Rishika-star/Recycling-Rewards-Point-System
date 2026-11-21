package com.recycling;

import com.recycling.service.MaterialService;
import com.recycling.model.Material;

import java.sql.SQLException;
import java.util.List;

public class MaterialViewTest {

    public static void main(String[] args) {

        MaterialService materialService = new MaterialService();

        try {
            List<Material> materials = materialService.getAllMaterials();

            System.out.println("===== MATERIAL LIST =====");

            for (Material material : materials) {
                System.out.println(
                        material.getMaterialId() + " | " +
                        material.getMaterialName() + " | " +
                        material.getPointsPerKg() + " points/kg"
                );
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving materials!");
            e.printStackTrace();
        }
    }
}