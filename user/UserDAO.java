package bhashaforge.user;
import bhashaforge.storage.DBConnection;
import java.sql.*;

// Handles all user database operations
public class UserDAO {

    // Register new user
    public boolean register(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.toLowerCase().trim());
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            // username already exists
            return false;
        } catch (Exception e) {
            System.out.println("❌ Register error: " + e.getMessage());
            return false;
        }
    }

    // Login: check username + password
    public int login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username=? AND password=?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.toLowerCase().trim());
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // login success
            }
        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }
        return -1; // login failed
    }

    // Check if username already taken
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username=?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return false;
    }
}