module com.mycompany.javamed_new {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires spring.security.crypto;

    exports com.mycompany.javamed_new;
   

    opens com.mycompany.javamed_new to javafx.fxml;
}
