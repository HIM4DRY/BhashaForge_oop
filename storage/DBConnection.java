package bhashaforge.storage;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/bhashaforge_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection connection = null;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (Exception e) {
            System.out.println("❌ DB Connection Error: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Database disconnected.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}