package view;

import controller.AuthController;
import dao.DriverApplicationDAO;
import model.DriverApplication;
import model.User;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 🧩 Login screen with driver approval check and role-based navigation
 * ✅ Now also stores username for personalized navbar greetings.
 */
public class LoginPage extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthController auth = new AuthController();

    public LoginPage(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🚗 RideShare Login");
        add(title, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        usernameField = UIStyleHelper.styleTextField(new JTextField(15));
        JLabel passLabel = new JLabel("Password:");
        passwordField = UIStyleHelper.stylePasswordField(new JPasswordField(15));

        JButton loginButton = UIStyleHelper.styleButton(new JButton("Login"), UIStyleHelper.PRIMARY_COLOR);
        JButton signUpButton = UIStyleHelper.styleButton(new JButton("Create an Account"), UIStyleHelper.SECONDARY_COLOR);
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> loginButton.doClick());
        // Layout
        gbc.gridx = 0; gbc.gridy = 0; cardPanel.add(userLabel, gbc);
        gbc.gridx = 1; cardPanel.add(usernameField, gbc);
        gbc.gridy++; gbc.gridx = 0; cardPanel.add(passLabel, gbc);
        gbc.gridx = 1; cardPanel.add(passwordField, gbc);
        gbc.gridy++; gbc.gridx = 1; gbc.insets = new Insets(20, 10, 10, 10);
        cardPanel.add(loginButton, gbc);
        gbc.gridy++; gbc.insets = new Insets(5, 10, 10, 10);
        cardPanel.add(signUpButton, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIStyleHelper.BG_COLOR);
        wrapper.add(cardPanel);
        add(wrapper, BorderLayout.CENTER);

        // === Login Button Logic ===
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                User user = auth.login(username, password);

                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ✅ Save user info globally (for Navbar greeting)
                ui.setCurrentUserId(user.getId());
                ui.setCurrentUserRole(user.getRole());
                ui.setCurrentUsername(user.getUsername());
                ui.setLoggedIn(true);

                // === Driver login approval logic ===
                if ("driver".equalsIgnoreCase(user.getRole())) {
                    DriverApplicationDAO dao = new DriverApplicationDAO();
                    DriverApplication da = dao.findById(user.getId());

                    if (da == null) {
                        JOptionPane.showMessageDialog(this,
                                "You have not applied as a driver yet.\nPlease complete the driver application form.",
                                "Access Denied", JOptionPane.WARNING_MESSAGE);
                        ui.showScreen("driver_application");
                        return;
                    }

                    switch (da.getStatus().toLowerCase()) {
                        case "pending" -> {
                            JOptionPane.showMessageDialog(this,
                                    "⏳ Your driver application is still pending approval by the admin.\nPlease try again later.",
                                    "Not Approved Yet", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        case "rejected" -> {
                            JOptionPane.showMessageDialog(this,
                                    "❌ Your driver application has been rejected.\nContact admin for clarification.",
                                    "Access Denied", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        case "approved" -> {
                            ui.showScreen("providerhome");
                            JOptionPane.showMessageDialog(this,
                                    "✅ Welcome, " + user.getUsername() + "!\nYour driver account is approved.",
                                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        default -> {
                            JOptionPane.showMessageDialog(this,
                                    "Unknown driver application status: " + da.getStatus(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // === Other Roles ===
                switch (user.getRole().toLowerCase()) {
                    case "admin" -> {
                        ui.showScreen("admin");
                        JOptionPane.showMessageDialog(this, "👑 Welcome Admin " + user.getUsername() + "!");
                    }
                    case "rider" -> {
                        ui.showScreen("userhome");
                        JOptionPane.showMessageDialog(this, "🚗 Welcome " + user.getUsername() + "!");
                    }
                    default -> {
                        JOptionPane.showMessageDialog(this, "Unknown role. Contact admin.", "Error", JOptionPane.ERROR_MESSAGE);
                        ui.showScreen("login");
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        signUpButton.addActionListener(e -> ui.showScreen("signup_choice"));
    }
}
