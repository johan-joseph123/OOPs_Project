package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewUsersPanel extends JPanel {

    public ViewUsersPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Registered Users", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41));
        add(title, BorderLayout.NORTH);

        // Panels container (Drivers & Riders)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(new Color(250, 250, 250));

        // Create drivers panel
        JPanel driversPanel = createUserPanel(
            "🚗 Approved Drivers",
            ApplicationData.approvedDrivers.stream().map(User::getName).toList(),
            new Color(220, 248, 255)
        );

        // Create riders panel
        JPanel ridersPanel = createUserPanel(
            "👥 Riders",
            ApplicationData.riders.stream().map(User::getName).toList(),
            new Color(255, 244, 229)
        );

        mainPanel.add(driversPanel);
        mainPanel.add(ridersPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(33, 150, 243));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> ui.showScreen("home"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(250, 250, 250));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createUserPanel(String title, List<String> users, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        panel.add(label, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        if (users.isEmpty()) {
            model.addElement("No users found");
        } else {
            for (String name : users) {
                model.addElement(name);
            }
        }

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        list.setSelectionBackground(new Color(33, 150, 243));
        list.setSelectionForeground(Color.WHITE);
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
