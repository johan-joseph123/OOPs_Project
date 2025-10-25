package view;

import controller.BookingController;
import dao.RideDAO;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SearchPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideDAO rideDAO = new RideDAO();
    private final BookingController bookingController = new BookingController();
    private final JPanel resultsPanel;

    public SearchPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🔍 Available Rides");
        add(title, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        add(new JScrollPane(resultsPanel), BorderLayout.CENTER);
        loadAllRides();
    }

    private void loadAllRides() {
        resultsPanel.removeAll();
        try {
            List<Ride> rides = rideDAO.findAllOpenRides();
            if (rides.isEmpty()) {
                resultsPanel.add(UIStyleHelper.createInfoLabel("No available rides right now."));
            } else {
                for (Ride r : rides) {
                    resultsPanel.add(createRideCard(r));
                    resultsPanel.add(Box.createVerticalStrut(8));
                }
            }
        } catch (SQLException e) {
            resultsPanel.add(UIStyleHelper.createInfoLabel("Error: " + e.getMessage()));
        }
        revalidate();
        repaint();
    }

    private JPanel createRideCard(Ride r) {
        JPanel card = UIStyleHelper.createContentPanel("");
        JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b> — " + r.getFromLocation() + " → " +
                r.getToLocation() + "<br/>" + r.getDate() + " " + r.getTime() +
                " • Vehicle: " + r.getVehicleType() + "</html>");
        info.setFont(UIStyleHelper.TEXT_FONT);

        JButton book = UIStyleHelper.styleButton(new JButton("Book Ride"), UIStyleHelper.PRIMARY_COLOR);
        book.addActionListener(e -> {
            try {
                if (!ui.isLoggedIn() || !"rider".equals(ui.getCurrentUserRole())) {
                    JOptionPane.showMessageDialog(this, "Please login as a rider.");
                    return;
                }
                bookingController.bookSeat(r.getRideId(), ui.getCurrentUserId());
                JOptionPane.showMessageDialog(this, "Ride booked successfully!");
                ui.showScreen("riderTrips");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        card.add(info, BorderLayout.CENTER);
        card.add(book, BorderLayout.EAST);
        return card;
    }
}
