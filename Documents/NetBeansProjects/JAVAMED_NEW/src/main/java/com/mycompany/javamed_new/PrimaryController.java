package com.mycompany.javamed_new;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;
import persistence.config.DbManager;
import persistence.DbUpdater;
import com.mycompany.javamed_new.services.auth.AuthService;
import java.util.Map;

public class PrimaryController {

    @FXML private BorderPane borderPane;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;

    private final DbUpdater dbUpdater;
    private final AuthService authService;

    public PrimaryController() {
        this.dbUpdater = new DbUpdater(new DbManager("jdbc:mysql://localhost:3306/javamed", "root", ""));
        this.authService = new AuthService(); // 🔹 Instanciamos `AuthService`
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (borderPane != null && borderPane.getScene() != null) {
                String css = getClass().getResource("/styles/style.css").toExternalForm();
                if (css != null) {
                    borderPane.getScene().getStylesheets().add(css);
                    System.out.println("✅ CSS cargado correctamente!");
                } else {
                    System.out.println("❌ Error: Archivo CSS no encontrado.");
                }
            } else {
                System.out.println("❌ Error: borderPane o la escena son null");
            }
        });
    }

    @FXML
    private void switchToSecondary() {
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error al acceder a SecondaryView.");
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
            System.out.println("❌ Error: Campos vacíos.");
            return;
        }

        System.out.println("🔐 Intento de acceso: Usuario = " + usuario);

        boolean isAuthenticated = authService.authenticateUser(usuario, password);

        if (isAuthenticated) {
            System.out.println("✅ Autenticación exitosa.");
            switchToSecondary();
        } else {
            System.out.println("❌ Autenticación errónea.");
        }
    }

    @FXML
    private void createNewUser() {
        String newUser = userField.getText();
        String newPassword = passwordField.getText();

        if (newUser.isEmpty() || newPassword.isEmpty()) {
            System.out.println("❌ Error: Todos los campos son obligatorios.");
            return;
        }

        DbUpdater dbUpdater = new DbUpdater(new DbManager("jdbc:mysql://localhost:3306/javamed", "root", ""));
        AuthService authService = new AuthService();

        // 🔹 Hashear la contraseña ANTES de insertar el usuario
        String hashedPassword = AuthService.hashPassword(newPassword);

        // 🔹 Insertar usuario en `users`
        boolean userInserted = dbUpdater.insertData("users", Map.of("username", newUser));

        if (userInserted) {
            int userId = authService.getUserId(newUser);

            if (userId > 0) {
                // 🔹 Insertar hash en `passwords`, vinculándolo con `users`
                boolean passwordInserted = dbUpdater.insertData("passwords", Map.of("id_user", userId, "password_hash", hashedPassword));

                if (passwordInserted) {
                    System.out.println("✅ Usuario y contraseña registrados correctamente.");
                    userField.clear();
                    passwordField.clear();
                } else {
                    System.out.println("❌ Error al registrar la contraseña.");
                }
            } else {
                System.out.println("❌ Error: No se pudo recuperar el ID del usuario.");
            }
        } else {
            System.out.println("❌ Error al registrar usuario.");
        }
    }
}
