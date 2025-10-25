package dao;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 💾 BookingDAO — Database Access Object for the `ride_bookings` table.
 */
public class BookingDAO {

    /**
     * ✅ Create a new booking (with duplicate check).
     */
    public boolean createBooking(Booking booking) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {

            // Prevent duplicate booking by same rider
            String checkSql = "SELECT COUNT(*) FROM ride_bookings WHERE ride_id = ? AND rider_id = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, booking.getRideId());
                checkPs.setString(2, booking.getRiderId());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️ Duplicate booking detected for rider: " + booking.getRiderId());
                    return false;
                }
            }

            // Insert booking
            String sql = "INSERT INTO ride_bookings (ride_id, rider_id, status) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, booking.getRideId());
                ps.setString(2, booking.getRiderId());
                ps.setString(3, booking.getStatus());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error (createBooking): " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Update booking status (for completion, cancellation, etc.)
     */
    public boolean updateBookingStatus(int bookingId, String newStatus) throws SQLException {
        if (bookingId <= 0 || newStatus == null || newStatus.isEmpty()) return false;

        String sql = "UPDATE ride_bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            int updated = ps.executeUpdate();
            System.out.println("🧾 Booking " + bookingId + " updated to status: " + newStatus);
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("❌ SQL Error (updateBookingStatus): " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Find all bookings for a specific rider.
     */
    public List<Booking> findByRider(String riderId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM ride_bookings WHERE rider_id = ? ORDER BY booking_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, riderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("booking_id"));
                booking.setRideId(rs.getInt("ride_id"));
                booking.setRiderId(rs.getString("rider_id"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    /**
     * ✅ Find all bookings for a specific ride.
     */
    public List<Booking> findByRideId(int rideId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM ride_bookings WHERE ride_id = ? ORDER BY booking_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("booking_id"));
                booking.setRideId(rs.getInt("ride_id"));
                booking.setRiderId(rs.getString("rider_id"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    /**
     * ✅ (Optional) Find all bookings for a ride, with rider usernames.
     *    - Joins with users table to return richer details.
     */
    public List<String> findRidersWithNamesByRideId(int rideId) throws SQLException {
        List<String> riders = new ArrayList<>();
        String sql = """
            SELECT u.username, b.rider_id, b.status
            FROM ride_bookings b
            JOIN users u ON b.rider_id = u.id
            WHERE b.ride_id = ?
            ORDER BY b.booking_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String line = rs.getString("username") + " (ID: " +
                              rs.getString("rider_id") + ") — " +
                              rs.getString("status");
                riders.add(line);
            }
        }
        return riders;
    }
}
