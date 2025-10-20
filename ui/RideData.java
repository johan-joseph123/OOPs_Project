package ui;

import java.util.ArrayList;
import java.util.List;

public class RideData {
    private static final List<Ride> rideList = new ArrayList<>();

    public static void addRide(Ride ride) {
        rideList.add(ride);
    }

    public static List<Ride> getAllRides() {
        return new ArrayList<>(rideList); // return copy to avoid modification
    }

    public static boolean isEmpty() {
        return rideList.isEmpty();
    }
}
