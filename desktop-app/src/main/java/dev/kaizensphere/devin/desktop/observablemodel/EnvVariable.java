package dev.kaizensphere.devin.desktop.observablemodel;

import dev.kaizensphere.devin.domain.model.EnvVariableModel;
import javafx.beans.property.SimpleObjectProperty;

public class EnvVariable {

    private final SimpleObjectProperty<EnvVariableModel> guiEnvVariableModel = new SimpleObjectProperty<>();

    public SimpleObjectProperty<EnvVariableModel> guiEnvVariableModelProperty() {
        return guiEnvVariableModel;
    }

    public EnvVariableModel getGuiEnvVariableModel() {
        return guiEnvVariableModel.get();
    }

    public void setGuiEnvVariableModel(EnvVariableModel envVariableModel) {
        this.guiEnvVariableModel.set(envVariableModel);
    }

}
