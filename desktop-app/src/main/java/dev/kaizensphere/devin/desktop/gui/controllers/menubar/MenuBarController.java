package dev.kaizensphere.devin.desktop.gui.controllers.menubar;

import dev.kaizensphere.devin.desktop.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class MenuBarController {

    @FXML
    MenuBar menuBar;

    FileMenu fileMenu;
    ViewMenu viewMenu;
    EditMenu editMenu;

    @FXML
    public void initialize() {
        fileMenu = new FileMenu(AppContext.environmentService);
        editMenu = new EditMenu(AppContext.environmentService);
        viewMenu = new ViewMenu();
        menuBar.getMenus().addAll(
                fileMenu,
                viewMenu,
                editMenu
        );
    }


}
