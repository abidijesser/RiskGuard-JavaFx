module com.example.pidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires java.sql;

    // External libraries
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires itextpdf;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires twilio;
    requires com.almasb.fxgl.all;

    // Export and open packages to javafx
    exports com.riskguard.crudjava to javafx.fxml;
    opens com.riskguard.crudjava to javafx.fxml, javafx.base;

    exports com.example.pidev.test;
    opens com.example.pidev.test to javafx.fxml, javafx.base;

    exports controllers;
    opens controllers to javafx.fxml, javafx.base;

    opens entites to javafx.base;

    exports com.assurance.assurancess;
    opens com.assurance.assurancess to javafx.fxml;

    exports com.assurance.assurancess.controller;
    opens com.assurance.assurancess.controller to javafx.fxml;
}
