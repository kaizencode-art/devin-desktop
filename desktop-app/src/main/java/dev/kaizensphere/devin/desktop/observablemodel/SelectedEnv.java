package dev.kaizensphere.devin.desktop.observablemodel;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.store.EnvStoreListener;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;

import java.util.List;

public class SelectedEnv implements EnvStoreListener {


    private static final SelectedEnv instance = new SelectedEnv();

    private static final ReadOnlyObjectWrapper<EnvModel> selectedEnv = new ReadOnlyObjectWrapper<>();

    private static final ReadOnlyIntegerWrapper selectedEnvIndex = new ReadOnlyIntegerWrapper(-1);

    private SelectedEnv() {
        AppContext.envStore.registerListener(this);
    }

    public static SelectedEnv getInstance() {
        return instance;
    }

    public static ReadOnlyProperty<EnvModel> selectedEnvProperty() {
        return selectedEnv.getReadOnlyProperty();
    }

    public void setSelectedEnv(EnvModel selectedEnv) {
        SelectedEnv.selectedEnv.set(selectedEnv);
    }

    public void setSelectedEnv(String title, String description, EnvModel.Status status) {
        EnvModel selectedEnvModel = new EnvModel(
                title,
                description,
                status
        );
        SelectedEnv.selectedEnv.set(selectedEnvModel);
    }

    public void setSelectedEnvIndex(int index) {
        selectedEnvIndex.set(index);
        List<EnvModel> envs = AppContext.envStore.getEnvs();
        EnvModel envModel = (index >= 0 && index < envs.size()) ? envs.get(index) : null;
        setSelectedEnv(envModel);
    }

    public EnvModel getReadOnlySelectedEnv() {
        return SelectedEnv.selectedEnv.get();
    }

    @Override
    public void onEnvironmentChanged() {
        int idx = selectedEnvIndex.get();
        List<EnvModel> envs = AppContext.envStore.getEnvs();
        int size = envs.size();

        int newIdx;
        if(size == 0) newIdx = -1;
        else if (idx < 0) newIdx = 0;
        else if (idx >= size) newIdx = size - 1;
        else newIdx = idx;

        EnvModel envModel = (idx >= 0 && idx < envs.size()) ? envs.get(idx) : null;

        Runnable apply = () ->  {
            selectedEnvIndex.set(newIdx);
            selectedEnv.set(envModel);
        };
        if(Platform.isFxApplicationThread()) {
            apply.run();
        } else {
            Platform.runLater(apply);
        }
    }
}
