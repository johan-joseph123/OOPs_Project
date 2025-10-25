
package view;

import controller.BookingController;
import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RiderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final BookingController bookingController = new BookingController();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RideDAO rideDAO = new RideDAO();
    private final JPanel tripList;

    public RiderTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🧍‍♂️ My Booked Rides");
        add(title, BorderLayout.NORTH);

        tripList = new JPanel();
        tripList.setLayout(new BoxLayout(tripList, BoxLayout.Y_AXIS));
        tripList.setBackground(Color.WHITE);

        add(new JScrollPane(tripList), BorderLayout.CENTER);
    }

    public void refreshData() {
        tripList.removeAll();
        String id = ui.getCurrentUserId();
        try {
            List<Booking> bookings = bookingDAO.findByRider(id);
            if (bookings.isEmpty()) {
                tripList.add(UIStyleHelper.createInfoLabel("No rides booked yet."));
            } else {
                for (Booking b : bookings) {
                	Ride r = rideDAO.findById((int) b.getRideId());

                    tripList.add(createCard(b, r));
                    tripList.add(Box.createVerticalStrut(8));
                }
            }
        } catch (SQLException e) {
            tripList.add(UIStyleHelper.createInfoLabel("Error: " + e.getMessage()));
        }
        revalidate();
        repaint();
    }

    private JPanel createCard(Booking b, Ride r) {
        JPanel card = UIStyleHelper.createContentPanel("");
        String text = "<html><b>" + r.getFromLocation() + " → " + r.getToLocation() +
                "</b><br/>" + r.getDate() + " " + r.getTime() +
                " • Status: " + b.getStatus() + "</html>";
        JLabel info = new JLabel(text);
        info.setFont(UIStyleHelper.TEXT_FONT);

        JButton complete = UIStyleHelper.styleButton(new JButton("Mark Completed"), UIStyleHelper.SUCCESS_COLOR);
        complete.addActionListener(e -> {
            try {
                bookingController.completeBooking(b.getId());
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        card.add(info, BorderLayout.CENTER);
        card.add(complete, BorderLayout.EAST);
        return card;
    }
}
