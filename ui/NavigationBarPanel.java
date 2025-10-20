package ui;

import javax.swing.*;
import java.awt.*;

public class NavigationBarPanel extends JPanel {
    private final JButton backBtn;
    private final JButton homeBtn;
    private final JButton logoutBtn;
    private boolean loggedIn = false;

    public void setLoggedIn(boolean status) {
        this.loggedIn = status;
    }

    public NavigationBarPanel(RideShareMobileUI ui) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));
        setBackground(new Color(52, 152, 219)); // same blue as signup header

        // ===== Buttons =====
        backBtn = new JButton("Back");
        homeBtn = new JButton(" Home");
        logoutBtn = new JButton("Logout");

        // ===== Common Button Styling =====
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        Color btnColor = Color.WHITE;

        for (JButton btn : new JButton[]{backBtn, homeBtn, logoutBtn}) {
            btn.setFont(btnFont);
            btn.setForeground(btnColor);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(41, 128, 185));
            btn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // ===== Button Actions =====
        backBtn.addActionListener(e -> ui.goBack());

        homeBtn.addActionListener(e -> {
            if (ui.isLoggedIn()) {   // ✅ check from main UI instead of local variable
                ui.showScreen("userhome");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please login or sign up first!",
                    "Access Denied", JOptionPane.WARNING_MESSAGE);
                ui.showScreen("login");
            }
        });

        logoutBtn.addActionListener(e -> {
            if (ui.isLoggedIn()) {
                ui.setLoggedIn(false);
                JOptionPane.showMessageDialog(this, "You have logged out successfully.");
                ui.showScreen("login");
            } else {
                JOptionPane.showMessageDialog(this, "You are not logged in!", "Logout Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ===== Add Buttons to Navigation Bar =====
        add(backBtn);
        add(homeBtn);
        add(logoutBtn);
    }

    public void update(String currentScreen) {
        boolean isLoginOrSignup = "login".equals(currentScreen) || "signup".equals(currentScreen);
        boolean isUserHome = "userhome".equals(currentScreen);

        homeBtn.setVisible(!(isLoginOrSignup || isUserHome));
        logoutBtn.setVisible(!isLoginOrSignup);
        backBtn.setVisible(!(isLoginOrSignup || isUserHome));
    }
}

