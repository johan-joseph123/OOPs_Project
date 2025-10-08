// In file: ui/Driver.java
package ui;

// Let's make a base User class to avoid repeating code
public class Driver extends User {
    private final String vehicleModel;
    private final String licensePlate;

    public Driver(String name, String password, String vehicleModel, String licensePlate) {
        super(name, password, "DRIVER");
        this.vehicleModel = vehicleModel;
        this.licensePlate = licensePlate;
    }

    public String getVehicleModel() { return vehicleModel; }
    public String getLicensePlate() { return licensePlate; }
}