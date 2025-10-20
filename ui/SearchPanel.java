package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchPanel extends JPanel {
    public SearchPanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        setBackground(new Color(245, 248, 255)); // Light blue background

        // Title
        JLabel title = new JLabel("🔍 Find a Ride");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 60, 120));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(30));

        // Labels and Inputs
        JLabel fromLabel = new JLabel("From:");
        JTextField fromField = new JTextField("St. Joseph's College");

        JLabel toLabel = new JLabel("To:");
        JComboBox<String> toField = new JComboBox<>(new String[]{
            "Pala KSRTC", "Bharananganam", "Pravithanam", "Kottayam RS"
        });

        JLabel timeLabel = new JLabel("Pickup Time (HH:mm):");
        JTextField timeField = new JTextField("07:30");

        // Date Dropdown (today/tomorrow)
        JLabel dateLabel = new JLabel("Date:");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        JComboBox<String> dateField = new JComboBox<>(new String[]{
            sdf.format(today), sdf.format(tomorrow)
        });

        // Search Button
        JButton searchBtn = new JButton("Search Rides");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        searchBtn.setBackground(new Color(60, 120, 220));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        searchBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchBtn.setBackground(new Color(40, 100, 200));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchBtn.setBackground(new Color(60, 120, 220));
            }
        });

        // Layout Components
        Dimension fieldMax = new Dimension(Integer.MAX_VALUE, 35);
        Component[] inputs = {fromField, toField, timeField, dateField};
        for (Component c : inputs) {
            c.setMaximumSize(fieldMax);
            ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        for (JLabel lbl : new JLabel[]{fromLabel, toLabel, timeLabel, dateLabel}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(50, 60, 90));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Add all components neatly
        add(fromLabel);
        add(fromField);
        add(Box.createVerticalStrut(15));

        add(toLabel);
        add(toField);
        add(Box.createVerticalStrut(15));

        add(timeLabel);
        add(timeField);
        add(Box.createVerticalStrut(15));

        add(dateLabel);
        add(dateField);
        add(Box.createVerticalStrut(30));

        add(searchBtn);
        add(Box.createVerticalGlue());

        // Button Action
        searchBtn.addActionListener(e -> {
            JPanel results = new JPanel();
            results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
            results.setPreferredSize(new Dimension(420, 300));

            if (ApplicationData.allRides.isEmpty()) {
                results.add(new JLabel("No rides available."));
            } else {
                for (Ride r : ApplicationData.allRides) {
                    if (!"Open".equals(r.getStatus())) continue; // show only open rides
                    JPanel card = new JPanel(new BorderLayout());
                    card.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
                    JLabel info = new JLabel("<html><b>" + r.getDriverName() + "</b> — " + r.getRoute() +
                                             "<br/>" + r.getDate() + " " + r.getTime() + " • Seats: " + r.getSeatsAvailable() +
                                             " • " + r.getVehicleType() + "</html>");
                    card.add(info, BorderLayout.CENTER);

                    JButton accept = new JButton("Accept");
                    accept.addActionListener(ae -> {
                        ApplicationData.acceptRide(r);
                        JOptionPane.showMessageDialog(this, "Ride accepted and added to My Trips.");
                        // refresh MyTripsPanel when shown
                        ui.showScreen("trips");
                    });
                    card.add(accept, BorderLayout.EAST);
                    results.add(card);
                    results.add(Box.createVerticalStrut(8));
                }
            }

            JOptionPane.showMessageDialog(this, results, "Search Results", JOptionPane.PLAIN_MESSAGE);
        });



    }
}
