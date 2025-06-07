package persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import persistence.config.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataFetcher {
    private DbManager dbManager;

    public DataFetcher(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    

    // ✅ Método para obtener datos y transformarlos sin usar DTO, Entity ni Mapper
    public Map<String, Object> fetchData(List<String> requestedFields, String userId) {
        Map<String, Object> fetchedData = new HashMap<>();

        // Construimos la consulta con los campos requeridos
        String query = "SELECT " + String.join(", ", requestedFields) + " FROM users WHERE user_id = ?";
        Map<String, Object> rawData = dbManager.queryMultiple(query, userId);
        
        // 🔹 Verificamos si `rawData` contiene información antes de procesarlo
        if (rawData == null || rawData.isEmpty()) {
            System.out.println("❌ Error: No se encontraron datos para el usuario " + userId);
            return fetchedData; // Devuelve un mapa vacío
        }

        // ✅ Transformación de datos que antes estaba en GenericDTO y GenericMapper
        for (String field : requestedFields) {
            Object value = rawData.getOrDefault(field, null);

            // ⚠️ Aplicamos una conversión básica según el tipo de dato esperado
            if (value instanceof Integer) {
                fetchedData.put(field, (Integer) value);
            } else if (value instanceof Boolean) {
                fetchedData.put(field, (Boolean) value);
            } else if (value instanceof String) {
                fetchedData.put(field, value.toString().trim()); // Eliminamos espacios innecesarios
            } else {
                fetchedData.put(field, value); // Guardamos tal cual si no necesita conversión
            }
        }

        return fetchedData;
    }
    
    public String getUserPasswordHash(String username) {
        String hash = "";
        String sql = "SELECT p.password_hash FROM users_credentials uc JOIN passwords p ON uc.id_password = p.id_password WHERE uc.username = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                hash = rs.getString("password_hash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hash;
    }
}

