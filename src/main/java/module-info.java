module io.github.kaizensphere.devin.devindesktop {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires atlantafx.base;
    requires java.logging;
    requires wellbehavedfx;

    opens io.github.kaizensphere.devin.devindesktop.gui.controllers to javafx.fxml;


    opens io.github.kaizensphere.devin.devindesktop to javafx.fxml;
    exports io.github.kaizensphere.devin.devindesktop;
    exports io.github.kaizensphere.devin.devindesktop.models;
    exports io.github.kaizensphere.devin.devindesktop.store;
    exports io.github.kaizensphere.devin.devindesktop.gui.models;
    exports io.github.kaizensphere.devin.devindesktop.gui.controllers.envdetail to javafx.fxml;
    opens io.github.kaizensphere.devin.devindesktop.gui.controllers.envdetail to javafx.fxml;
    exports io.github.kaizensphere.devin.devindesktop.gui.controllers.envdetail.section to javafx.fxml;
    opens io.github.kaizensphere.devin.devindesktop.gui.controllers.envdetail.section to javafx.fxml;
    exports io.github.kaizensphere.devin.devindesktop.gui.controllers.envlist to javafx.fxml;
    opens io.github.kaizensphere.devin.devindesktop.gui.controllers.envlist to javafx.fxml;
    exports io.github.kaizensphere.devin.devindesktop.gui.controllers;
    exports io.github.kaizensphere.devin.devindesktop.gui.components.env to javafx.fxml;
    opens io.github.kaizensphere.devin.devindesktop.gui.components.env to javafx.fxml;
}