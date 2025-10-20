package ui;

public class Ride {
    private String driverName;
    private String vehicleType;
    private String time;
    private int seatsAvailable;
    private String from;
    private String destination;
    private String date;
    private String status; // "Open", "Accepted by Rider", "Completed"
 // 🔹 Overloaded constructor for compatibility
    public Ride(String driverName, String from, String destination, int seatsAvailable, String date) {
        this(driverName, "Car", "10:00 AM", seatsAvailable, from, destination, date);
    }

    public Ride(String driverName, String vehicleType, String time, int seatsAvailable,
                String from, String destination, String date) {
        this.driverName = driverName;
        this.vehicleType = vehicleType;
        this.time = time;
        this.seatsAvailable = seatsAvailable;
        this.from = from;
        this.destination = destination;
        this.date = date;
        this.status = "Open";
    }

    // main getters
    public String getDriverName() { return driverName; }
    public String getVehicleType() { return vehicleType; }
    public String getTime() { return time; }
    public int getSeatsAvailable() { return seatsAvailable; }
    public String getFrom() { return from; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    // compatibility getters used elsewhere in your code
    public String getName() { return driverName; }
    public int getSeats() { return seatsAvailable; }
    public String getRoute() { return from + " → " + destination; }

    @Override
    public String toString() {
        return driverName + " | " + from + " → " + destination + " | " + date + " " + time + " | " + status;
    }
}

