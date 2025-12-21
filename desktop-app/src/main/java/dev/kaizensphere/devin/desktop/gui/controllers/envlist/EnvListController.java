package dev.kaizensphere.devin.desktop.gui.controllers.envlist;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.model.EnvModel;
import dev.kaizensphere.devin.desktop.observablemodel.SelectedEnv;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class EnvListController {

    @FXML
    private ListView<EnvModel> environmentListView;

    @FXML
    private void initialize() {
        environmentListView.setItems(AppContext.envStoreFx.getEnvsView());

        environmentListView.setCellFactory(listView -> new EnvItemCell());

        environmentListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx != null) {
                AppContext.selectedEnvFx.setSelectedEnvIndex(newIdx.intValue());
            }
        });
        if(!environmentListView.getItems().isEmpty()) {
            environmentListView.getSelectionModel().select(0);
            SelectedEnv.getInstance().setSelectedEnvIndex(0);
        } else {
            SelectedEnv.getInstance().setSelectedEnvIndex(-1);
        }
    }
}
