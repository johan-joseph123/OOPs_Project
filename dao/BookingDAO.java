package dao;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean createBooking(Booking b) throws SQLException {
        String sql = "INSERT INTO ride_bookings (ride_id, rider_id, status) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getRideId());
            ps.setString(2, b.getRiderId());
            ps.setString(3, b.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE ride_bookings SET status = ? WHERE booking_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Booking> findByRider(String riderId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM ride_bookings WHERE rider_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, riderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setRideId(rs.getInt("ride_id"));
                    b.setRiderId(rs.getString("rider_id"));
                    b.setStatus(rs.getString("status"));
                    list.add(b);
                }
            }
        }
        return list;
    }
}
