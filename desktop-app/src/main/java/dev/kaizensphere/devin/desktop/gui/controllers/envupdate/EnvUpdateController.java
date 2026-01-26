package dev.kaizensphere.devin.desktop.gui.controllers.envupdate;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.domain.model.EnvModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class EnvUpdateController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button updateButton;

    private UpdateType updateType;
    private EnvModel existingEnv;

    @FXML
    public void initialize() {
        cancelButton.setOnAction(e -> closeWindow());
        updateButton.setOnAction(e -> handleUpdate());
    }

    public void setUpdateType(UpdateType updateType, EnvModel env) {
        this.updateType = updateType;
        this.existingEnv = env;
        
        if (updateType == UpdateType.MODIFY_ENV && env != null) {
            titleField.setText(env.title());
            descriptionArea.setText(env.description());
            updateButton.setText("Update");
        } else {
            titleField.setText("");
            descriptionArea.setText("");
            updateButton.setText("Create");
        }
    }

    private void handleUpdate() {
        String title = titleField.getText();
        String description = descriptionArea.getText();

        if (updateType == UpdateType.CREATE_ENV) {
            EnvModel newEnv = new EnvModel(title, description, EnvModel.Status.VALID, new ArrayList<>());
            AppContext.envStore.addEnv(newEnv);
        } else if (existingEnv != null) {
            AppContext.environmentService.updateEnvironment(existingEnv.id(), title, description);
        }
        
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public static void showModal(UpdateType updateType, EnvModel env) {
        try {
            FXMLLoader loader = new FXMLLoader(EnvUpdateController.class.getResource("env-update.fxml"));
            Scene scene = new Scene(loader.load());
            
            EnvUpdateController controller = loader.getController();
            controller.setUpdateType(updateType, env);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(updateType == UpdateType.CREATE_ENV ? "Create Environment" : "Update Environment");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
