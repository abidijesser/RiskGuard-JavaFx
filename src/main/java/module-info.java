module com.assurance.assurancess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.assurance.assurancess to javafx.fxml;
    exports com.assurance.assurancess;

    opens com.assurance.assurancess.controller to javafx.fxml;
    exports com.assurance.assurancess.controller;
}
