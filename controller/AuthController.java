package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws Exception {
        User u = userDAO.findByUsernameAndPassword(username, password);
        if (u == null) throw new Exception("Invalid credentials");
        return u;
    }

    public boolean registerRider(String id, String username, String password) throws Exception {
        model.User u = new model.User(id, username, password, "rider");
        return userDAO.createUser(u);
    }
    public boolean registerDriver(User u) throws Exception {
        return userDAO.createUser(u);
    }

}
