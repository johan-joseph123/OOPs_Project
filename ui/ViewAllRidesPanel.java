// Create new file: ui/ViewAllRidesPanel.java
package ui;

import javax.swing.*;
import java.awt.*;

public class ViewAllRidesPanel extends JPanel {
    public ViewAllRidesPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel title = new JLabel("All Offered Rides", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel rideList = new JPanel();
        rideList.setLayout(new BoxLayout(rideList, BoxLayout.Y_AXIS));
        
        if (ApplicationData.allOfferedRides.isEmpty()) {
            rideList.add(new JLabel("No rides have been offered yet."));
        } else {
            for (Ride ride : ApplicationData.allOfferedRides) {
                rideList.add(createRideInfoCard(ride));
                rideList.add(Box.createVerticalStrut(5));
            }
        }
        
        add(new JScrollPane(rideList), BorderLayout.CENTER);
    }
    
    private JPanel createRideInfoCard(Ride ride) {
        JPanel card = new JPanel(new GridLayout(0, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.add(new JLabel(" Driver: " + ride.getName()));
        card.add(new JLabel(" Route: " + ride.getRoute()));
        card.add(new JLabel(" Time: " + ride.getTime()));
        card.add(new JLabel(" Vehicle: " + ride.getVehicleType()));
        return card;
    }
}