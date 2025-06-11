package com.mycompany.javamed_new.services.sessioncontext;

import java.util.Map;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class WorkArea {
    private static final Map<String, List<String>> subMenuOptions = Map.of(
        "Agenda", List.of("Consultar", "Nuevo", "Eliminar", "Modificar"),
        "Facturación", List.of("Ver reportes", "Emitir factura", "Modificar"),
        "Pacientes", List.of("Buscar historial", "Registrar paciente", "Eliminar registro"),
        "Base de Datos", List.of("Añadir registro", "Consultar", "Exportar")
    );

    private static final Map<String, String> viewPaths = Map.of(
        "Consultar", "/views/consultar.fxml",
        "Nuevo", "/views/nuevo.fxml",
        "Eliminar", "/views/eliminar.fxml",
        "Modificar", "/views/modificar.fxml"
    );

    public static VBox getSubMenu(String mainMenuOption) {
        VBox subMenuContainer = new VBox(10); // 🔹 Espaciado dinámico
        List<String> subOptions = subMenuOptions.getOrDefault(mainMenuOption, List.of("Volver"));

        subOptions.forEach(option -> {
            Button button = new Button(option);
            button.setOnAction(e -> updateWorkArea(option));
            subMenuContainer.getChildren().add(button);
        });

        return subMenuContainer;
    }

    private static void updateWorkArea(String option) {
        SessionService.setCurrentView(getViewForMenuOption(option)); // 🔹 Actualiza vista en sesión
    }

    public static Pane getViewForMenuOption(String menuOption) {
        try {
            return FXMLLoader.load(WorkArea.class.getResource(viewPaths.get(menuOption)));
        } catch (Exception e) {
            System.out.println("❌ Error al cargar vista: " + menuOption);
            return new Pane(); // 🔹 Vista vacía si no se encuentra el archivo
        }
    }
}
