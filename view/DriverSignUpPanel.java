package view;

import dao.DriverApplicationDAO;
import model.DriverApplication;
import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;

public class DriverSignUpPanel extends JPanel {
    private JTextField admissionField, nameField, vehicleField, licenseField;
    private JPasswordField passwordField;
    private JLabel fileLabel;
    private java.io.File selectedFile;
    private final DriverApplicationDAO dao = new DriverApplicationDAO();

    public DriverSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));
        JLabel title = new JLabel(" Driver Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24)); title.setForeground(new Color(0,102,204));
        title.setBorder(BorderFactory.createEmptyBorder(30,0,20,0)); add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,200),1), BorderFactory.createEmptyBorder(30,40,30,40)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets=new Insets(10,10,10,10); gbc.fill = GridBagConstraints.HORIZONTAL;

        admissionField = new JTextField(15); nameField = new JTextField(15); passwordField = new JPasswordField(15);
        vehicleField = new JTextField(15); licenseField = new JTextField(15);
        JLabel admissionLabel = new JLabel("Admission No:"); JLabel nameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:"); JLabel vehicleLabel = new JLabel("Vehicle Model:");
        JLabel licenseLabel = new JLabel("License Number:");
        JButton fileButton = new JButton("Upload Driving License"); fileLabel = new JLabel("No file selected");
        JButton registerButton = new JButton("Register for Approval");

        gbc.gridx=0; gbc.gridy=0; formPanel.add(admissionLabel, gbc); gbc.gridx=1; formPanel.add(admissionField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(nameLabel, gbc); gbc.gridx=1; formPanel.add(nameField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(passwordLabel, gbc); gbc.gridx=1; formPanel.add(passwordField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(vehicleLabel, gbc); gbc.gridx=1; formPanel.add(vehicleField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(licenseLabel, gbc); gbc.gridx=1; formPanel.add(licenseField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(fileButton, gbc); gbc.gridx=1; formPanel.add(fileLabel, gbc);
        gbc.gridy++; gbc.gridx=0; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(20,10,10,10);
        formPanel.add(registerButton, gbc);

        fileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                fileLabel.setText(selectedFile.getName());
            }
        });

        registerButton.addActionListener(e -> {
            String id = admissionField.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String vehicle = vehicleField.getText().trim();
            String license = licenseField.getText().trim();
            String licenseFilePath = selectedFile != null ? selectedFile.getAbsolutePath() : null;
            if (id.isEmpty() || name.isEmpty() || password.isEmpty() || vehicle.isEmpty() || license.isEmpty() || licenseFilePath == null) {
                JOptionPane.showMessageDialog(this, "All fields and license file are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
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
        });

        JPanel container = new JPanel(new GridBagLayout()); container.setBackground(new Color(245,245,245)); container.add(formPanel);
        add(container, BorderLayout.CENTER);
    }
}
