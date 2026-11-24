
package com.recycling.ui;

import com.recycling.model.Material;
import com.recycling.service.MaterialService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class MaterialUI extends JPanel {

    // ---------- THEME ----------

    private final Color BACKGROUND = new Color(11, 16, 32);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);
    private final Color BORDER = new Color(42, 55, 78);

    // ---------- COMPONENTS ----------

    private MaterialService materialService;
    private JTable materialTable;
    private DefaultTableModel tableModel;

    public MaterialUI() {

        materialService = new MaterialService();

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        createUI();
        loadMaterials();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        // ---------- HEADER ----------

        JLabel heading = new JLabel("Material Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Manage recyclable materials and reward points per kg"
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
                "Material ID",
                "Material Name",
                "Points per KG"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialTable = new JTable(tableModel);

        materialTable.setRowHeight(35);
        materialTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        materialTable.setBackground(CARD);
        materialTable.setForeground(TEXT);
        materialTable.setGridColor(BORDER);
        materialTable.setSelectionBackground(new Color(40, 65, 85));
        materialTable.setSelectionForeground(TEXT);

        materialTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        materialTable.getTableHeader().setBackground(
                new Color(30, 42, 65)
        );
        materialTable.getTableHeader().setForeground(ACCENT);
        materialTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        materialTable.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );

        JScrollPane scrollPane = new JScrollPane(materialTable);
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        // ---------- BUTTONS ----------

        JButton addButton = createButton(
                "+ Add Material", ACCENT, BACKGROUND
        );

        JButton searchButton = createButton(
                "Search", CARD, TEXT
        );

        JButton updateButton = createButton(
                "Update Rate", CARD, TEXT
        );

        JButton deleteButton = createButton(
                "Delete", CARD, TEXT
        );

        JButton refreshButton = createButton(
                "Refresh", ACCENT, BACKGROUND
        );

        addButton.addActionListener(e -> addMaterial());
        searchButton.addActionListener(e -> searchMaterial());
        updateButton.addActionListener(e -> updateMaterial());
        deleteButton.addActionListener(e -> deleteMaterial());
        refreshButton.addActionListener(e -> loadMaterials());

        // ---------- BUTTON PANEL ----------

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

        // ---------- MAIN LAYOUT ----------

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
    // LOAD ALL MATERIALS
    // =====================================================

    private void loadMaterials() {

        tableModel.setRowCount(0);

        try {

            List<Material> materials =
                    materialService.getAllMaterials();

            for (Material material : materials) {

                Object[] row = {
                        material.getMaterialId(),
                        material.getMaterialName(),
                        material.getPointsPerKg()
                };

                tableModel.addRow(row);
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load materials:\n" + e.getMessage()
            );
        }
    }

    // =====================================================
    // ADD MATERIAL
    // =====================================================

    private void addMaterial() {

        JTextField nameField = new JTextField(15);
        JTextField pointsField = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));

        form.add(new JLabel("Material Name:"));
        form.add(nameField);

        form.add(new JLabel("Points per KG:"));
        form.add(pointsField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Material",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String materialName = nameField.getText().trim();
        String pointsText = pointsField.getText().trim();

        if (materialName.isEmpty() || pointsText.isEmpty()) {

            showWarning("All fields are required!");
            return;
        }

        BigDecimal points;

        try {

            points = new BigDecimal(pointsText);

            if (points.compareTo(BigDecimal.ZERO) <= 0) {
                showWarning("Points per KG must be greater than zero!");
                return;
            }

        } catch (NumberFormatException e) {

            showWarning("Please enter a valid numeric points rate!");
            return;
        }

        try {

            boolean added = materialService.addMaterial(
                    materialName,
                    points
            );

            if (added) {

                JOptionPane.showMessageDialog(
                        this,
                        "Material added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadMaterials();

            } else {

                showWarning("Material could not be added!");
            }

        } catch (SQLException e) {

            showError(
                    "Unable to add material:\n" + e.getMessage()
            );
        }
    }

    // =====================================================
    // SEARCH MATERIAL
    // =====================================================

    private void searchMaterial() {

        JTextField nameField = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));

        form.add(new JLabel("Material Name:"));
        form.add(nameField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Search Material",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String materialName = nameField.getText().trim();

        if (materialName.isEmpty()) {
            showWarning("Please enter a material name!");
            return;
        }

        try {

            Material material =
                    materialService.searchMaterial(materialName);

            if (material == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Material not found!",
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            // Select the material in the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {

                String tableName =
                        tableModel.getValueAt(i, 1).toString();

                if (tableName.equalsIgnoreCase(materialName)) {

                    materialTable.setRowSelectionInterval(i, i);

                    materialTable.scrollRectToVisible(
                            materialTable.getCellRect(i, 0, true)
                    );

                    break;
                }
            }

            showMaterialDetails(material);

        } catch (SQLException e) {

            showError(
                    "Search failed:\n" + e.getMessage()
            );
        }
    }

    // =====================================================
    // DISPLAY MATERIAL DETAILS
    // =====================================================

    private void showMaterialDetails(Material material) {

        String details =
                "Material ID: " + material.getMaterialId()
                + "\nMaterial Name: " + material.getMaterialName()
                + "\nPoints per KG: " + material.getPointsPerKg();

        JOptionPane.showMessageDialog(
                this,
                details,
                "Material Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // UPDATE MATERIAL
    // =====================================================

    private void updateMaterial() {

        int selectedRow = materialTable.getSelectedRow();

        if (selectedRow == -1) {

            showWarning(
                    "Please select a material from the table first!"
            );
            return;
        }

        String materialName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        String oldRate = tableModel
                .getValueAt(selectedRow, 2)
                .toString();

        JTextField pointsField = new JTextField(oldRate);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));

        form.add(new JLabel("New Points per KG:"));
        form.add(pointsField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Update Material - " + materialName,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newRateText = pointsField.getText().trim();

        if (newRateText.isEmpty()) {
            showWarning("Points rate cannot be empty!");
            return;
        }

        BigDecimal newRate;

        try {

            newRate = new BigDecimal(newRateText);

            if (newRate.compareTo(BigDecimal.ZERO) <= 0) {
                showWarning("Points per KG must be greater than zero!");
                return;
            }

        } catch (NumberFormatException e) {

            showWarning("Enter a valid numeric points rate!");
            return;
        }

        try {

            boolean updated = materialService.updateMaterial(
                    materialName,
                    newRate
            );

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Material rate updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadMaterials();

            } else {

                showWarning("Material could not be updated!");
            }

        } catch (SQLException e) {

            showError(
                    "Update failed:\n" + e.getMessage()
            );
        }
    }

    // =====================================================
    // DELETE MATERIAL
    // =====================================================

    private void deleteMaterial() {

        int selectedRow = materialTable.getSelectedRow();

        if (selectedRow == -1) {

            showWarning(
                    "Please select a material from the table first!"
            );
            return;
        }

        String materialName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete "
                        + materialName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean deleted =
                    materialService.deleteMaterial(materialName);

            if (deleted) {

                JOptionPane.showMessageDialog(
                        this,
                        "Material deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadMaterials();

            } else {

                showWarning("Material could not be deleted!");
            }

        } catch (SQLException e) {

            showError(
                    "Delete failed:\n" + e.getMessage()
                    + "\n\nThe material may be linked to existing transactions."
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
