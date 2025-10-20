package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RideListPanel extends JPanel {

    public RideListPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255)); // Light background tint

        JLabel title = new JLabel("🚗 Available Rides", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel ridesContainer = new JPanel();
        ridesContainer.setLayout(new BoxLayout(ridesContainer, BoxLayout.Y_AXIS));
        ridesContainer.setOpaque(false);
        ridesContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(ridesContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, BorderLayout.CENTER);

        // Add sample rides (dummy data)
        ridesContainer.add(createRideItem(ui, new Ride("Ajay", "Car", "07:30 AM", 2, "St. Joseph’s → Pala")));
        ridesContainer.add(Box.createVerticalStrut(10));
        ridesContainer.add(createRideItem(ui, new Ride("Meera", "Bike", "07:20 AM", 1, "St. Joseph’s → Bharananganam")));
        ridesContainer.add(Box.createVerticalStrut(10));
        ridesContainer.add(createRideItem(ui, new Ride("Rohit", "Car", "08:15 AM", 3, "St. Joseph’s → Kottayam RS")));
    }

    private JPanel createRideItem(RideShareMobileUI ui, Ride ride) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 250), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Left side — Ride info
        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("👤 " + ride.getName() + "  |  " + ride.getVehicleType());
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(30, 50, 100));

        JLabel details = new JLabel("🕒 " + ride.getTime() + "   |   👥 Seats: " + ride.getSeats() + "   |   📍 " + ride.getRoute());
        details.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        details.setForeground(new Color(70, 80, 110));

        textPanel.add(title);
        textPanel.add(details);
        panel.add(textPanel, BorderLayout.CENTER);

        // Right side — join label (acts as a button)
        JLabel joinLabel = new JLabel("Join ➜");
        joinLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        joinLabel.setForeground(new Color(0, 102, 204));
        panel.add(joinLabel, BorderLayout.EAST);

        // Hover effects (simple and syllabus-level)
        panel.addMouseListener(new MouseAdapter() {
            Color defaultBg = panel.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(225, 235, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(defaultBg);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "Join ride with " + ride.getName() + " to " + ride.getRoute() + "?",
                    "Confirm Ride",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    ui.addRideToMyTrips(ride);
                    JOptionPane.showMessageDialog(panel, "Ride added to My Trips!");
                    ui.showScreen("trips");
                }
            }
        });

        return panel;
    }
}
