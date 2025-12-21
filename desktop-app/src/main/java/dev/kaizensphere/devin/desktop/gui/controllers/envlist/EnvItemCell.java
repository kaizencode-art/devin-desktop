package dev.kaizensphere.devin.desktop.gui.controllers.envlist;

import dev.kaizensphere.devin.model.EnvModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.Parent;

import java.io.IOException;

public class EnvItemCell extends ListCell<EnvModel> {

    private Parent cellRoot;
    private EnvItemCellController controller;

    @Override
    protected void updateItem(EnvModel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            // If cell is empty, remove graphic
            setGraphic(null);
        } else {
            // Lazy-load the FXML once, then reuse it
            if (cellRoot == null) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/dev/kaizensphere/devin/desktop/gui/controllers/envlist/env-item-view.fxml")
                );
                try {
                    cellRoot = loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Make sure our cell displays the loaded content
            setGraphic(cellRoot);
            // Pass the data to the controller
            controller.bindData(item);
        }
    }
}
