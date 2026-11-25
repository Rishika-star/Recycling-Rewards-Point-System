
package com.recycling.ui;

import com.recycling.model.Resident;
import com.recycling.model.Material;
import com.recycling.service.ResidentService;
import com.recycling.service.MaterialService;
import com.recycling.service.CollectionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CollectionUI extends JPanel {

    // Theme colors
    private final Color BG = new Color(11, 16, 32);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);

    private final ResidentService residentService = new ResidentService();
    private final MaterialService materialService = new MaterialService();
    private final CollectionService collectionService = new CollectionService();

    private JComboBox<Resident> residentCombo;
    private JComboBox<Material> materialCombo;
    private JTextField transactionIdField;
    private JTextField weightField;
    private JLabel pointsLabel;

    public CollectionUI() {
        setLayout(new BorderLayout());
        setBackground(BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);

        loadDropdownData();
    }

    // ================= HEADER =================

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(25, 30, 15, 30));

        JLabel title = new JLabel("Collection Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Record recyclable waste and reward residents"
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

        JLabel formTitle = new JLabel("New Collection Entry");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formTitle.setForeground(TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // Transaction ID
        gbc.gridy++;
        gbc.gridx = 0;
        card.add(createLabel("Transaction ID"), gbc);

        transactionIdField = createTextField("e.g. C001");
        gbc.gridx = 1;
        card.add(transactionIdField, gbc);

        // Resident
        gbc.gridy++;
        gbc.gridx = 0;
        card.add(createLabel("Select Resident"), gbc);

        residentCombo = new JComboBox<>();
        styleCombo(residentCombo);
        residentCombo.setRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus
        );

        if (value instanceof Resident) {
            Resident resident = (Resident) value;

            setText(resident.getName() + " (" +
                    resident.getMembershipId() + ")");
        }

        return this;
    }
});
        gbc.gridx = 1;
        card.add(residentCombo, gbc);

        // Material
        gbc.gridy++;
        gbc.gridx = 0;
        card.add(createLabel("Recycling Material"), gbc);

        materialCombo = new JComboBox<>();
        styleCombo(materialCombo);
        materialCombo.setRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus
        );

        if (value instanceof Material) {
            Material material = (Material) value;

            setText(material.getMaterialName());
        }

        return this;
    }
});
        gbc.gridx = 1;
        card.add(materialCombo, gbc);

        // Weight
        gbc.gridy++;
        gbc.gridx = 0;
        card.add(createLabel("Weight (kg)"), gbc);

        weightField = createTextField("Enter weight in kg");
        gbc.gridx = 1;
        card.add(weightField, gbc);

        // Reward Points Preview
        gbc.gridy++;
        gbc.gridx = 0;
        card.add(createLabel("Estimated Reward Points"), gbc);

        pointsLabel = new JLabel("0 Points");
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        pointsLabel.setForeground(ACCENT);

        gbc.gridx = 1;
        card.add(pointsLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));

buttonPanel.setBackground(CARD);

JButton submitButton = new JButton("Submit Collection");
styleButton(submitButton);

buttonPanel.add(submitButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        card.add(buttonPanel, gbc);

        wrapper.add(card);

        // Listeners
        materialCombo.addActionListener(e -> updatePointsPreview());

        weightField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePointsPreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePointsPreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePointsPreview();
            }
        });

        submitButton.addActionListener(e -> recordCollection());

        return wrapper;
    }

    // ================= DATABASE DATA =================

    private void loadDropdownData() {
        try {
            List<Resident> residents = residentService.getAllResidents();

            residentCombo.removeAllItems();

            for (Resident resident : residents) {
                residentCombo.addItem(resident);
            }

            List<Material> materials = materialService.getAllMaterials();

            materialCombo.removeAllItems();

            for (Material material : materials) {
                materialCombo.addItem(material);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load residents or materials.\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= POINT CALCULATION =================

    private void updatePointsPreview() {
        try {
            Material material = (Material) materialCombo.getSelectedItem();

            if (material == null || weightField.getText().trim().isEmpty()) {
                pointsLabel.setText("0 Points");
                return;
            }

            BigDecimal weight = new BigDecimal(weightField.getText().trim());

            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                pointsLabel.setText("0 Points");
                return;
            }

            int points = collectionService.calculatePoints(weight, material);

            pointsLabel.setText(points + " Points");

        } catch (NumberFormatException e) {
            pointsLabel.setText("Invalid Weight");
        } catch (Exception e) {
            pointsLabel.setText("0 Points");
        }
    }

    // ================= RECORD COLLECTION =================

    private void recordCollection() {
        try {
            String transactionId = transactionIdField.getText().trim();

            if (transactionId.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a Transaction ID.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Resident selectedResident =
                    (Resident) residentCombo.getSelectedItem();

            Material selectedMaterial =
                    (Material) materialCombo.getSelectedItem();

            if (selectedResident == null || selectedMaterial == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a resident and material.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String weightText = weightField.getText().trim();

            if (weightText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the collected weight.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            BigDecimal weight = new BigDecimal(weightText);

            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Weight must be greater than 0 kg.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int residentId = collectionService.findResidentId(
                    selectedResident.getMembershipId()
            );

            if (residentId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Resident not found in database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Material material = collectionService.findMaterial(
                    selectedMaterial.getMaterialName()
            );

            if (material == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Material not found in database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int points = collectionService.calculatePoints(weight, material);

            boolean success = collectionService.recordCollection(
                    transactionId,
                    residentId,
                    material.getMaterialId(),
                    weight,
                    points
            );

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Collection recorded successfully!\n\n"
                                + "Resident: " + selectedResident.getName() + "\n"
                                + "Material: " + material.getMaterialName() + "\n"
                                + "Weight: " + weight + " kg\n"
                                + "Reward Earned: " + points + " points",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

                resetForm();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric weight.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error while recording collection:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= RESET =================

    private void resetForm() {
        transactionIdField.setText("");
        weightField.setText("");
        pointsLabel.setText("0 Points");

        if (residentCombo.getItemCount() > 0) {
            residentCombo.setSelectedIndex(0);
        }

        if (materialCombo.getItemCount() > 0) {
            materialCombo.setSelectedIndex(0);
        }
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
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 70, 100)),
                new EmptyBorder(8, 10, 8, 10)
        ));
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

        button.setEnabled(true);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        if (button.getText().equals("Record Collection")) {
            button.setBackground(ACCENT);
            button.setForeground(BG);
        } else {
            button.setBackground(new Color(40, 50, 75));
            button.setForeground(TEXT);
        }
    }
}
