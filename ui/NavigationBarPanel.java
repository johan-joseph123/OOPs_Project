package ui;

import javax.swing.*;
import java.awt.*;

public class NavigationBarPanel extends JPanel {
    private final JButton backBtn;
    private final JButton homeBtn;
    private final JButton logoutBtn;

    public NavigationBarPanel(RideShareMobileUI ui) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        setBackground(new Color(230, 230, 230));

        backBtn = new JButton("← Back");
        homeBtn = new JButton("🏠 Home");
        logoutBtn = new JButton("👤 Logout");

        backBtn.addActionListener(e -> ui.goBack());
        
        homeBtn.addActionListener(e -> ui.showScreen("userhome"));
        
        logoutBtn.addActionListener(e -> ui.showScreen("login"));

        // Style buttons
        for (JButton btn : new JButton[]{backBtn, homeBtn, logoutBtn}) {
            btn.setBorderPainted(false);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);  // also hides default background fill
            btn.setFocusPainted(false);       // removes focus border on click
        }

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
