package dev.kaizensphere.devin.desktop.gui.controllers.envlist;

import dev.kaizensphere.devin.domain.model.EnvModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.logging.Logger;

public class EnvItemCellController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private HBox container;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private FontIcon statusIcon;

    private ChangeListener<EnvModel.Status> statusListener;

    public ObjectProperty<EnvModel.Status> status;


    public void bindData(EnvModel env) {

        if(env == null) {
            titleLabel.setText("");
            descriptionLabel.setText("");
            statusIcon.getStyleClass().removeAll("status-valid", "status-warning", "status-invalid");
            return;
        }
        titleLabel.setText(env.title());
        descriptionLabel.setText(env.description());
        updateStatusIcon(env.status());
    }

    private void updateStatusIcon(EnvModel.Status status) {
        statusIcon.getStyleClass().removeAll("status-valid", "status-warning", "status-invalid");
        switch (status) {
            case VALID -> statusIcon.getStyleClass().add("status-valid");
            case WARNING -> statusIcon.getStyleClass().add("status-warning");
            case INVALID -> statusIcon.getStyleClass().add("status-invalid");
        }
    }


}
