package com.recycling;

import com.recycling.service.MaterialService;
import java.math.BigDecimal;
import java.sql.SQLException;

public class MaterialUpdateTest {

    public static void main(String[] args) {

        MaterialService materialService = new MaterialService();

        try {
            boolean result = materialService.updateMaterial(
                    "Plastic",
                    new BigDecimal("12.00")
            );

            if (result) {
                System.out.println("Material updated successfully!");
            } else {
                System.out.println("Material not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating material!");
            e.printStackTrace();
        }
    }
}