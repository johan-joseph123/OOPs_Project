package view;

import controller.BookingController;
import dao.RideDAO;
import model.Ride;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Simplified SearchPanel — lists all open rides and allows booking.
 */
public class SearchPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideDAO rideDAO = new RideDAO();
    private final BookingController bookingController = new BookingController();
    private final JPanel resultsPanel;

    public SearchPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Header (Title + Refresh button) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(230, 238, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Available Rides", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 60, 120));

        JButton refreshBtn = new JButton("↻ Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(0x4A90E2));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadAllRides());

        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- Ride Results List ---
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Load rides on startup
        loadAllRides();
    }

    /** ✅ Loads all open rides from the database */
    private void loadAllRides() {
        resultsPanel.removeAll();
        try {
            List<Ride> rides = rideDAO.findAllOpenRides();
            if (rides.isEmpty()) {
                JLabel empty = new JLabel("No rides are currently available.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(Color.GRAY);
                resultsPanel.add(empty);
            } else {
                for (Ride ride : rides) {
                    resultsPanel.add(createRideCard(ride));
                    resultsPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (SQLException e) {
            resultsPanel.add(new JLabel("Database error: " + e.getMessage()));
        }
        revalidate();
        repaint();
    }

    /** ✅ Creates a simple ride info card with a Book button */
    private JPanel createRideCard(Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b> — " +
                r.getFromLocation() + " → " + r.getToLocation() +
                "<br/>" + r.getDate() + " " + r.getTime() +
                " • " + r.getVehicleType() + "</html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(info, BorderLayout.CENTER);

        JButton bookButton = new JButton("Book");
        bookButton.setBackground(new Color(0x50C878)); // green tone
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookButton.setFocusPainted(false);
        bookButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        bookButton.addActionListener(e -> {
            if (!ui.isLoggedIn() || !"rider".equals(ui.getCurrentUserRole())) {
                JOptionPane.showMessageDialog(this, "Please login as a rider to book a ride.");
                return;
            }
            try {
                boolean ok = bookingController.bookSeat(r.getRideId(), ui.getCurrentUserId());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Ride booked successfully!");
                    ui.showScreen("riderTrips");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book ride.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        card.add(bookButton, BorderLayout.EAST);
        return card;
    }
}
