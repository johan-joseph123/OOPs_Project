package ui;

import javax.swing.*;
import java.awt.*;

public class MyTripsPanel extends JPanel {
    private final RideShareMobileUI parent;
    private final JPanel tripListPanel;

    public MyTripsPanel(RideShareMobileUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("🧭 My Trips", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(title, BorderLayout.NORTH);

        tripListPanel = new JPanel();
        tripListPanel.setLayout(new BoxLayout(tripListPanel, BoxLayout.Y_AXIS));
        tripListPanel.setBackground(new Color(240,245,255));
        tripListPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JScrollPane scroll = new JScrollPane(tripListPanel);
        add(scroll, BorderLayout.CENTER);

        updateTripList();
    }

    // call this from RideShareMobileUI.showScreen("trips") to refresh
    public void updateTripList() {
        tripListPanel.removeAll();

        if (ApplicationData.myTrips.isEmpty()) {
            JLabel lbl = new JLabel("No trips yet.");
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            tripListPanel.add(Box.createVerticalGlue());
            tripListPanel.add(lbl);
            tripListPanel.add(Box.createVerticalGlue());
        } else {
            for (Ride r : ApplicationData.myTrips) {
                tripListPanel.add(createTripCard(r));
                tripListPanel.add(Box.createVerticalStrut(10));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createTripCard(Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,220,255), 1),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        card.setBackground(Color.WHITE);

        JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b> — " + r.getRoute() +
                                 "<br/>" + r.getDate() + " " + r.getTime() + " • Seats: " + r.getSeatsAvailable() +
                                 " • " + r.getVehicleType() + "</html>");
        card.add(info, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2,1,6,6));
        right.setOpaque(false);

        JLabel status = new JLabel(r.getStatus(), SwingConstants.CENTER);
        status.setForeground(switch (r.getStatus()) {
            case "Completed" -> new Color(40,160,40);
            case "Accepted by Rider" -> new Color(80,120,200);
            default -> Color.DARK_GRAY;
        });
        right.add(status);

        JButton completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(e -> {
            ApplicationData.completeRide(r);
            JOptionPane.showMessageDialog(this, "Ride marked as completed.");
            updateTripList();
        });
        right.add(completeBtn);

        card.add(right, BorderLayout.EAST);
        return card;
    }
}
