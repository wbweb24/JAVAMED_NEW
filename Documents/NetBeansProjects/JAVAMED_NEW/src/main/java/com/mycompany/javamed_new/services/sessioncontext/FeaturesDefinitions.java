package com.mycompany.javamed_new.services.sessioncontext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FeaturesDefinitions {

    public static final Map<String, Map<String, Object>> FEATURES = Map.of(
        "Agenda", Map.of(
            "tabla", "appointments",
            "vista_inicial", Map.of(
                "tipo", "salida",
                "tabla", "appointments",
                "campos", List.of("hora", "paciente", "descripcion", "usuario_asignado"),
                "filtros", filtrosAgenda(),
                "titulo", "Agenda desde hoy"
            ),
            "acciones", Map.of(
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
                    "clave", "id",
                    "campos", List.of("hora", "descripcion")
                ),
                "🗑️ Eliminar cita", Map.of(
                    "tipo", "eliminacion",
                    "tabla", "appointments",
                    "clave", "id"
                )
            )
        )

        // Aquí podés agregar más módulos como "Clientes", "Usuarios", etc.
    );

    private static Map<String, String> filtrosAgenda() {
        String hoy = LocalDate.now().toString();
        String usuario = SessionService.getUser();

        if (List.of("user1", "user2").contains(usuario)) {
            return Map.of("fecha >=", hoy); // Mostrar desde hoy
        } else {
            return Map.of(
                "fecha >=", hoy,
                "usuario_asignado =", usuario
            );
        }
    }
}
