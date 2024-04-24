module com.example.pidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.pidev to javafx.fxml;
    exports com.example.pidev.test;
    opens com.example.pidev.test to javafx.fxml;
    exports controllers;
    opens controllers;
    opens entites;
}