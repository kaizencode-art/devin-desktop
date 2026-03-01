package dev.kaizensphere.devin.desktop.gui.controllers.envdetail.section;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.domain.model.EnvVariableModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class EnvHeaderSectionController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private FontIcon statusIcon;

    @FXML
    private Label lastUpdateLabel;


    @FXML
    private void initialize() {
        AppContext.selectedEnvFx.selectedEnvProperty().addListener((obs, oldEnv, newEnv) -> {
            if(newEnv != null) {
                this.titleLabel.setText(newEnv.title());
                statusIcon.getStyleClass().removeAll("status-valid", "status-warning", "status-invalid");
                Instant lastupdate = newEnv.variables().stream()
                        .map(EnvVariableModel::updatedDate)
                        .filter(date -> date != null)
                        .max(Comparator.naturalOrder())
                        .orElse(null);
                this.lastUpdateLabel.setText(lastupdate != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd").format(lastupdate.atZone(ZoneId.systemDefault())) : "N/C");
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
