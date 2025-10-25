package controller;

import dao.RideDAO;
import model.Ride;
import java.sql.SQLException;
import java.util.List;

/**
 * 🧠 Controller — bridge between UI and DAO for rides.
 */
public class RideController {
    private final RideDAO rideDAO = new RideDAO();

    // ✅ Create new ride (Offer Ride)
    public boolean createRide(Ride ride) {
        try {
            return rideDAO.createRide(ride);
        } catch (SQLException e) {
            System.err.println("❌ Error creating ride: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get all rides (for search or admin)
    public List<Ride> getAllRides() {
        try {
            return rideDAO.findAllOpenRides();
        } catch (SQLException e) {
            System.err.println("❌ Error fetching rides: " + e.getMessage());
            return null;
        }
    }

    // ✅ Get all rides for a specific driver (for ProviderTripsPanel)
    public List<Ride> getProviderRides(String driverId) {
        try {
            return rideDAO.findRidesByDriver(driverId);
        } catch (SQLException e) {
            System.err.println("❌ Error fetching provider rides: " + e.getMessage());
            return null;
        }
    }

    // ✅ Mark a ride as completed
    public void markRideCompleted(int rideId) throws SQLException {
        boolean updated = rideDAO.updateRideStatus(rideId, "Completed");
        if (!updated) {
            throw new SQLException("Failed to mark ride as completed.");
        }
    }
}
