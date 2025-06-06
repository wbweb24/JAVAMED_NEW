package persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import persistence.config.DBSchManager;

public class DataFetcher {
    private DBSchManager dbManager;

    public DataFetcher(DBSchManager dbManager) {
        this.dbManager = dbManager;
    }

    // ✅ Método para obtener datos y transformarlos sin usar DTO, Entity ni Mapper
    public Map<String, Object> fetchData(List<String> requestedFields, String userId) {
        Map<String, Object> fetchedData = new HashMap<>();

        // Construimos la consulta con los campos requeridos
        String query = "SELECT " + String.join(", ", requestedFields) + " FROM users WHERE user_id = ?";
        Map<String, Object> rawData = dbManager.queryMultiple(query, userId);

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
}
