package view;

import controller.AuthController;
import dao.DriverApplicationDAO;
import model.DriverApplication;
import model.User;

import javax.swing.*;
import java.awt.*;

public class DriverSignUpPanel extends JPanel {
    private JTextField idField, nameField, vehicleField, licenseField;
    private JPasswordField passwordField;
    private JButton uploadButton, submitButton;
    private String licenseFilePath;
    private final DriverApplicationDAO dao = new DriverApplicationDAO();
    private final AuthController auth = new AuthController();
    private final RideShareMobileUI ui;

    public DriverSignUpPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Driver Application", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(15);
        nameField = new JTextField(15);
        vehicleField = new JTextField(15);
        licenseField = new JTextField(15);
        passwordField = new JPasswordField(15);
        uploadButton = new JButton("Upload License");
        submitButton = new JButton("Submit Application");

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Admission/Driver ID:"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);

        gbc.gridy++; gbc.gridx = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        gbc.gridy++; gbc.gridx = 0; formPanel.add(new JLabel("Vehicle Model:"), gbc);
        gbc.gridx = 1; formPanel.add(vehicleField, gbc);

        gbc.gridy++; gbc.gridx = 0; formPanel.add(new JLabel("License Number:"), gbc);
        gbc.gridx = 1; formPanel.add(licenseField, gbc);

        gbc.gridy++; gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridy++; gbc.gridx = 1; formPanel.add(uploadButton, gbc);

        gbc.gridy++; gbc.gridx = 1; formPanel.add(submitButton, gbc);

        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                licenseFilePath = chooser.getSelectedFile().getAbsolutePath();
                JOptionPane.showMessageDialog(this, "File selected: " + licenseFilePath);
            }
        });

        submitButton.addActionListener(e -> submitApplication());

        add(formPanel, BorderLayout.CENTER);
    }

    private void submitApplication() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String vehicle = vehicleField.getText().trim();
        String license = licenseField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (id.isEmpty() || name.isEmpty() || vehicle.isEmpty() || license.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try {
            // 1️⃣ Create a user first
            User newUser = new User(id, name, password, "driver");
            boolean userCreated = auth.registerDriver(newUser);

            if (!userCreated) {
                JOptionPane.showMessageDialog(this, "User already exists or failed to create user.");
                return;
            }

            // 2️⃣ Create driver application
            DriverApplication da = new DriverApplication(id, name, vehicle, license, licenseFilePath, "pending");
            boolean ok = dao.createApplication(da);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Application submitted! Wait for admin approval.");
                ui.showScreen("login");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
