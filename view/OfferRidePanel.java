package view;

import controller.RideController;
import dao.DriverApplicationDAO;
import model.DriverApplication;
import model.Ride;
import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OfferRidePanel extends JPanel {
    private final RideShareMobileUI ui;
    private JComboBox<String> fromDropdown, toDropdown, dateDropdown, vehicleTypeDropdown;
    private JTextField timeField, seatsField;
    private final RideController controller = new RideController();

    public OfferRidePanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240,245,255));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(8,8,8,8); gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel("         Offer a Ride", SwingConstants.CENTER); title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridwidth = 2; gbc.gridx=0; gbc.gridy=0; add(title, gbc); gbc.gridwidth = 1; gbc.gridy++;

        add(new JLabel("From:"), gbc); gbc.gridy++; fromDropdown = new JComboBox<>(new String[]{"St. Joseph’s College","Pala KSRTC","Bharananganam"}); add(fromDropdown, gbc);
        gbc.gridy++; add(new JLabel("To:"), gbc); gbc.gridy++; toDropdown = new JComboBox<>(new String[]{"Pravithanam","Kottayam RS","Pala KSRTC"}); add(toDropdown, gbc);
        gbc.gridy++; add(new JLabel("Date (yyyy-MM-dd):"), gbc); gbc.gridy++; DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(f); String tomorrow = LocalDate.now().plusDays(1).format(f);
        dateDropdown = new JComboBox<>(new String[]{today, tomorrow}); add(dateDropdown, gbc);
        gbc.gridy++; add(new JLabel("Time (HH:mm):"), gbc); gbc.gridy++; timeField = new JTextField("14:00"); add(timeField, gbc);
        gbc.gridy++; add(new JLabel("Vehicle Type:"), gbc); gbc.gridy++; vehicleTypeDropdown = new JComboBox<>(new String[]{"Car","Bike","Auto"}); add(vehicleTypeDropdown, gbc);
        gbc.gridy++; add(new JLabel("Available Seats:"), gbc); gbc.gridy++; seatsField = new JTextField(5); add(seatsField, gbc);
        gbc.gridy++; JButton submitButton = new JButton("        Offer Ride"); add(submitButton, gbc);

        submitButton.addActionListener(e -> submitRide());
    }

    private void submitRide() {
        String driverId = ui.getCurrentUserId();
        if (driverId == null) { JOptionPane.showMessageDialog(this,"Please login."); return; }
        // check driver application approved
        try {
            DriverApplicationDAO da = new DriverApplicationDAO();
            DriverApplication app = da.findById(driverId);
            if (app == null || !"approved".equalsIgnoreCase(app.getStatus())) {
                JOptionPane.showMessageDialog(this, "Your driver application is not approved yet. You cannot offer rides.","Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String from = (String) fromDropdown.getSelectedItem();
            String to = (String) toDropdown.getSelectedItem();
            LocalDate date = LocalDate.parse((String) dateDropdown.getSelectedItem());
            String time = timeField.getText().trim();
            String vehicleType = (String) vehicleTypeDropdown.getSelectedItem();
            int seats = Integer.parseInt(seatsField.getText().trim());

            Ride r = new Ride();
            r.setDriverId(driverId);
            r.setDriverName(app.getName());
            r.setFromLocation(from);
            r.setToLocation(to);
            r.setDate(date);
            r.setTime(time);
            r.setVehicleType(vehicleType);
            r.setSeatsAvailable(seats);

            boolean ok = controller.offerRide(r);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ride offered successfully!");
                ui.showScreen("providerhome");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to offer ride.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Seats must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
