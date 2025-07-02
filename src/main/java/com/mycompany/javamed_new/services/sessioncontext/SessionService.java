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
        "healthcare_pro", List.of("Pacientes", "Historial Médico")
    );

    public static void startSession(String user, String position) {
        currentUser = user;
        currentPosition = position;
        menuButtons = menuOptions.getOrDefault(position, List.of("Inicio"))
            .stream().map(buttonName -> {
                Button button = new Button(buttonName);
                button.setOnAction(e -> switchWorkArea(buttonName));
                return button;
            }).collect(Collectors.toList());
    }

    private static void switchWorkArea(String menuOption) {
        // Si lo deseás, aquí podrías delegar a FeaturesProvider para mostrar algo base
    }

    public static String getUser() { return currentUser; }
    public static String getPosition() { return currentPosition; }
    public static List<Button> getMenu() { return menuButtons; }

    public static void endSession() {
        currentUser = null;
        currentPosition = null;
        menuButtons = null;
        currentView = null;
        secondaryController = null;
    }

    public static void setCurrentView(Pane viewForMenuOption) {
        currentView = viewForMenuOption;
        updateWorkArea();
    }

    private static void updateWorkArea() {
        if (secondaryController != null && currentView != null) {
            secondaryController.updateWorkArea(currentView);
        }
    }

    public static void setControllerInstance(SecondaryController controller) {
        secondaryController = controller;
    }

    public static SecondaryController getControllerInstance() {
        return secondaryController;
    }
}
