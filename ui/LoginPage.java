package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JPanel {
	
    private  JTextField usernameField;
    private  JPasswordField passwordField;
    private  JButton loginButton;          
    private JButton signUpButton;

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

        // Row 0: Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);

        // Row 1: Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        // Row 2: Login Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(loginButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 8, 8);
        signUpButton.setBackground(getBackground());
        signUpButton.setBorderPainted(false);
        panel.add(signUpButton,gbc);
        
        

        loginButton.addActionListener(e -> performLogin(ui));
     // In LoginPage.java, inside the initComponents method:
     // Change the action listener for the sign-up button
     signUpButton.addActionListener(e -> ui.showScreen("signup_choice")); // Was "signup"
        
        return panel;
    }

 // In file ui/LoginPage.java, replace the performLogin method
    private void performLogin(RideShareMobileUI ui) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 1. Check for Admin
        if (username.equals("admin") && password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Admin login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            ui.showScreen("admin");
            passwordField.setText("");
            return;
        }

        // 2. Check for approved Drivers
        for (Driver driver : ApplicationData.pendingDrivers) {
            if (driver.getName().equals(username) && driver.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(this, "Driver login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // We should create a dedicated driver dashboard, but for now, home works
                ui.showScreen("providerhome");
                

                passwordField.setText("");
                return;
            }
        }

        // 3. Check for Riders
        for (Rider rider : ApplicationData.riders) {
            if (rider.getName().equals(username) && rider.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                ui.showScreen("userhome");
                passwordField.setText("");
                return;
            }
        }

        // 4. If no user was found
        JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        passwordField.setText("");
    }
}