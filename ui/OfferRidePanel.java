package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class OfferRidePanel extends JPanel {
    
    private final RideShareMobileUI ui; // Made final and accessible
    private JLabel imageLabel;
    private JTextField seatsField;
    private JTextField numberField;
    private JComboBox<String> placeDropdown;
    private JTextField timeField;
    private JComboBox<String> vehicleTypeDropdown;

    public OfferRidePanel(RideShareMobileUI ui) {
        this.ui = ui; // Store the reference
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Ensures components expand horizontally
        
        JLabel title = new JLabel("Offer a Ride", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridwidth = 2; // Span two columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);
        
        gbc.gridwidth = 1; // Reset to single column
        gbc.gridy++;
        
        // 1. Upload vehicle photo
        JButton uploadButton = new JButton("Upload Vehicle Photo");
        imageLabel = new JLabel("No file chosen", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy++;
        add(uploadButton, gbc);
        gbc.gridy++;
        add(imageLabel, gbc);

        uploadButton.addActionListener(e -> chooseImage());
        gbc.gridy++;
        add(new JLabel("number plate number:"), gbc);
        gbc.gridy++;
        numberField = new JTextField(10);
        add(numberField, gbc);
        
        
        gbc.gridy++;
        add(new JLabel("Available Seats:"), gbc);
        gbc.gridy++;
        seatsField = new JTextField(10);
        add(seatsField, gbc);

        // 3. Drop-down of places
        gbc.gridy++;
        add(new JLabel("Select Place (Destination):"), gbc);
        gbc.gridy++;
        String[] places = {"Bharananganam", "Pala KSRTC", "Pravithanam", "Kottayam RS"};
        placeDropdown = new JComboBox<>(places);
        add(placeDropdown, gbc);

        // 4. Time box
        gbc.gridy++;
        add(new JLabel("Pickup Time (HH:mm):"), gbc);
        gbc.gridy++;
        timeField = new JTextField("14:00", 10);
        add(timeField, gbc);

        // 5. Vehicle type dropdown
        gbc.gridy++;
        add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridy++;
        String[] vehicleTypes = {"Car", "Bike", "Auto"};
        vehicleTypeDropdown = new JComboBox<>(vehicleTypes);
        add(vehicleTypeDropdown, gbc);

        // 6. Submit button
        gbc.gridy++;
        JButton submitButton = new JButton("Offer Ride");
        gbc.ipady = 10; // Add some padding
        add(submitButton, gbc);

        submitButton.addActionListener(e -> submitRide());
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            imageLabel.setText(selected.getName());
        }
    }

    private void submitRide() {
        String seatsText = seatsField.getText().trim();
        String place = (String) placeDropdown.getSelectedItem();
        String time = timeField.getText().trim();
        String vehicleType = (String) vehicleTypeDropdown.getSelectedItem();
        String image = imageLabel.getText();
        int seats;

        if (seatsText.isEmpty() || time.isEmpty() || place == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            seats = Integer.parseInt(seatsText);
            if (seats <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Available Seats must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the Ride object (Note: "You" as the name for the current user's offered ride)
        // For the offered ride, the 'route' is the destination place.
        Ride ride = new Ride("You", vehicleType, time, seats, place);
        ui.addRideToMyTrips(ride);

        JOptionPane.showMessageDialog(this,
            "Ride Offered and added to My Trips!\n" +
            "Destination: " + place + "\n" +
            "Time: " + time,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate back to Home or My Trips
        ui.showScreen("home");
    }
}