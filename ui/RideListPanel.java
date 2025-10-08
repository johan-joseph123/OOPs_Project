package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RideListPanel extends JPanel {

    public RideListPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Available Rides", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel ridesContainer = new JPanel();
        ridesContainer.setLayout(new BoxLayout(ridesContainer, BoxLayout.Y_AXIS));
        ridesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(ridesContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Add dummy rides
        ridesContainer.add(createRideItem(ui, new Ride("Ajay", "Car", "07:30", 2, "St. Joseph's → Pala")));
        ridesContainer.add(Box.createVerticalStrut(10));
        ridesContainer.add(createRideItem(ui, new Ride("Meera", "Bike", "07:20", 1, "St. Joseph's → Bharananganam")));
        ridesContainer.add(Box.createVerticalStrut(10));
        ridesContainer.add(createRideItem(ui, new Ride("Rohit", "Car", "08:15", 3, "St. Joseph's → Kottayam RS")));

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createRideItem(RideShareMobileUI ui, Ride ride) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 255)); // Light blue tint
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Constrain height

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false);
        
        JLabel top = new JLabel(ride.getName() + " • " + ride.getVehicleType() + " • " + ride.getTime());
        top.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel bottom = new JLabel("Seats: " + ride.getSeats() + " | Route: " + ride.getRoute());
        
        textPanel.add(top);
        textPanel.add(bottom);
        
        panel.add(textPanel, BorderLayout.CENTER);
        
        // Add a small label for action/status
        JLabel actionLabel = new JLabel("TAP to JOIN");
        actionLabel.setForeground(Color.BLUE);
        panel.add(actionLabel, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(panel, "Join ride with " + ride.getName() + " to " + ride.getRoute() + "?", "Confirm Ride", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ui.addRideToMyTrips(ride);
                    JOptionPane.showMessageDialog(panel, "Ride added to My Trips!");
                    ui.showScreen("trips"); // Navigate to "trips" screen
                }
            }
        });

        return panel;
    }
}
