package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchPanel extends JPanel {
    public SearchPanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Find a Ride");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(25));

        JTextField fromField = new JTextField("St. Joseph's College");
        JComboBox<String> toField = new JComboBox<>(new String[]{
            "Pala KSRTC",
            "Bharananganam",
            "Pravithanam",
            "Kottayam RS"
        });

        JTextField timeField = new JTextField("07:30");

        // Create date options: today and tomorrow
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        JComboBox<String> dateField = new JComboBox<>(new String[]{
            sdf.format(today),
            sdf.format(tomorrow)
        });

        JButton searchBtn = new JButton("Search Rides");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));

        // Add components to panel
        add(fromField);
        add(Box.createVerticalStrut(10));
        add(toField);
        add(Box.createVerticalStrut(10));
        add(timeField);
        add(Box.createVerticalStrut(10));
        add(dateField); // <-- Date picker added here
        add(Box.createVerticalStrut(30));
        add(searchBtn);
        add(Box.createVerticalGlue());

        // Sizing constraints
        Dimension fieldMax = new Dimension(Integer.MAX_VALUE, 35);
        fromField.setMaximumSize(fieldMax);
        toField.setMaximumSize(fieldMax);
        timeField.setMaximumSize(fieldMax);
        dateField.setMaximumSize(fieldMax); // Apply to date picker
        searchBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchBtn.addActionListener(e -> {
            // Sample action (navigate to ride list)
            ui.showScreen("rides");
        });
    }
}
