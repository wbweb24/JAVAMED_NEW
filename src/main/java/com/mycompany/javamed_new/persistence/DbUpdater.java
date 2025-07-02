package com.mycompany.javamed_new.persistence;

import com.mycompany.javamed_new.persistence.config.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DbUpdater {
    private final DbManager dbManager;

    public DbUpdater(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean insertData(String table, Map<String, Object> data) {
        if (data.isEmpty()) {
            System.out.println("❌ Error: No se proporcionaron datos para insertar en " + table);
            return false;
        }

        String query = "INSERT INTO " + table + " (" + String.join(", ", data.keySet()) + ") VALUES (" +
                       String.join(", ", data.keySet().stream().map(k -> "?").toList()) + ")";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int index = 1;
            for (Object value : data.values()) {
                stmt.setObject(index++, value);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Datos insertados correctamente en `" + table + "`.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar datos en `" + table + "`: " + e.getMessage());
        }
        return false;
    }
}
