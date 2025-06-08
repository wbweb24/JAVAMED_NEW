package persistence.config;

import java.sql.*;

public class DbManager {
    private Connection connection;

    public DbManager(String dbUrl, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("✅ Conexión establecida con " + dbUrl);

            updateSchema(); // 🔹 Se actualiza el esquema de inmediato tras conectar
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error conectando a la BD: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void updateSchema() {
        System.out.println("▶ Actualizando esquema de la BD...");
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Esquema actualizado correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error actualizando esquema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error cerrando conexión: " + e.getMessage());
        }
    }
}
