
package com.recycling.ui;

import com.recycling.model.Resident;
import com.recycling.service.ResidentService;
import com.recycling.service.RedemptionService;
import com.recycling.exception.InsufficientPointsException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RedemptionUI extends JPanel {

    // Theme colors
    private final Color BG = new Color(11, 16, 32);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);

    private final ResidentService residentService = new ResidentService();
    private final RedemptionService redemptionService = new RedemptionService();

    private JComboBox<Resident> residentCombo;
    private JTextField transactionIdField;
    private JTextField pointsField;

    private JLabel balanceLabel;
    private JLabel remainingLabel;

    private int currentBalance = 0;

    public RedemptionUI() {

        setLayout(new BorderLayout());
        setBackground(BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);

        loadResidents();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(25, 30, 15, 30));

        JLabel title = new JLabel("Reward Redemption");

        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Redeem earned points for rewards"
        );

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        return header;
    }

    // ================= FORM =================

    private JPanel createForm() {

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(new EmptyBorder(10, 30, 30, 30));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(30, 35, 30, 35));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // FORM TITLE

        JLabel formTitle = new JLabel("Redeem Reward Points");

        formTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formTitle.setForeground(TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        card.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // TRANSACTION ID

        gbc.gridy++;
        gbc.gridx = 0;

        card.add(createLabel("Transaction ID"), gbc);

        transactionIdField = createTextField("e.g. D001");

        gbc.gridx = 1;

        card.add(transactionIdField, gbc);

        // RESIDENT

        gbc.gridy++;
        gbc.gridx = 0;

        card.add(createLabel("Select Resident"), gbc);

        residentCombo = new JComboBox<>();
        styleCombo(residentCombo);

        gbc.gridx = 1;

        card.add(residentCombo, gbc);

        // AVAILABLE BALANCE

        gbc.gridy++;
        gbc.gridx = 0;

        card.add(createLabel("Available Points"), gbc);

        balanceLabel = new JLabel("0 Points");

        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        balanceLabel.setForeground(ACCENT);

        gbc.gridx = 1;

        card.add(balanceLabel, gbc);

        // POINTS TO REDEEM

        gbc.gridy++;
        gbc.gridx = 0;

        card.add(createLabel("Points to Redeem"), gbc);

        pointsField = createTextField("Enter points");

        gbc.gridx = 1;

        card.add(pointsField, gbc);

        // REMAINING BALANCE PREVIEW

        gbc.gridy++;
        gbc.gridx = 0;

        card.add(createLabel("Remaining Balance"), gbc);

        remainingLabel = new JLabel("0 Points");

        remainingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        remainingLabel.setForeground(TEXT);

        gbc.gridx = 1;

        card.add(remainingLabel, gbc);

        // BUTTONS

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 5)
        );

        buttonPanel.setBackground(CARD);

        JButton redeemButton = new JButton("Redeem Points");
        JButton resetButton = new JButton("Reset");

        styleButton(redeemButton);
        styleButton(resetButton);

        buttonPanel.add(redeemButton);
        buttonPanel.add(resetButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        card.add(buttonPanel, gbc);

        wrapper.add(card);

        // LISTENERS

        residentCombo.addActionListener(e -> updateBalance());

        pointsField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {
                        updateRemainingBalance();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {
                        updateRemainingBalance();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {
                        updateRemainingBalance();
                    }
                }
        );

        redeemButton.addActionListener(e -> redeemPoints());

        resetButton.addActionListener(e -> resetForm());

        return wrapper;
    }

    // ================= LOAD RESIDENTS =================

    private void loadResidents() {

        try {

            List<Resident> residents =
                    residentService.getAllResidents();

            residentCombo.removeAllItems();

            for (Resident resident : residents) {
                residentCombo.addItem(resident);
            }

            if (residentCombo.getItemCount() > 0) {
                residentCombo.setSelectedIndex(0);
                updateBalance();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load residents.\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= UPDATE BALANCE =================

    private void updateBalance() {

        Resident selectedResident =
                (Resident) residentCombo.getSelectedItem();

        if (selectedResident == null) {

            currentBalance = 0;

            balanceLabel.setText("0 Points");
            remainingLabel.setText("0 Points");

            return;
        }

        try {

            int residentId = redemptionService.findResidentId(
                    selectedResident.getMembershipId()
            );

            if (residentId == -1) {

                currentBalance = 0;
                balanceLabel.setText("Resident Not Found");
                remainingLabel.setText("0 Points");

                return;
            }

            currentBalance = redemptionService.getRewardBalance(residentId);

            balanceLabel.setText(currentBalance + " Points");

            updateRemainingBalance();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to fetch reward balance.\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= REMAINING BALANCE =================

    private void updateRemainingBalance() {

        String pointsText = pointsField.getText().trim();

        if (pointsText.isEmpty()) {

            remainingLabel.setText(currentBalance + " Points");
            remainingLabel.setForeground(TEXT);

            return;
        }

        try {

            int points = Integer.parseInt(pointsText);

            if (points <= 0) {

                remainingLabel.setText("Invalid Points");
                remainingLabel.setForeground(Color.RED);

                return;
            }

            int remaining = currentBalance - points;

            remainingLabel.setText(remaining + " Points");

            if (remaining < 0) {

                remainingLabel.setForeground(Color.RED);

            } else {

                remainingLabel.setForeground(ACCENT);
            }

        } catch (NumberFormatException e) {

            remainingLabel.setText("Invalid Input");
            remainingLabel.setForeground(Color.RED);
        }
    }

    // ================= REDEEM =================

    private void redeemPoints() {

        try {

            // TRANSACTION ID

            String transactionId =
                    transactionIdField.getText().trim();

            if (transactionId.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a Transaction ID.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // RESIDENT

            Resident selectedResident =
                    (Resident) residentCombo.getSelectedItem();

            if (selectedResident == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a resident.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // POINTS

            String pointsText = pointsField.getText().trim();

            if (pointsText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the points to redeem.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int points = Integer.parseInt(pointsText);

            if (points <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Points must be greater than zero.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // RESIDENT ID

            int residentId = redemptionService.findResidentId(
                    selectedResident.getMembershipId()
            );

            if (residentId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resident not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // CHECK BALANCE

            currentBalance =
                    redemptionService.getRewardBalance(residentId);

            if (points > currentBalance) {

                JOptionPane.showMessageDialog(
                        this,
                        "Insufficient reward points!\n\n"
                                + "Available: " + currentBalance + "\n"
                                + "Requested: " + points,
                        "Insufficient Points",
                        JOptionPane.WARNING_MESSAGE
                );

                updateBalance();

                return;
            }

            // CONFIRM REDEMPTION

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to redeem "
                            + points + " points?\n\n"
                            + "Resident: " + selectedResident.getName() + "\n"
                            + "Current Balance: " + currentBalance + "\n"
                            + "Remaining Balance: "
                            + (currentBalance - points),
                    "Confirm Redemption",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // DATABASE REDEMPTION

            boolean success = redemptionService.redeemPoints(
                    transactionId,
                    residentId,
                    points
            );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Points redeemed successfully!\n\n"
                                + "Resident: "
                                + selectedResident.getName() + "\n"
                                + "Points Redeemed: " + points + "\n"
                                + "Remaining Balance: "
                                + (currentBalance - points),
                        "Redemption Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                resetForm();
                updateBalance();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid whole numbers for points.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (InsufficientPointsException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Insufficient Points",
                    JOptionPane.WARNING_MESSAGE
            );

            updateBalance();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error during redemption:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= RESET =================

    private void resetForm() {

        transactionIdField.setText("");
        pointsField.setText("");

        updateBalance();
    }

    // ================= UI HELPERS =================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return label;
    }

    private JTextField createTextField(String placeholder) {

        JTextField field = new JTextField();

        field.setPreferredSize(new Dimension(280, 38));

        field.setBackground(new Color(30, 40, 65));
        field.setForeground(TEXT);
        field.setCaretColor(ACCENT);

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(55, 70, 100)
                        ),
                        new EmptyBorder(8, 10, 8, 10)
                )
        );

        field.setToolTipText(placeholder);

        return field;
    }

    private void styleCombo(JComboBox<?> combo) {

        combo.setPreferredSize(new Dimension(280, 38));
        combo.setBackground(new Color(30, 40, 65));
        combo.setForeground(TEXT);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (button.getText().equals("Redeem Points")) {

            button.setBackground(ACCENT);
            button.setForeground(BG);

        } else {

            button.setBackground(new Color(40, 50, 75));
            button.setForeground(TEXT);
        }
    }
}
