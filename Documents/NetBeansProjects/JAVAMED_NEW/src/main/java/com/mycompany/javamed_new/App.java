package com.mycompany.javamed_new;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import com.mycompany.javamed_new.errors.ErrorsHandler;
import java.net.URL;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javamed_new/Primary.fxml"));
        Parent root = loader.load();

        PrimaryController controller = loader.getController();
        System.out.println("Controlador inicializado: " + controller);

         

        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

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
