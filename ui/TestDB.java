package ui;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("✅ Connected to DB: " + (c != null && !c.isClosed()));
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
