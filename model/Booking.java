package model;

/**
 * 🧾 Booking — Represents a single seat booking in a ride.
 */
public class Booking {
    private int id;
    private int rideId;
    private String riderId;
    private String status;

    public Booking() {}

    public Booking(int id, int rideId, String riderId, String status) {
        this.id = id;
        this.rideId = rideId;
        this.riderId = riderId;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRideId() { return rideId; }
    public void setRideId(int rideId) { this.rideId = rideId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
