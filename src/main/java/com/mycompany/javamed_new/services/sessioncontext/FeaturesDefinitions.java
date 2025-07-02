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
                    "modo", "popup",
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
                    "modo", "popup",
                    "tabla", "appointments",
                    "clave", "id",
                    "campos", List.of("hora", "descripcion")
                ),
                "🗑️ Eliminar cita", Map.of(
                    "tipo", "eliminacion",
                    "modo", "popup",
                    "tabla", "appointments",
                    "clave", "id"
                ),
                "📅 Consultar citas antiguas", Map.of(
                    "tipo", "entrada",
                    "modo", "popup",
                    "tabla", "appointments",
                    "campos", List.of(
                        Map.of("nombre", "desde", "tipo", "date")
                    ),
                    "accion_custom", "consultarCitasAntiguas"
                )
            )
        )

        // Podés seguir agregando otros módulos aquí
    );

    private static Map<String, String> filtrosAgenda() {
        String hoy = LocalDate.now().toString();
        String usuario = SessionService.getUser();
        String puesto = SessionService.getPosition();

        if ("reception".equalsIgnoreCase(puesto)) {
            return Map.of("fecha >=", hoy);
        } else {
            return Map.of(
                "fecha >=", hoy,
                "usuario_asignado =", usuario
            );
        }
    }
}
