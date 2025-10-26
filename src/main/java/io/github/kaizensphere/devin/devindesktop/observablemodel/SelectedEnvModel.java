package io.github.kaizensphere.devin.devindesktop.observablemodel;

import io.github.kaizensphere.devin.devindesktop.gui.models.GuiEnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;

public class SelectedEnvModel {

    private static final SelectedEnvModel instance = new SelectedEnvModel();

    private static final ReadOnlyObjectWrapper<EnvModel> selectedEnv = new ReadOnlyObjectWrapper<>();

    private SelectedEnvModel() {}

    public static SelectedEnvModel getInstance() {
        return instance;
    }

    public static ReadOnlyProperty<EnvModel> selectedEnvProperty() {
        return selectedEnv.getReadOnlyProperty();
    }

    public void setSelectedEnv(EnvModel selectedEnv) {
        SelectedEnvModel.selectedEnv.set(selectedEnv);
    }

    public void setSelectedEnv(String title, String description, EnvModel.Status status) {
        EnvModel selectedEnvModel = new EnvModel(
                title,
                description,
                status
        );
        SelectedEnvModel.selectedEnv.set(selectedEnvModel);
    }

    public EnvModel getReadOnlySelectedEnv() {
        return SelectedEnvModel.selectedEnv.get();
    }

}
