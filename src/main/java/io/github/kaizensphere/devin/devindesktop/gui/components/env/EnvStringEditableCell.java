package io.github.kaizensphere.devin.devindesktop.gui.components.env;

import io.github.kaizensphere.devin.devindesktop.AppContext;
import io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel;
import io.github.kaizensphere.devin.devindesktop.gui.behaviors.interaction.TableNavigationHelper;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static javafx.scene.input.KeyCombination.SHIFT_ANY;

public class EnvStringEditableCell extends TextFieldTableCell<EnvVariableModel, String> {

    private final Function<String, String> formatter;
    private final Function<String, String> filter;
    private TextField textField;


    public EnvStringEditableCell() {
        this(null, null);
    }

    public EnvStringEditableCell(Function<String, String> formatter) {
        this(formatter, null);
    }

    public EnvStringEditableCell(Function<String, String> formatter,
                                 Function<String, String> filter) {
        this.formatter = formatter;
        this.filter = filter;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null) {
            textField = new TextField();

            this.setupTextFieldFormatter(textField);

            this.setupTextFieldInputMap(textField);
        }
        textField.setText(getItem() != null ? getItem() : "");

        setText(null);
        setGraphic(this.textField);
        this.textField.requestFocus();
        Platform.runLater(() -> this.textField.positionCaret(this.textField.getText().length()));

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
    }


    private void setupTextFieldFormatter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>((change) -> {
            String inputText = Optional.ofNullable(change.getText()).orElse("");
            inputText = filter != null ? filter.apply(inputText) : inputText;
            inputText = formatter != null ? formatter.apply(inputText) : inputText;
            change.setText(inputText);
            return change;
        }));
    }

    private void setupTextFieldInputMap(TextField textField) {
        Nodes.addInputMap(textField, InputMap.sequence(
                InputMap.consume(EventPattern.keyPressed(KeyCode.ENTER), event -> {
                    commitEdit(textField.getText());
                    TableNavigationHelper.navigateToNextCell(getTableView(), getTablePosition());
                }),
                InputMap.consume(EventPattern.keyPressed(KeyCode.TAB), event -> {
                    commitEdit(textField.getText());
                    TableNavigationHelper.navigateToNextCell(getTableView(), getTablePosition());
                }),
                InputMap.consume(EventPattern.keyPressed(KeyCode.TAB, SHIFT_ANY), event -> {
                    commitEdit(textField.getText());
                    TableNavigationHelper.navigateToPreviousCell(getTableView(), getTablePosition());
                })
        ));
    }

    private TablePosition<EnvVariableModel, String> getTablePosition() {
        return new TablePosition<>(getTableView(), getIndex(), getTableColumn());
    }
}

