package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OfferRidePanel extends JPanel {
    private final RideShareMobileUI ui;
    private JTextField driverNameField;
    private JTextField seatsField;
    private JComboBox<String> fromDropdown;
    private JComboBox<String> toDropdown;
    private JTextField timeField;
    private JComboBox<String> vehicleTypeDropdown;
    private JComboBox<String> dateDropdown;

    public OfferRidePanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 245, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("🚗 Offer a Ride", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Driver Name
        add(new JLabel("Driver Name:"), gbc);
        gbc.gridy++;
        driverNameField = new JTextField(15);
        add(driverNameField, gbc);

        // From
        gbc.gridy++;
        add(new JLabel("From:"), gbc);
        gbc.gridy++;
        fromDropdown = new JComboBox<>(new String[]{"St. Joseph’s College", "Pala KSRTC", "Bharananganam"});
        add(fromDropdown, gbc);

        // To
        gbc.gridy++;
        add(new JLabel("To:"), gbc);
        gbc.gridy++;
        toDropdown = new JComboBox<>(new String[]{"Pravithanam", "Kottayam RS", "Pala KSRTC"});
        add(toDropdown, gbc);

        // Date
        gbc.gridy++;
        add(new JLabel("Date:"), gbc);
        gbc.gridy++;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        String tomorrow = sdf.format(cal.getTime());
        dateDropdown = new JComboBox<>(new String[]{today, tomorrow});
        add(dateDropdown, gbc);

        // Time
        gbc.gridy++;
        add(new JLabel("Time (HH:mm):"), gbc);
        gbc.gridy++;
        timeField = new JTextField("14:00");
        add(timeField, gbc);

        // Vehicle Type
        gbc.gridy++;
        add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridy++;
        vehicleTypeDropdown = new JComboBox<>(new String[]{"Car", "Bike", "Auto"});
        add(vehicleTypeDropdown, gbc);

        // Seats
        gbc.gridy++;
        add(new JLabel("Available Seats:"), gbc);
        gbc.gridy++;
        seatsField = new JTextField(5);
        add(seatsField, gbc);

        // Submit
        gbc.gridy++;
        JButton submitButton = new JButton("🚀 Offer Ride");
        add(submitButton, gbc);

        submitButton.addActionListener(e -> submitRide());
    }

    private void submitRide() {
        String driverName = (driverNameField != null) ? driverNameField.getText().trim() : "Driver";
        String from = (fromDropdown != null) ? (String) fromDropdown.getSelectedItem() : "St. Joseph's College";
        String to = (toDropdown != null) ? (String) toDropdown.getSelectedItem() : "";
        String date = (dateDropdown != null) ? (String) dateDropdown.getSelectedItem() : "";
        String time = (timeField != null) ? timeField.getText().trim() : "";
        String vehicleType = (vehicleTypeDropdown != null) ? (String) vehicleTypeDropdown.getSelectedItem() : "";
        String seatsText = (seatsField != null) ? seatsField.getText().trim() : "";

        if (driverName.isEmpty() || from.isEmpty() || to.isEmpty() || time.isEmpty() || seatsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int seats;
        try {
            seats = Integer.parseInt(seatsText);
            if (seats <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ride ride = new Ride(driverName, vehicleType, time, seats, from, to, date);
        ApplicationData.addRide(ride);

        JOptionPane.showMessageDialog(this, "Ride offered successfully!");
        // If provider UI exists, go to providerhome; else go to userhome
        ui.showScreen("providerhome");
    }

}
