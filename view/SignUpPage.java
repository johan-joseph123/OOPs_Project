package view;

import util.UIStyleHelper;
import javax.swing.*;
import java.awt.*;

/**
 * ✨ Modern Sign-Up Page with Gradient Buttons and Clean Layout
 */
public class SignUpPage extends JPanel {

    public SignUpPage(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        // --- Header ---
        JLabel title = UIStyleHelper.createTitle("📝 Create Your Account");
        add(title, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 240), 1),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(16);
        JLabel unameLabel = new JLabel("Username:");
        JTextField unameField = new JTextField(16);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(16);
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Rider", "Driver"});

        JButton signUpBtn = new JButton("✅ Sign Up");
        JButton backBtn = new JButton("← Back");

        // ✅ Style Buttons
        UIStyleHelper.styleButton(signUpBtn, UIStyleHelper.SUCCESS_COLOR);
        UIStyleHelper.styleButton(backBtn, UIStyleHelper.PRIMARY_COLOR);
        nameField.addActionListener(e -> unameField.requestFocusInWindow());
        unameField.addActionListener(e -> passField.requestFocusInWindow());
        passField.addActionListener(e -> roleBox.requestFocusInWindow());

        // When Enter pressed in role dropdown → Click Sign Up
        roleBox.addActionListener(e -> signUpBtn.doClick());


        // --- Add components ---
        gbc.gridx = 0; gbc.gridy = 0; form.add(nameLabel, gbc);
        gbc.gridx = 1; form.add(nameField, gbc);

        gbc.gridy++; gbc.gridx = 0; form.add(unameLabel, gbc);
        gbc.gridx = 1; form.add(unameField, gbc);

        gbc.gridy++; gbc.gridx = 0; form.add(passLabel, gbc);
        gbc.gridx = 1; form.add(passField, gbc);

        gbc.gridy++; gbc.gridx = 0; form.add(roleLabel, gbc);
        gbc.gridx = 1; form.add(roleBox, gbc);

        gbc.gridy++; gbc.gridx = 1; gbc.insets = new Insets(20, 10, 10, 10);
        form.add(signUpBtn, gbc);

        gbc.gridy++; form.add(backBtn, gbc);

        // --- Center it nicely ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIStyleHelper.BG_COLOR);
        centerPanel.add(form);
        add(centerPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        signUpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "✅ Sign-up successful! Connect backend to register.")
        );

        backBtn.addActionListener(e -> ui.showScreen("login"));
    }
}
