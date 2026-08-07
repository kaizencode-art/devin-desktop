package dev.kaizensphere.devin.desktop.observablemodel;

import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.store.EnvStore;
import dev.kaizensphere.devin.domain.store.EnvStoreListener;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;

import java.util.List;

public class SelectedEnvFx implements EnvStoreListener {

    private final EnvStore store;
    private static  SelectedEnvFx INSTANCE ;

    private final ReadOnlyIntegerWrapper selectedEnvIndex = new ReadOnlyIntegerWrapper(-1);
    private final ReadOnlyObjectWrapper<EnvModel> selectedEnv = new ReadOnlyObjectWrapper<>();

    public static SelectedEnvFx init(EnvStore store) {
        INSTANCE = new SelectedEnvFx(store);
        return INSTANCE;
    }

    private SelectedEnvFx(EnvStore store) {
        this.store = store;
        store.registerListener(this);
    }

    private int clampIndex(int index, int size) {
        if (size == 0 || index < 0) return -1;
        return Math.min(index, size - 1);
    }

    private void applySelection(int idx, EnvModel model) {
        selectedEnvIndex.set(idx);
        selectedEnv.set(model);
    }


    public static SelectedEnvFx getInstance() {
        return INSTANCE;
    }

    public ReadOnlyIntegerProperty selectedEnvIndex() {
        return selectedEnvIndex.getReadOnlyProperty();
    }

    public int getSelectedIndex() {
        return selectedEnvIndex.get();
    }

    public ReadOnlyProperty<EnvModel> selectedEnvProperty() {
        return selectedEnv.getReadOnlyProperty();
    }

    public EnvModel getSelectedEnv() {
        return selectedEnv.get();
    }

    public void setSelectedEnvIndex(int index) {
        List<EnvModel> envs = store.getEnvs();
        int size = envs.size();
        int newIdx = clampIndex(index, size);

        if (newIdx == selectedEnvIndex.get()) {
            return;
        }

        EnvModel envModel = (newIdx >= 0) ? envs.get(newIdx) : null;
        applySelection(newIdx, envModel);
    }

    public void selectByModel(EnvModel envModel) {
        int idx = store.getEnvs().indexOf(envModel);
        setSelectedEnvIndex(idx);
    }

    public ReadOnlyIntegerWrapper selectedEnvIndexProperty() {
        return selectedEnvIndex;
    }

    @Override
    public void onEnvironmentChanged(Change change) {
        Platform.runLater(() -> {
            List<EnvModel> envs = store.getEnvs();
            int idx = selectedEnvIndex.get();
            int size = envs.size();
            int newIdx = clampIndex(idx, size);
            EnvModel model = (newIdx >= 0) ? envs.get(newIdx) : null;
            applySelection(newIdx, model);
        });
    }
}
