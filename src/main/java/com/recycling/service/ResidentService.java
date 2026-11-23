package com.recycling.service;

import com.recycling.database.DBConnection;
import com.recycling.model.Resident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class ResidentService {

    // 1. Register Resident
    public void registerResident(String name, String membershipId, String phone)
            throws SQLException {

        String query = "INSERT INTO residents (name, membership_id, phone) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, membershipId);
            ps.setString(3, phone);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Resident registered successfully!");
            }
        }
    }

    // 2. View All Residents
    public List<Resident> getAllResidents() throws SQLException {

        String query = "SELECT resident_id, name, membership_id, phone, reward_balance FROM residents";

        List<Resident> residents = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Resident resident = new Resident(
                    rs.getInt("resident_id"),
                    rs.getString("name"),
                    rs.getString("membership_id"),
                    rs.getString("phone"),
                    rs.getInt("reward_balance")
                );

                residents.add(resident);
            }

            return residents;
        }
    }

    // 3. Search Resident
    public Resident searchResident(String membershipId) throws SQLException {

        String query = "SELECT * FROM residents WHERE membership_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, membershipId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Resident resident = new Resident(
                        rs.getInt("resident_id"),
                        rs.getString("name"),
                        rs.getString("membership_id"),
                        rs.getString("phone"),
                        rs.getInt("reward_balance")
                    );

                    return resident;
                }

                return null;
            }
        }
    }
        // 4. Update Resident
    public boolean updateResident(String membershipId, String name, String phone) throws SQLException {

        String query = "UPDATE residents SET name = ?, phone = ? WHERE membership_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, membershipId);

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated > 0;
        }
    }
    // 5. Delete Resident
public boolean deleteResident(String membershipId) throws SQLException {

    String query = "DELETE FROM residents WHERE membership_id = ?";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

        ps.setString(1, membershipId);

        int rowsDeleted = ps.executeUpdate();

        return rowsDeleted > 0;
    }
}
}