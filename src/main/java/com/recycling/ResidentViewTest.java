package com.recycling;

import com.recycling.service.ResidentService;
import com.recycling.model.Resident;

import java.sql.SQLException;
import java.util.List;

public class ResidentViewTest {

    public static void main(String[] args) {

        ResidentService residentService = new ResidentService();

        try {

            List<Resident> residents = residentService.getAllResidents();

            for (Resident resident : residents) {

                System.out.println("Resident ID: " + resident.getResidentId());
                System.out.println("Name: " + resident.getName());
                System.out.println("Membership ID: " + resident.getMembershipId());
                System.out.println("Phone: " + resident.getPhone());
                System.out.println("Reward Balance: " + resident.getRewardBalance());

                System.out.println("--------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Unable to fetch residents.");
            e.printStackTrace();
        }
    }
}