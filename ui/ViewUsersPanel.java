// Create new file: ui/ViewUsersPanel.java
package ui;

import javax.swing.*;
import java.awt.*;

public class ViewUsersPanel extends JPanel {
    public ViewUsersPanel(RideShareMobileUI ui) {
        setLayout(new GridLayout(1, 2, 20, 0)); // 1 row, 2 columns, with a gap
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for Drivers
        JPanel driversPanel = new JPanel(new BorderLayout());
        driversPanel.add(new JLabel("Approved Drivers", SwingConstants.CENTER), BorderLayout.NORTH);
        JList<String> driverList = new JList<>(
            ApplicationData.approvedDrivers.stream().map(User::getName).toArray(String[]::new)
        );
        driversPanel.add(new JScrollPane(driverList), BorderLayout.CENTER);

        // Panel for Riders
        JPanel ridersPanel = new JPanel(new BorderLayout());
        ridersPanel.add(new JLabel("Riders", SwingConstants.CENTER), BorderLayout.NORTH);
        JList<String> riderList = new JList<>(
            ApplicationData.riders.stream().map(User::getName).toArray(String[]::new)
        );
        ridersPanel.add(new JScrollPane(riderList), BorderLayout.CENTER);

        add(driversPanel);
        add(ridersPanel);
    }
}