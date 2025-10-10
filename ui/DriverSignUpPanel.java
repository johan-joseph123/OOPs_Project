package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class DriverSignUpPanel extends JPanel {
    private JTextField nameField, vehicleField, licenseField, admissionField;
    private JPasswordField passwordField;
    private JLabel fileLabel;
    private File selectedFile;
    private Connection con;

    // DB credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rideshare";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

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

        // Fields
        admissionField = new JTextField(15);
        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        vehicleField = new JTextField(15);
        licenseField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Admission No:"), gbc);
        gbc.gridx = 1; formPanel.add(admissionField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Vehicle Model:"), gbc);
        gbc.gridx = 1; formPanel.add(vehicleField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("License Number:"), gbc);
        gbc.gridx = 1; formPanel.add(licenseField, gbc);
        gbc.gridy++;

        JButton fileButton = new JButton("Upload Driving License");
        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        formPanel.add(fileButton, gbc);
        gbc.gridx = 1;
        formPanel.add(fileLabel, gbc);

        fileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                fileLabel.setText(selectedFile.getName());
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton registerButton = new JButton("Register for Approval");
        formPanel.add(registerButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Register button action
        registerButton.addActionListener(e -> registerDriver(ui));
    }

    private void registerDriver(RideShareMobileUI ui) {
        String id = admissionField.getText().trim();
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String vehicle = vehicleField.getText().trim();
        String license = licenseField.getText().trim();
        String licenseFilePath = (selectedFile != null) ? selectedFile.getAbsolutePath() : null;

        if (id.isEmpty() || name.isEmpty() || password.isEmpty() || vehicle.isEmpty() || license.isEmpty() || licenseFilePath == null) {
            JOptionPane.showMessageDialog(this, "All fields and license file are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Insert into users table
            String insertUser = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, 'driver')";
            PreparedStatement psUser = con.prepareStatement(insertUser);
            psUser.setString(1, id);
            psUser.setString(2, name);
            psUser.setString(3, password);
            psUser.executeUpdate();

            // Insert into driver_applications table
            String insertApp = "INSERT INTO driver_applications (id, name, vehicle_model, license_number, license_file, status) VALUES (?, ?, ?, ?, ?, 'pending')";
            PreparedStatement psApp = con.prepareStatement(insertApp);
            psApp.setString(1, id);
            psApp.setString(2, name);
            psApp.setString(3, vehicle);
            psApp.setString(4, license);
            psApp.setString(5, licenseFilePath);
            psApp.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration submitted! Please wait for admin approval.");
            ui.showScreen("login");
            con.close();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Admission number or username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
