package com.mycompany.javamed_new;

import com.mycompany.javamed_new.errors.ErrorsHandler;
import com.mycompany.javamed_new.persistence.DataFetcher;
import com.mycompany.javamed_new.persistence.DbUpdater;
import com.mycompany.javamed_new.persistence.config.DbManager;
import com.mycompany.javamed_new.services.auth.AuthService;
import com.mycompany.javamed_new.services.sessioncontext.FeaturesProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        // 📦 Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javamed_new/Primary.fxml"));
        Parent root = loader.load();

        // 📌 Inyección manual de dependencias
        DbManager dbManager = new DbManager("jdbc:mysql://localhost:3306/javamed", "root", "");
        DataFetcher dataFetcher = new DataFetcher(dbManager);
        FeaturesProvider.setDataFetcher(dataFetcher); // 🎯 ¡Esta es la clave!

        AuthService authService = new AuthService(dataFetcher);
        DbUpdater dbUpdater = new DbUpdater(dbManager);

        // 👉 Inyectar en el controlador principal
        PrimaryController controller = loader.getController();
        controller.setAuthService(authService);
        controller.setDbUpdater(dbUpdater);

        System.out.println("✅ Controlador inicializado con dependencias.");

        // 🎨 Cargar escena y estilos
        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        // 🪟 Mostrar la ventana
        stage.setScene(scene);
        stage.setTitle("JAVAMED");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        URL location = App.class.getResource("/com/mycompany/javamed_new/" + fxml + ".fxml");

        if (location == null) {
            throw new IOException("❌ Error: No se encontró la vista " + fxml + ".fxml");
        }

        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        ErrorsHandler.setGlobalHandler();

        try {
            launch();
        } catch (Exception e) {
            ErrorsHandler.handle(e);
        }
    }
}
