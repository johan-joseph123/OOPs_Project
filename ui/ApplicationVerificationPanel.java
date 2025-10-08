package ui;

import javax.swing.*;
import java.awt.*;

public class ApplicationVerificationPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final JPanel listPanel;

    public ApplicationVerificationPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Pending Applications", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        refreshDriverList(); // Load data
    }

    public void refreshDriverList() {
        //listPanel.removeAll();

        if (ApplicationData.pendingDrivers.isEmpty()) {
            JLabel emptyLabel = new JLabel("No pending applications.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (Driver driver : ApplicationData.pendingDrivers) {
                listPanel.add(createDriverCard(driver));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createDriverCard(Driver driver) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(new Color(245, 245, 255)); // Same as RideListPanel
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(driver.getName() + " • " + driver.getVehicleModel());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel infoLabel = new JLabel("Pending approval to become a driver");

        textPanel.add(nameLabel);
        textPanel.add(infoLabel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton approve = new JButton("Approve");
        JButton reject = new JButton("Reject");

        approve.addActionListener(e -> {
            ApplicationData.approvedDrivers.add(driver);
            ApplicationData.pendingDrivers.remove(driver);
            JOptionPane.showMessageDialog(this, driver.getName() + " approved.");
            refreshDriverList();
        });

        reject.addActionListener(e -> {
            ApplicationData.pendingDrivers.remove(driver);
            JOptionPane.showMessageDialog(this, driver.getName() + " rejected.");
            refreshDriverList();
        });

        buttons.add(approve);
        buttons.add(reject);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);

        return card;
    }
}
