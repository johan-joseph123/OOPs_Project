package view;

import controller.RideController;
import controller.BookingController;
import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;
import model.Ride;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Displays the user's trips (driver or rider).
 */
public class MyTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideDAO rideDAO = new RideDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RideController rideController = new RideController();
    private final BookingController bookingController = new BookingController();
    private final JPanel tripListPanel;

    public MyTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("My Trips", SwingConstants.CENTER);
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

        updateTripList();
    }

    /** Updates trips based on user role. */
    public void updateTripList() {
        tripListPanel.removeAll();

        if (ui.getCurrentUserId() == null) {
            tripListPanel.add(new JLabel("Please log in to view your trips."));
            refresh();
            return;
        }

        try {
            if ("driver".equalsIgnoreCase(ui.getCurrentUserRole())) {
                List<Ride> rides = rideController.getProviderRides(ui.getCurrentUserId());
                if (rides.isEmpty()) {
                    tripListPanel.add(new JLabel("You haven’t offered any rides yet."));
                } else {
                    for (Ride r : rides) {
                        tripListPanel.add(createDriverCard(r));
                        tripListPanel.add(Box.createVerticalStrut(10));
                    }
                }
            } else if ("rider".equalsIgnoreCase(ui.getCurrentUserRole())) {
                List<Booking> bookings = bookingDAO.findByRider(ui.getCurrentUserId());
                if (bookings.isEmpty()) {
                    tripListPanel.add(new JLabel("You have no active bookings."));
                } else {
                    for (Booking b : bookings) {
                        Ride r = rideDAO.findById(b.getRideId());
                        tripListPanel.add(createRiderCard(b, r));
                        tripListPanel.add(Box.createVerticalStrut(10));
                    }
                }
            } else {
                tripListPanel.add(new JLabel("Unsupported role."));
            }
        } catch (SQLException ex) {
            tripListPanel.setLayout(new BorderLayout());
            tripListPanel.add(new JLabel("Database Error: " + ex.getMessage()), BorderLayout.CENTER);
        }

        refresh();
    }

    /** Refresh layout. */
    private void refresh() {
        revalidate();
        repaint();
    }

    /** Card for driver’s offered rides. */
    private JPanel createDriverCard(Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 255), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b> — " +
                r.getFromLocation() + " → " + r.getToLocation() +
                "<br/>" + r.getDate() + " " + r.getTime() +
                " • Seats Left: " + r.getSeatsAvailable() +
                " • Status: " + r.getStatus() + "</html>");
        card.add(info, BorderLayout.CENTER);

        JButton completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(e -> {
            try {
                if (rideController.markRideCompleted(r.getRideId())) {
                    JOptionPane.showMessageDialog(this, "Ride marked as completed!");
                    updateTripList();
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

    /** Card for rider’s bookings. */
    private JPanel createRiderCard(Booking b, Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 255), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        String rideInfo = (r != null)
                ? "<b>" + r.getDriverName() + "</b> — " + r.getFromLocation() + " → " + r.getToLocation() +
                  "<br/>" + r.getDate() + " " + r.getTime() + " • " + r.getVehicleType()
                : "<i>Ride details unavailable</i>";

        JLabel info = new JLabel("<html>" + rideInfo + "<br/>Status: " + b.getStatus() + "</html>");
        card.add(info, BorderLayout.CENTER);

        JButton completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(e -> {
            try {
                if (bookingController.completeBooking(b.getBookingId())) {
                    JOptionPane.showMessageDialog(this, "Booking marked as completed!");
                    updateTripList();
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
}
