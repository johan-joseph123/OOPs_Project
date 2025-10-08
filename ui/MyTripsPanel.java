package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MyTripsPanel extends JPanel {
    private final RideShareMobileUI parent;
    private final JPanel tripListPanel;
    private final List<Ride> myRides;

    public MyTripsPanel(RideShareMobileUI parent) {
        this.parent = parent;
        this.myRides = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("My Trips", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        tripListPanel = new JPanel();
        tripListPanel.setLayout(new BoxLayout(tripListPanel, BoxLayout.Y_AXIS));
        tripListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(tripListPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        updateTripList(); // Initial draw
    }

    public void addRide(Ride ride) {
        myRides.add(ride);
        // The update is typically done when the screen is shown by the main UI class,
        // but we can call it here for immediate effect if needed.
    }

    public void updateTripList() {
        tripListPanel.removeAll();

        if (myRides.isEmpty()) {
            JLabel noTripsLabel = new JLabel("No trips booked or offered yet. Start a ride!");
            noTripsLabel.setAlignmentX(CENTER_ALIGNMENT);
            tripListPanel.add(Box.createVerticalGlue());
            tripListPanel.add(noTripsLabel);
            tripListPanel.add(Box.createVerticalGlue());
        } else {
            for (Ride ride : myRides) {
                tripListPanel.add(createTripItem(ride));
                tripListPanel.add(Box.createVerticalStrut(10));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createTripItem(Ride ride) {
        JPanel ridePanel = new JPanel(new GridLayout(0, 1));
        ridePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        ridePanel.setBackground(new Color(245, 245, 245)); // Light background for list item
        ridePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Constrain vertical size

        ridePanel.add(new JLabel("📍 Pickup: " + ride.getPlace() + " (" + ride.getRoute() + ")"));
        ridePanel.add(new JLabel("🕒 Time: " + ride.getTime()));
        ridePanel.add(new JLabel("🚗 Vehicle: " + ride.getVehicleType()));
        ridePanel.add(new JLabel("👥 Seats: " + ride.getSeats()));

        return ridePanel;
    }
}