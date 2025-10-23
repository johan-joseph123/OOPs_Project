package view;

import controller.AuthController;
import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;

public class RiderSignUpPanel extends JPanel {
    private JTextField nameField, admission;
    private JPasswordField passwordField, CpasswordField;
    private final AuthController auth = new AuthController();

    public RiderSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245,247,250));
        JPanel header = new JPanel(); header.setBackground(new Color(52,152,219));
        JLabel title = new JLabel("Create Rider Account", SwingConstants.CENTER);
        title.setForeground(Color.WHITE); title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title); add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(30,40,30,40), BorderFactory.createLineBorder(new Color(220,220,220),1)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(10,10,10,10); gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(15); admission = new JTextField(8);
        passwordField = new JPasswordField(15); CpasswordField = new JPasswordField(15);

        JLabel userLabel = new JLabel("Username:"); JLabel admLabel = new JLabel("Admission No:");
        JLabel passLabel = new JLabel("Password:"); JLabel cpassLabel = new JLabel("Confirm Password:");
        gbc.gridx=0; gbc.gridy=0; formPanel.add(userLabel, gbc); gbc.gridx=1; formPanel.add(nameField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(admLabel, gbc); gbc.gridx=1; formPanel.add(admission, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(passLabel, gbc); gbc.gridx=1; formPanel.add(passwordField, gbc);
        gbc.gridy++; gbc.gridx=0; formPanel.add(cpassLabel, gbc); gbc.gridx=1; formPanel.add(CpasswordField, gbc);

        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(46,204,113)); registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridy++; gbc.gridx=0; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.CENTER; formPanel.add(registerButton, gbc);

        JPanel centerWrapper = new JPanel(new GridBagLayout()); centerWrapper.setBackground(new Color(245,247,250));
        centerWrapper.add(formPanel); add(centerWrapper, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            String Id = admission.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(CpasswordField.getPassword());
            if (name.isEmpty() || password.isEmpty() || confirm.isEmpty() || Id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                boolean ok = auth.registerRider(Id, name, password);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Account created! Login now.");
                    ui.showScreen("login");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
