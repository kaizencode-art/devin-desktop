package dev.kaizensphere.devin.desktop.gui.controllers.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ViewMenu extends Menu {

    MenuItem refreshMenu;
    Menu filterMenu;
    MenuItem fullScreenMenu;

    public ViewMenu() {
        super("View");
        refreshMenu = initializeRefreshMenu();
        filterMenu = initializeFilterMenu();
        fullScreenMenu = initializeFullScreenMenu();
        getItems().addAll(
                refreshMenu,
                filterMenu,
                fullScreenMenu
        );
    }

    public MenuItem initializeRefreshMenu() {
        MenuItem menu = new MenuItem("Refresh");
        menu.setOnAction(e -> System.out.println("Refresh"));
        return menu;
    }

    public Menu initializeFilterMenu() {
        Menu menu = new Menu("Filter");

        MenuItem showOnlyModified = new MenuItem("Show Only Modified");
        showOnlyModified.setOnAction(e -> System.out.println("Show Only Modified"));

        MenuItem showDulplicated = new MenuItem("Show Duplicated");
        showDulplicated.setOnAction(e -> System.out.println("Show Duplicated"));

        menu.getItems().addAll(
                showOnlyModified,
                showDulplicated
        );

        return menu;
    }

    private MenuItem initializeFullScreenMenu() {
        MenuItem menu = new MenuItem("Full Screen");
        menu.setOnAction(e -> System.out.println("Full Screen"));
        return menu;
    }
}
