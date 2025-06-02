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

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javamed_new/Primary.fxml"));
        Parent root = loader.load();

        // Obtener el controlador después de cargar el FXML
        PrimaryController controller = loader.getController();
        System.out.println("Controlador inicializado: " + controller);

        // **Verificar conexión a la base de datos**
        comprobarConexionDB();

        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Mi Aplicación");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mycompany/javamed_new/" + fxml + ".fxml"));
        Parent root = loader.load();
        
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }

    // **✅ Método agregado para comprobar la conexión a la base de datos**
    private static void comprobarConexionDB() {
        String url = "jdbc:mysql://localhost:3306/javamed"; // Cambia "javamed" por el nombre de tu base de datos
        String usuario = "root"; // Ajusta según tu configuración
        String contraseña = ""; // Agrega la contraseña si la tienes

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            if (conexion != null) {
                System.out.println("✅ Conexión exitosa a la base de datos JAVAMED!");
                conexion.close();
            } else {
                System.out.println("❌ Error: No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
