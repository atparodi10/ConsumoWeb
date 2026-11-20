module org.example.proyectoweb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens ni.edu.uam.proyectoweb.controller to javafx.fxml;
    opens ni.edu.uam.proyectoweb.model
            to javafx.base, com.fasterxml.jackson.databind;

    exports ni.edu.uam.proyectoweb;
}