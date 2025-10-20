package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RiderSignUpPanel extends JPanel {
    private JTextField nameField;
    private JTextField admission;
    private JPasswordField passwordField;
    private JPasswordField CpasswordField;
    private Connection con;

    public RiderSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // light background

        // ====== HEADER ======
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 152, 219)); // blue shade
        JLabel title = new JLabel("Create Rider Account", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ====== FORM PANEL ======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Fields
        nameField = new JTextField(15);
        admission = new JTextField(8);
        passwordField = new JPasswordField(15);
        CpasswordField = new JPasswordField(15);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        JLabel admLabel = new JLabel("Admission No:");
        admLabel.setFont(labelFont);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        JLabel cpassLabel = new JLabel("Confirm Password:");
        cpassLabel.setFont(labelFont);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(userLabel, gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(admLabel, gbc);
        gbc.gridx = 1; formPanel.add(admission, gbc);

        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(passLabel, gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(cpassLabel, gbc);
        gbc.gridx = 1; formPanel.add(CpasswordField, gbc);

        // ====== BUTTON ======
        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113)); // green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);

        // Center form
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 247, 250));
        centerWrapper.add(formPanel);

        add(centerWrapper, BorderLayout.CENTER);

        // ====== DATABASE CONNECTION ======
        try {
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost:3306/rideshare";

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("Connection established");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
        }

        // ====== ACTION ======
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
                String sql = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, 'rider')";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, Id);
                ps.setString(2, name);
                ps.setString(3, password);
                ps.executeUpdate();
                ps.close();

                JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
                ui.showScreen("login");

            } catch (SQLIntegrityConstraintViolationException exDup) {
                JOptionPane.showMessageDialog(this, "Admission no already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex2) {
                ex2.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex2.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
