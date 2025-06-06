module com.mycompany.javamed_new {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql; // Si usas conexión a BD

    opens com.mycompany.javamed_new to javafx.fxml;
    exports com.mycompany.javamed_new;
    requires jbcrypt;
    


}
    
