package io.github.kaizensphere.devin.devindesktop.gui.controllers.envdetail.section;

import io.github.kaizensphere.devin.devindesktop.AppContext;
import io.github.kaizensphere.devin.devindesktop.gui.components.env.EnvStringEditableCell;
import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel;
import io.github.kaizensphere.devin.devindesktop.observablemodel.SelectedEnvModel;
import io.github.kaizensphere.devin.devindesktop.store.EnvStoreListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class EnvTableviewSectionController implements EnvStoreListener {

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
    private boolean isInternalChange = false;

    private EnvModel boundModel;


    @FXML
    private void initialize() {
        setupNameColumn();
        setupValueColumn();
        setupUpdatedDateColumn();

        boundModel = SelectedEnvModel.getInstance().getReadOnlySelectedEnv();
        AppContext.envStore.registerListener(this);

        tableVariablesView.autosize();

        SelectedEnvModel.selectedEnvProperty().addListener((obs, oldEnv, newEnv) -> {
            if (newEnv == null && newEnv.variables() == null) {
                tableEnvVariables.clear();
                addEmptyRow();
                return;
            }

            List<EnvVariableModel> newEnvVariables = newEnv.variables();
            if (!newEnv.variables().equals(tableEnvVariables)) {
                this.tableEnvVariables.setAll();
                tableEnvVariables.setAll(newEnvVariables);
                addEmptyRow();
            } else {
                logger.info("Env variables did not change, not re-rendering");
            }
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
                String::toUpperCase,
                s -> s.replaceAll("[^a-zA-Z0-9]", ""),
                (oldVar, newValue) -> new EnvVariableModel(newValue, oldVar.value())
        ));
        nameColumn.prefWidthProperty().bind(tableVariablesView.widthProperty().divide(3));
    }

    private void setupValueColumn() {
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().value()));
        valueColumn.setCellFactory(col -> new EnvStringEditableCell(
                (oldVar, newValue) -> new EnvVariableModel(oldVar.name(), newValue)
        ));
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

    @Override
    public void onEnvironmentChanged() {
        this.tableEnvVariables.setAll(AppContext.envStore.getEnvs().stream()
                .filter(env -> env.id().equals(boundModel.id()))
                .findFirst()
                .orElse(boundModel)
                .variables()
        );
    }
}
