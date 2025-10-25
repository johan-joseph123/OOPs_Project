package view;

import controller.RideController;
import dao.BookingDAO;
import dao.DBConnection;
import model.Booking;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class ProviderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideController controller = new RideController();
    private final BookingDAO bookingDAO = new BookingDAO();
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

    public void refreshData() {
        tripList.removeAll();
        List<Ride> rides = controller.getProviderRides(ui.getCurrentUserId());
		if (rides == null || rides.isEmpty()) {
		    tripList.add(UIStyleHelper.createInfoLabel("You haven’t offered any rides yet."));
		} else {
		    for (Ride r : rides) {
		        tripList.add(createCard(r));
		        tripList.add(Box.createVerticalStrut(8));
		    }
		}
        revalidate();
        repaint();
    }

    private JPanel createCard(Ride r) {
        JPanel card = UIStyleHelper.createContentPanel("");
        JLabel info = new JLabel("<html><b>" + r.getFromLocation() + " → " + r.getToLocation() +
                "</b><br/>" + r.getDate() + " " + r.getTime() +
                " • Status: " + r.getStatus() +
                "<br/>Seats left: " + r.getSeatsAvailable() + "</html>");
        info.setFont(UIStyleHelper.TEXT_FONT);

        // === Buttons Section ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton viewRiders = UIStyleHelper.styleButton(new JButton("👥 View Riders"), UIStyleHelper.PRIMARY_COLOR);
        viewRiders.addActionListener(e -> showRidersForRide(r.getRideId()));

        JButton done = UIStyleHelper.styleButton(new JButton("✅ Mark Completed"), UIStyleHelper.SUCCESS_COLOR);
        done.addActionListener(e -> {
            try {
                controller.markRideCompleted(r.getRideId());
                JOptionPane.showMessageDialog(this, "Ride marked as completed!");
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        buttonPanel.add(viewRiders);
        buttonPanel.add(done);

        card.add(info, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    /**
     * 🧾 Shows all riders who booked a given ride.
     */
    private void showRidersForRide(int rideId) {
        try {
        	List<String> riders = bookingDAO.findRidersWithNamesByRideId(rideId);
        	if (riders.isEmpty()) {
        	    JOptionPane.showMessageDialog(this, "No riders have booked this ride yet.");
        	} else {
        	    String list = String.join("<br/>", riders);
        	    JOptionPane.showMessageDialog(this, "<html><b>Riders:</b><br/>" + list + "</html>");
        	}

           

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading riders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper to get rider's username from users table.
     */
    private String getRiderName(Connection conn, String riderId) throws SQLException {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, riderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return "Unknown Rider";
    }
}
