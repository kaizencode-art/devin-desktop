package io.github.kaizensphere.devin.devindesktop.gui.controllers.envlist;

import io.github.kaizensphere.devin.devindesktop.AppContext;
import io.github.kaizensphere.devin.devindesktop.gui.models.GuiEnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.observablemodel.SelectedEnvModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvItemCellController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private HBox container;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private FontIcon statusIcon;

    private EnvModel boundModel;

    private ChangeListener<EnvModel.Status> statusListener;

    public ObjectProperty<EnvModel.Status> status;


    public void bindData(EnvModel model) {

        this.boundModel = model;

        titleLabel.setText(boundModel.title());
        descriptionLabel.setText(boundModel.description());
        updateStatusIcon(boundModel.status());
    }

    private void updateStatusIcon(EnvModel.Status status) {
        statusIcon.getStyleClass().removeAll("status-valid", "status-warning", "status-invalid");
        switch (status) {
            case VALID -> statusIcon.getStyleClass().add("status-valid");
            case WARNING -> statusIcon.getStyleClass().add("status-warning");
            case INVALID -> statusIcon.getStyleClass().add("status-invalid");
        }
    }

    @FXML
    protected void onContainerClick() {
        SelectedEnvModel.getInstance().setSelectedEnv(boundModel);
        logger.info(SelectedEnvModel.getInstance().getReadOnlySelectedEnv().toString());
    }

}
