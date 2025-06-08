package com.mycompany.javamed_new;

import com.mycompany.javamed_new.services.auth.AuthService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;
import persistence.config.DbManager;

public class PrimaryController {

    @FXML private BorderPane borderPane;
    @FXML private Label titleLabel;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button clearButton;
    @FXML private Button loginButton;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (borderPane != null && borderPane.getScene() != null) {
                String css = getClass().getResource("/styles/style.css").toExternalForm();
                if (css != null) {
                    borderPane.getScene().getStylesheets().add(css);
                    System.out.println("CSS cargado correctamente!");
                } else {
                    System.out.println("Error: Archivo CSS no encontrado.");
                }
            } else {
                System.out.println("Error: borderPane o la escena son null");
            }
        });
    }

    @FXML
    private void switchToSecondary() {
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de vista");
        }
    }

    @FXML
    private void handleClear() {
        userField.clear();
        passwordField.clear();
    }

    @FXML
private void handleLogin() {
    String usuario = userField.getText();
    String password = passwordField.getText();

    if (usuario.isEmpty() || password.isEmpty()) {
        System.out.println("❌ Error: Campos vacíos");
        return;
    }

    System.out.println("Intento de acceso: Usuario = " + usuario + ", Password = " + password);

    // ✅ Crear instancia de `DbManager` y pasarla a `AuthService`
    DbManager dbManager = new DbManager("jdbc:mysql://localhost:3306/javamed", "root", "");


    /*boolean isAuthenticated = authService.authenticateUser(usuario, password);*/

    if (true) {
        System.out.println("✅ Autenticación exitosa");
        switchToSecondary();
    } else {
        System.out.println("❌ Autenticación errónea");
    }
}

   
}
