package com.mycompany.javamed_new;

import com.mycompany.javamed_new.errors.ErrorsHandler;
import com.mycompany.javamed_new.services.auth.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PrimaryController {

    @FXML private BorderPane borderPane;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;

    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (borderPane != null && borderPane.getScene() != null) {
                String css = getClass().getResource("/styles/style.css").toExternalForm();
                if (css != null) {
                    borderPane.getScene().getStylesheets().add(css);
                    ErrorsHandler.log("css.loaded");
                } else {
                    ErrorsHandler.log("css.not.found");
                }
            } else {
                ErrorsHandler.log("scene.null");
            }
        });
    }

    @FXML
    private void switchToSecondary() {
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            ErrorsHandler.handle(e);
            ErrorsHandler.log("scene.secondary.error");
        }
    }

    @FXML
    private void handleClear() {
        userField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleLogin() {
        String usuario = userField.getText().trim();
        String password = passwordField.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            ErrorsHandler.log("login.empty.fields");
            return;
        }

        ErrorsHandler.log("login.attempt", usuario);

        boolean isAuthenticated = authService.authenticateUser(usuario, password);

        if (isAuthenticated) {
            ErrorsHandler.log("login.success", usuario);
            switchToSecondary();
        } else {
            ErrorsHandler.log("login.failure", usuario);
        }
    }
}
