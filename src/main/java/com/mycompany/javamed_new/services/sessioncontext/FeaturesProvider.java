package com.mycompany.javamed_new.services.sessioncontext;

import com.mycompany.javamed_new.App;
import com.mycompany.javamed_new.SecondaryController;
import com.mycompany.javamed_new.persistence.DataFetcher;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;

public class FeaturesProvider {

    private static DataFetcher dataFetcher;

    public static void setDataFetcher(DataFetcher fetcher) {
        dataFetcher = fetcher;
    }

    public static List<Button> getSubMenu(String featureName) {
        Map<String, Object> feature = FeaturesDefinitions.FEATURES.get(featureName);
        if (feature == null) return List.of();

        Map<String, Map<String, Object>> acciones = (Map<String, Map<String, Object>>) feature.get("acciones");
        List<Button> botones = new ArrayList<>();

        for (String accion : acciones.keySet()) {
            Button btn = new Button(accion);
            btn.setOnAction(e -> {
                Map<String, Object> infoAccion = acciones.get(accion);
                Node contenido = handleActionDirect(infoAccion);
                if (contenido != null) {
                    SecondaryController controller = SessionService.getControllerInstance();
                    if (controller != null) controller.updateWorkArea(contenido);
                }
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

        return handleActionDirect(infoAccion);
    }

    public static Node handleActionDirect(Map<String, Object> accionData) {
        String tipo = (String) accionData.get("tipo");
        String modo = (String) accionData.getOrDefault("modo", "");

        if ("entrada".equals(tipo) && "popup".equalsIgnoreCase(modo)) {
            showFormDialog(accionData);
            return null;
        }

        return switch (tipo) {
            case "entrada" -> renderInputView(accionData);
            case "salida" -> renderOutputView(accionData);
            case "edicion" -> renderEditView(accionData);
            case "eliminacion" -> renderDeleteView(accionData);
            default -> fallback("❓ Tipo de acción desconocido");
        };
    }

    public static Node getDefaultView(String featureName) {
        Map<String, Object> feature = FeaturesDefinitions.FEATURES.get(featureName);
        if (feature == null) return fallback("🚫 Módulo no encontrado");

        Map<String, Object> vistaInicial = (Map<String, Object>) feature.get("vista_inicial");
        if (vistaInicial == null) return fallback("ℹ️ Sin vista inicial definida");

        return handleActionDirect(vistaInicial);
    }

    private static Node renderInputView(Map<String, Object> accionData) {
        List<Map<String, String>> campos = (List<Map<String, String>>) accionData.get("campos");
        WorkAreaView vista = new WorkAreaView();
        List<Node> filas = new ArrayList<>();

        for (Map<String, String> campo : campos) {
            String nombre = campo.get("nombre");
            String tipo = campo.get("tipo");

            Label label = new Label(capitalize(nombre) + ":");

            Node input = switch (tipo) {
                case "text" -> new TextField();
                case "date" -> new DatePicker();
                case "time" -> new TextField();
                case "number" -> {
                    TextField tf = new TextField();
                    tf.textProperty().addListener((obs, oldV, newV) -> {
                        if (!newV.matches("\\d*")) tf.setText(oldV);
                    });
                    yield tf;
                }
                default -> new TextField();
            };

            GridPane fila = new GridPane();
            fila.setHgap(10);
            fila.setVgap(5);
            fila.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(input, Priority.ALWAYS);
            fila.add(label, 0, 0);
            fila.add(input, 1, 0);

            filas.add(fila);
        }

        vista.setVerticalContent(filas);
        return vista;
    }

    private static void showFormDialog(Map<String, Object> accionData) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Formulario");

        VBox content = new VBox(15);
        List<Node> filas = new ArrayList<>();

        List<Map<String, String>> campos = (List<Map<String, String>>) accionData.get("campos");
        Map<String, Node> inputs = new LinkedHashMap<>();

        for (Map<String, String> campo : campos) {
            String nombre = campo.get("nombre");
            String tipo = campo.get("tipo");

            Label label = new Label(capitalize(nombre) + ":");

            Node input = switch (tipo) {
                case "text" -> new TextField();
                case "date" -> new DatePicker();
                case "time" -> new TextField();
                case "number" -> {
                    TextField tf = new TextField();
                    tf.textProperty().addListener((obs, oldV, newV) -> {
                        if (!newV.matches("\\d*")) tf.setText(oldV);
                    });
                    yield tf;
                }
                default -> new TextField();
            };

            inputs.put(nombre, input);

            GridPane fila = new GridPane();
            fila.setHgap(10);
            fila.setVgap(5);
            fila.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(input, Priority.ALWAYS);
            fila.add(label, 0, 0);
            fila.add(input, 1, 0);

            filas.add(fila);
        }

        Button aceptar = new Button("Aceptar");
        aceptar.setOnAction(e -> dialog.close());

        content.getChildren().addAll(filas);
        content.getChildren().add(aceptar);

        Scene scene = new Scene(content);
        scene.getStylesheets().add(App.class.getResource("/styles/style2.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private static Node renderOutputView(Map<String, Object> accionData) {
        String tabla = (String) accionData.get("tabla");
        List<String> campos = (List<String>) accionData.get("campos");
        Map<String, String> filtroCrudo = (Map<String, String>) accionData.getOrDefault("filtros", Map.of());
        String titulo = (String) accionData.getOrDefault("titulo", capitalize(tabla));

        Map<String, String> valores = new LinkedHashMap<>();
        Map<String, String> operadores = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : filtroCrudo.entrySet()) {
            String clave = entry.getKey();
            String valor = entry.getValue();
            if (clave.contains(" ")) {
                String[] partes = clave.split(" ", 2);
                valores.put(partes[0], valor);
                operadores.put(partes[0], partes[1]);
            } else {
                valores.put(clave, valor);
                operadores.put(clave, "=");
            }
        }

        List<Map<String, Object>> registros = dataFetcher.fetchMultipleData(campos, tabla, valores, operadores);

        if (registros.isEmpty()) {
            return fallback("📭 No hay datos para mostrar.");
        }

        List<Node> nodos = new ArrayList<>();
        for (String campo : campos) {
            Label cabecera = new Label(capitalize(campo));
            cabecera.getStyleClass().add("output-header");
            nodos.add(cabecera);
        }

        for (Map<String, Object> fila : registros) {
            for (String campo : campos) {
                String valor = Objects.toString(fila.getOrDefault(campo, ""), "");
                Label celda = new Label(valor);
                celda.getStyleClass().add("output-cell");
                nodos.add(celda);
            }
        }

        WorkAreaView grid = new WorkAreaView();
        grid.setContent(nodos, campos.size());

        VBox contenedor = new VBox(new Label(titulo), grid);
        contenedor.setSpacing(15);
        return contenedor;
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
        return s == null || s.isEmpty()
            ? ""
            : s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
