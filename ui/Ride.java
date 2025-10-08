package ui;

public class Ride {
    private final String name;         
    private final String vehicleType;
    private final String time;
    private final int seats;
    private final String route; // Use 'route' to store general route info (e.g., "St. Joseph's -> Pala") or destination.

    public Ride(String name, String vehicleType, String time, int seats, String route) {
        this.name = name;
        this.vehicleType = vehicleType;
        this.time = time;
        this.seats = seats;
        this.route = route;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getTime() {
        return time;
    }

    public int getSeats() {
        return seats;
    }

    // For simplicity, using 'route' as the destination in MyTripsPanel,
    // but exposing getPlace() for clarity in some contexts.
    public String getPlace() { 
        return route;
    }
    
    public String getRoute() {
        return route;
    }
}