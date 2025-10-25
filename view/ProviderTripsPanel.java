package view;

import controller.RideController;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProviderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideController controller = new RideController();
    private final JPanel tripList;

    public ProviderTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🚗 Your Offered Rides");
        add(title, BorderLayout.NORTH);

        tripList = new JPanel();
        tripList.setLayout(new BoxLayout(tripList, BoxLayout.Y_AXIS));
        tripList.setBackground(Color.WHITE);

        add(new JScrollPane(tripList), BorderLayout.CENTER);
    }

    public void refreshData() throws SQLException {
        tripList.removeAll();
        try {
            List<Ride> rides = controller.getProviderRides(ui.getCurrentUserId());
            if (rides.isEmpty()) {
                tripList.add(UIStyleHelper.createInfoLabel("You haven’t offered any rides yet."));
            } else {
                for (Ride r : rides) {
                    tripList.add(createCard(r));
                    tripList.add(Box.createVerticalStrut(8));
                }
            }
        } catch (SQLException e) {
            tripList.add(UIStyleHelper.createInfoLabel("Error: " + e.getMessage()));
        }
        revalidate();
        repaint();
    }


    private JPanel createCard(Ride r) {
        JPanel card = UIStyleHelper.createContentPanel("");
        JLabel info = new JLabel("<html><b>" + r.getFromLocation() + " → " + r.getToLocation() +
                "</b><br/>" + r.getDate() + " " + r.getTime() +
                " • Status: " + r.getStatus() + "</html>");
        info.setFont(UIStyleHelper.TEXT_FONT);

        JButton done = UIStyleHelper.styleButton(new JButton("Mark Completed"), UIStyleHelper.SUCCESS_COLOR);
        done.addActionListener(e -> {
            try {
                //controller.markRideCompleted(r.getRideId());
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        card.add(info, BorderLayout.CENTER);
        card.add(done, BorderLayout.EAST);
        return card;
    }
}
