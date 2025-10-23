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

        JLabel title = new JLabel("Available Rides", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        loadAllRides();
    }

    /** Loads all open rides from the database. */
    private void loadAllRides() {
        resultsPanel.removeAll();
        try {
            List<Ride> rides = rideDAO.findAllOpenRides();
            if (rides.isEmpty()) {
                resultsPanel.add(new JLabel("No rides are currently available.", SwingConstants.CENTER));
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

    /** Creates a simple ride info card with a Book button. */
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
        card.add(info, BorderLayout.CENTER);

        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e -> {
            if (!ui.isLoggedIn() || !"rider".equals(ui.getCurrentUserRole())) {
                JOptionPane.showMessageDialog(this, "Please login as a rider to book a ride.");
                return;
            }
            try {
                boolean ok = bookingController.bookSeat(r.getRideId(), ui.getCurrentUserId());
                if (ok) JOptionPane.showMessageDialog(this, "Ride booked successfully!");
                else JOptionPane.showMessageDialog(this, "Failed to book ride.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        card.add(bookButton, BorderLayout.EAST);
        return card;
    }
}
