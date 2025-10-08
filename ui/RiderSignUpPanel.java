// Create new file: ui/RiderSignUpPanel.java
package ui;

import javax.swing.*;
import java.awt.*;

public class RiderSignUpPanel extends JPanel {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField CpasswordField;

    public RiderSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Create Rider Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        CpasswordField = new JPasswordField(15);
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; formPanel.add(CpasswordField, gbc);


        JButton registerButton = new JButton("Create Account");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Rider newRider = new Rider(name, password);
            ApplicationData.riders.add(newRider);
            JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
            ui.showScreen("login");
        });
    }
}