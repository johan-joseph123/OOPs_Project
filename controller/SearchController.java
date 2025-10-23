package controller;

import dao.RideDAO;
import model.Ride;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SearchController {
    private final RideDAO dao = new RideDAO();

    public List<Ride> searchByDestination(String to, LocalDate date) throws SQLException {
        return dao.searchOpenRidesByDestination(to, date);
    }
}
