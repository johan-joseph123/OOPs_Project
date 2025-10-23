package view;

import controller.BookingController;
import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;
import model.Ride;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RiderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final BookingController bookingController = new BookingController();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RideDAO rideDAO = new RideDAO();
    private final JPanel tripListPanel;

    public RiderTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("My Booked Rides", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(title, BorderLayout.NORTH);

        tripListPanel = new JPanel();
        tripListPanel.setLayout(new BoxLayout(tripListPanel, BoxLayout.Y_AXIS));
        tripListPanel.setBackground(new Color(240, 245, 255));
        tripListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(tripListPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    /** ✅ Refresh data when screen shown */
    public void refreshData() {
        tripListPanel.removeAll();
        String riderId = ui.getCurrentUserId();

        if (riderId == null) {
            tripListPanel.add(new JLabel("Please log in as a rider."));
            refresh();
            return;
        }

        try {
            List<Booking> bookings = bookingDAO.findByRider(riderId);
            if (bookings.isEmpty()) {
                tripListPanel.add(new JLabel("No booked rides found."));
            } else {
                for (Booking b : bookings) {
                    Ride r = rideDAO.findById(b.getRideId());
                    tripListPanel.add(createBookingCard(b, r));
                    tripListPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (SQLException e) {
            tripListPanel.add(new JLabel("Database error: " + e.getMessage()));
        }

        refresh();
    }

    private JPanel createBookingCard(Booking b, Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 255), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        String rideInfo = (r != null)
                ? "<b>" + r.getFromLocation() + " → " + r.getToLocation() +
                "</b><br/>" + r.getDate() + " " + r.getTime() +
                " • Vehicle: " + r.getVehicleType()
                : "<i>Ride details unavailable</i>";

        JLabel info = new JLabel("<html>" + rideInfo + "<br/>Status: " + b.getStatus() + "</html>");
        card.add(info, BorderLayout.CENTER);

        JButton completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(e -> {
            try {
                if (bookingController.completeBooking(b.getBookingId())) {
                    JOptionPane.showMessageDialog(this, "Booking marked as completed!");
                    refreshData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(completeBtn, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private void refresh() {
        revalidate();
        repaint();
    }
}
