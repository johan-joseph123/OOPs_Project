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

    public boolean createRide(Ride ride) {
        try {
            return rideDAO.createRide(ride);
        } catch (SQLException e) {
            System.err.println("❌ Error creating ride: " + e.getMessage());
            return false;
        }
    }

    public List<Ride> getAllRides() {
        try {
            return rideDAO.findAllOpenRides();
        } catch (SQLException e) {
            System.err.println("❌ Error fetching rides: " + e.getMessage());
            return null;
        }
    }
     public List<Ride> getProviderRides(String id) throws SQLException{
    	 try {
    		 return rideDAO.findRidesById(id);
    	 }catch (SQLException e) {
             System.err.println("❌ Error fetching rides: " + e.getMessage());
             return null;
     }}
     public void markRideCompleted(int rideId) throws SQLException {
    	    boolean updated = rideDAO.updateRideStatus(rideId, "Completed");
    	    if (!updated) throw new SQLException("Failed to mark ride as completed.");
    	}

}
