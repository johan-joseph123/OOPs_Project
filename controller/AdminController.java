package controller;

import dao.DriverApplicationDAO;
import model.DriverApplication;

import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private final DriverApplicationDAO dao = new DriverApplicationDAO();

    public List<DriverApplication> getPendingApplications() throws SQLException {
        return dao.getPendingApplications();
    }

    public boolean updateApplicationStatus(String id, String status) throws SQLException {
        return dao.updateStatus(id, status);
    }
}
