package dev.kaizensphere.devin.desktop.gui.controllers.envdetail.section;

import dev.kaizensphere.devin.desktop.AppContext;
import dev.kaizensphere.devin.desktop.gui.components.env.EnvStringEditableCell;
import dev.kaizensphere.devin.models.EnvModel;
import dev.kaizensphere.devin.models.EnvVariableModel;
import dev.kaizensphere.devin.utils.EnvValidationUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class EnvTableviewSectionController {

    Logger logger = Logger.getLogger(EnvTableviewSectionController.class.getName());

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    @FXML
    private TableView<EnvVariableModel> tableVariablesView;

    @FXML
    private TableColumn<EnvVariableModel, String> nameColumn;

    @FXML
    private TableColumn<EnvVariableModel, String> valueColumn;

    @FXML
    private TableColumn<EnvVariableModel, String> updatedDateColumn;

    private final ObservableList<EnvVariableModel> tableEnvVariables = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        setupNameColumn();
        setupValueColumn();
        setupUpdatedDateColumn();

        EnvModel boundModel = AppContext.selectedEnvFx.getSelectedEnv();

        tableVariablesView.autosize();

        AppContext.selectedEnvFx.selectedEnvProperty().addListener((obs, oldEnv, newEnv) -> {
            if (newEnv == null || newEnv.variables() == null) {
                tableEnvVariables.clear();
                addEmptyRow();
                return;
            }

            tableEnvVariables.setAll(newEnv.variables());
            addEmptyRow();
        });

        if (boundModel != null) {
            tableEnvVariables.addAll(boundModel.variables());
        }
        tableVariablesView.setItems(tableEnvVariables);
        this.addEmptyRow();
    }

    private void setupNameColumn() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));

        nameColumn.setCellFactory(col -> new EnvStringEditableCell(
                EnvValidationUtils::formatVariableName,
                EnvValidationUtils::sanitizeVariableName)
        );
        nameColumn.setOnEditCommit(this::onEnvVariableNameEditCommitEdit);
        nameColumn.prefWidthProperty().bind(tableVariablesView.widthProperty().divide(3));
    }

    private void setupValueColumn() {
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().value()));
        valueColumn.setCellFactory(col -> new EnvStringEditableCell());
        valueColumn.setOnEditCommit(this::onEnvValueNameEditCommitEdit);
        valueColumn.prefWidthProperty().bind(tableVariablesView.widthProperty().divide(3));
    }

    private void setupUpdatedDateColumn() {
        updatedDateColumn.setCellValueFactory(cellData -> {
            String updatedDate = cellData.getValue().updatedDate() != null ? formatter.format(cellData.getValue().updatedDate()) : "";
            return new SimpleStringProperty(updatedDate);
        });
        updatedDateColumn.setEditable(false);
        updatedDateColumn.prefWidthProperty().bind(tableVariablesView.widthProperty().divide(3));
    }

    private void addEmptyRow() {
        if (this.tableEnvVariables.isEmpty()) {
            this.tableEnvVariables.add(new EnvVariableModel("", ""));
        } else {
            EnvVariableModel lastRow = this.tableEnvVariables.getLast();
            if (!lastRow.name().isEmpty() || !lastRow.value().isEmpty()) {
                this.tableEnvVariables.add(new EnvVariableModel("", ""));
            }
        }
        tableVariablesView.scrollTo(this.tableEnvVariables.size() - 1);
    }

    private void onEnvVariableNameEditCommitEdit(TableColumn.CellEditEvent<EnvVariableModel, String> event) {
        this.updateVariable(event, (oldEnvVariable, newValue) -> new EnvVariableModel(newValue, oldEnvVariable.value()));
    }

    private void onEnvValueNameEditCommitEdit(TableColumn.CellEditEvent<EnvVariableModel, String> event) {
        this.updateVariable(event, (oldEnvVariable, newValue) -> new EnvVariableModel(oldEnvVariable.name(), newValue));
    }

    private void updateVariable(TableColumn.CellEditEvent<EnvVariableModel, String> event, BiFunction<EnvVariableModel, String, EnvVariableModel> modelMapper) {
        EnvVariableModel oldEnvVariable = event.getRowValue();
        String newValue = event.getNewValue();
        EnvVariableModel updatedEnvVariable = modelMapper.apply(oldEnvVariable, newValue);
        UUID envModelId = getSelectedEnv().id();
        if (envModelId != null) {
            AppContext.envStore.updateEnvVariable(envModelId, oldEnvVariable.id(), updatedEnvVariable);
        }
    }

    private EnvModel getSelectedEnv() {
        return AppContext.selectedEnvFx.getSelectedEnv();
    }
}
