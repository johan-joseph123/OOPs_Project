package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewAllRidesPanel extends JPanel {

    public ViewAllRidesPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        // Header section
        JLabel title = new JLabel("All Offered Rides", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // Scrollable ride list area
        JPanel rideListPanel = new JPanel();
        rideListPanel.setLayout(new BoxLayout(rideListPanel, BoxLayout.Y_AXIS));
        rideListPanel.setBackground(Color.WHITE);
        rideListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        List<Ride> rides = ApplicationData.allOfferedRides;

        if (rides == null || rides.isEmpty()) {
            JLabel emptyLabel = new JLabel("No rides have been offered yet.");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            rideListPanel.add(emptyLabel);
        } else {
            for (Ride ride : rides) {
                rideListPanel.add(createRideCard(ride));
                rideListPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(rideListPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(33, 150, 243));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> ui.showScreen("home"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(250, 250, 250));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createRideCard(Ride ride) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel driver = new JLabel("👤 Driver: " + ride.getName());
        JLabel route = new JLabel("📍 Route: " + ride.getRoute());
        JLabel time = new JLabel("⏰ Time: " + ride.getTime());
        JLabel vehicle = new JLabel("🚗 Vehicle: " + ride.getVehicleType());

        Font font = new Font("Segoe UI", Font.PLAIN, 15);
        driver.setFont(font);
        route.setFont(font);
        time.setFont(font);
        vehicle.setFont(font);

        card.add(driver);
        card.add(route);
        card.add(time);
        card.add(vehicle);

        return card;
    }
}
