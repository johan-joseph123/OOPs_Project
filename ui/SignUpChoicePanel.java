package ui;

import javax.swing.*;
import java.awt.*;

public class SignUpChoicePanel extends JPanel {

    public SignUpChoicePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== Title =====
        JLabel title = new JLabel("Join Campus RideShare", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel subtitle = new JLabel("Choose your role:", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(subtitle, gbc);

        // ===== Buttons =====
        JButton driverBtn = new JButton(" Driver");
        JButton riderBtn = new JButton("  Rider");

        Dimension btnSize = new Dimension(220, 45);
        driverBtn.setPreferredSize(btnSize);
        riderBtn.setPreferredSize(btnSize);

        // Match the style used in Login/Signup pages
        Color primaryColor = new Color(33, 150, 243);  // soft blue
        Font btnFont = new Font("Arial", Font.BOLD, 15);

        for (JButton btn : new JButton[]{driverBtn, riderBtn}) {
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(btnFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }

        gbc.gridy++;
        gbc.gridwidth = 2;
        centerPanel.add(driverBtn, gbc);

        gbc.gridy++;
        centerPanel.add(riderBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Back to Login Link =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JLabel haveAcc = new JLabel("Already have an account?");
        JButton loginLink = new JButton("Login");
        loginLink.setFocusPainted(false);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setForeground(primaryColor);
        loginLink.setFont(new Font("Arial", Font.BOLD, 13));
        bottomPanel.add(haveAcc);
        bottomPanel.add(loginLink);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        driverBtn.addActionListener(e -> ui.showScreen("driver_signup"));
        riderBtn.addActionListener(e -> ui.showScreen("rider_signup"));
        loginLink.addActionListener(e -> ui.showScreen("login"));
    }
}
