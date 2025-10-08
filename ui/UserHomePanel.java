package ui;

import javax.swing.*;
import java.awt.*;

public class UserHomePanel extends JPanel {
    public UserHomePanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Campus RideShare");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(40));

        JButton findRideBtn = new JButton("Find a Ride");
        //JButton offerRideBtn = new JButton("Offer a Ride");
        JButton myTripsBtn = new JButton("My Trips");

        Dimension btnSize = new Dimension(200, 40);
        findRideBtn.setPreferredSize(btnSize);
        //offerRideBtn.setPreferredSize(btnSize);
        myTripsBtn.setPreferredSize(btnSize);
        findRideBtn.setMaximumSize(btnSize);
        //offerRideBtn.setMaximumSize(btnSize);
        myTripsBtn.setMaximumSize(btnSize);

        findRideBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        //offerRideBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        myTripsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(findRideBtn);
        add(Box.createVerticalStrut(20));
        //add(offerRideBtn);
        //add(Box.createVerticalStrut(20));
        add(myTripsBtn);
        add(Box.createVerticalGlue()); // Push buttons up

        //offerRideBtn.addActionListener(e -> ui.showScreen("offer"));
        findRideBtn.addActionListener(e -> ui.showScreen("search"));
        myTripsBtn.addActionListener(e -> ui.showScreen("trips")); 
    }
}