package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProviderHome extends JPanel {
    public ProviderHome(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(245, 248, 255)); // Soft background

        // Title
        JLabel title = new JLabel("Campus RideShare");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 90));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(40));

        // Create buttons
        JButton offerRideBtn = createStyledButton("Offer a Ride");
        JButton myTripsBtn = createStyledButton("My Trips");

        // Add buttons
        add(offerRideBtn);
        add(Box.createVerticalStrut(20));
        add(myTripsBtn);
        add(Box.createVerticalGlue());

        // Action Listeners
        offerRideBtn.addActionListener(e -> ui.showScreen("offer"));
        myTripsBtn.addActionListener(e -> ui.showScreen("providerTrips"));
    }

    // ✅ Helper method to create attractive buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color startColor = new Color(66, 135, 245);
                Color endColor = new Color(30, 85, 230);
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 45));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(new Color(255, 255, 180)); // Light yellow text on hover
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}