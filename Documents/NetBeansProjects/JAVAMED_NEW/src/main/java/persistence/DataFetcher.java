package persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import persistence.config.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataFetcher {
    private final DbManager dbManager;

    public DataFetcher() {
        this.dbManager = new DbManager("jdbc:mysql://localhost:3306/javamed", "root", "");
    }

    public Map<String, Object> fetchData(List<String> requestedFields, String table, Map<String, String> conditions) {
        Map<String, Object> fetchedData = new HashMap<>();

        // 🔹 Verificar si hay columnas a solicitar
        if (requestedFields.isEmpty()) {
            System.out.println("❌ Error: No se proporcionaron campos para la consulta.");
            return fetchedData;
        }

        // 🔹 Construir la consulta SQL dinámica
        String query = "SELECT " + String.join(", ", requestedFields) + " FROM " + table;
        if (!conditions.isEmpty()) {
            query += " WHERE " + String.join(" AND ", conditions.keySet().stream().map(k -> k + " = ?").toList());
        }

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 🔹 Asignar valores a los parámetros en la consulta
            int index = 1;
            for (String value : conditions.values()) {
                stmt.setString(index++, value);
            }

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String field = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fetchedData.put(field, value instanceof String ? value.toString().trim() : value);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error ejecutando consulta en " + table + ": " + e.getMessage());
            e.printStackTrace();
        }

        return fetchedData;
    }
}
