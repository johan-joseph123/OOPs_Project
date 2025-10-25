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
     * ✅ Create a new booking.
     */
    public boolean createBooking(Booking booking) throws SQLException {
        Connection conn = DBConnection.getConnection();

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

        String sql = "INSERT INTO ride_bookings (ride_id, rider_id, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getRideId());
            ps.setString(2, booking.getRiderId());
            ps.setString(3, booking.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ SQL Error (createBooking): " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Update booking status.
     */
    public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE ride_bookings SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ SQL Error (updateBookingStatus): " + e.getMessage());
            return false;
        }
    }

    public List<Booking> findByRider(String riderId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM ride_bookings WHERE rider_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, riderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setRideId(rs.getInt("ride_id"));
                booking.setRiderId(rs.getString("rider_id"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    /**
     * ✅ Get all bookings for a specific ride.
     */
    public List<Booking> findByRideId(int rideId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM ride_bookings WHERE ride_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setRideId(rs.getInt("ride_id"));
                booking.setRiderId(rs.getString("rider_id"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }
}
