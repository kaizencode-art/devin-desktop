package dev.kaizensphere.devin.desktop.gui.controllers.envdetail.section;

import dev.kaizensphere.devin.desktop.observablemodel.SelectedEnv;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

public class EnvHeaderSectionController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private FontIcon statusIcon;

    @FXML
    private void initialize() {
        SelectedEnv.selectedEnvProperty().addListener((obs, oldEnv, newEnv) -> {
            if(newEnv != null) {
                this.titleLabel.setText(newEnv.title());
                statusIcon.getStyleClass().removeAll("status-valid", "status-warning", "status-invalid");
                String statusStr = newEnv.status().getStatus();
                if(statusStr != null) {
                    this.statusLabel.setText(statusStr.substring(0, 1).toUpperCase() + statusStr.substring(1));
                }
                switch (newEnv.status()) {
                    case VALID -> statusIcon.getStyleClass().add("status-valid");
                    case WARNING -> statusIcon.getStyleClass().add("status-warning");
                    case INVALID -> statusIcon.getStyleClass().add("status-invalid");
                }
            }
        });
    }

}
