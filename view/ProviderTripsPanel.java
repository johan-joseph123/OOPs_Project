package view;

import controller.RideController;
import model.Ride;
import util.UIStyleHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * 🚗 ProviderTripsPanel — shows all rides offered by the logged-in driver
 */
public class ProviderTripsPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final RideController rideController = new RideController();
    private JTable ridesTable;
    private DefaultTableModel model;

    public ProviderTripsPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🛣️ Your Offered Rides");
        add(title, BorderLayout.NORTH);

        // === Table setup ===
        String[] columns = {"ID", "From", "To", "Date", "Time", "Vehicle", "Seats", "Status"};
        model = new DefaultTableModel(columns, 0);
        ridesTable = new JTable(model);
        ridesTable.setRowHeight(28);
        ridesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ridesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(ridesTable);
        add(scrollPane, BorderLayout.CENTER);

        // === Bottom Buttons ===
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton refreshButton = UIStyleHelper.styleButton(new JButton("🔄 Refresh"), UIStyleHelper.PRIMARY_COLOR);
        JButton backButton = UIStyleHelper.styleButton(new JButton("← Back"), UIStyleHelper.SECONDARY_COLOR);

        btnPanel.add(refreshButton);
        btnPanel.add(backButton);
        add(btnPanel, BorderLayout.SOUTH);

        // Actions
        refreshButton.addActionListener(e -> refreshRides());
        backButton.addActionListener(e -> ui.showScreen("providerhome"));
    }

    /** ✅ Refresh table whenever opened or clicked */
    public void refreshRides() {
        model.setRowCount(0); // clear existing rows
        String driverId = ui.getCurrentUserId();

        if (driverId == null || driverId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ You must be logged in as a driver.");
            return;
        }

        try {
            List<Ride> rides = rideController.getProviderRides(driverId);

            if (rides == null || rides.isEmpty()) {
                JOptionPane.showMessageDialog(this, "🚘 No offered rides found.");
                return;
            }

            int i = 1;
            for (Ride r : rides) {
                model.addRow(new Object[]{
                    i++, r.getFromLocation(), r.getToLocation(),
                    r.getDate(), r.getTime(),
                    r.getVehicleType(), r.getSeatsAvailable(),
                    r.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading rides: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
