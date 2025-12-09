package io.github.kaizensphere.devin.devindesktop.gui.controllers.envlist;

import io.github.kaizensphere.devin.devindesktop.AppContext;
import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.observablemodel.SelectedEnv;
import io.github.kaizensphere.devin.devindesktop.store.EnvStoreListener;
import javafx.application.Platform;
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
