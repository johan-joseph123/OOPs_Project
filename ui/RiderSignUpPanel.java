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
        admission = new JTextField(8);
        passwordField = new JPasswordField(15);
        CpasswordField = new JPasswordField(15);
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Admission no:"), gbc);
        gbc.gridx = 1; formPanel.add(admission, gbc);
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
        
       
        registerButton.addActionListener(e -> {
        		String Id = admission.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(CpasswordField.getPassword());

            if (name.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
              
                String sql = "INSERT INTO users (id,username, password, role) VALUES (?, ?, ?, 'rider')";
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
