package com.recycling;

import com.recycling.service.ReportService;
import java.util.Scanner;
import java.sql.SQLException;

public class ReportTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ReportService reportService = new ReportService();

        int choice;

        do {
            System.out.println("\n===== TRANSACTION REPORT MENU =====");
            System.out.println("1. View All Transactions");
            System.out.println("2. View Resident Transaction History");
            System.out.println("3. Transaction Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {

                    case 1:
                        reportService.viewAllTransactions();
                        break;

                    case 2:
                        System.out.print("Enter Membership ID: ");
                        String membershipId = sc.nextLine();

                        reportService.viewResidentTransactions(membershipId);
                        break;

                    case 3:
                        reportService.transactionSummary();
                        break;

                    case 4:
                        System.out.println("Exiting Report Module...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        } while (choice != 4);

        sc.close();
    }
}