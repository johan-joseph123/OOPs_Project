package ui;

import java.util.ArrayList;
import java.util.List;

public class ApplicationData {
    // All offered rides (open/accepted/completed)
    public static final List<Ride> allRides = new ArrayList<>();
    // Rides accepted / in user's trips
    public static final List<Ride> myTrips = new ArrayList<>();

    public static void addRide(Ride r) {
        allRides.add(r);
    }

    public static void acceptRide(Ride r) {
        if (!myTrips.contains(r)) {
            r.setStatus("Accepted by Rider");
            myTrips.add(r);
        }
    }

    public static void completeRide(Ride r) {
        r.setStatus("Completed");
    }
}
