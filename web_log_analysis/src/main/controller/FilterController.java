package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.action.Action;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.RangeSlider;
import javafx.util.Duration;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;


public class FilterController {

    // Closes the filter window (discarding any uncommitted changes)
    @FXML
    private void onBackButtonPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Closes the filter window (intended to apply selected filters later)
    @FXML
    private void onApplyButtonPressed(ActionEvent event) {
        // TODO: Pass selected filter values to the dashboard controller or data layer

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML private RangeSlider sizeRangeSlider;
    @FXML private Label minSizeLabel;
    @FXML private Label maxSizeLabel;
    @FXML private StackPane sizeSliderStack;
    @FXML private TextField sizeFieldMin;
    @FXML private TextField sizeFieldMax;
    @FXML private Label minErrorLabel;
    @FXML private Label maxErrorLabel;
    @FXML private ComboBox<Integer> responseCodeCombo;
    @FXML private VBox responseCodeBox;

    @FXML
    private Button addResponseCodeBtn;

    private final int MAX_COMBOBOXES = 3;
    private final ObservableList<Integer> httpCodes = FXCollections.observableArrayList(
        100, 101, 102, 103,
        200, 201, 202, 203, 204, 205, 206, 207, 208, 226,
        300, 301, 302, 303, 304, 305, 307, 308,
        400, 401, 402, 403, 404, 405, 406, 407, 408, 409,
        410, 411, 412, 413, 414, 415, 416, 417, 418, 422,
        426, 428, 429, 431, 451,
        500, 501, 502, 503, 504, 505, 506, 507, 508, 510, 511
    );
    
    @FXML
    public void initialize() {

        sizeRangeSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> updateSizeTextFieldsFromSlider());
        sizeRangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> updateSizeTextFieldsFromSlider());

        // Show/hide on hover
        // sizeSliderStack.setOnMouseEntered(e -> fadeLabels(true));
        // sizeSliderStack.setOnMouseExited(e -> fadeLabels(false));
        
        sizeFieldMin.setOnAction(e -> validateAndApplyMin());
        sizeFieldMax.setOnAction(e -> validateAndApplyMax());

         sizeFieldMin.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) validateAndApplyMin();
        });

        sizeFieldMax.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) validateAndApplyMax();
        });

        addResponseCodeBox();
    }

    public void onAddResponseCodeBox() {
        if (responseCodeBox.getChildren().size() < MAX_COMBOBOXES) {
            addResponseCodeBox();
        }
        if (responseCodeBox.getChildren().size() >= MAX_COMBOBOXES) {
            addResponseCodeBtn.setDisable(true);
        }
    }

    private void addResponseCodeBox() {
        ComboBox<Integer> comboBox = new ComboBox<>(httpCodes);
        comboBox.setPromptText("Select Code...");
        comboBox.setEditable(true);
        comboBox.getStyleClass().add("combo-box");

        // Make sure it displays Integer properly
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        responseCodeBox.getChildren().add(comboBox);
    }

    
    private void updateSizeTextFieldsFromSlider() {
        long min = (long) sizeRangeSlider.getLowValue();
        long max = (long) sizeRangeSlider.getHighValue();

        sizeFieldMin.setText(String.valueOf(min));
        sizeFieldMax.setText(String.valueOf(max));
        clearError(sizeFieldMin, minErrorLabel);
        clearError(sizeFieldMax, maxErrorLabel);
    }

    private void validateAndApplyMin() {
        try {
            long min = Long.parseLong(sizeFieldMin.getText());
            long max = (long) sizeRangeSlider.getHighValue();
            if (min > max) {
                showError(sizeFieldMin, minErrorLabel);
            } else {
                sizeRangeSlider.setLowValue(min);
                clearError(sizeFieldMin, minErrorLabel);
            }
        } catch (NumberFormatException e) {
            showError(sizeFieldMin, minErrorLabel);
        }
    }

    private void validateAndApplyMax() {
        try {
            long max = Long.parseLong(sizeFieldMax.getText());
            long min = (long) sizeRangeSlider.getLowValue();
            if (max < min) {
                showError(sizeFieldMax, maxErrorLabel);
            } else {
                sizeRangeSlider.setHighValue(max);
                clearError(sizeFieldMax, maxErrorLabel);
            }
        } catch (NumberFormatException e) {
            showError(sizeFieldMax, maxErrorLabel);
        }
    }

    private void showError(TextField field, Label errorLabel) {
        field.getStyleClass().add("error");
        errorLabel.setVisible(true);
    }

    private void clearError(TextField field, Label errorLabel) {
        field.getStyleClass().remove("error");
        errorLabel.setVisible(false);
    }

    // private void updateSizeLabels() {
    //     long minVal = (long) sizeRangeSlider.getLowValue();
    //     long maxVal = (long) sizeRangeSlider.getHighValue();

    //     minSizeLabel.setText(formatBytes(minVal));
    //     maxSizeLabel.setText(formatBytes(maxVal));
    // }

    // private String formatBytes(long bytes) {
    //     if (bytes >= 1_000_000) return bytes / 1_000_000 + " MB";
    //     if (bytes >= 1_000) return bytes / 1_000 + " KB";
    //     return bytes + " B";
    // }
}