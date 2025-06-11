package com.mycompany.javamed_new.services.sessioncontext;

import com.mycompany.javamed_new.SecondaryController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class SessionService {
    private static String currentUser;
    private static String currentPosition;
    private static List<Button> menuButtons;
    private static Pane currentView;
    private static SecondaryController secondaryController;



    private static final Map<String, List<String>> menuOptions = Map.of(
        "reception", List.of("Agenda", "Clientes", "Base de Datos"),
        "accounting", List.of("Facturación", "Informes"),
        "management", List.of("Reportes", "Administración"),
        "healthcare_provider", List.of("Pacientes", "Historial Médico")
    );

    public static void startSession(String user, String position) {
        currentUser = user;
        currentPosition = position;
        menuButtons = menuOptions.getOrDefault(position, List.of("Inicio"))
                .stream().map(buttonName -> {
                    Button button = new Button(buttonName);
                    button.setOnAction(e -> switchWorkArea(buttonName)); // 🔹 Agregar evento de cambio de vista
                    return button;
                }).collect(Collectors.toList());
    }

    private static void switchWorkArea(String menuOption) {
        // Aquí se llamaría a WorkArea para actualizar la vista de trabajo
    }

    public static String getUser() { return currentUser; }
    public static String getPosition() { return currentPosition; }
    public static List<Button> getMenu() { return menuButtons; }

    public static void endSession() {
        currentUser = null;
        currentPosition = null;
        menuButtons = null;
    }
    
    private static void updateWorkArea() {
        if (secondaryController != null) {
            secondaryController.updateWorkArea(currentView); // 🔹 Usa la instancia correcta
        }
    }



    public static void setCurrentView(Pane viewForMenuOption) {
        currentView = viewForMenuOption; // 🔹 Guarda la nueva vista en la sesión
        updateWorkArea(); // 🔹 Refresca `workAreaContainer` en `SecondaryController`
    }
    
    public static void setControllerInstance(SecondaryController controller) {
        secondaryController = controller;
    }


}
