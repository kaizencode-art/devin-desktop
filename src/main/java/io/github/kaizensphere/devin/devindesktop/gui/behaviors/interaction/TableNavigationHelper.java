package io.github.kaizensphere.devin.devindesktop.gui.behaviors.interaction;

import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

import java.util.logging.Logger;


public final class TableNavigationHelper {

    private TableNavigationHelper() {
    }

    public static <T> void navigateToNextCell(TableView<T> tableView, TablePosition<?, ?> cellPosition) {
        if (cellPosition == null || tableView == null) return;

        int nextColumnIndex = cellPosition.getColumn() + 1;
        int nextRow = cellPosition.getRow();

        while(nextRow < tableView.getItems().size()) {
            while (nextColumnIndex < tableView.getColumns().size()) {
                TableColumn<T, ?> nextColumn = tableView.getVisibleLeafColumn(nextColumnIndex);
                if (isEditableColumn(tableView, nextColumn)) {
                    focusAndEditCell(tableView, nextRow, nextColumn);
                    return;
                }
                nextColumnIndex++;
            }
            nextRow++;
            nextColumnIndex = 0;
        }
    }

    public static <T> void navigateToPreviousCell(TableView<T> tableView, TablePosition<?, ?> cellPosition) {
        if (cellPosition == null || tableView == null) return;

        int previousColumnIndex = cellPosition.getColumn() - 1;
        int previousRow = cellPosition.getRow();

        while (previousRow >= 0) {
            while (previousColumnIndex >= 0) {
                TableColumn<T, ?> previousColumn = tableView.getVisibleLeafColumn(previousColumnIndex);
                if (isEditableColumn(tableView, previousColumn)) {
                    focusAndEditCell(tableView, previousRow, previousColumn);
                    return;
                }
                previousColumnIndex--;
            }
            previousRow--;
            previousColumnIndex = tableView.getColumns().size() - 1;
        }

    }

    private static <T> boolean isEditableColumn(TableView<T> tableView, TableColumn<T, ?> column) {
        return column != null && column.isEditable() && tableView.isEditable();
    }


    private static <T> void focusAndEditCell(TableView<T> tableView, int row, TableColumn<T, ?> column) {
        tableView.getSelectionModel().clearAndSelect(row, column);
        tableView.getFocusModel().focus(row, column);
        Platform.runLater(() -> tableView.edit(row, column));
    }
}
