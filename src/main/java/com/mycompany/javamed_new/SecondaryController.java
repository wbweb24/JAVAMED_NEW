package com.mycompany.javamed_new;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.mycompany.javamed_new.services.sessioncontext.SessionService;
import com.mycompany.javamed_new.services.sessioncontext.FeaturesProvider;

public class SecondaryController {

    @FXML private GridPane secondaryMenuContainer;
    @FXML private VBox secondarySubMenuContainer;
    @FXML private StackPane secondaryWorkAreaContainer;

    @FXML
    private void initialize() {
        SessionService.setControllerInstance(this);

        // Cargar el menú principal con acción completa
        int column = 0;
        for (Button button : SessionService.getMenu()) {
            secondaryMenuContainer.add(button, column++, 0);
            button.setOnAction(e -> {
                String nombre = button.getText();
                updateSubMenu(nombre);
                updateWorkArea(FeaturesProvider.getDefaultView(nombre));
            });
        }

        // Pantalla de bienvenida inicial
        Label welcome = new Label("👋 ¡Bienvenido, " + SessionService.getUser() + "!");
        Label dateInfo = new Label("Hoy es " + LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"))
        ));
        Label hint = new Label("Selecciona una opción del menú para comenzar.");

        VBox welcomeBox = new VBox(10, welcome, dateInfo, hint);
        welcomeBox.setAlignment(Pos.CENTER);
        secondaryWorkAreaContainer.getChildren().setAll(welcomeBox);
    }

    public void updateSubMenu(String mainMenuOption) {
        secondarySubMenuContainer.getChildren().setAll(
            FeaturesProvider.getSubMenu(mainMenuOption)
        );
    }

    public void updateWorkArea(Node newView) {
        secondaryWorkAreaContainer.getChildren().setAll(newView);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        SessionService.endSession();
        App.setRoot("primary");
    }
}
