package view;

import controller.AdminController;
import model.DriverApplication;
import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final AdminController adminController = new AdminController();

    public AdminDashboardPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Applications", createApplicationsTab());
        tabbedPane.addTab("Users", createUsersTab());
        tabbedPane.addTab("Rides", createRidesTab());
        add(tabbedPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        footer.setBackground(new Color(245, 245, 245));
        JButton refreshBtn = new JButton("↻ Refresh");
        refreshBtn.addActionListener(e -> refreshTabs(tabbedPane));
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    private void refreshTabs(JTabbedPane tabbedPane) {
        tabbedPane.setComponentAt(0, createApplicationsTab());
        tabbedPane.setComponentAt(1, createUsersTab());
        tabbedPane.setComponentAt(2, createRidesTab());
    }

    /** -------------------- Applications -------------------- **/
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

        try {
            List<DriverApplication> apps = adminController.getPendingApplications();
            if (apps.isEmpty()) {
                JLabel empty = new JLabel("No pending applications!", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            } else {
                for (DriverApplication da : apps) {
                    JPanel card = new JPanel(new BorderLayout());
                    card.setBackground(new Color(245, 248, 255));
                    card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

                    JPanel info = new JPanel(new GridLayout(3, 1));
                    info.setOpaque(false);
                    info.add(new JLabel("     " + da.getName() + " | " + da.getVehicleModel()));
                    info.add(new JLabel("License: " + da.getLicenseNumber()));
                    info.add(new JLabel("File: " + da.getLicenseFile()));

                    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
                    buttons.setOpaque(false);
                    JButton approve = new JButton("Approve");
                    JButton reject = new JButton("Reject");
                    approve.addActionListener(e -> updateStatus(da.getId(), "approved"));
                    reject.addActionListener(e -> updateStatus(da.getId(), "rejected"));
                    buttons.add(approve);
                    buttons.add(reject);

                    card.add(info, BorderLayout.CENTER);
                    card.add(buttons, BorderLayout.EAST);
                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (SQLException ex) {
            listPanel.add(new JLabel("Error: " + ex.getMessage()));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void updateStatus(String id, String status) {
        try {
            boolean ok = adminController.updateApplicationStatus(id, status);
            if (ok)
                JOptionPane.showMessageDialog(this, "Driver " + id + " has been " + status + ".");
            else
                JOptionPane.showMessageDialog(this, "Failed to update status.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** -------------------- Users -------------------- **/
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

        try (java.sql.Connection conn = dao.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT id, username, role FROM users ORDER BY role, username")) {

            if (!rs.isBeforeFirst()) {
                JLabel empty = new JLabel("No registered users found.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            } else {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String username = rs.getString("username");
                    String role = rs.getString("role");

                    JPanel card = new JPanel(new BorderLayout());
                    card.setBackground(new Color(245, 248, 255));
                    card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

                    JLabel info = new JLabel("<html><b>" + username + "</b> (" + role + ")<br>ID: " + id + "</html>");
                    info.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                    JButton deleteBtn = new JButton("Delete");
                    deleteBtn.setBackground(new Color(255, 80, 80));
                    deleteBtn.setForeground(Color.WHITE);
                    deleteBtn.addActionListener(e -> deleteUser(id, username, role));

                    card.add(info, BorderLayout.CENTER);
                    card.add(deleteBtn, BorderLayout.EAST);
                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(8));
                }
            }

        } catch (Exception ex) {
            listPanel.add(new JLabel("Error loading users: " + ex.getMessage()));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void deleteUser(String id, String username, String role) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user '" + username + "' (" + role + ")?\nAll their data will be permanently removed.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (java.sql.Connection conn = dao.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

                ps.setString(1, id);
                int rows = ps.executeUpdate();

                if (rows > 0)
                    JOptionPane.showMessageDialog(this, "User deleted successfully (ID: " + id + ")");
                else
                    JOptionPane.showMessageDialog(this, "Failed to delete user (ID: " + id + ")");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
            }
        }
    }

    /** -------------------- Rides -------------------- **/
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

        try (java.sql.Connection conn = dao.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT * FROM rides ORDER BY date DESC")) {

            if (!rs.isBeforeFirst()) {
                JLabel empty = new JLabel("No rides available.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            } else {
                while (rs.next()) {
                    int rideId = rs.getInt("ride_id");
                    String driver = rs.getString("driver_name");
                    String from = rs.getString("from_location");
                    String to = rs.getString("to_location");
                    String date = rs.getDate("date").toString();
                    String time = rs.getString("time");
                    int seats = rs.getInt("seats_available");
                    String status = rs.getString("status");

                    JPanel card = new JPanel(new BorderLayout());
                    card.setBackground(new Color(245, 248, 255));
                    card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

                    JLabel info = new JLabel("<html><b>" + driver + "</b> — " + from + " → " + to
                            + "<br/>Date: " + date + " " + time
                            + " | Seats: " + seats
                            + " | Status: <b>" + status + "</b></html>");
                    info.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                    JButton deleteBtn = new JButton("Delete Ride");
                    deleteBtn.setBackground(new Color(255, 100, 100));
                    deleteBtn.setForeground(Color.WHITE);
                    deleteBtn.addActionListener(e -> deleteRide(rideId));

                    card.add(info, BorderLayout.CENTER);
                    card.add(deleteBtn, BorderLayout.EAST);
                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(8));
                }
            }

        } catch (Exception ex) {
            listPanel.add(new JLabel("Error loading rides: " + ex.getMessage()));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void deleteRide(int rideId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this ride and all related bookings?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (java.sql.Connection conn = dao.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM rides WHERE ride_id = ?")) {

                ps.setInt(1, rideId);
                int rows = ps.executeUpdate();

                if (rows > 0)
                    JOptionPane.showMessageDialog(this, "Ride deleted successfully (ID: " + rideId + ")");
                else
                    JOptionPane.showMessageDialog(this, "Failed to delete ride.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting ride: " + ex.getMessage());
            }
        }
    }
}
