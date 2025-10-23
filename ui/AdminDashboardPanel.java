package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Combined Admin Dashboard for RideShare — Laptop-style UI
 * Tabs: Applications | Users | Rides
 */
public class AdminDashboardPanel extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rideshare";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final RideShareMobileUI ui;
    private final JTabbedPane tabbedPane;
    private final JPanel applicationsTab;
    private final JPanel usersTab;
    private final JPanel ridesTab;

    public AdminDashboardPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== Dashboard Header =====
        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(40, 40, 40));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // ===== Tabbed Content =====
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        // Create Tabs
        applicationsTab = createApplicationsTab();
        usersTab = createUsersTab();
        ridesTab = createRidesTab();

        tabbedPane.addTab("Applications", applicationsTab);
        tabbedPane.addTab("Users", usersTab);
        tabbedPane.addTab("Rides", ridesTab);

        add(tabbedPane, BorderLayout.CENTER);

        // ===== Footer =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        footer.setBackground(new Color(245, 245, 245));

        JButton refreshBtn = new JButton("↻ Refresh");
        JButton logoutBtn = new JButton("Logout");

        styleFooterButton(refreshBtn, new Color(51, 153, 255));
        styleFooterButton(logoutBtn, new Color(244, 67, 54));

        refreshBtn.addActionListener(e -> refreshTabs());
        logoutBtn.addActionListener(e -> ui.showScreen("login"));

        footer.add(refreshBtn);
        footer.add(logoutBtn);
        add(footer, BorderLayout.SOUTH);
    }

    // ===== Applications Tab =====
    private JPanel createApplicationsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Pending Driver Applications", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM driver_applications WHERE status = 'pending'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                JPanel card = createDriverCard(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("vehicle_model"),
                        rs.getString("license_number"),
                        rs.getString("license_file")
                );
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(10));
            }

            if (!hasData) {
                JLabel empty = new JLabel("No pending applications!", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            }

        } catch (SQLException e) {
            listPanel.add(new JLabel("Error: " + e.getMessage()));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ===== Users Tab =====
    private JPanel createUsersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Registered Users", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT id, username, role FROM users";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JPanel card = new JPanel(new GridLayout(1, 3));
                card.setBackground(new Color(240, 245, 255));
                card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                card.add(new JLabel("👤 " + rs.getString("username")));
                card.add(new JLabel("🆔 " + rs.getString("id")));
                card.add(new JLabel("🎭 Role: " + rs.getString("role")));
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(5));
            }

        } catch (SQLException e) {
            listPanel.add(new JLabel("Error: " + e.getMessage()));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

 // ===== Rides Tab =====
    private JPanel createRidesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("All Offered Rides", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM rides";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;

                JPanel card = new JPanel(new GridLayout(3, 2));
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                String status = rs.getString("status");
                JLabel statusLabel = new JLabel("🚦 Status: " + status);
                statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

                // Color-code the status text
                if (status.equalsIgnoreCase("Available")) {
                    statusLabel.setForeground(new Color(76, 175, 80)); // Green
                } else if (status.equalsIgnoreCase("Booked")) {
                    statusLabel.setForeground(new Color(255, 152, 0)); // Orange
                } else if (status.equalsIgnoreCase("Completed")) {
                    statusLabel.setForeground(new Color(33, 150, 243)); // Blue
                } else if (status.equalsIgnoreCase("Cancelled")) {
                    statusLabel.setForeground(new Color(244, 67, 54)); // Red
                } else {
                    statusLabel.setForeground(Color.GRAY); // Default
                }

                card.add(new JLabel("🚗 Driver: " + rs.getString("driver_name")));
                card.add(new JLabel("🪑 Seats: " + rs.getInt("seats_available")));
                card.add(new JLabel("📍 From: " + rs.getString("from_location")));
                card.add(new JLabel("➡️ To: " + rs.getString("to_location")));
                card.add(new JLabel("⏰ Date & Time: " + rs.getString("date") + " " + rs.getString("time")));
                card.add(statusLabel);

                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(8));
            }

            if (!hasData) {
                JLabel empty = new JLabel("No rides available.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            }

        } catch (SQLException e) {
            listPanel.add(new JLabel("Error: " + e.getMessage()));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }


    // ===== Helper: Driver Card for Applications Tab =====
    private JPanel createDriverCard(String id, String name, String vehicle, String licenseNo, String licenseFile) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 248, 255));
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setOpaque(false);
        info.add(new JLabel("👤 " + name + "  |  " + vehicle));
        info.add(new JLabel("License: " + licenseNo));
        info.add(new JLabel("File: " + (licenseFile != null ? licenseFile : "N/A")));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        buttons.setOpaque(false);
        JButton approve = createMiniButton("Approve", new Color(76, 175, 80));
        JButton reject = createMiniButton("Reject", new Color(244, 67, 54));

        approve.addActionListener(e -> updateStatus(id, "approved"));
        reject.addActionListener(e -> updateStatus(id, "rejected"));

        buttons.add(approve);
        buttons.add(reject);

        card.add(info, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);
        return card;
    }

    // ===== Helper Buttons =====
    private JButton createMiniButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(85, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(baseColor.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); }
        });

        return btn;
    }

    private void styleFooterButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ===== Refresh Tabs =====
    private void refreshTabs() {
        tabbedPane.setComponentAt(0, createApplicationsTab());
        tabbedPane.setComponentAt(1, createUsersTab());
        tabbedPane.setComponentAt(2, createRidesTab());
    }

    // ===== Database Update =====
    private void updateStatus(String id, String status) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE driver_applications SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(this, "Driver " + id + " has been " + status + ".");
            refreshTabs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
