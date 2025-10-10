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
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel loginPanel = initComponents(ui);
        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel initComponents(RideShareMobileUI ui) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton = new JButton("SignUp");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(loginButton, gbc);

        gbc.gridy++;
        signUpButton.setBackground(getBackground());
        signUpButton.setBorderPainted(false);
        panel.add(signUpButton, gbc);

        loginButton.addActionListener(e -> performLogin(ui));
        signUpButton.addActionListener(e -> ui.showScreen("signup_choice"));

        return panel;
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

                if (role.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Admin login successful!");
                    ui.showScreen("admin");
                } 
                else if (role.equals("rider")) {
                    JOptionPane.showMessageDialog(this, "Rider login successful!");
                    ui.showScreen("userhome");
                } 
                else if (role.equals("driver")) {
                    // Check if driver is approved
                    String check = "SELECT status FROM driver_applications WHERE id = ?";
                    PreparedStatement ps2 = con.prepareStatement(check);
                    ps2.setString(1, id);
                    ResultSet rs2 = ps2.executeQuery();

                    if (rs2.next()) {
                        String status = rs2.getString("status");
                        if (status.equals("approved")) {
                            JOptionPane.showMessageDialog(this, "Driver login successful!");
                            ui.showScreen("providerhome");
                        } else {
                            JOptionPane.showMessageDialog(this, "Your application is " + status + ". Please wait for admin approval.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No driver application found.");
                    }
                    rs2.close();
                    ps2.close();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
