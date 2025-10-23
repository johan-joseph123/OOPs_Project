package view;

import controller.AuthController;
import model.User;
import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final AuthController auth = new AuthController();

    public LoginPage(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));
        JLabel title = new JLabel(" RideShare Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(0,102,204));
        title.setBorder(BorderFactory.createEmptyBorder(30,0,20,0));
        add(title, BorderLayout.NORTH);

        JPanel loginPanel = initComponents(ui);
        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel initComponents(RideShareMobileUI ui) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,200),1), BorderFactory.createEmptyBorder(30,40,30,40)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Create an Account");

        // layout
        gbc.gridx = 0; gbc.gridy = 0; cardPanel.add(userLabel, gbc);
        gbc.gridx = 1; cardPanel.add(usernameField, gbc);
        gbc.gridy++; gbc.gridx=0; cardPanel.add(passLabel, gbc);
        gbc.gridx=1; cardPanel.add(passwordField, gbc);
        gbc.gridy++; gbc.gridx=1; gbc.insets = new Insets(20,10,5,10);
        cardPanel.add(loginButton, gbc);
        gbc.gridy++; gbc.insets = new Insets(5,10,10,10);
        cardPanel.add(signUpButton, gbc);

        loginButton.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword()).trim();
            if (u.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter credentials"); return; }
            try {
                User user = auth.login(u, p);
                ui.setCurrentUser(user.getId(), user.getRole());
                ui.setLoggedIn(true);
                switch (user.getRole()) {
                    case "admin": ui.showScreen("admin"); break;
                    case "rider": ui.showScreen("userhome"); break;
                    case "driver":
                        // check driver application status
                        controller.AdminController admin = new controller.AdminController();
                        // we will check driver_applications status via DAO inside OfferRidePanel when needed
                        ui.showScreen("providerhome");
                        break;
                    default: ui.showScreen("login");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> ui.showScreen("signup_choice"));

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(245,245,245));
        container.add(cardPanel);
        return container;
    }
}
