package com.recycling.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.CardLayout;


public class DashboardUI extends JFrame {

    // Colors
    private final Color BACKGROUND = new Color(11, 16, 32);
    private final Color SIDEBAR = new Color(17, 24, 42);
    private final Color CARD = new Color(23, 31, 53);
    private final Color ACCENT = new Color(77, 226, 197);
    private final Color TEXT = new Color(235, 240, 255);
    private final Color MUTED = new Color(155, 168, 190);

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public DashboardUI() {

        // Main window settings
          setTitle("Recycling Rewards System");
    setSize(1100, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(BACKGROUND);

    // Create sidebar
    JPanel sidebar = createSidebar();

    // Initialize CardLayout
    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);
    contentPanel.setBackground(BACKGROUND);

    // Add screens
    contentPanel.add(createDashboard(), "Dashboard");
    contentPanel.add(new ResidentUI(), "Residents");
    contentPanel.add(new MaterialUI(), "Materials");
    contentPanel.add(new CollectionUI(), "Collection");
    contentPanel.add(new RedemptionUI(), "Redemption");
    contentPanel.add(new ReportsUI(), "Reports");

    // Add components to main window
    mainPanel.add(sidebar, BorderLayout.WEST);
    mainPanel.add(contentPanel, BorderLayout.CENTER);

    add(mainPanel);

    setVisible(true);
    }

    // Sidebar
    private JPanel createSidebar() {

         JPanel sidebar = new JPanel();
    sidebar.setPreferredSize(new Dimension(220, 700));
    sidebar.setBackground(SIDEBAR);
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

    JLabel title = new JLabel(" ♻ Recycling Rewards");
    title.setForeground(ACCENT);
    title.setFont(new Font("SansSerif", Font.BOLD, 18));

    sidebar.add(title);
    sidebar.add(Box.createVerticalStrut(40));

    String[] menuItems = {
            "Dashboard",
            "Residents",
            "Materials",
            "Collection",
            "Redemption",
            "Reports"
    };

    for (String item : menuItems) {

        JButton button = new JButton(item);

        button.setMaximumSize(new Dimension(190, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setForeground(TEXT);
        button.setBackground(SIDEBAR);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Navigation functionality
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, item);
        });

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(10));
    }

    return sidebar;
    }

    private JPanel createPage(String pageTitle) {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND);
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    JLabel heading = new JLabel(pageTitle);
    heading.setForeground(TEXT);
    heading.setFont(new Font("SansSerif", Font.BOLD, 28));

    JLabel message = new JLabel("This section will be implemented next.");
    message.setForeground(MUTED);
    message.setFont(new Font("SansSerif", Font.PLAIN, 16));

    JPanel center = new JPanel();
    center.setBackground(BACKGROUND);
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    heading.setAlignmentX(Component.LEFT_ALIGNMENT);
    message.setAlignmentX(Component.LEFT_ALIGNMENT);

    center.add(heading);
    center.add(Box.createVerticalStrut(20));
    center.add(message);

    panel.add(center, BorderLayout.NORTH);

    return panel;
}

    // Dashboard
    private JPanel createDashboard() {

         JPanel dashboard = new JPanel(new BorderLayout(20, 20));
    dashboard.setBackground(BACKGROUND);
    dashboard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(BACKGROUND);

    JLabel heading = new JLabel("Dashboard");
    heading.setFont(new Font("SansSerif", Font.BOLD, 28));
    heading.setForeground(TEXT);

    JLabel subtitle = new JLabel("Recycling Rewards System Overview");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subtitle.setForeground(MUTED);

    JPanel headerText = new JPanel();
    headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
    headerText.setBackground(BACKGROUND);

    headerText.add(heading);
    headerText.add(Box.createVerticalStrut(8));
    headerText.add(subtitle);

    header.add(headerText, BorderLayout.WEST);

    // Statistics cards
    JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
    cardsPanel.setBackground(BACKGROUND);

    cardsPanel.add(createCard("Total Residents", "120", "Registered participants"));
    cardsPanel.add(createCard("Materials", "8", "Recyclable categories"));
    cardsPanel.add(createCard("Total Collected", "540 KG", "Recycling contribution"));
    cardsPanel.add(createCard("Points Awarded", "12,450", "Rewards generated"));

    dashboard.add(header, BorderLayout.NORTH);
    dashboard.add(cardsPanel, BorderLayout.CENTER);

    return dashboard;
    }

    // Statistics card
    private JPanel createCard(String title, String value, String description) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 55, 78)),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(ACCENT);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(MUTED);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descriptionLabel);

        return card;
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new DashboardUI();
        });

    }
}