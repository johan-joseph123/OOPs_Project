package view;

import controller.RideController;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * 🚗 Offer Ride Panel — Smart version with Indian time, fixed location, and dynamic seats.
 */
public class OfferRidePanel extends JPanel {
    private final RideShareMobileUI ui;
    private final JComboBox<String> fromField;
    private final JComboBox<String> toField;
    private final JTextField dateField;
    private final JTextField timeField;
    private final JComboBox<String> vehicleTypeField;
    private final JComboBox<String> seatField;
    private final JButton offerButton;

    private final RideController rideController = new RideController();

    public OfferRidePanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🚘 Offer a Ride");
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Fields ===
        fromField = new JComboBox<>(new String[]{"St. Joseph's College"});
        fromField.setEnabled(false); // fixed value

        toField = new JComboBox<>(new String[]{
                "Pala", "Pravithanam", "Kottayam RS", "Erattupetta", "Ponkunnam", "Thodupuzha"
        });

        dateField = new JTextField(LocalDate.now().toString());
        dateField.setEditable(false);

        // Fixed time to 4:40 PM IST
        timeField = new JTextField("16:40");
        timeField.setEditable(false);

        vehicleTypeField = new JComboBox<>(new String[]{"Car", "Bike", "Scooter"});
        seatField = new JComboBox<>();
        updateSeatOptions("Car");

        vehicleTypeField.addActionListener(e -> {
            String selected = (String) vehicleTypeField.getSelectedItem();
            updateSeatOptions(selected);
        });

        offerButton = UIStyleHelper.styleButton(new JButton("🚀 Offer Ride"), UIStyleHelper.PRIMARY_COLOR);

        // === Layout ===
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1; panel.add(fromField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("To:"), gbc);
        gbc.gridx = 1; panel.add(toField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; panel.add(dateField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Time (IST):"), gbc);
        gbc.gridx = 1; panel.add(timeField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1; panel.add(vehicleTypeField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Available Seats:"), gbc);
        gbc.gridx = 1; panel.add(seatField, gbc);
        row++;
        gbc.gridx = 1; gbc.gridy = row; gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(offerButton, gbc);

        add(panel, BorderLayout.CENTER);

        offerButton.addActionListener(e -> submitRide());
    }

    private void updateSeatOptions(String vehicleType) {
        seatField.removeAllItems();
        switch (vehicleType.toLowerCase()) {
            case "car" -> {
                for (int i = 1; i <= 4; i++) seatField.addItem(String.valueOf(i));
            }
            case "bike", "scooter" -> seatField.addItem("1");
        }
    }

    private void submitRide() {
        try {
            String from = "St. Joseph's College";
            String to = (String) toField.getSelectedItem();
            String date = dateField.getText();
            String time = timeField.getText();
            String vehicle = (String) vehicleTypeField.getSelectedItem();
            int seats = Integer.parseInt((String) seatField.getSelectedItem());

            // ✅ Fix: Correct null + empty check syntax
            if (to == null || to.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a destination.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String driverId = ui.getCurrentUserId();
            if (driverId == null) {
                JOptionPane.showMessageDialog(this, "⚠️ You must be logged in as a driver to offer rides.");
                return;
            }

            Ride ride = new Ride(driverId, from, to, date, time, vehicle, seats, "Open");
            boolean success = rideController.createRide(ride);

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Ride successfully offered from " + from + " to " + to + "!");
                ui.showScreen("providerhome");
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠️ You already have an active ride  today!",
                        "Duplicate Ride", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
