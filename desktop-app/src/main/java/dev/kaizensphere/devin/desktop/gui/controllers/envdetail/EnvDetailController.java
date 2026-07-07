package dev.kaizensphere.devin.desktop.gui.controllers.envdetail;

import dev.kaizensphere.devin.desktop.AppContext;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EnvDetailController {

    @FXML private VBox mainContent;
    @FXML private VBox placeholderPane;


    @FXML
    public void initialize() {
        AppContext.selectedEnvFx.selectedEnvProperty().addListener((obs, oldEnv, newEnv) -> {
            updateView(newEnv != null);
        });

        updateView(AppContext.selectedEnvFx.getSelectedEnv() != null);

    }

    private void updateView(boolean hasSelection) {
        mainContent.setVisible(hasSelection);
        mainContent.setManaged(hasSelection);
        placeholderPane.setVisible(!hasSelection);
        placeholderPane.setManaged(!hasSelection);
    }

}
