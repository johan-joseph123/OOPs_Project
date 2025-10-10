// In file: ui/AdminHomePanel.java (Replace with this)
package ui;

import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    public AdminHomePanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(40));

        // Create buttons
        JButton verifyApplicationsBtn = new JButton("Application Verification");
        JButton viewUsersBtn = new JButton("View All Users");
        JButton viewRidesBtn = new JButton("View All Rides");

        // Set button sizes
        Dimension btnSize = new Dimension(250, 40);
        verifyApplicationsBtn.setPreferredSize(btnSize);
        viewUsersBtn.setPreferredSize(btnSize);
        viewRidesBtn.setPreferredSize(btnSize);
        verifyApplicationsBtn.setMaximumSize(btnSize);
        viewUsersBtn.setMaximumSize(btnSize);
        viewRidesBtn.setMaximumSize(btnSize);
        verifyApplicationsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewUsersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewRidesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons to panel
        add(verifyApplicationsBtn);
        add(Box.createVerticalStrut(20));
        add(viewUsersBtn);
        add(Box.createVerticalStrut(20));
        add(viewRidesBtn);
        add(Box.createVerticalGlue()); 

        // Add action listeners
        verifyApplicationsBtn.addActionListener(e -> ui.showScreen("verify"));
        //viewUsersBtn.addActionListener(e -> ui.showScreen("view_users"));
       // viewRidesBtn.addActionListener(e -> ui.showScreen("view_rides"));
    }
}