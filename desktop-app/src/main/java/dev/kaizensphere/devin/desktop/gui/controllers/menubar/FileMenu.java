package dev.kaizensphere.devin.desktop.gui.controllers.menubar;

import dev.kaizensphere.devin.application.ports.in.EnvironmentUseCases;
import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.desktop.observablemodel.SelectedEnvFx;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {

    private final EnvironmentUseCases environmentUseCases;
    Menu newMenu;
    Menu openMenu;
    Menu importExportMenu;
    MenuItem exitMenu;

    public FileMenu(EnvironmentUseCases environmentUseCases) {
        super("File");
        this.environmentUseCases = environmentUseCases;
        newMenu = initializeNewMenu();
        openMenu = initializeOpenMenu();
        importExportMenu = initializeImportExportMenu();
        exitMenu = initializeExitMenu();

        getItems().addAll(
                newMenu,
                openMenu,
                importExportMenu,
                exitMenu
        );
    }

    private Menu initializeNewMenu() {
        Menu menu = new Menu("New");
        MenuItem newEnv = new MenuItem("New Environment");
        newEnv.setOnAction(e -> environmentUseCases.createNewEnvironment());

        MenuItem newEnvVar = new MenuItem("New Environment Variable");
        newEnvVar.setOnAction(e -> {
            EnvModel selected = SelectedEnvFx.getInstance().getSelectedEnv();
            if (selected != null) {
                environmentUseCases.createNewEnvironmentVariable(selected.id());
            }
        });

        menu.getItems().addAll(
                newEnv,
                newEnvVar
        );
        return menu;
    }

    private Menu initializeImportExportMenu() {
        Menu menu = new Menu("Import/Export");

        MenuItem importEnv = new MenuItem("Import Environment");
        importEnv.setOnAction(e -> environmentUseCases.importEnvironment());

        MenuItem exportEnv = new MenuItem("Export Environment");
        exportEnv.setOnAction(e -> {
            EnvModel selected = SelectedEnvFx.getInstance().getSelectedEnv();
            if (selected != null) {
                environmentUseCases.exportEnvironment(selected.id());
            }
        });

        menu.getItems().addAll(
                importEnv,
                exportEnv
        );
        return menu;
    }

    private Menu initializeOpenMenu() {
        Menu menu = new Menu("Open");
        MenuItem openEnv = new MenuItem("Recent Environment");
        openEnv.setOnAction(e -> environmentUseCases.openRecentEnvironment());

        menu.getItems().addAll(
                openEnv
        );
        return menu;
    }

    private MenuItem initializeExitMenu() {
        MenuItem menu = new MenuItem("Exit");
        menu.setOnAction(e -> Platform.exit());

        return menu;
    }
}
