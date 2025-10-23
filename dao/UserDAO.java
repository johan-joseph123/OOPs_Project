package dao;

import model.User;

import java.sql.*;

public class UserDAO {

    public boolean createUser(User u) throws SQLException {
        String sql = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            return ps.executeUpdate() > 0;
        }
    }

    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                }
            }
        }
        return null;
    }

    public User findById(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        }
        return null;
    }
}
