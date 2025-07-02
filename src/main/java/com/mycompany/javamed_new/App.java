package com.mycompany.javamed_new;

import com.mycompany.javamed_new.errors.ErrorsHandler;
import com.mycompany.javamed_new.persistence.DataFetcher;
import com.mycompany.javamed_new.persistence.config.DbManager;
import com.mycompany.javamed_new.persistence.config.EnvLoader;
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
import java.util.Properties;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader loader = loadFXML("Primary");
        Parent root = loader.getRoot();
        injectDependencies(loader);

        ErrorsHandler.log("app.dependencies.injected");
        ErrorsHandler.log("view.primary.loaded");

        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("JAVAMED");
        stage.show();
    }

    private static void injectDependencies(FXMLLoader loader) {
        try {
            Properties env = EnvLoader.load("db.env");

            String dbUrl = env.getProperty("DB_URL");
            String dbUser = env.getProperty("DB_USER");
            String dbPassword = env.getProperty("DB_PASSWORD");

            DbManager dbManager = new DbManager(dbUrl, dbUser, dbPassword);
            DataFetcher dataFetcher = new DataFetcher(dbManager);
            AuthService authService = new AuthService(dataFetcher);
            FeaturesProvider.setDataFetcher(dataFetcher);

            PrimaryController controller = loader.getController();
            controller.setAuthService(authService);

        } catch (IOException e) {
            ErrorsHandler.handle(new RuntimeException("❌ No se pudo cargar el archivo db.env", e));
        }
    }

    public static FXMLLoader loadFXML(String nombreVista) throws IOException {
        URL location = App.class.getResource("/com/mycompany/javamed_new/" + nombreVista + ".fxml");
        if (location == null) {
            throw new IOException("❌ Vista no encontrada: " + nombreVista);
        }
        FXMLLoader loader = new FXMLLoader(location);
        loader.load();
        return loader;
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = loadFXML(fxml);
        scene.setRoot(loader.getRoot());

        if ("primary".equalsIgnoreCase(fxml)) {
            injectDependencies(loader);
            ErrorsHandler.log("app.dependencies.injected");
            ErrorsHandler.log("view.primary.loaded");
        }
    }

    public static Scene getScene() {
        return scene;
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
