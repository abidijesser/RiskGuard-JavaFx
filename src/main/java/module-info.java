module com.riskguard.crudjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires org.controlsfx.controls;
    requires java.mail;
    requires itextpdf;
    requires java.desktop;


    opens com.riskguard.crudjava to javafx.fxml;
    exports com.riskguard.crudjava;
}