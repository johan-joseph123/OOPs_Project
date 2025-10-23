package model;

public class DriverApplication {
    private String id;
    private String name;
    private String vehicleModel;
    private String licenseNumber;
    private String licenseFile;
    private String status; // pending/approved/rejected

    public DriverApplication(){}
    public DriverApplication(String id, String name, String vehicleModel, String licenseNumber, String licenseFile, String status){
        this.id=id; this.name=name; this.vehicleModel=vehicleModel; this.licenseNumber=licenseNumber; this.licenseFile=licenseFile; this.status=status;
    }
    // getters/setters
    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getVehicleModel(){ return vehicleModel; }
    public String getLicenseNumber(){ return licenseNumber; }
    public String getLicenseFile(){ return licenseFile; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}
