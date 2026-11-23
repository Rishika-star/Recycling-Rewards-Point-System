package com.recycling;

import com.recycling.service.ResidentService;
import java.sql.SQLException;

public class ResidentUpdateTest {

    public static void main(String[] args) {

        ResidentService service = new ResidentService();

        try {
            boolean updated = service.updateResident(
                "RR103",
                "Rahul Sharma Updated",
                "9999999999"
            );

            if (updated) {
                System.out.println("Resident updated successfully!");
            } else {
                System.out.println("Resident not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}