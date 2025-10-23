package controller;

import dao.RideDAO;
import model.Ride;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for handling ride-related operations.
 */
public class RideController {
    private final RideDAO dao = new RideDAO();

    /** Create (offer) a new ride. */
    public boolean offerRide(Ride ride) throws SQLException {
        if (ride == null) return false;
        return dao.createRide(ride);
    }

    /** Get all rides offered by a specific driver. */
    public List<Ride> getProviderRides(String driverId) throws SQLException {
        if (driverId == null || driverId.isEmpty()) return List.of();
        return dao.findByDriverId(driverId);
    }

    /** Mark ride as completed. */
    public boolean markRideCompleted(int rideId) throws SQLException {
        if (rideId <= 0) return false;
        return dao.updateRideStatus(rideId, "Completed");
    }

    /** Auto-close ride when no seats are available. */
    public void autoCloseIfFull(int rideId) throws SQLException {
        Ride r = dao.findById(rideId);
        if (r != null && r.getSeatsAvailable() <= 0 &&
            !"Completed".equalsIgnoreCase(r.getStatus())) {
            dao.updateRideStatus(rideId, "Completed");
        }
    }
}
