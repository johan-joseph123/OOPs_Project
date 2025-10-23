package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OfferRidePanel extends JPanel {
    private final RideShareMobileUI ui;
    private JComboBox<String> fromDropdown;
    private JComboBox<String> toDropdown;
    private JTextField timeField;
    private JComboBox<String> vehicleTypeDropdown;
    private JComboBox<String> dateDropdown;
    private JTextField seatsField;

    private String driverId;
    private String driverName;
    private String vehicleModel;

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

        // From
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
        driverId = ui.getCurrentUserId();

        // Step 1 — Fetch driver info
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT name, vehicle_model, status FROM driver_applications WHERE id=?");
            ps.setString(1, driverId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                if (!"approved".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(this,
                            "Your driver application is not approved yet. You cannot offer rides.",
                            "Access Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                driverName = rs.getString("name");
                vehicleModel = rs.getString("vehicle_model");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Driver record not found. Please re-register or contact admin.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            return;
        }

        // Step 2 — Collect ride info
        String from = (String) fromDropdown.getSelectedItem();
        String to = (String) toDropdown.getSelectedItem();
        String date = (String) dateDropdown.getSelectedItem();
        String time = timeField.getText().trim();
        String vehicleType = (String) vehicleTypeDropdown.getSelectedItem();
        String seatsText = seatsField.getText().trim();

        if (from.isEmpty() || to.isEmpty() || time.isEmpty() || seatsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int seats;
        try {
            seats = Integer.parseInt(seatsText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3 — Insert into rides table
        String insertSql = "INSERT INTO rides (driver_id, driver_name, from_location, to_location, date, time, vehicle_type, seats_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insertSql)) {

            java.sql.Date sqlDate = java.sql.Date.valueOf(convertToISO(date));

            ps.setString(1, driverId);
            ps.setString(2, driverName);
            ps.setString(3, from);
            ps.setString(4, to);
            ps.setDate(5, sqlDate);
            ps.setString(6, time);
            ps.setString(7, vehicleModel); // using actual vehicle model
            ps.setInt(8, seats);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ride offered successfully!");
            ui.showScreen("providerhome");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String convertToISO(String ddMMyyyy) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = input.parse(ddMMyyyy);
            return output.format(parsedDate);
        } catch (Exception e) {
            return "2000-01-01";
        }
    }
}
