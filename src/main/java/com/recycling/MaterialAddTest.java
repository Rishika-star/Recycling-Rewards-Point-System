
package com.recycling;

import com.recycling.service.MaterialService;
import java.math.BigDecimal;
import java.sql.SQLException;

public class MaterialAddTest {

    public static void main(String[] args) {

        MaterialService materialService = new MaterialService();

        try {
            boolean result = materialService.addMaterial(
                    "Test Material",
                    new BigDecimal("1.00")
            );

            if (result) {
                System.out.println("Material added successfully!");
            } else {
                System.out.println("Material could not be added.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding material!");
            e.printStackTrace();
        }
    }
}
