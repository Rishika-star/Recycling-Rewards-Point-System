package com.recycling.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsUI extends JPanel {

    // Midnight Aurora Theme
    private final Color BG = new Color(11, 16, 32);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);
    private final Color BORDER = new Color(45, 57, 82);

    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ReportsUI() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BG);

        mainPanel.add(createStatisticsPanel(), BorderLayout.NORTH);
        mainPanel.add(createReportTable(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG);

        JLabel title = new JLabel("Reports & Analytics");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel(
                "Monitor recycling activity and reward performance"
        );
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setForeground(TEXT);
        refreshButton.setBackground(CARD);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        refreshButton.addActionListener(e -> refreshReports());

        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(refreshButton, BorderLayout.EAST);

        return panel;
    }

    // ================= STATISTICS =================

    private JPanel createStatisticsPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBackground(BG);

        panel.add(createStatCard(
                "Total Residents",
                "0",
                "Registered members"
        ));

        panel.add(createStatCard(
                "Waste Recycled",
                "0 KG",
                "Total collected"
        ));

        panel.add(createStatCard(
                "Points Distributed",
                "0",
                "Collection rewards"
        ));

        panel.add(createStatCard(
                "Points Redeemed",
                "0",
                "Rewards claimed"
        ));

        return panel;
    }

    private JPanel createStatCard(
            String title,
            String value,
            String description
    ) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(ACCENT);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(MUTED);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descriptionLabel);

        return card;
    }

    // ================= REPORT TABLE =================

    private JPanel createReportTable() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel heading = new JLabel("Material Collection Report");
        heading.setForeground(TEXT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Material",
                        "Total Weight (KG)",
                        "Points Generated"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        reportTable.setBackground(CARD);
        reportTable.setForeground(TEXT);
        reportTable.setGridColor(BORDER);
        reportTable.setRowHeight(35);
        reportTable.setFont(new Font("SansSerif", Font.PLAIN, 13));

        reportTable.getTableHeader().setBackground(
                new Color(17, 24, 42)
        );

        reportTable.getTableHeader().setForeground(TEXT);
        reportTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        reportTable.setSelectionBackground(new Color(35, 55, 75));
        reportTable.setSelectionForeground(TEXT);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= REFRESH =================

    private void refreshReports() {

        tableModel.setRowCount(0);

        JOptionPane.showMessageDialog(
                this,
                "Reports refreshed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}