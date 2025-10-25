package dao;

import model.Ride;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 💾 DAO for rides table — handles DB operations.
 */
public class RideDAO {

    // ✅ Create new ride with duplicate prevention
    public boolean createRide(Ride ride) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String checkSql = "SELECT COUNT(*) FROM rides WHERE driver_id = ? AND date = ? AND to_location = ? AND status = 'Open'";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, ride.getDriverId());
            checkPs.setString(2, ride.getDate());
            checkPs.setString(3, ride.getToLocation());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("⚠️ Duplicate ride detected for driver: " + ride.getDriverId());
                return false;
            }
        }

        String sql = "INSERT INTO rides (driver_id, driver_name, from_location, to_location, date, time, vehicle_type, seats_available, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ride.getDriverId());
            ps.setString(2, ride.getDriverName());
            ps.setString(3, ride.getFromLocation());
            ps.setString(4, ride.getToLocation());
            ps.setString(5, ride.getDate());
            ps.setString(6, ride.getTime());
            ps.setString(7, ride.getVehicleType());
            ps.setInt(8, ride.getSeatsAvailable());
            ps.setString(9, ride.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in createRide(): " + e.getMessage());
            return false;
        }
    }

    // ✅ Retrieve all rides (for admin or browsing)
    public List<Ride> findAllOpenRides() throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides ORDER BY ride_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rides.add(mapToRide(rs));
            }
        }
        return rides;
    }
    /**
     * ✅ Update available seats for a ride.
     */
    public boolean updateSeats(int rideId, int newSeats) throws SQLException {
        if (rideId <= 0 || newSeats < 0) return false;

        String sql = "UPDATE rides SET seats_available = ? WHERE ride_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newSeats);
            ps.setInt(2, rideId);
            int updated = ps.executeUpdate();
            System.out.println("🪑 Seats updated for ride " + rideId + " → " + newSeats);
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("❌ SQL Error (updateSeats): " + e.getMessage());
            return false;
        }
    }


    // ✅ Find rides by destination and date
    public List<Ride> findRidesByDestinationAndDate(String to, String date) throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE to_location = ? AND date = ? ORDER BY time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, to);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rides.add(mapToRide(rs));
            }
        }
        return rides;
    }

    // ✅ Find single ride by ID
    public Ride findById(int rideId) throws SQLException {
        String sql = "SELECT * FROM rides WHERE ride_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToRide(rs);
                }
            }
        }
        return null;
    }

    // ✅ Find all rides for a specific driver
    public List<Ride> findRidesByDriver(String driverId) throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE driver_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rides.add(mapToRide(rs));
                }
            }
        }
        return rides;
    }

    // ✅ Update ride status (e.g., from 'Open' → 'Completed')
    public boolean updateRideStatus(int rideId, String status) throws SQLException {
        String sql = "UPDATE rides SET status = ? WHERE ride_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, rideId);
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Map a ResultSet row → Ride object
    private Ride mapToRide(ResultSet rs) throws SQLException {
        Ride ride = new Ride(
                rs.getString("driver_id"),
                rs.getString("from_location"),
                rs.getString("to_location"),
                rs.getString("date"),
                rs.getString("time"),
                rs.getString("vehicle_type"),
                rs.getInt("seats_available"),
                rs.getString("status")
        );
        ride.setRideId(rs.getInt("ride_id"));
        ride.setDriverName(rs.getString("driver_name"));
        return ride;
    }
}
