package com.recycling;

import com.recycling.service.ResidentService;
import com.recycling.model.Resident;

import java.sql.SQLException;
import java.util.Scanner;

public class ResidentSearchTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Membership ID to search: ");
        String membershipId = sc.nextLine();

        ResidentService residentService = new ResidentService();

        try {
            Resident resident = residentService.searchResident(membershipId);

            if (resident != null) {

                System.out.println("\nResident Found!");
                System.out.println("Resident ID: " + resident.getResidentId());
                System.out.println("Name: " + resident.getName());
                System.out.println("Membership ID: " + resident.getMembershipId());
                System.out.println("Phone: " + resident.getPhone());
                System.out.println("Reward Balance: " + resident.getRewardBalance());

            } else {
                System.out.println("Resident not found!");
            }

        } catch (SQLException e) {
            System.out.println("Unable to search resident.");
            e.printStackTrace();
        }

        sc.close();
    }
}