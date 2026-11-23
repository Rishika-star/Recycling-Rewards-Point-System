
package com.recycling;

import com.recycling.service.RedemptionService;
import com.recycling.exception.InsufficientPointsException;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class RedemptionTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RedemptionService service = new RedemptionService();

        try {

            // RESIDENT INPUT
            System.out.print("Enter Membership ID: ");
            String membershipId = sc.nextLine().trim();

            int residentId = service.findResidentId(membershipId);

            if (residentId == -1) {
                System.out.println("Resident not found!");
                return;
            }

            // CHECK CURRENT BALANCE
            int balance = service.getRewardBalance(residentId);

            if (balance == -1) {
                System.out.println("Unable to fetch reward balance!");
                return;
            }

            System.out.println("Current Reward Balance: " + balance);

            // POINTS TO REDEEM
            System.out.print("Enter points to redeem: ");
            int points = Integer.parseInt(sc.nextLine().trim());

            // GENERATE TRANSACTION ID
            String transactionId = "RED-" +
                    UUID.randomUUID().toString()
                    .substring(0, 8).toUpperCase();

            // REDEEM POINTS
            boolean success = service.redeemPoints(
                    transactionId,
                    residentId,
                    points
            );

            // DISPLAY RESULT
            if (success) {

                System.out.println("\nRedemption successful!");
                System.out.println("------------------------------");
                System.out.println("Transaction ID  : " + transactionId);
                System.out.println("Membership ID   : " + membershipId);
                System.out.println("Points Redeemed : " + points);
                System.out.println("Remaining Points: " + (balance - points));
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid points entered!");

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (InsufficientPointsException e) {

            System.out.println("Redemption failed: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());

        } finally {

            sc.close();
        }
    }
}
