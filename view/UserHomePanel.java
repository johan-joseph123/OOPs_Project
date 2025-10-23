package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserHomePanel extends JPanel {

    public UserHomePanel(RideShareMobileUI ui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(245, 248, 255));

        JLabel title = new JLabel("Campus RideShare");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(50, 50, 90));

        add(title);
        add(Box.createVerticalStrut(40));

        JButton findRideBtn = createStyledButton("Find a Ride", new Color(0x4A90E2));
        JButton myTripsBtn = createStyledButton("My Trips", new Color(0x50C878));

        add(findRideBtn);
        add(Box.createVerticalStrut(20));
        add(myTripsBtn);
        add(Box.createVerticalGlue()); // Push buttons up

        // --- Button Actions ---
        findRideBtn.addActionListener(e -> ui.showScreen("search"));
        myTripsBtn.addActionListener(e -> ui.showScreen("trips"));
    }

    /** Helper method to create attractive buttons */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // --- Rounded corners effect ---
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()
                        ? baseColor.darker()
                        : (getModel().isRollover() ? baseColor.brighter() : baseColor));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No visible border
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setFocusable(false);

        return btn;
    }
}