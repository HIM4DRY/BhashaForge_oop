package bhashaforge.storage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Saves/loads code using MySQL database
public class DatabaseStorage implements Storage {

    // Save code with a title for this user
    public void saveCode(String username, String code, String title, int userId) {
        String sql = "INSERT INTO saved_codes (user_id, title, code) VALUES (?, ?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, code);
            stmt.executeUpdate();
            System.out.println("✅ Code saved to database!");
        } catch (Exception e) {
            System.out.println("❌ Save error: " + e.getMessage());
        }
    }

    // Basic save (from Storage interface)
    @Override
    public void saveCode(String username, String code) {
        // Do nothing - we always use the full version
        // kept only because Storage interface requires it
    }

    // Load all codes for a user
    @Override
    public List<String> loadCodes(String username) {
        List<String> codes = new ArrayList<>();
        String sql = """
            SELECT sc.title, sc.code, sc.saved_at
            FROM saved_codes sc
            JOIN users u ON sc.user_id = u.id
            WHERE u.username = ?
            ORDER BY sc.saved_at DESC
        """;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String entry =
                        "Title: " + rs.getString("title") + "\n" +
                                "Date:  " + rs.getString("saved_at") + "\n" +
                                rs.getString("code");
                codes.add(entry);
            }
        } catch (Exception e) {
            System.out.println("❌ Load error: " + e.getMessage());
        }
        return codes;
    }

    // Delete a saved code by its position
    public boolean deleteCode(int userId, int codeId) {
        String sql = "DELETE FROM saved_codes WHERE id=? AND user_id=?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, codeId);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("❌ Delete error: " + e.getMessage());
            return false;
        }
    }

    // Get code IDs for a user (for delete feature)
    public List<Integer> getCodeIds(String username) {
        List<Integer> ids = new ArrayList<>();
        String sql = """
            SELECT sc.id FROM saved_codes sc
            JOIN users u ON sc.user_id = u.id
            WHERE u.username = ?
            ORDER BY sc.saved_at DESC
        """;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return ids;
    }
}