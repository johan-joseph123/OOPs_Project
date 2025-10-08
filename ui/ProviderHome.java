package ui;

import javax.swing.*;
import java.awt.*;

public class ProviderHome extends JPanel {
    public ProviderHome(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Campus RideShare");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(40)); // Space below title

        // Buttons
        JButton offerRideBtn = new JButton("Offer a Ride");
        JButton myTripsBtn = new JButton("My Trips");

        Dimension btnSize = new Dimension(180, 40);
        for (JButton btn : new JButton[]{offerRideBtn, myTripsBtn}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Add buttons
        add(offerRideBtn);
        add(Box.createVerticalStrut(20)); // Space between buttons
        add(myTripsBtn);

        add(Box.createVerticalGlue()); // Push content upward

        // Action Listeners
        offerRideBtn.addActionListener(e -> ui.showScreen("offer"));
        myTripsBtn.addActionListener(e -> ui.showScreen("trips"));
    }
}
