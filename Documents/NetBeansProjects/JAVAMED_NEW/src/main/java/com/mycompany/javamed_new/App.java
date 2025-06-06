package com.mycompany.javamed_new;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mycompany.javamed_new.errors.ErrorsHandler;

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

        comprobarConexionDB(); // Verificar conexión a la base de datos

        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Mi Aplicación");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com.mycompany.javamed_new/" + fxml + ".fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        // ✅ Activamos el manejador global de errores
        ErrorsHandler.setGlobalHandler();

        try {
            launch(); // 🚀 Toda la app dentro del try-catch global
        } catch (Exception e) {
            ErrorsHandler.handle(e); // 🔥 Cualquier fallo inesperado se maneja en un solo lugar
        }
    }

    private static void comprobarConexionDB() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/javamed"; 
        String usuario = "root"; 
        String contraseña = ""; 

        Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
        System.out.println("✅ Conexión exitosa a la base de datos JAVAMED!");
        conexion.close();
    }
}
