package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ApplicationVerificationPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final JPanel listPanel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/rideshare";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public ApplicationVerificationPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Pending Driver Applications", SwingConstants.CENTER);
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

        refreshDriverList();
    }

    public void refreshDriverList() {
        listPanel.removeAll();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM driver_applications WHERE status = 'pending'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                JPanel card = createDriverCard(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("vehicle_model"),
                    rs.getString("license_number"),
                    rs.getString("license_file")
                );
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(10));
            }

            if (!hasData) {
                JLabel emptyLabel = new JLabel("No pending applications.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(emptyLabel);
            }

        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("Error loading applications: " + e.getMessage(), SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            listPanel.add(errorLabel);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createDriverCard(String id, String name, String vehicle, String licenseNo, String licenseFile) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(new Color(245, 245, 255));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(name + " • " + vehicle);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel infoLabel = new JLabel("Number plate no: " + licenseNo);
        JLabel fileLabel = new JLabel("File: " + (licenseFile != null ? licenseFile : "None"));

        textPanel.add(nameLabel);
        textPanel.add(infoLabel);
        textPanel.add(fileLabel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton approve = new JButton("Approve");
        JButton reject = new JButton("Reject");

        approve.addActionListener(e -> updateStatus(id, "approved"));
        reject.addActionListener(e -> updateStatus(id, "rejected"));

        buttons.add(approve);
        buttons.add(reject);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);

        return card;
    }

    private void updateStatus(String id, String status) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE driver_applications SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(this, "Driver " + id + " " + status + ".");
            refreshDriverList();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage());
        }
    }
}
