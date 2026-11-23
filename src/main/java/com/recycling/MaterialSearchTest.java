
package com.recycling;

import com.recycling.service.MaterialService;
import com.recycling.model.Material;
import java.sql.SQLException;

public class MaterialSearchTest {

    public static void main(String[] args) {

        MaterialService materialService = new MaterialService();

        try {
            Material material = materialService.searchMaterial("Plastic");

            if (material != null) {
                System.out.println("Material Found!");
                System.out.println("--------------------");
                System.out.println("ID: " + material.getMaterialId());
                System.out.println("Name: " + material.getMaterialName());
                System.out.println("Points per Kg: " + material.getPointsPerKg());
            } else {
                System.out.println("Material not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error searching material!");
            e.printStackTrace();
        }
    }
}
