module com.example.notewizard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.logging;
    requires TarsosDSP;
    requires com.google.common;

    exports com.example.notewizard.controllers;
    exports com.example.notewizard;
    exports com.example.notewizard.detection.algorithms;
    exports com.example.notewizard.detection.audioprocessing;
    opens com.example.notewizard to javafx.fxml;
    opens com.example.notewizard.controllers to javafx.fxml;
    opens com.example.notewizard.detection.algorithms to javafx.fxml;
    opens com.example.notewizard.detection.audioprocessing to javafx.fxml;
    exports com.example.notewizard.model.dto;
    exports com.example.notewizard.model.guitarmodel;
    exports com.example.notewizard.updaters;
    opens com.example.notewizard.updaters to javafx.fxml;
}