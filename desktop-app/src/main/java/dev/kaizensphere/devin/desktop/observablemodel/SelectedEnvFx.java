package dev.kaizensphere.devin.desktop.observablemodel;

import dev.kaizensphere.devin.models.EnvModel;
import dev.kaizensphere.devin.store.EnvStore;
import dev.kaizensphere.devin.store.EnvStoreListener;
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
        var envs = store.getEnvs();
        if(!envs.isEmpty()) selectedEnv.set(envs.get(0));
    }

    private int clampIndex(int index, int size) {
        if (size == 0) return -1;
        if (index < 0) return 0;
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
    public void onEnvironmentChanged() {
        List<EnvModel> envs = store.getEnvs();
        int idx = selectedEnvIndex().get();
        int size = envs.size();
        int newIdx = clampIndex(idx, size);
        EnvModel model = (newIdx >= 0) ? envs.get(newIdx) : null;
        applySelection(newIdx, model);
    }
}
