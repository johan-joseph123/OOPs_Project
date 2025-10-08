// In file: ui/ApplicationData.java
package ui;

import java.util.ArrayList;
import java.util.List;

public class ApplicationData {
    // User data
    public static List<Driver> pendingDrivers = new ArrayList<>();
    public static List<Driver> approvedDrivers = new ArrayList<>();
    public static List<Rider> riders = new ArrayList<>();

    // Ride data
    public static List<Ride> allOfferedRides = new ArrayList<>();
    
    // Add a static initializer block to create a default admin user for testing
    static {
        // You can add sample riders or drivers here for testing
        riders.add(new Rider("user", "pass"));
    }
}