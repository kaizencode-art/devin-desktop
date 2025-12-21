package dev.kaizensphere.devin.desktop.store;

import dev.kaizensphere.devin.model.EnvModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnvStoreFx  implements dev.kaizensphere.devin.store.EnvStoreListener {

    private final dev.kaizensphere.devin.store.EnvStore store;
    private final ObservableList<EnvModel> envsFx = FXCollections.observableArrayList();
    private final ObservableList<EnvModel> envsViewFx = FXCollections.unmodifiableObservableList(envsFx);

    public EnvStoreFx(dev.kaizensphere.devin.store.EnvStore store) {
        this.store = store;
        store.registerListener(this);
        envsFx.setAll(store.getEnvs());
    }

    public ObservableList<EnvModel> getEnvsView() {
        return envsViewFx;
    }

    @Override
    public void onEnvironmentChanged() {
        Runnable apply = () -> envsFx.setAll(store.getEnvs());
        if(Platform.isFxApplicationThread()) apply.run();
        else Platform.runLater(apply);
    }
}
