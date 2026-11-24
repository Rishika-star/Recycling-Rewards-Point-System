
package com.recycling.ui;

import com.recycling.model.Resident;
import com.recycling.service.ResidentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ResidentUI extends JPanel {

    // ---------- THEME ----------

    private final Color BACKGROUND = new Color(11, 16, 32);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);
    private final Color BORDER = new Color(42, 55, 78);

    // ---------- COMPONENTS ----------

    private ResidentService residentService;
    private JTable residentTable;
    private DefaultTableModel tableModel;

    public ResidentUI() {

        residentService = new ResidentService();

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        createUI();
        loadResidents();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        // ---------- HEADER ----------

        JLabel heading = new JLabel("Resident Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Manage residents, membership details and reward balances"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        header.add(heading);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        // ---------- TABLE ----------

        String[] columns = {
                "Resident ID",
                "Name",
                "Membership ID",
                "Phone",
                "Reward Balance"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        residentTable = new JTable(tableModel);

        residentTable.setRowHeight(35);
        residentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        residentTable.setBackground(CARD);
        residentTable.setForeground(TEXT);
        residentTable.setGridColor(BORDER);
        residentTable.setSelectionBackground(new Color(40, 65, 85));
        residentTable.setSelectionForeground(TEXT);

        residentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        residentTable.getTableHeader().setBackground(
                new Color(30, 42, 65)
        );
        residentTable.getTableHeader().setForeground(ACCENT);
        residentTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        residentTable.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );

        JScrollPane scrollPane = new JScrollPane(residentTable);
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        // ---------- BUTTONS ----------

        JButton addButton = createButton("+ Add Resident", ACCENT, BACKGROUND);
        JButton searchButton = createButton("Search", CARD, TEXT);
        JButton updateButton = createButton("Update", CARD, TEXT);
        JButton deleteButton = createButton("Delete", CARD, TEXT);
        JButton refreshButton = createButton("Refresh", ACCENT, BACKGROUND);

        addButton.addActionListener(e -> addResident());
        searchButton.addActionListener(e -> searchResident());
        updateButton.addActionListener(e -> updateResident());
        deleteButton.addActionListener(e -> deleteResident());
        refreshButton.addActionListener(e -> loadResidents());

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        buttonPanel.setBackground(BACKGROUND);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // ---------- TOP PANEL ----------

        JPanel topPanel = new JPanel(new BorderLayout(15, 10));
        topPanel.setBackground(BACKGROUND);

        topPanel.add(header, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // =====================================================
    // BUTTON DESIGN
    // =====================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button = new JButton(text);

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        return button;
    }

    // =====================================================
    // LOAD ALL RESIDENTS
    // =====================================================

    private void loadResidents() {

        tableModel.setRowCount(0);

        try {

            List<Resident> residents =
                    residentService.getAllResidents();

            for (Resident resident : residents) {

                Object[] row = {
                        resident.getResidentId(),
                        resident.getName(),
                        resident.getMembershipId(),
                        resident.getPhone(),
                        resident.getRewardBalance()
                };

                tableModel.addRow(row);
            }

        } catch (SQLException e) {

            showError("Unable to load residents:\n" + e.getMessage());
        }
    }

    // =====================================================
    // ADD RESIDENT
    // =====================================================

    private void addResident() {

        JTextField nameField = new JTextField(15);
        JTextField membershipField = new JTextField(15);
        JTextField phoneField = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

        form.add(new JLabel("Name:"));
        form.add(nameField);

        form.add(new JLabel("Membership ID:"));
        form.add(membershipField);

        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Resident",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        String membershipId = membershipField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || membershipId.isEmpty() || phone.isEmpty()) {

            showWarning("All fields are required!");
            return;
        }

        try {

            residentService.registerResident(
                    name,
                    membershipId,
                    phone
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Resident registered successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadResidents();

        } catch (SQLException e) {

            showError(
                    "Unable to register resident:\n" + e.getMessage()
            );
        }
    }

    // =====================================================
    // SEARCH RESIDENT
    // =====================================================

    private void searchResident() {

        JTextField membershipField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        panel.add(new JLabel("Membership ID:"));
        panel.add(membershipField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Search Resident",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String membershipId = membershipField.getText().trim();

        if (membershipId.isEmpty()) {
            showWarning("Please enter a Membership ID!");
            return;
        }

        try {

            Resident resident =
                    residentService.searchResident(membershipId);

            if (resident == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resident not found!",
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            // Select the searched resident in the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {

                String tableMembershipId =
                        tableModel.getValueAt(i, 2).toString();

                if (tableMembershipId.equalsIgnoreCase(membershipId)) {

                    residentTable.setRowSelectionInterval(i, i);

                    residentTable.scrollRectToVisible(
                            residentTable.getCellRect(i, 0, true)
                    );

                    break;
                }
            }

            showResidentDetails(resident);

        } catch (SQLException e) {

            showError("Search failed:\n" + e.getMessage());
        }
    }

    // =====================================================
    // DISPLAY RESIDENT DETAILS
    // =====================================================

    private void showResidentDetails(Resident resident) {

        String details =
                "Resident ID: " + resident.getResidentId()
                + "\nName: " + resident.getName()
                + "\nMembership ID: " + resident.getMembershipId()
                + "\nPhone: " + resident.getPhone()
                + "\nReward Balance: " + resident.getRewardBalance();

        JOptionPane.showMessageDialog(
                this,
                details,
                "Resident Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // UPDATE RESIDENT
    // =====================================================

    private void updateResident() {

        int selectedRow = residentTable.getSelectedRow();

        if (selectedRow == -1) {

            showWarning(
                    "Please select a resident from the table first!"
            );
            return;
        }

        String membershipId = tableModel
                .getValueAt(selectedRow, 2)
                .toString();

        String oldName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        String oldPhone = tableModel
                .getValueAt(selectedRow, 3)
                .toString();

        JTextField nameField = new JTextField(oldName);
        JTextField phoneField = new JTextField(oldPhone);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));

        form.add(new JLabel("New Name:"));
        form.add(nameField);

        form.add(new JLabel("New Phone:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Update Resident - " + membershipId,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {

            showWarning("Name and Phone cannot be empty!");
            return;
        }

        try {

            boolean updated = residentService.updateResident(
                    membershipId,
                    newName,
                    newPhone
            );

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resident updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadResidents();

            } else {

                showWarning("Resident could not be updated!");
            }

        } catch (SQLException e) {

            showError("Update failed:\n" + e.getMessage());
        }
    }

    // =====================================================
    // DELETE RESIDENT
    // =====================================================

    private void deleteResident() {

        int selectedRow = residentTable.getSelectedRow();

        if (selectedRow == -1) {

            showWarning(
                    "Please select a resident from the table first!"
            );
            return;
        }

        String membershipId = tableModel
                .getValueAt(selectedRow, 2)
                .toString();

        String residentName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete "
                        + residentName + "?\nMembership ID: "
                        + membershipId,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean deleted =
                    residentService.deleteResident(membershipId);

            if (deleted) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resident deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadResidents();

            } else {

                showWarning("Resident could not be deleted!");
            }

        } catch (SQLException e) {

            showError(
                    "Delete failed:\n" + e.getMessage()
                    + "\n\nThe resident may have existing transactions."
            );
        }
    }

    // =====================================================
    // MESSAGE HELPERS
    // =====================================================

    private void showWarning(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
