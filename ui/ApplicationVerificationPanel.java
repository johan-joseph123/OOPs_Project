package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        // ===== Header =====
        JLabel title = new JLabel("Pending Driver Applications", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ===== Scrollable List =====
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        refreshDriverList();
    }

    // ===== Load Applications from Database =====
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
                listPanel.add(Box.createVerticalStrut(15));
            }

            if (!hasData) {
                JLabel emptyLabel = new JLabel(" No pending applications!", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(emptyLabel);
            }

        } catch (SQLException e) {
            JLabel errorLabel = new JLabel(" Error loading applications: " + e.getMessage(), SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            listPanel.add(errorLabel);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ===== Single Card UI =====
    private JPanel createDriverCard(String id, String name, String vehicle, String licenseNo, String licenseFile) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setBackground(new Color(245, 248, 255));
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // ===== Left Text Info =====
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(" " + name + "  |  " + vehicle);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel infoLabel = new JLabel(" License No: " + licenseNo);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel fileLabel = new JLabel(" File: " + (licenseFile != null ? licenseFile : "None"));
        fileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fileLabel.setForeground(Color.DARK_GRAY);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(infoLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(fileLabel);

        // ===== Right Buttons =====
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        buttons.setOpaque(false);
        JButton approve = createStyledButton("Approve", new Color(76, 175, 80));
        JButton reject = createStyledButton("Reject", new Color(244, 67, 54));

        approve.addActionListener(e -> updateStatus(id, "approved"));
        reject.addActionListener(e -> updateStatus(id, "rejected"));

        buttons.add(approve);
        buttons.add(reject);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);
        return card;
    }

    // ===== Styled Button Factory =====
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(baseColor);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 35));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    // ===== Update Database =====
    private void updateStatus(String id, String status) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE driver_applications SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(this, "Driver " + id + " has been " + status + ".");
            refreshDriverList();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage());
        }
    }
}
