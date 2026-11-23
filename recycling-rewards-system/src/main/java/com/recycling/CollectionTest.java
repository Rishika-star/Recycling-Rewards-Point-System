
package com.recycling;

import com.recycling.model.Material;
import com.recycling.service.CollectionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class CollectionTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CollectionService service = new CollectionService();

        try {

            // RESIDENT INPUT
            System.out.print("Enter Membership ID: ");
            String membershipId = sc.nextLine().trim();

            int residentId = service.findResidentId(membershipId);

            if (residentId == -1) {
                System.out.println("Resident not found!");
                return;
            }

            // MATERIAL INPUT
            System.out.print("Enter Material Name: ");
            String materialName = sc.nextLine().trim();

            Material material = service.findMaterial(materialName);

            if (material == null) {
                System.out.println("Material not found!");
                return;
            }

            // WEIGHT INPUT
            System.out.print("Enter Weight (kg): ");
            BigDecimal weight = new BigDecimal(sc.nextLine().trim());

            // CALCULATE POINTS
            int points = service.calculatePoints(weight, material);

            // GENERATE TRANSACTION ID
            String transactionId = "COL-" +
                    UUID.randomUUID().toString()
                    .substring(0, 8).toUpperCase();

            // RECORD COLLECTION
            boolean success = service.recordCollection(
                    transactionId,
                    residentId,
                    material.getMaterialId(),
                    weight,
                    points
            );

            // DISPLAY RESULT
            if (success) {

                System.out.println("\nCollection recorded successfully!");
                System.out.println("------------------------------");
                System.out.println("Transaction ID : " + transactionId);
                System.out.println("Membership ID  : " + membershipId);
                System.out.println("Material       : " + material.getMaterialName());
                System.out.println("Weight         : " + weight + " kg");
                System.out.println("Points Earned  : " + points);
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid weight entered!");

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());

        } finally {

            sc.close();
        }
    }
}
