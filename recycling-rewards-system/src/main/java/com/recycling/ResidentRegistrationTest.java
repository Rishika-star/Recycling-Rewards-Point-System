package com.recycling;

import com.recycling.service.ResidentService;
import java.sql.SQLException;
import java.util.Scanner;

public class ResidentRegistrationTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ResidentService residentService = new ResidentService();

        System.out.print("Enter resident name: ");
        String name = sc.nextLine();

        System.out.print("Enter membership ID: ");
        String membershipId = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        try {
            residentService.registerResident(name, membershipId, phone);
        } catch (SQLException e) {
            System.out.println("Unable to register resident.");
            e.printStackTrace();
        }

        sc.close();
    }
}