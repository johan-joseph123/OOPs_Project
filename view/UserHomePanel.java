package view;

import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 🏠 Rider Home Page — modern clean UI
 */
public class UserHomePanel extends JPanel {

    public UserHomePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        // --- Title ---
        JLabel title = UIStyleHelper.createTitle("🏠 Campus RideShare");
        add(title, BorderLayout.NORTH);

        // --- Button Section ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(UIStyleHelper.BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton findRideBtn = UIStyleHelper.createGradientButton("🔍 Find a Ride");
        JButton myTripsBtn = UIStyleHelper.createGradientButton("🧳 My Trips");

        findRideBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        myTripsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(findRideBtn);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(myTripsBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // --- Actions ---
        findRideBtn.addActionListener(e -> ui.showScreen("search"));
        myTripsBtn.addActionListener(e -> ui.showScreen("riderTrips"));
    }
}
