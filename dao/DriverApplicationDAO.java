package dao;

import model.DriverApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverApplicationDAO {

    public boolean createApplication(DriverApplication da) throws SQLException {
        // Assumes user row already created via UserDAO.createUser or created here with empty password as needed.
        String sqlApp = "INSERT INTO driver_applications (id, name, vehicle_model, license_number, license_file, status) VALUES (?, ?, ?, ?, ?, 'pending')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlApp)) {
            ps.setString(1, da.getId());
            ps.setString(2, da.getName());
            ps.setString(3, da.getVehicleModel());
            ps.setString(4, da.getLicenseNumber());
            ps.setString(5, da.getLicenseFile());
            return ps.executeUpdate() > 0;
        }
    }

    public DriverApplication findById(String id) throws SQLException {
        String sql = "SELECT * FROM driver_applications WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DriverApplication(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("vehicle_model"),
                        rs.getString("license_number"),
                        rs.getString("license_file"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public List<DriverApplication> getPendingApplications() throws SQLException {
        List<DriverApplication> out = new ArrayList<>();
        String sql = "SELECT * FROM driver_applications WHERE status = 'pending'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new DriverApplication(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("vehicle_model"),
                        rs.getString("license_number"),
                        rs.getString("license_file"),
                        rs.getString("status")
                ));
            }
        }
        return out;
    }

    public boolean updateStatus(String id, String status) throws SQLException {
        String sql = "UPDATE driver_applications SET status = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}
