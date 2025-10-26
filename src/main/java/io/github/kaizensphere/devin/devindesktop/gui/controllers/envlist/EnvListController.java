package io.github.kaizensphere.devin.devindesktop.gui.controllers.envlist;

import io.github.kaizensphere.devin.devindesktop.AppContext;
import io.github.kaizensphere.devin.devindesktop.gui.models.GuiEnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.store.EnvStoreListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class EnvListController implements EnvStoreListener {

    @FXML
    private ListView<EnvModel> environmentListView;

    @FXML
    private void initialize() {
        AppContext.envStore.registerListener(this);

        environmentListView.getItems().addAll(AppContext.envStore.getEnvs());

        environmentListView.setCellFactory(listView -> new EnvItemCell());
    }

    @Override
    public void onEnvironmentChanged() {
        environmentListView.getItems().clear();
        environmentListView.getItems().addAll(AppContext.envStore.getEnvs());
    }
}
