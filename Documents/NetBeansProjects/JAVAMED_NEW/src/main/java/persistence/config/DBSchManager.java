package persistence.config;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBSchManager {
    private Connection connection;

    // ✅ Constructor para establecer la conexión con la base de datos
    public DBSchManager(String dbUrl, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e); // ⚠️ No manejamos, solo propagamos para que App lo capture
        }
    }

    // ✅ Método para realizar consultas que devuelven múltiples valores
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
        } catch (Exception e) {
            throw new RuntimeException(e); // ⚠️ Propagamos sin manejar internamente
        }

        return results;
    }
}
