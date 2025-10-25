package util;

import javax.swing.*;
import java.awt.*;

/**
 * 🎨 UIStyleHelper — Centralized styling utilities for Campus RideShare.
 * Keeps every panel consistent with a modern, smooth look.
 */
public class UIStyleHelper {

    // 🎨 --- COLORS ---
    public static final Color PRIMARY_COLOR = new Color(0x4A90E2);     // Blue
    public static final Color SUCCESS_COLOR = new Color(0x2ECC71);     // Green
    public static final Color SECONDARY_COLOR = new Color(0x5C6BC0);   // Indigo
    public static final Color ACCENT_COLOR = new Color(0xF39C12);      // Orange for highlights
    public static final Color TEXT_COLOR = new Color(40, 40, 60);
    public static final Color BG_COLOR = new Color(245, 247, 250);     // Soft light gray-blue

    // 🅰️ --- FONTS ---
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // 🏷️ --- TITLE ---
    public static JLabel createTitle(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(TITLE_FONT);
        lbl.setForeground(new Color(40, 60, 120));
        return lbl;
    }

    // 🪄 --- INFO LABEL (small descriptive text) ---
    public static JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lbl.setForeground(new Color(90, 90, 120));
        return lbl;
    }

    // 🧱 --- CARD STYLE PANEL ---
    public static JPanel createContentPanel(String titleText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        if (titleText != null && !titleText.isEmpty()) {
            JLabel title = new JLabel(titleText, SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(new Color(50, 70, 120));
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
            panel.add(title, BorderLayout.NORTH);
        }

        return panel;
    }

    // ✏️ --- TEXT FIELD STYLE ---
    public static JTextField styleTextField(JTextField field) {
        field.setFont(TEXT_FONT);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    // ✏️ --- PASSWORD FIELD STYLE ---
    public static JPasswordField stylePasswordField(JPasswordField field) {
        field.setFont(TEXT_FONT);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    // 🔘 --- SOLID BUTTON STYLE ---
    public static JButton styleButton(JButton button, Color color) {
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    // 🌈 --- GRADIENT BUTTON STYLE (for fancy highlights) ---
    public static JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2.setPaint(gp);
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
                // No border outline
            }
        };

        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }
}
