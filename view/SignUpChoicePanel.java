package view;

import view.RideShareMobileUI;

import javax.swing.*;
import java.awt.*;

public class SignUpChoicePanel extends JPanel {
    public SignUpChoicePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel title = new JLabel("Join Campus RideShare", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20,10,20,10); gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel subtitle = new JLabel("Choose your role:", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; centerPanel.add(subtitle, gbc);

        JButton driverBtn = new JButton(" Driver");
        JButton riderBtn = new JButton(" Rider");
        Dimension btnSize = new Dimension(220,45);
        driverBtn.setPreferredSize(btnSize); riderBtn.setPreferredSize(btnSize);
        Color primaryColor = new Color(33,150,243);
        for (JButton b : new JButton[]{driverBtn, riderBtn}) {
            b.setBackground(primaryColor); b.setForeground(Color.WHITE); b.setFocusPainted(false);
        }
        gbc.gridy++; centerPanel.add(driverBtn, gbc);
        gbc.gridy++; centerPanel.add(riderBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(); bottom.setBackground(Color.WHITE);
        JLabel haveAcc = new JLabel("Already have an account?");
        JButton loginLink = new JButton("Login");
        loginLink.setContentAreaFilled(false); loginLink.setBorderPainted(false);
        loginLink.setForeground(primaryColor);
        bottom.add(haveAcc); bottom.add(loginLink);
        add(bottom, BorderLayout.SOUTH);

        driverBtn.addActionListener(e -> ui.showScreen("driver_signup"));
        riderBtn.addActionListener(e -> ui.showScreen("rider_signup"));
        loginLink.addActionListener(e -> ui.showScreen("login"));
    }
}
