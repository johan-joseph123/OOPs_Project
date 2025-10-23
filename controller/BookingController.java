package controller;

import dao.BookingDAO;
import dao.RideDAO;
import model.Booking;

import java.sql.SQLException;

/**
 * Handles booking creation and completion logic.
 */
public class BookingController {
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RideDAO rideDAO = new RideDAO();

    /** Book a seat for a given ride and rider. */
    public boolean bookSeat(int rideId, String riderId) throws SQLException {
        if (rideId <= 0 || riderId == null || riderId.isEmpty()) return false;

        // Try to reserve seat
        boolean seatReserved = rideDAO.reserveSeat(rideId);
        if (!seatReserved) return false;

        // Create booking entry
        Booking b = new Booking();
        b.setRideId(rideId);
        b.setRiderId(riderId);
        b.setStatus("Accepted");
        boolean created = bookingDAO.createBooking(b);

        // If seat booked, verify if ride now full → close it
        if (created) {
            new RideController().autoCloseIfFull(rideId);
        }

        return created;
    }

    /** Mark booking as completed. */
    public boolean completeBooking(int bookingId) throws SQLException {
        if (bookingId <= 0) return false;
        return bookingDAO.updateBookingStatus(bookingId, "Completed");
    }
}
