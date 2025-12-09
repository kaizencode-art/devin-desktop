package io.github.kaizensphere.devin.devindesktop.observablemodel;

import javafx.beans.property.SimpleObjectProperty;

public class EnvVariable {

    private final SimpleObjectProperty<io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel> guiEnvVariableModel = new SimpleObjectProperty<>();

    public SimpleObjectProperty<io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel> guiEnvVariableModelProperty() {
        return guiEnvVariableModel;
    }

    public io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel getGuiEnvVariableModel() {
        return guiEnvVariableModel.get();
    }

    public void setGuiEnvVariableModel(io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel envVariableModel) {
        this.guiEnvVariableModel.set(envVariableModel);
    }

}
