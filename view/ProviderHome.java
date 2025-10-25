package view;

import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 🚘 Modern Driver (Provider) Home Screen
 */
public class ProviderHome extends JPanel {
    public ProviderHome(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Header ---
        JLabel title = UIStyleHelper.createTitle("🚘 Driver Dashboard");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // --- Main Buttons Panel ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIStyleHelper.BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JButton offerRideBtn = createGradientButton("Offer a Ride");
        JButton myTripsBtn = createGradientButton("My Offered Rides");

        offerRideBtn.addActionListener(e -> ui.showScreen("offer"));
        myTripsBtn.addActionListener(e -> ui.showScreen("providerTrips"));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(offerRideBtn);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(myTripsBtn);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                        UIStyleHelper.PRIMARY_COLOR,
                        0, getHeight(),
                        UIStyleHelper.PRIMARY_COLOR.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(240, 50));

        // Hover animation
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setForeground(new Color(255, 255, 180));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}
