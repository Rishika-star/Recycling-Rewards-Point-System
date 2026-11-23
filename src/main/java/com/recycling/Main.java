package com.recycling;

import com.recycling.service.ResidentService;
import com.recycling.service.MaterialService;
import com.recycling.service.CollectionService;
import com.recycling.service.RedemptionService;
import com.recycling.service.ReportService;

import com.recycling.model.Resident;
import com.recycling.model.Material;

import com.recycling.exception.InsufficientPointsException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final ResidentService residentService = new ResidentService();
    private static final MaterialService materialService = new MaterialService();
    private static final CollectionService collectionService = new CollectionService();
    private static final RedemptionService redemptionService = new RedemptionService();
    private static final ReportService reportService = new ReportService();

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n========================================");
            System.out.println("       RECYCLING REWARDS SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Resident Management");
            System.out.println("2. Material Management");
            System.out.println("3. Collection Management");
            System.out.println("4. Redemption Management");
            System.out.println("5. Reports & Transactions");
            System.out.println("6. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    residentMenu();
                    break;

                case 2:
                    materialMenu();
                    break;

                case 3:
                    collectionMenu();
                    break;

                case 4:
                    redemptionMenu();
                    break;

                case 5:
                    reportMenu();
                    break;

                case 6:
                    System.out.println("Thank you for using Recycling Rewards System!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 6);

        sc.close();
    }

    // =========================================
    // RESIDENT MANAGEMENT
    // =========================================

    private static void residentMenu() {

        int choice;

        do {
            System.out.println("\n===== RESIDENT MANAGEMENT =====");
            System.out.println("1. Register Resident");
            System.out.println("2. View All Residents");
            System.out.println("3. Search Resident");
            System.out.println("4. Update Resident");
            System.out.println("5. Delete Resident");
            System.out.println("6. Back to Main Menu");

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {

                    case 1:
                        registerResident();
                        break;

                    case 2:
                        viewResidents();
                        break;

                    case 3:
                        searchResident();
                        break;

                    case 4:
                        updateResident();
                        break;

                    case 5:
                        deleteResident();
                        break;

                    case 6:
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        } while (choice != 6);
    }

    private static void registerResident() throws SQLException {

        System.out.println("\n--- Register Resident ---");

        String name = readString("Enter Name: ");
        String membershipId = readString("Enter Membership ID: ");
        String phone = readString("Enter Phone: ");

        residentService.registerResident(name, membershipId, phone);
    }

    private static void viewResidents() throws SQLException {

        System.out.println("\n--- All Residents ---");

        var residents = residentService.getAllResidents();

        if (residents.isEmpty()) {
            System.out.println("No residents found.");
            return;
        }

        for (Resident resident : residents) {

            System.out.println("--------------------------------");
            System.out.println("Name          : " + resident.getName());
            System.out.println("Membership ID : " + resident.getMembershipId());
            System.out.println("Phone         : " + resident.getPhone());
            System.out.println("Reward Balance: " + resident.getRewardBalance());
        }
    }

    private static void searchResident() throws SQLException {

        String membershipId = readString("Enter Membership ID: ");

        Resident resident = residentService.searchResident(membershipId);

        if (resident == null) {
            System.out.println("Resident not found.");
        } else {
            displayResident(resident);
        }
    }

    private static void updateResident() throws SQLException {

        String membershipId = readString("Enter Membership ID: ");

        Resident resident = residentService.searchResident(membershipId);

        if (resident == null) {
            System.out.println("Resident not found.");
            return;
        }

        System.out.println("Current Name: " + resident.getName());
        System.out.println("Current Phone: " + resident.getPhone());

        String newName = readString("Enter New Name: ");
        String newPhone = readString("Enter New Phone: ");

        boolean updated = residentService.updateResident(
                membershipId, newName, newPhone);

        System.out.println(updated
                ? "Resident updated successfully!"
                : "Resident update failed.");
    }

    private static void deleteResident() throws SQLException {

        String membershipId = readString("Enter Membership ID to delete: ");

        Resident resident = residentService.searchResident(membershipId);

        if (resident == null) {
            System.out.println("Resident not found.");
            return;
        }

        displayResident(resident);

        String confirm = readString("Are you sure you want to delete? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {

            boolean deleted = residentService.deleteResident(membershipId);

            System.out.println(deleted
                    ? "Resident deleted successfully!"
                    : "Resident could not be deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void displayResident(Resident resident) {

        System.out.println("\n--- Resident Details ---");
        System.out.println("Name          : " + resident.getName());
        System.out.println("Membership ID : " + resident.getMembershipId());
        System.out.println("Phone         : " + resident.getPhone());
        System.out.println("Reward Balance: " + resident.getRewardBalance());
    }

    // =========================================
    // MATERIAL MANAGEMENT
    // =========================================

    private static void materialMenu() {

        int choice;

        do {
            System.out.println("\n===== MATERIAL MANAGEMENT =====");
            System.out.println("1. Add Material");
            System.out.println("2. View All Materials");
            System.out.println("3. Search Material");
            System.out.println("4. Update Material Rate");
            System.out.println("5. Delete Material");
            System.out.println("6. Back to Main Menu");

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {

                    case 1:
                        addMaterial();
                        break;

                    case 2:
                        viewMaterials();
                        break;

                    case 3:
                        searchMaterial();
                        break;

                    case 4:
                        updateMaterial();
                        break;

                    case 5:
                        deleteMaterial();
                        break;

                    case 6:
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        } while (choice != 6);
    }

    private static void addMaterial() throws SQLException {

        String materialName = readString("Enter Material Name: ");
        BigDecimal pointsPerKg = readDecimal("Enter Points per Kg: ");

        if (pointsPerKg.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Points per Kg must be greater than zero.");
            return;
        }

        boolean added = materialService.addMaterial(materialName, pointsPerKg);

        System.out.println(added
                ? "Material added successfully!"
                : "Material could not be added.");
    }

    private static void viewMaterials() throws SQLException {

        System.out.println("\n--- Available Materials ---");

        var materials = materialService.getAllMaterials();

        if (materials.isEmpty()) {
            System.out.println("No materials found.");
            return;
        }

        for (Material material : materials) {

            System.out.println("--------------------------------");
            System.out.println("Material : " + material.getMaterialName());
            System.out.println("Points/Kg: " + material.getPointsPerKg());
        }
    }

    private static void searchMaterial() throws SQLException {

        String materialName = readString("Enter Material Name: ");

        Material material = materialService.searchMaterial(materialName);

        if (material == null) {
            System.out.println("Material not found.");
        } else {
            System.out.println("\nMaterial Name : " + material.getMaterialName());
            System.out.println("Points per Kg : " + material.getPointsPerKg());
        }
    }

    private static void updateMaterial() throws SQLException {

        String materialName = readString("Enter Material Name: ");

        Material material = materialService.searchMaterial(materialName);

        if (material == null) {
            System.out.println("Material not found.");
            return;
        }

        System.out.println("Current Rate: " + material.getPointsPerKg());

        BigDecimal newRate = readDecimal("Enter New Points per Kg: ");

        if (newRate.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Rate must be greater than zero.");
            return;
        }

        boolean updated = materialService.updateMaterial(materialName, newRate);

        System.out.println(updated
                ? "Material rate updated successfully!"
                : "Material update failed.");
    }

    private static void deleteMaterial() throws SQLException {

        String materialName = readString("Enter Material Name to delete: ");

        Material material = materialService.searchMaterial(materialName);

        if (material == null) {
            System.out.println("Material not found.");
            return;
        }

        System.out.println("Material: " + material.getMaterialName());
        System.out.println("Rate: " + material.getPointsPerKg());

        String confirm = readString("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {

            boolean deleted = materialService.deleteMaterial(materialName);

            System.out.println(deleted
                    ? "Material deleted successfully!"
                    : "Material could not be deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // =========================================
    // COLLECTION MANAGEMENT
    // =========================================

    private static void collectionMenu() {

        System.out.println("\n===== COLLECTION MANAGEMENT =====");

        try {

            String membershipId = readString("Enter Membership ID: ");

            int residentId = collectionService.findResidentId(membershipId);

            if (residentId == -1) {
                System.out.println("Resident not found.");
                return;
            }

            String materialName = readString("Enter Material Name: ");

            Material material = collectionService.findMaterial(materialName);

            if (material == null) {
                System.out.println("Material not found.");
                return;
            }

            BigDecimal weight = readDecimal("Enter Weight in Kg: ");

            int points = collectionService.calculatePoints(weight, material);

            String transactionId = "COL-" +
                    UUID.randomUUID().toString()
                            .substring(0, 8)
                            .toUpperCase();

            boolean recorded = collectionService.recordCollection(
                    transactionId,
                    residentId,
                    material.getMaterialId(),
                    weight,
                    points
            );

            if (recorded) {

                System.out.println("\nCollection recorded successfully!");
                System.out.println("------------------------------");
                System.out.println("Transaction ID : " + transactionId);
                System.out.println("Material       : " + material.getMaterialName());
                System.out.println("Weight         : " + weight + " Kg");
                System.out.println("Points Earned  : " + points);
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // =========================================
    // REDEMPTION MANAGEMENT
    // =========================================

    private static void redemptionMenu() {

        System.out.println("\n===== REDEMPTION MANAGEMENT =====");

        try {

            String membershipId = readString("Enter Membership ID: ");

            int residentId = redemptionService.findResidentId(membershipId);

            if (residentId == -1) {
                System.out.println("Resident not found.");
                return;
            }

            int balance = redemptionService.getRewardBalance(residentId);

            System.out.println("Current Reward Balance: " + balance);

            if (balance <= 0) {
                System.out.println("No points available for redemption.");
                return;
            }

            int points = readInt("Enter points to redeem: ");

            String transactionId = "RED-" +
                    UUID.randomUUID().toString()
                            .substring(0, 8)
                            .toUpperCase();

            boolean redeemed = redemptionService.redeemPoints(
                    transactionId, residentId, points);

            if (redeemed) {

                int remainingBalance =
                        redemptionService.getRewardBalance(residentId);

                System.out.println("\nRedemption successful!");
                System.out.println("------------------------------");
                System.out.println("Transaction ID  : " + transactionId);
                System.out.println("Points Redeemed : " + points);
                System.out.println("Remaining Points: " + remainingBalance);
            }

        } catch (SQLException | InsufficientPointsException
                 | IllegalArgumentException e) {

            System.out.println("Redemption Error: " + e.getMessage());
        }
    }

    // =========================================
    // REPORT MANAGEMENT
    // =========================================

    private static void reportMenu() {

        int choice;

        do {
            System.out.println("\n===== REPORTS & TRANSACTIONS =====");
            System.out.println("1. View All Transactions");
            System.out.println("2. View Resident Transaction History");
            System.out.println("3. Transaction Summary");
            System.out.println("4. Back to Main Menu");

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {

                    case 1:
                        reportService.viewAllTransactions();
                        break;

                    case 2:
                        String membershipId =
                                readString("Enter Membership ID: ");

                        reportService.viewResidentTransactions(membershipId);
                        break;

                    case 3:
                        reportService.transactionSummary();
                        break;

                    case 4:
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        } while (choice != 4);
    }

    // =========================================
    // INPUT HELPERS
    // =========================================

    private static String readString(String message) {

        System.out.print(message);
        return sc.nextLine().trim();
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static BigDecimal readDecimal(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}