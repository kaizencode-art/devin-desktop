package dev.kaizensphere.devin.desktop.gui.models;


import dev.kaizensphere.devin.models.EnvModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GuiEnvModel {

    public StringProperty title;
    public StringProperty description;
    public ObjectProperty<EnvModel.Status> status;

    public GuiEnvModel(EnvModel envModel) {
        this.title = new SimpleStringProperty(envModel.title());
        this.description = new SimpleStringProperty(envModel.description());
        this.status = new SimpleObjectProperty<>(envModel.status());
    }

    public GuiEnvModel(String title, String description, EnvModel.Status status) {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleObjectProperty<>(status);
    }

    public GuiEnvModel() {}
}