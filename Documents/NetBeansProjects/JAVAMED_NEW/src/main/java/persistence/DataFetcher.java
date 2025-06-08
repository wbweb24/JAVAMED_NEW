package persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import persistence.config.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class DataFetcher {
    private DbManager dbManager;

    public DataFetcher(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    
    public Map<String, Object> fetchData(List<String> requestedFields, String identifier) {
        Map<String, Object> fetchedData = new HashMap<>();
        String query = "SELECT " + String.join(", ", requestedFields) + " FROM users WHERE user_id = ?";

        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setString(1, identifier);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String field = metaData.getColumnName(i);
                    Object value = rs.getObject(i);

                // ✅ Aplicamos transformación antes de devolver los datos
                    if (value instanceof String) {
                        fetchedData.put(field, value.toString().trim()); // 🔹 Eliminamos espacios
                    } else {
                        fetchedData.put(field, value);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error ejecutando consulta: " + e.getMessage());
            e.printStackTrace();
        }

        return fetchedData;
    }
}
