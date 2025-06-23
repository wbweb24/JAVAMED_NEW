package com.mycompany.javamed_new.services.sessioncontext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FeaturesDefinitions {

    public static final Map<String, Map<String, Object>> FEATURES = Map.of(
        "Agenda", Map.of(
            "tabla", "appointments",
            "acciones", Map.of(
                "📅 Ver agenda", Map.of(
                    "tipo", "salida",
                    "campos", List.of("hora", "paciente", "descripcion", "usuario_asignado"),
                    "filtros", filtrosAgenda(),
                    "titulo", "Agenda del día"
                ),
                "🆕 Nueva cita", Map.of(
                    "tipo", "entrada",
                    "tabla", "appointments",
                    "campos", List.of(
                        Map.of("nombre", "fecha", "tipo", "date"),
                        Map.of("nombre", "hora", "tipo", "time"),
                        Map.of("nombre", "paciente", "tipo", "text"),
                        Map.of("nombre", "descripcion", "tipo", "text")
                    )
                ),
                "✏️ Modificar cita", Map.of(
                    "tipo", "edicion",
                    "tabla", "appointments",
                    "clave", "id", // campo clave único
                    "campos", List.of("hora", "descripcion") // campos editables
                ),
                "🗑️ Eliminar cita", Map.of(
                    "tipo", "eliminacion",
                    "tabla", "appointments",
                    "clave", "id"
                )
            )
        )

        // Podés agregar más módulos como "Clientes", "Usuarios", etc.
    );

    private static Map<String, String> filtrosAgenda() {
        String usuario = SessionService.getUser();
        if (List.of("user1", "user2").contains(usuario)) {
            return Map.of("fecha", LocalDate.now().toString());
        } else {
            return Map.of(
                "fecha", LocalDate.now().toString(),
                "usuario_asignado", usuario
            );
        }
    }
}
