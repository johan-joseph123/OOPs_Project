package dao;

import model.Ride;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RideDAO {

    public boolean createRide(Ride r) throws SQLException {
        String sql = "INSERT INTO rides (driver_id, driver_name, from_location, to_location, date, time, vehicle_type, seats_available, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Open')";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getDriverId());
            ps.setString(2, r.getDriverName());
            ps.setString(3, r.getFromLocation());
            ps.setString(4, r.getToLocation());
            ps.setDate(5, Date.valueOf(r.getDate()));
            ps.setString(6, r.getTime());
            ps.setString(7, r.getVehicleType());
            ps.setInt(8, r.getSeatsAvailable());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) r.setRideId(keys.getInt(1));
                }
            }
            return ok;
        }
    }

    /** Returns all open rides (no search filter). */
    public List<Ride> findAllOpenRides() throws SQLException {
        List<Ride> list = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE status = 'Open'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Ride findById(int rideId) throws SQLException {
        String sql = "SELECT * FROM rides WHERE ride_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Ride> findByDriverId(String driverId) throws SQLException {
        List<Ride> list = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE driver_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, driverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean updateRideStatus(int rideId, String status) throws SQLException {
        String sql = "UPDATE rides SET status = ? WHERE ride_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, rideId);
            return ps.executeUpdate() > 0;
        }
    }

    private Ride mapRow(ResultSet rs) throws SQLException {
        Ride r = new Ride();
        r.setRideId(rs.getInt("ride_id"));
        r.setDriverId(rs.getString("driver_id"));
        r.setDriverName(rs.getString("driver_name"));
        r.setFromLocation(rs.getString("from_location"));
        r.setToLocation(rs.getString("to_location"));
        r.setDate(rs.getDate("date").toLocalDate());
        r.setTime(rs.getString("time"));
        r.setVehicleType(rs.getString("vehicle_type"));
        r.setSeatsAvailable(rs.getInt("seats_available"));
        r.setStatus(rs.getString("status"));
        return r;
    }
}
