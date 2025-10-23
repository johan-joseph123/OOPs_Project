package model;

public class Booking {
    private int bookingId;
    private int rideId;
    private String riderId;
    private String status; // Accepted, Completed, Cancelled

    public Booking() {}
    public int getBookingId(){ return bookingId; }
    public void setBookingId(int bookingId){ this.bookingId = bookingId; }
    public int getRideId(){ return rideId; }
    public void setRideId(int rideId){ this.rideId = rideId; }
    public String getRiderId(){ return riderId; }
    public void setRiderId(String riderId){ this.riderId = riderId; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}
