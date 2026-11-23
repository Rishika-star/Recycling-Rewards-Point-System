package com.recycling;

import com.recycling.service.ResidentService;
import java.sql.SQLException;

public class ResidentDeleteTest {

    public static void main(String[] args) {

        ResidentService service = new ResidentService();

        try {
            boolean deleted = service.deleteResident("RR999");

            if (deleted) {
                System.out.println("Resident deleted successfully!");
            } else {
                System.out.println("Resident not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}