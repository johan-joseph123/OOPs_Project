package model;

import java.time.LocalDate;

public class Ride {
    private int rideId;
    private String driverId;
    private String driverName;
    private String fromLocation;
    private String toLocation;
    private LocalDate date;
    private String time;
    private String vehicleType;
    private int seatsAvailable;
    private String status; // Open, Accepted, Completed, Cancelled

    public Ride() {}
    // constructors & getters/setters
    public int getRideId(){ return rideId; }
    public void setRideId(int rideId){ this.rideId = rideId; }
    public String getDriverId(){ return driverId; }
    public void setDriverId(String driverId){ this.driverId = driverId; }
    public String getDriverName(){ return driverName; }
    public void setDriverName(String driverName){ this.driverName = driverName; }
    public String getFromLocation(){ return fromLocation; }
    public void setFromLocation(String fromLocation){ this.fromLocation = fromLocation; }
    public String getToLocation(){ return toLocation; }
    public void setToLocation(String toLocation){ this.toLocation = toLocation; }
    public LocalDate getDate(){ return date; }
    public void setDate(LocalDate date){ this.date = date; }
    public String getTime(){ return time; }
    public void setTime(String time){ this.time = time; }
    public String getVehicleType(){ return vehicleType; }
    public void setVehicleType(String vehicleType){ this.vehicleType = vehicleType; }
    public int getSeatsAvailable(){ return seatsAvailable; }
    public void setSeatsAvailable(int seatsAvailable){ this.seatsAvailable = seatsAvailable; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
    // convenience
    public String getRoute(){ return fromLocation + " → " + toLocation; }
}
