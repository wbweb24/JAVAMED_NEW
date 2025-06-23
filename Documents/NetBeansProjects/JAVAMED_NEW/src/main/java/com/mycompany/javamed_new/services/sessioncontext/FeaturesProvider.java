package com.mycompany.javamed_new.services.sessioncontext;

import com.mycompany.javamed_new.SecondaryController;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;

public class FeaturesProvider {

    public static List<Button> getSubMenu(String featureName) {
        Map<String, Object> feature = FeaturesDefinitions.FEATURES.get(featureName);
        if (feature == null) return List.of();

        Map<String, Map<String, Object>> acciones = (Map<String, Map<String, Object>>) feature.get("acciones");
        List<Button> botones = new ArrayList<>();

        for (String accion : acciones.keySet()) {
            Button btn = new Button(accion);
            btn.setOnAction(e -> {
                Node contenido = handleAction(featureName, accion);
                SecondaryController controller = SessionService.getControllerInstance();
                if (controller != null) controller.updateWorkArea(contenido);
            });
            botones.add(btn);
        }

        return botones;
    }

    public static Node handleAction(String featureName, String accion) {
        Map<String, Object> feature = FeaturesDefinitions.FEATURES.get(featureName);
        if (feature == null) return fallback("🚫 Feature no encontrada");

        Map<String, Map<String, Object>> acciones = (Map<String, Map<String, Object>>) feature.get("acciones");
        Map<String, Object> infoAccion = acciones.get(accion);
        if (infoAccion == null) return fallback("⚠️ Acción no reconocida");

        String tipo = (String) infoAccion.get("tipo");

        return switch (tipo) {
            case "entrada" -> renderInputView(infoAccion);
            case "salida" -> renderOutputView(infoAccion);
            case "edicion" -> renderEditView(infoAccion);
            case "eliminacion" -> renderDeleteView(infoAccion);
            default -> fallback("❓ Tipo de acción desconocido");
        };
    }

    // 🧾 Formulario dinámico a partir de campos declarados
    private static Node renderInputView(Map<String, Object> accionData) {
        List<Map<String, String>> campos = (List<Map<String, String>>) accionData.get("campos");
        WorkAreaView vista = new WorkAreaView();
        List<Node> nodos = new ArrayList<>();

        for (Map<String, String> campo : campos) {
            String nombre = campo.get("nombre");
            String tipo = campo.get("tipo");

            Label label = new Label(capitalize(nombre) + ":");

            Node input = switch (tipo) {
                case "text" -> new TextField();
                case "date" -> new DatePicker();
                case "time" -> new TextField(); // Podés sustituir con control más específico
                case "number" -> {
                    TextField tf = new TextField();
                    tf.textProperty().addListener((obs, oldV, newV) -> {
                        if (!newV.matches("\\d*")) tf.setText(oldV);
                    });
                    yield tf;
                }
                default -> new TextField();
            };

            nodos.add(label);
            nodos.add(input);
        }

        vista.setContent(nodos, 2);
        return vista;
    }

    private static Node renderOutputView(Map<String, Object> accionData) {
        return fallback("📊 Tabla dinámica aún no implementada");
    }

    private static Node renderEditView(Map<String, Object> accionData) {
        return fallback("✏️ Edición aún no implementada");
    }

    private static Node renderDeleteView(Map<String, Object> accionData) {
        return fallback("🗑️ Eliminación aún no implementada");
    }

    private static Node fallback(String mensaje) {
        return new VBox(new Label(mensaje));
    }

    private static String capitalize(String s) {
        return s == null || s.isEmpty() ? "" : s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
