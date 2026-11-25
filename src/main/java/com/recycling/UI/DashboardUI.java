
package com.recycling.ui;

import com.recycling.service.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardUI extends JFrame {

    // ================= THEME =================

    private final Color BACKGROUND = new Color(11, 16, 32);
    private final Color SIDEBAR = new Color(17, 24, 42);
    private final Color CARD = new Color(23, 31, 53);
    private final Color CARD_LIGHT = new Color(29, 39, 64);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color VIOLET = new Color(139, 124, 255);
    private final Color BLUE = new Color(92, 164, 255);
    private final Color ORANGE = new Color(255, 184, 107);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);
    private final Color BORDER = new Color(42, 55, 78);

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JLabel residentsValue;
    private JLabel materialsValue;
    private JLabel collectedValue;
    private JLabel pointsValue;

    // ================= CONSTRUCTOR =================

    public DashboardUI() {

        setTitle("Recycling Rewards System");
        setSize(1250, 800);
        setMinimumSize(new Dimension(1050, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        JPanel sidebar = createSidebar();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(createDashboard(), "Dashboard");
        contentPanel.add(new ResidentUI(), "Residents");
        contentPanel.add(new MaterialUI(), "Materials");
        contentPanel.add(new CollectionUI(), "Collection");
        contentPanel.add(new RedemptionUI(), "Redemption");
        contentPanel.add(new ReportsUI(), "Reports");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        loadDashboardStats();

        setVisible(true);
    }

    // ================= SIDEBAR =================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 800));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 18, 20, 18));

        JLabel logo = new JLabel("♻ Recycling");
        logo.setForeground(ACCENT);
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoSub = new JLabel("REWARDS SYSTEM");
        logoSub.setForeground(MUTED);
        logoSub.setFont(new Font("SansSerif", Font.BOLD, 10));
        logoSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(logoSub);
        sidebar.add(Box.createVerticalStrut(40));

        JLabel menuLabel = new JLabel("MAIN MENU");
        menuLabel.setForeground(MUTED);
        menuLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(15));

        String[] menuItems = {
                "Dashboard",
                "Residents",
                "Materials",
                "Collection",
                "Redemption",
                "Reports"
        };

        String[] icons = {
                "▦", "♙", "♻", "♧", "★", "▤"
        };

        for (int i = 0; i < menuItems.length; i++) {

            String item = menuItems[i];

            JButton button = createMenuButton(icons[i] + "   " + item);

            button.addActionListener(e ->
                    cardLayout.show(contentPanel, item)
            );

            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(SIDEBAR);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel systemLabel = new JLabel("SYSTEM STATUS");
        systemLabel.setForeground(MUTED);
        systemLabel.setFont(new Font("SansSerif", Font.BOLD, 10));

        JLabel statusLabel = new JLabel("●  Application Running");
        statusLabel.setForeground(ACCENT);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        bottomPanel.add(systemLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(statusLabel);

        sidebar.add(bottomPanel);

        return sidebar;
    }

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(200, 45));
        button.setPreferredSize(new Dimension(200, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setForeground(TEXT);
        button.setBackground(SIDEBAR);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setForeground(Color.BLACK);
button.setBackground(Color.GREEN);
button.setOpaque(true);
button.setBorderPainted(true);
button.setContentAreaFilled(true);
button.setEnabled(true);

        return button;
    }

    // ================= DASHBOARD =================

    private JPanel createDashboard() {

        JPanel dashboard = new JPanel(new BorderLayout(20, 25));
        dashboard.setBackground(BACKGROUND);
        dashboard.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        // Header
        JPanel header = createHeader();

        // Main content
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 18, 18));
        statsPanel.setBackground(BACKGROUND);

        statsPanel.add(createStatCard(
                "TOTAL RESIDENTS",
                "0",
                "Registered participants",
                "♙",
                BLUE
        ));

        statsPanel.add(createStatCard(
                "MATERIALS",
                "0",
                "Recyclable categories",
                "♻",
                ACCENT
        ));

        statsPanel.add(createStatCard(
                "TOTAL COLLECTED",
                "0 KG",
                "Recycling contribution",
                "⚖",
                ORANGE
        ));

        statsPanel.add(createStatCard(
                "POINTS AWARDED",
                "0",
                "Rewards generated",
                "★",
                VIOLET
        ));

        centerPanel.add(statsPanel);
        centerPanel.add(Box.createVerticalStrut(25));

        // Lower section
        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 18, 18));
        lowerPanel.setBackground(BACKGROUND);

        lowerPanel.add(createOverviewPanel(
                "Recycling Program",
                "Turn recyclable waste into rewards",
                "Residents can submit recyclable materials and "
                        + "earn reward points based on collected weight.",
                ACCENT
        ));

        lowerPanel.add(createOverviewPanel(
                "Reward System",
                "Points-based incentive program",
                "Collected materials generate reward points. "
                        + "Residents can use their accumulated points "
                        + "for redemption.",
                VIOLET
        ));

        centerPanel.add(lowerPanel);

        dashboard.add(header, BorderLayout.NORTH);
        dashboard.add(centerPanel, BorderLayout.CENTER);

        return dashboard;
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BACKGROUND);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "Recycling Rewards System Overview"
        );
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);

        JButton refreshButton = new JButton("⟳  Refresh");

        refreshButton.setForeground(ACCENT);
        refreshButton.setBackground(CARD);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));

        refreshButton.addActionListener(e -> loadDashboardStats());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        return header;
    }

    // ================= STATISTIC CARDS =================

    private JPanel createStatCard(
            String title,
            String value,
            String description,
            String icon,
            Color accent
    ) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        // Top row
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CARD);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(iconLabel, BorderLayout.EAST);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(accent);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (title.equals("TOTAL RESIDENTS")) {
            residentsValue = valueLabel;
        }
        else if (title.equals("MATERIALS")) {
            materialsValue = valueLabel;
        }
        else if (title.equals("TOTAL COLLECTED")) {
            collectedValue = valueLabel;
        }
        else if (title.equals("POINTS AWARDED")) {
            pointsValue = valueLabel;
        }

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(MUTED);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(topPanel);
        card.add(Box.createVerticalStrut(18));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(descriptionLabel);

        return card;
    }

    // ================= OVERVIEW PANELS =================

    private JPanel createOverviewPanel(
            String title,
            String subtitle,
            String description,
            Color accent
    ) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(accent);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(TEXT);
        subtitleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setEditable(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setForeground(MUTED);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(descriptionLabel);

        return panel;
    }

    // ================= LOAD DATABASE DATA =================

    private void loadDashboardStats() {

        try {

            DashboardService service = new DashboardService();

            DashboardService.DashboardStats stats =
                    service.getDashboardStats();

            residentsValue.setText(
                    String.valueOf(stats.totalResidents)
            );

            materialsValue.setText(
                    String.valueOf(stats.totalMaterials)
            );

            collectedValue.setText(
                    String.format("%.2f KG", stats.totalCollected)
            );

            pointsValue.setText(
                    NumberFormat.getNumberInstance(Locale.US)
                            .format(stats.pointsAwarded)
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard data.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new DashboardUI());
    }
}
