package dev.kaizensphere.devin.desktop.gui.controllers.envlist;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.domain.model.EnvModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class EnvListController {

    @FXML
    private ListView<EnvModel> environmentListView;

    private boolean isUpdating = false;

    @FXML
    private void initialize() {
        environmentListView.setItems(AppContext.envStoreFx.getEnvsView());

        environmentListView.setCellFactory(listView -> new EnvItemCell());

        // Listener: Vue -> Modèle
        environmentListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (isUpdating) return;
            
            // On ignore le -1 provenant de la vue (souvent une phase de transition lors d'un rafraîchissement)
            if (newIdx != null && newIdx.intValue() != -1) {
                isUpdating = true;
                try {
                    AppContext.selectedEnvFx.setSelectedEnvIndex(newIdx.intValue());
                } finally {
                    isUpdating = false;
                }
            }
        });

        // Listener: Modèle -> Vue
        AppContext.selectedEnvFx.selectedEnvIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (isUpdating) return;

            if (newIdx != null && newIdx.intValue() != environmentListView.getSelectionModel().getSelectedIndex()) {
                isUpdating = true;
                try {
                    environmentListView.getSelectionModel().select(newIdx.intValue());
                } finally {
                    isUpdating = false;
                }
            }
        });

        if(!environmentListView.getItems().isEmpty()) {
            environmentListView.getSelectionModel().select(0);
            AppContext.selectedEnvFx.setSelectedEnvIndex(0);
        } else {
            AppContext.selectedEnvFx.setSelectedEnvIndex(-1);
        }
    }
}
