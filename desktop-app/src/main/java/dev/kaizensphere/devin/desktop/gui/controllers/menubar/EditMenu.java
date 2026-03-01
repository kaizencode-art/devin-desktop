package dev.kaizensphere.devin.desktop.gui.controllers.menubar;

import dev.kaizensphere.devin.application.ports.in.EnvironmentUseCases;
import dev.kaizensphere.devin.desktop.observablemodel.SelectedEnvFx;
import dev.kaizensphere.devin.domain.model.EnvModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class EditMenu extends Menu {

    private final EnvironmentUseCases environmentUseCases;
    MenuItem deleteMenu;

    public EditMenu(EnvironmentUseCases environmentUseCases) {
        super("Edit");
        this.environmentUseCases = environmentUseCases;
        MenuItem deleteMenu = initializeDeleteMenu();
        getItems().addAll(
                deleteMenu
        );
    }

    public MenuItem initializeDeleteMenu() {
        MenuItem menu = new MenuItem("Delete Current Environment");
        menu.setOnAction(e -> {
            EnvModel selected = SelectedEnvFx.getInstance().getSelectedEnv();
            if(selected != null) {
                environmentUseCases.deleteEnvironment(selected.id());
            }
        });

        return menu;
    }

}
