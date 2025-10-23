package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/rideshare";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public LoginPage(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // light gray background

        JLabel title = new JLabel(" RideShare Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204)); // blue text
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel loginPanel = initComponents(ui);
        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel initComponents(RideShareMobileUI ui) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        passwordField.addActionListener(e -> performLogin(ui));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        signUpButton = new JButton("Create an Account");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 13));
        signUpButton.setForeground(new Color(0, 102, 204));
        signUpButton.setBackground(Color.WHITE);
        signUpButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        signUpButton.setFocusPainted(false);

        // Layout setup
        gbc.gridx = 0; gbc.gridy = 0; cardPanel.add(userLabel, gbc);
        gbc.gridx = 1; cardPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; cardPanel.add(passLabel, gbc);
        gbc.gridx = 1; cardPanel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        cardPanel.add(loginButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        cardPanel.add(signUpButton, gbc);

        // Button actions
        loginButton.addActionListener(e -> performLogin(ui));
        signUpButton.addActionListener(e -> ui.showScreen("signup_choice"));

        // Center alignment container
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(245, 245, 245));
        container.add(cardPanel);

        return container;
    }

    private void performLogin(RideShareMobileUI ui) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String id = rs.getString("id");

                // ✅ Store login info globally
                ui.setCurrentUser(id, role);

                switch (role) {
                    case "admin" -> {
                        JOptionPane.showMessageDialog(this, "Admin login successful!");
                        ui.setLoggedIn(true);
                        ui.showScreen("admin");
                    }

                    case "rider" -> {
                        JOptionPane.showMessageDialog(this, "Rider login successful!");
                        ui.setLoggedIn(true);
                        ui.showScreen("userhome");
                    }

                    case "driver" -> {
                        String check = "SELECT status FROM driver_applications WHERE id = ?";
                        PreparedStatement ps2 = con.prepareStatement(check);
                        ps2.setString(1, id);
                        ResultSet rs2 = ps2.executeQuery();

                        if (rs2.next()) {
                            String status = rs2.getString("status");
                            if (status.equals("approved")) {
                                JOptionPane.showMessageDialog(this, "Driver login successful!");
                                ui.setLoggedIn(true);
                                ui.showScreen("providerhome");
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Your application is " + status + ". Please wait for admin approval.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No driver application found.");
                        }
                        rs2.close();
                        ps2.close();
                    }
                }
            

            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
