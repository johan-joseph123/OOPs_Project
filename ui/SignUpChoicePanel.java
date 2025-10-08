// Create new file: ui/SignUpChoicePanel.java
package ui;

import javax.swing.*;
import java.awt.*;

public class SignUpChoicePanel extends JPanel {
    public SignUpChoicePanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Join Us!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Are you a...", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(20));
        add(subtitle);
        add(Box.createVerticalStrut(40));

        JButton driverBtn = new JButton("Driver");
        JButton riderBtn = new JButton("Ride Seeker");

        Dimension btnSize = new Dimension(250, 50);
        driverBtn.setMaximumSize(btnSize);
        riderBtn.setMaximumSize(btnSize);
        driverBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        riderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(driverBtn);
        add(Box.createVerticalStrut(20));
        add(riderBtn);

        driverBtn.addActionListener(e -> ui.showScreen("driver_signup"));
        riderBtn.addActionListener(e -> ui.showScreen("rider_signup"));
    }
}