module com.riskguard.crudjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;


    opens com.riskguard.crudjava to javafx.fxml;
    exports com.riskguard.crudjava;
}