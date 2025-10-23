package view;

import controller.RideController;
import dao.RideDAO;
import model.Ride;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProviderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideController rideController = new RideController();
    private final JPanel tripListPanel;

    public ProviderTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("Offered Rides", SwingConstants.CENTER);
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
        String driverId = ui.getCurrentUserId();

        if (driverId == null) {
            tripListPanel.add(new JLabel("Please log in as a driver."));
            refresh();
            return;
        }

        try {
            List<Ride> rides = rideController.getProviderRides(driverId);
            if (rides.isEmpty()) {
                tripListPanel.add(new JLabel("You haven’t offered any rides yet."));
            } else {
                for (Ride r : rides) {
                    tripListPanel.add(createRideCard(r));
                    tripListPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (SQLException e) {
            tripListPanel.add(new JLabel("Database error: " + e.getMessage()));
        }

        refresh();
    }

    private JPanel createRideCard(Ride r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 255), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel info = new JLabel("<html><b>" + r.getFromLocation() + " → " + r.getToLocation() +
                "</b><br/>" + r.getDate() + " " + r.getTime() +
                " • Status: " + r.getStatus() + "</html>");
        card.add(info, BorderLayout.CENTER);

        JButton completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(e -> {
            try {
                if (rideController.markRideCompleted(r.getRideId())) {
                    JOptionPane.showMessageDialog(this, "Ride marked as completed!");
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
