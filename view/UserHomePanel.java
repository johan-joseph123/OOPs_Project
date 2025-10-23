package view;

import javax.swing.*;
import java.awt.*;

/**
 * Rider home screen — simplified, clean, and consistent with the full UI.
 */
public class UserHomePanel extends JPanel {

    public UserHomePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        // --- Title Section ---
        JLabel title = new JLabel("Campus RideShare", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0x2C3E50));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- Center Buttons Section ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 245, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        JButton findRideBtn = createStyledButton("Find a Ride", new Color(0x4A90E2));
        JButton myTripsBtn = createStyledButton("My Trips", new Color(0x4A90E2));

        findRideBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        myTripsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(findRideBtn);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(myTripsBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // --- Button Actions ---
        findRideBtn.addActionListener(e -> ui.showScreen("search"));
        myTripsBtn.addActionListener(e -> ui.showScreen("riderTrips"));
    }

    /** ✅ Helper method: Modern rounded consistent buttons */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Smooth background transitions
                Color fill = getModel().isPressed()
                        ? baseColor.darker()
                        : (getModel().isRollover() ? baseColor.brighter() : baseColor);

                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Draw text
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Remove border outline
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return btn;
    }
}
