package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminHomePanel extends JPanel {

    public AdminHomePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== Header =====
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 30));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // ===== Center Buttons =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton verifyApplicationsBtn = createStyledButton("  Application Verification");
        JButton viewUsersBtn = createStyledButton("  View All Users");
        JButton viewRidesBtn = createStyledButton("  View All Rides");

        // Add buttons with spacing
        buttonPanel.add(verifyApplicationsBtn);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(viewUsersBtn);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(viewRidesBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // ===== Footer / Navigation =====
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        navPanel.setBackground(new Color(245, 245, 245));

        JButton homeBtn = createNavButton(" Home");
        JButton logoutBtn = createNavButton(" Logout");

        navPanel.add(homeBtn);
        navPanel.add(logoutBtn);
        add(navPanel, BorderLayout.SOUTH);

        // ===== Action Listeners =====
        verifyApplicationsBtn.addActionListener(e -> ui.showScreen("verify"));
        viewUsersBtn.addActionListener(e -> ui.showScreen("view_users"));
        viewRidesBtn.addActionListener(e -> ui.showScreen("view_rides"));
        homeBtn.addActionListener(e -> ui.showScreen("userhome"));
        logoutBtn.addActionListener(e -> ui.showScreen("login"));
    }

    // ===== Helper: Fancy Styled Button =====
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            // Custom rounded button paint
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color baseColor = getBackground();
                GradientPaint gradient = new GradientPaint(0, 0, baseColor.brighter(), 0, getHeight(), baseColor.darker());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(51, 153, 255));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover + press effects
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 120, 255));
                btn.setFont(btn.getFont().deriveFont(Font.BOLD, 17f));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(51, 153, 255));
                btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
            }

            public void mousePressed(MouseEvent e) {
                btn.setBackground(new Color(0, 100, 230));
            }

            public void mouseReleased(MouseEvent e) {
                btn.setBackground(new Color(0, 120, 255));
            }
        });

        return btn;
    }

    // ===== Helper: Navigation Button (minimalist style) =====
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setForeground(new Color(50, 50, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(0, 120, 255));
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(new Color(50, 50, 50));
            }
        });
        return btn;
    }
}
