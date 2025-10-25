package view;

import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Allows user to choose whether to sign up as a Rider or a Driver.
 */
public class SignUpChoicePanel extends JPanel {
    public SignUpChoicePanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🧍 Join Campus RideShare");
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel prompt = new JLabel("Select your role:");
        prompt.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        prompt.setHorizontalAlignment(SwingConstants.CENTER);

        JButton driverBtn = UIStyleHelper.styleButton(new JButton("🚗 Driver"), UIStyleHelper.PRIMARY_COLOR);
        JButton riderBtn = UIStyleHelper.styleButton(new JButton("🧑‍🎓 Rider"), UIStyleHelper.PRIMARY_COLOR);
        JButton backBtn = UIStyleHelper.styleButton(new JButton("← Back"), UIStyleHelper.SECONDARY_COLOR);

        // === Layout ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(prompt, gbc);

        gbc.gridy++;
        centerPanel.add(driverBtn, gbc);

        gbc.gridy++;
        centerPanel.add(riderBtn, gbc);

        gbc.gridy++;
        centerPanel.add(backBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // === Actions ===
        driverBtn.addActionListener(e -> {
            ui.showScreen("driver_application"); // ✅ Correct navigation
        });

        riderBtn.addActionListener(e -> {
            ui.showScreen("rider_signup"); // ✅ Goes to RiderSignUpPanel
        });

        backBtn.addActionListener(e -> {
            ui.showScreen("login"); // ✅ Back to login
        });
    }
}
