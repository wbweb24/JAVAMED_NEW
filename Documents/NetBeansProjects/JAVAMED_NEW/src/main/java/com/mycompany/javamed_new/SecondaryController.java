package com.mycompany.javamed_new;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import com.mycompany.javamed_new.services.sessioncontext.SessionService;
import com.mycompany.javamed_new.services.sessioncontext.WorkArea;
import javafx.scene.layout.Pane;

public class SecondaryController {
    
    @FXML private VBox menuContainer;
    @FXML private VBox subMenuContainer;
    @FXML private StackPane workAreaContainer;

    @FXML
    private void initialize() {
        SessionService.setControllerInstance(this);
        menuContainer.getChildren().addAll(SessionService.getMenu()); // 🔹 Carga el menú principal
        menuContainer.getChildren().forEach(button -> 
            button.setOnMouseClicked(e -> updateSubMenu(((javafx.scene.control.Button) button).getText())) // 🔹 Evento para submenú
        );
        
        workAreaContainer.getChildren().setAll(WorkArea.getViewForMenuOption("Inicio")); // 🔹 Vista inicial
    }

    private void updateSubMenu(String mainMenuOption) {
        subMenuContainer.getChildren().setAll(WorkArea.getSubMenu(mainMenuOption)); // 🔹 Actualiza el submenú
    }

    public void updateWorkArea(Pane newView) {
        workAreaContainer.getChildren().setAll(newView); // 🔹 Cambia la vista activamente
    }

    @FXML
    private void switchToPrimary() throws IOException {
        SessionService.endSession(); // 🔹 Limpia datos de sesión
        App.setRoot("primary");
    }
    
    
}
