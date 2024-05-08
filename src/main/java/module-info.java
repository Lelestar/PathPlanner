module com.ap4b.pathplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.xml;

    opens com.ap4b.pathplanner to javafx.fxml;
    exports com.ap4b.pathplanner;
    exports com.ap4b.pathplanner.controller;
    opens com.ap4b.pathplanner.controller to javafx.fxml;
}