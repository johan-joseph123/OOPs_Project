// Rename SignUpPage.java -> DriverSignUpPanel.java and replace its content
package ui;

import javax.swing.*;
import java.awt.*;

public class DriverSignUpPanel extends JPanel {
    private JTextField nameField, vehicleField, licenseField;
    private JPasswordField passwordField;

    public DriverSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Driver Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add fields for Name, Password, Vehicle Model, License Plate
        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        vehicleField = new JTextField(15);
        licenseField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Vehicle Model:"), gbc);
        gbc.gridx = 1; formPanel.add(vehicleField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("License Plate:"), gbc);
        gbc.gridx = 1; formPanel.add(licenseField, gbc);
        
        JButton registerButton = new JButton("Register for Approval");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String vehicle = vehicleField.getText().trim();
            String license = licenseField.getText().trim();

            if (name.isEmpty() || password.isEmpty() || vehicle.isEmpty() || license.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Driver newDriver = new Driver(name, password, vehicle, license);
            ApplicationData.pendingDrivers.add(newDriver);
            JOptionPane.showMessageDialog(this, "Registration submitted! Please wait for admin approval.");
            ui.showScreen("login");
        });
    }
}