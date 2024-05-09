module com.example.pidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    exports com.riskguard.crudjava to javafx.fxml;
    opens com.riskguard.crudjava to javafx.fxml, javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires itextpdf;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires twilio;
    requires layout;
    requires kernel;
    requires io;
    requires jbcrypt;

    opens com.example.pidev to javafx.fxml, javafx.base;
    exports com.example.pidev.test;
    opens com.example.pidev.test to javafx.fxml, javafx.base;
    exports controllers;
    opens controllers to javafx.fxml, javafx.base;
    opens entites to javafx.base;

    // Open the models package to javafx.base to allow property access
    opens models to javafx.base;
}
