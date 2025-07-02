package com.mycompany.javamed_new.persistence;

import com.mycompany.javamed_new.persistence.config.DbManager;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataFetcher {
    private final DbManager dbManager;

    // ✅ Constructor con inyección de dependencia
    public DataFetcher(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    // Consulta simple
    public Map<String, Object> fetchData(List<String> campos, String tabla, Map<String, String> condiciones) {
        Map<String, Object> fila = new HashMap<>();
        List<Map<String, Object>> lista = fetchMultipleData(
            campos,
            tabla,
            condiciones,
            condiciones.keySet().stream().collect(Collectors.toMap(k -> k, k -> "="))
        );
        if (!lista.isEmpty()) fila = lista.get(0);
        return fila;
    }

    // Consulta múltiple con operadores dinámicos
    public List<Map<String, Object>> fetchMultipleData(List<String> campos, String tabla, Map<String, String> valores, Map<String, String> operadores) {
        List<Map<String, Object>> resultados = new ArrayList<>();

        if (campos == null || campos.isEmpty()) {
            System.out.println("❌ Error: campos vacíos.");
            return resultados;
        }

        String query = "SELECT " + String.join(", ", campos) + " FROM " + tabla;
        if (!valores.isEmpty()) {
            List<String> condiciones = new ArrayList<>();
            for (String campo : valores.keySet()) {
                String operador = operadores.getOrDefault(campo, "=").trim();
                condiciones.add(campo + " " + operador + " ?");
            }
            query += " WHERE " + String.join(" AND ", condiciones);
        }

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int index = 1;
            for (String campo : valores.keySet()) {
                stmt.setString(index++, valores.get(campo));
            }

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String key = meta.getColumnName(i);
                    Object val = rs.getObject(i);
                    fila.put(key, val instanceof String ? val.toString().trim() : val);
                }
                resultados.add(fila);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error ejecutando consulta en " + tabla + ": " + e.getMessage());
            e.printStackTrace();
        }

        return resultados;
    }
}
