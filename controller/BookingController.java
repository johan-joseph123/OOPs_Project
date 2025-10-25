package controller;

import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;
import java.sql.SQLException;

/**
 * 🎟️ BookingController — Handles booking creation, cancellation, and completion.
 */
public class BookingController {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final RideDAO rideDAO = new RideDAO();

    /**
     * ✅ Book a seat for a given ride and rider.
     */
    public boolean bookSeat(int rideId, String riderId) throws SQLException {
        if (rideId <= 0 || riderId == null || riderId.isEmpty()) return false;

        Booking booking = new Booking();
        booking.setRideId(rideId);
        booking.setRiderId(riderId);
        booking.setStatus("Accepted");

        boolean created = bookingDAO.createBooking(booking);

        // Automatically close the ride if seats are full
        if (created) {
            RideController rc = new RideController();
            //rc.autoCloseIfFull(rideId);
        }

        return created;
    }

    /**
     * ✅ Mark a booking as completed.
     */
    public void completeBooking(int bookingId) throws SQLException {
        bookingDAO.updateBookingStatus(bookingId, "Completed");
        System.out.println("✅ Booking ID " + bookingId + " marked as completed.");
    }
    /**
     * ✅ Cancel a booking.
     */
    public boolean cancelBooking(int bookingId) throws SQLException {
        if (bookingId <= 0) return false;
        return bookingDAO.updateBookingStatus(bookingId, "Cancelled");
    }
}
