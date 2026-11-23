package com.recycling;

import com.recycling.service.MaterialService;
import java.sql.SQLException;

public class MaterialDeleteTest {

    public static void main(String[] args) {

        MaterialService materialService = new MaterialService();

        try {
            boolean result = materialService.deleteMaterial("Test Material");

            if (result) {
                System.out.println("Material deleted successfully!");
            } else {
                System.out.println("Material not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting material!");
            e.printStackTrace();
        }
    }
}