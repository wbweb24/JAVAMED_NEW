package persistence.config;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbManager {

    public Connection getConnection() {
        return connection;
    }
    private Connection connection;

    // ✅ Constructor: Establece la conexión y actualiza el esquema
    public DbManager(String dbUrl, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("✅ Database connection established!");

            updateSchema(); // ✅ Verificar y actualizar el esquema al conectar
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error connecting to database: " + e.getMessage());
        }
    }

    // ✅ Método para actualizar el esquema automáticamente
    public void updateSchema() {
        System.out.println("▶ Checking and updating database schema...");

        String sqlUpdateSchema = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sqlUpdateSchema);
            System.out.println("✅ Database schema updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Método para ejecutar consultas y devolver múltiples valores
    public Map<String, Object> queryMultiple(String query, String userId) {
        Map<String, Object> results = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    results.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error executing query: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    // ✅ Método para cerrar la conexión correctamente
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error closing database connection: " + e.getMessage());
        }
    }
}
