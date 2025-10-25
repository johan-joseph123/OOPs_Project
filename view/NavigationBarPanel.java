package view;

import util.UIStyleHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 🌐 Navigation Bar — Personalized with User Name + Role
 */
public class NavigationBarPanel extends JPanel {
    private final JButton backBtn;
    private final JButton homeBtn;
    private final JButton logoutBtn;
    private final JLabel welcomeLabel;
    private boolean loggedIn = false;
    private final RideShareMobileUI ui;

    public NavigationBarPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new FlowLayout(FlowLayout.CENTER, 35, 8));
        setBackground(UIStyleHelper.PRIMARY_COLOR);

        backBtn = createNavButton("🔙 Back");
        homeBtn = createNavButton("🏠 Home");
        logoutBtn = createNavButton("🚪 Logout");

        welcomeLabel = new JLabel("👋 Welcome Guest");
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);

        backBtn.addActionListener(e -> ui.goBack());
        homeBtn.addActionListener(e -> {
            if (!ui.isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "Please login first.", "Not Logged In", JOptionPane.WARNING_MESSAGE);
                ui.showScreen("login");
                return;
            }

            String role = ui.getCurrentUserRole();
            if (role == null) role = "";
            switch (role.toLowerCase()) {
                case "rider" -> ui.showScreen("userhome");
                case "driver" -> ui.showScreen("providerhome");
                case "admin" -> ui.showScreen("admin");
                default -> ui.showScreen("login");
            }
        });

        logoutBtn.addActionListener(e -> {
            if (!ui.isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "⚠️ You are not logged in!", "Logout Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(ui, "Are you sure you want to log out?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                ui.resetUserSession();
                setGuestMode();
                JOptionPane.showMessageDialog(ui, "🚪 Logged out successfully!");
            }
        });

        add(backBtn);
        add(homeBtn);
        add(logoutBtn);
        add(welcomeLabel);
        setGuestMode();
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(UIStyleHelper.PRIMARY_COLOR.darker());
        button.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIStyleHelper.ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIStyleHelper.PRIMARY_COLOR.darker());
            }
        });
        return button;
    }

    public void update(String currentScreen) {
        boolean isLoginScreen = currentScreen.equals("login");
        boolean isSignupScreen = currentScreen.contains("signup");
        boolean isHomeScreen = currentScreen.equals("userhome")
                || currentScreen.equals("providerhome")
                || currentScreen.equals("admin");

        if (isLoginScreen || isSignupScreen) {
            setGuestMode();
            return;
        }

        if (ui.isLoggedIn()) {
            setBackground(UIStyleHelper.PRIMARY_COLOR);
            backBtn.setVisible(!isHomeScreen);
            homeBtn.setVisible(!isHomeScreen);
            logoutBtn.setVisible(true);
            welcomeLabel.setVisible(true);

            // 👋 Personalized welcome using username & role
            String username = ui.getCurrentUsername() == null ? "User" : ui.getCurrentUsername();
            String role = ui.getCurrentUserRole() == null ? "" : ui.getCurrentUserRole();
            role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();

            welcomeLabel.setText("👋 Welcome " + username + " (" + role + ")");
        } else {
            setGuestMode();
        }
    }

    private void setGuestMode() {
        setBackground(new Color(160, 160, 160));
        backBtn.setVisible(false);
        homeBtn.setVisible(false);
        logoutBtn.setVisible(false);
        welcomeLabel.setVisible(true);
        welcomeLabel.setText("👋 Welcome Guest");
        revalidate();
        repaint();
    }

    public void setLoggedIn(boolean status) {
        this.loggedIn = status;
        if (!status) setGuestMode();
    }
}
