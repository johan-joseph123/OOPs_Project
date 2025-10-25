package view;

import controller.BookingController;
import dao.RideDAO;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ResultPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideDAO rideDAO = new RideDAO();
    private final BookingController bookingController = new BookingController();
    private final JPanel resultsPanel;
    private final String destination;
    private final String date;

    public ResultPanel(RideShareMobileUI ui, String destination, String date) {
        this.ui = ui;
        this.destination = destination;
        this.date = date;

        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🧾 Rides to " + destination + " (" + date + ")");
        add(title, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        add(new JScrollPane(resultsPanel), BorderLayout.CENTER);

        JButton backButton = UIStyleHelper.styleButton(new JButton("⬅ Back"), UIStyleHelper.SECONDARY_COLOR);
        backButton.addActionListener(e -> ui.showScreen("search"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadRides();
    }

    private void loadRides() {
        resultsPanel.removeAll();
        try {
            List<Ride> rides = rideDAO.findRidesByDestinationAndDate(destination, date);
            if (rides.isEmpty()) {
                resultsPanel.add(UIStyleHelper.createInfoLabel("😔 No rides found for this route."));
            } else {
                for (Ride r : rides) {
                    resultsPanel.add(createRideCard(r));
                    resultsPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (SQLException e) {
            resultsPanel.add(UIStyleHelper.createInfoLabel("❌ Database Error: " + e.getMessage()));
        }
        revalidate();
        repaint();
    }

    private JPanel createRideCard(Ride r) {
        JPanel card = UIStyleHelper.createContentPanel("");
        JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b><br/>" +
                r.getFromLocation() + " → " + r.getToLocation() + "<br/>" +
                r.getDate() + " " + r.getTime() + " • " + r.getVehicleType() +
                "<br/>Seats left: " + r.getSeatsAvailable() +
                "</html>");
        info.setFont(UIStyleHelper.TEXT_FONT);

        JButton bookBtn;
        if ("Open".equalsIgnoreCase(r.getStatus())) {
            bookBtn = UIStyleHelper.styleButton(new JButton("🚗 Book Ride"), UIStyleHelper.PRIMARY_COLOR);
            bookBtn.addActionListener(e -> bookRide(r));
        } else {
            bookBtn = UIStyleHelper.styleButton(new JButton("❌ Full"), UIStyleHelper.PRIMARY_COLOR);
            bookBtn.setEnabled(false);
        }

        card.add(info, BorderLayout.CENTER);
        card.add(bookBtn, BorderLayout.EAST);
        return card;
    }

    private void bookRide(Ride r) {
        try {
            if (!ui.isLoggedIn() || !"rider".equals(ui.getCurrentUserRole())) {
                JOptionPane.showMessageDialog(this, "⚠️ Please login as a rider to book a ride.");
                return;
            }
            bookingController.bookSeat(r.getRideId(), ui.getCurrentUserId());
            JOptionPane.showMessageDialog(this, "✅ Ride booked successfully!");
            ui.showScreen("riderTrips");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
