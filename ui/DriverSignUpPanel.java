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

    private static final String DB_URL = "jdbc:mysql://localhost:3306/rideshare";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public DriverSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel(" Driver Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        admissionField = new JTextField(15);
        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        vehicleField = new JTextField(15);
        licenseField = new JTextField(15);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel admissionLabel = new JLabel("Admission No:");
        admissionLabel.setFont(labelFont);
        admissionField.setFont(fieldFont);

        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setFont(labelFont);
        nameField.setFont(fieldFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordField.setFont(fieldFont);

        JLabel vehicleLabel = new JLabel("Vehicle Model:");
        vehicleLabel.setFont(labelFont);
        vehicleField.setFont(fieldFont);

        JLabel licenseLabel = new JLabel("License Number:");
        licenseLabel.setFont(labelFont);
        licenseField.setFont(fieldFont);

        JButton fileButton = new JButton("Upload Driving License");
        fileButton.setBackground(new Color(0, 102, 204));
        fileButton.setForeground(Color.WHITE);
        fileButton.setFont(new Font("Arial", Font.BOLD, 13));
        fileButton.setFocusPainted(false);
        fileButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton registerButton = new JButton("Register for Approval");
        registerButton.setFont(new Font("Arial", Font.BOLD, 15));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0, 102, 204));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(admissionLabel, gbc);
        gbc.gridx = 1; formPanel.add(admissionField, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(vehicleLabel, gbc);
        gbc.gridx = 1; formPanel.add(vehicleField, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(licenseLabel, gbc);
        gbc.gridx = 1; formPanel.add(licenseField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(fileButton, gbc);
        gbc.gridx = 1;
        formPanel.add(fileLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(registerButton, gbc);

        // Add Enter key focus movements
        admissionField.addActionListener(e -> nameField.requestFocusInWindow());
        nameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> vehicleField.requestFocusInWindow());
        vehicleField.addActionListener(e -> licenseField.requestFocusInWindow());
        licenseField.addActionListener(e -> fileButton.requestFocusInWindow());
        // Optional: pressing Enter on fileButton submits form
        fileButton.addActionListener(e -> chooseFile());
        registerButton.addActionListener(e -> registerDriver(ui));

        // Center panel
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(245, 245, 245));
        container.add(formPanel);

        add(container, BorderLayout.CENTER);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            fileLabel.setText(selectedFile.getName());
        }
    }

    private void registerDriver(RideShareMobileUI ui) {
        String id = admissionField.getText().trim();
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String vehicle = vehicleField.getText().trim();
        String license = licenseField.getText().trim();
        String licenseFilePath = (selectedFile != null) ? selectedFile.getAbsolutePath() : null;

        if (id.isEmpty() || name.isEmpty() || password.isEmpty() ||
                vehicle.isEmpty() || license.isEmpty() || licenseFilePath == null) {
            JOptionPane.showMessageDialog(this, "All fields and license file are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String insertUser = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, 'driver')";
            PreparedStatement psUser = con.prepareStatement(insertUser);
            psUser.setString(1, id);
            psUser.setString(2, name);
            psUser.setString(3, password);
            psUser.executeUpdate();

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
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Admission number or username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
