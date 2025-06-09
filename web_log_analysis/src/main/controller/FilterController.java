package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bson.Document;

import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.action.Action;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.RangeSlider;
import java.time.Duration;
import models.logData.logData;
import models.mongoDB.mongoDB;
import models.parsers.fileParsers.apacheFileParser;
import models.parsers.fileParsers.nginxFileParser;
import models.utils.dateToUTC;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.util.AbstractMap.SimpleEntry;


public class FilterController implements DataReceiver<HashMap<String, Object>> {    
    @FXML private Button backButton;

    @FXML private TextField ipAddressField;

    @FXML private DatePicker timestampFromDate;
    @FXML private TextField timestampFromTime;
    @FXML private Label timestampFromTimeErrorLabel;

    @FXML private DatePicker timestampToDate;
    @FXML private TextField timestampToTime;
    @FXML private Label timestampToTimeErrorLabel;

    @FXML private TextField countryField;
    @FXML private TextField regionField;
    @FXML private TextField cityField;

    @FXML private FlowPane requestMethodFlowPane;

    @FXML private TextField userField;

    @FXML private VBox responseStatusCodeVBox;
    @FXML private Button addResponseStatusCodeButton;

    @FXML private TextField bytesSizeMinField;
    @FXML private Label bytesSizeMinFieldErrorLabel;
    @FXML private TextField bytesSizeMaxField;
    @FXML private Label bytesSizeMaxFieldErrorLabel;
    @FXML private StackPane bytesSizeSliderStack;
    @FXML private RangeSlider bytesSizeRangeSlider;
    @FXML private Label minBytesSizeRangeSliderLabel;
    @FXML private Label maxBytesSizeRangeSliderLabel;
    
    @FXML private TextField requestURLField;

    @FXML private TextField osField;
    @FXML private TextField browserField;
    @FXML private TextField deviceField;

    @FXML private TextField referrerField;

    @FXML private Button clearButton;
    @FXML private Button applyButton;

    // Closes the filter window (discarding any uncommitted changes)
    @FXML
    private void onBackButtonPressed(ActionEvent event) {
        Platform.runLater(() -> {
            main.App.closeFilterStage();
        });
    }

    // Clear the filter window (clear any uncommitted changes)
    @FXML
    private void onClearButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/filter.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            Platform.runLater(() -> {
                main.App.closeFilterStage();
            });
        }
    }

    // Closes the filter window (intended to apply selected filters later)
    @FXML
    private void onApplyButtonPressed(ActionEvent event) {
        if(!validateBytesSizeAndApplyMin() || !validateBytesSizeAndApplyMax() || !validateTimestampFromTime() || !validateTimestampToTime())
        {
            Platform.runLater(() -> {
                main.App.showInvalidFilterStage(null);
            });
        }
        else
        {
            String ipAddressValue = ipAddressField.getText().length() > 0 ? ipAddressField.getText() : null;
    
            LocalDate timestampFromDateValue = timestampFromDate.getValue();
            LocalTime timestampFromTimeValue;
            try {
                timestampFromTimeValue = timestampFromTime.isDisable() ? null : LocalTime.parse(timestampFromTime.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                timestampFromTimeValue = null;
            }
    
            LocalDate timestampToDateValue = timestampToDate.getValue();
            LocalTime timestampToTimeValue;
            try {
                timestampToTimeValue = timestampToTime.isDisable() ? null : LocalTime.parse(timestampToTime.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                timestampToTimeValue = null;
            }
    
            String countryValue = countryField.getText().length() > 0 ? countryField.getText() : null;
            String regionValue = regionField.getText().length() > 0 ? regionField.getText() : null;
            String cityValue = cityField.getText().length() > 0 ? cityField.getText() : null;
    
            ArrayList<String> requestMethodValue = new ArrayList<>();
            for (Node node : requestMethodFlowPane.getChildren()) {
                if (node instanceof ToggleButton) {
                    ToggleButton toggle = (ToggleButton) node;
                    if(toggle.isSelected())
                    {
                        requestMethodValue.add(toggle.getText());
                    }
                }
            }
    
            ArrayList<Integer> responseStatusCodeValue = new ArrayList<>();
            for (Node node : responseStatusCodeVBox.getChildren()) {
                if (node instanceof ComboBox<?>) {
                    ComboBox<?> combo = (ComboBox<?>) node;
                    // Check if the ComboBox contains Integer values
                    if (combo.getItems().stream().allMatch(item -> item instanceof Integer)) {
                        @SuppressWarnings("unchecked")
                        ComboBox<Integer> intCombo = (ComboBox<Integer>) combo;
                        Integer value = intCombo.getValue();
                        if (value != null) {
                            responseStatusCodeValue.add(value);
                        }
                    }
                }
            }

            String userValue = userField.getText().length() > 0 ? userField.getText() : null;
    
            Integer minBytesSizeValue = Double.valueOf(bytesSizeRangeSlider.getLowValue()).intValue();
            Integer maxBytesSizeValue = Double.valueOf(bytesSizeRangeSlider.getHighValue()).intValue();
    
            String requestURLValue = requestURLField.getText().length() > 0 ? requestURLField.getText() : null;
    
            String osValue = osField.getText().length() > 0 ? osField.getText() : null;
            String browseValue = browserField.getText().length() > 0 ? browserField.getText() : null;
            String deviceValue = deviceField.getText().length() > 0 ? deviceField.getText() : null;
    
            String referrerValue = referrerField.getText().length() > 0 ? referrerField.getText() : null;

            // System.out.println(ipAddressValue);
    
            // System.out.println(timestampFromDateValue);
            // System.out.println(timestampFromTimeValue);
            
            // System.out.println(timestampToDateValue);
            // System.out.println(timestampToTimeValue);
    
            // System.out.println(countryValue);
            // System.out.println(regionValue);
            // System.out.println(cityValue);
    
            // System.out.println(requestMethodValue);
    
            // System.out.println(responseStatusCodeValue);

            // System.out.println(userValue);
    
            // System.out.println(minBytesSizeValue);
            // System.out.println(maxBytesSizeValue);
    
            // System.out.println(requestURLValue);
    
            // System.out.println(osValue);
            // System.out.println(browseValue);
            // System.out.println(deviceValue);

            // System.out.println(referrerValue);

            HashMap<String, Object> filter_rules = new HashMap<>();
            if(ipAddressValue != null)
            {
                filter_rules.put("byRemoteIp", true);
                filter_rules.put("byRemoteIpValue", Arrays.asList(ipAddressValue));
            }
            if(timestampFromDateValue != null || timestampToDateValue != null)
            {
                filter_rules.put("byPeriod", true);
                HashMap<String, Date> byPeriodValueHashMap = new HashMap<>();
                LocalDateTime fromDateTime = timestampFromDateValue != null ? LocalDateTime.of(timestampFromDateValue, timestampFromTimeValue == null ? LocalTime.of(0, 0, 0) : timestampFromTimeValue) : null;
                LocalDateTime toDateTime = timestampToDateValue != null ? LocalDateTime.of(timestampToDateValue, timestampToTimeValue == null ? LocalTime.of(23, 59, 59) : timestampToTimeValue) : null;

                if(fromDateTime != null) byPeriodValueHashMap.put("byPeriodStartValue", (Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant())));
                if(toDateTime != null) byPeriodValueHashMap.put("byPeriodEndValue", (Date.from(toDateTime.atZone(ZoneId.systemDefault()).toInstant())));
                
                filter_rules.put("byPeriodValue", Arrays.asList(byPeriodValueHashMap));
            }
            if(countryValue != null)
            {
                filter_rules.put("byCountryLong", true);
                filter_rules.put("byCountryLongValue", Arrays.asList(countryValue));
            }
            if(regionValue != null)
            {
                filter_rules.put("byRegion", true);
                filter_rules.put("byRegionValue", Arrays.asList(regionValue));
            }
            if(cityValue != null)
            {
                filter_rules.put("byCity", true);
                filter_rules.put("byCityValue", Arrays.asList(cityValue));
            }
            if(requestMethodValue != null && !requestMethodValue.isEmpty())
            {
                filter_rules.put("byRequestMethod", true);
                filter_rules.put("byRequestMethodValue", Arrays.asList(requestMethodValue));
            }
            if(responseStatusCodeValue != null && !responseStatusCodeValue.isEmpty())
            {
                filter_rules.put("byResponseStatusCode", true);
                filter_rules.put("byResponseStatusCodeValue", responseStatusCodeValue);
            }
            if(userValue != null)
            {
                filter_rules.put("byRemoteUser", true);
                filter_rules.put("byRemoteUserValue", Arrays.asList(userValue));
            }
            if((minBytesSizeValue != null || maxBytesSizeValue != null) && (!(minBytesSizeValue == -1 && maxBytesSizeValue == 524288)))
            {
                filter_rules.put("byBytes", true);
                HashMap<String, Integer> byBytesValueHashMap = new HashMap<>();

                if(minBytesSizeValue != null) byBytesValueHashMap.put("byBytesStartValue", minBytesSizeValue);
                if(maxBytesSizeValue != null) byBytesValueHashMap.put("byBytesEndValue", maxBytesSizeValue);
                
                filter_rules.put("byBytesValue", Arrays.asList(byBytesValueHashMap));
            }
            if(requestURLValue != null)
            {
                filter_rules.put("byRequest", true);
                filter_rules.put("byRequestValue", Arrays.asList(requestURLValue));
            }
            if(osValue != null)
            {
                filter_rules.put("byOS", true);
                filter_rules.put("byOSValue", Arrays.asList(osValue));
            }
            if(browseValue != null)
            {
                filter_rules.put("byBrowser", true);
                filter_rules.put("byBrowserValue", Arrays.asList(browseValue));
            }
            if(deviceValue != null)
            {
                filter_rules.put("byDevice", true);
                filter_rules.put("byDeviceValue", Arrays.asList(deviceValue));
            }
            if(referrerValue != null)
            {
                filter_rules.put("byReferrer", true);
                filter_rules.put("byReferrerValue", Arrays.asList(referrerValue));
            }

            Platform.runLater(() -> {
                main.App.showLoadingStage(null);
                
                Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
                    @Override
                    protected HashMap<String, Object> call() throws Exception {
                        return PrimaryController.prepareData(filter_rules);
                    }
                };
    
                fetchDataTask.setOnSucceeded(v2 -> {
                    HashMap<String, Object> data = fetchDataTask.getValue();
                    Platform.runLater(() -> {
                        main.App.closeLoadingStage();
                        Platform.runLater(() -> {
                            main.App.closeFilterStage();
                            Platform.runLater(() -> {
                                main.App.switchToDashboard(data);
                            });
                        });
                    });
                });
    
                Thread thread = new Thread(fetchDataTask);
                thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
                thread.start();
            });
        }
    }

    private final int MAX_COMBOBOXES = 3;
    private static final ObservableList<Integer> httpCodes = FXCollections.observableArrayList(
        -1,
        100, 101, 102, 103,
        200, 201, 202, 203, 204, 205, 206, 207, 208, 226,
        300, 301, 302, 303, 304, 305, 306, 307, 308,
        400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 421, 422, 423, 424, 425, 426, 428, 429, 431, 451,
        500, 501, 502, 503, 504, 505, 506, 507, 508, 510, 511
    );
    
    @FXML
    public void initialize() {
        timestampFromTime.setDisable(true);
        timestampToTime.setDisable(true);
        // startDatePicker.getEditor().setDisable(true);
        // endDatePicker.getEditor().setDisable(true);

        
        String displayDatePattern = "dd/MM/yyyy";

        DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern(displayDatePattern);

        timestampFromDate.setPromptText(displayDatePattern);
        timestampToDate.setPromptText(displayDatePattern);
        
        DateTimeFormatter[] inputFormatters = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
        };

        timestampFromDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return displayDateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    for (DateTimeFormatter formatter : inputFormatters) {
                        try {
                            return LocalDate.parse(string, formatter);
                        } catch (DateTimeParseException e) {
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            }
        });

        timestampToDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return displayDateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    for (DateTimeFormatter formatter : inputFormatters) {
                        try {
                            return LocalDate.parse(string, formatter);
                        } catch (DateTimeParseException e) {
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            }
        });

        bytesSizeRangeSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> updateSizeTextFieldsFromSlider());
        bytesSizeRangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> updateSizeTextFieldsFromSlider());

        // Show/hide on hover
        // sizeSliderStack.setOnMouseEntered(e -> fadeLabels(true));
        // sizeSliderStack.setOnMouseExited(e -> fadeLabels(false));

        bytesSizeMinField.textProperty().addListener((obs, oldText, newText) -> validateBytesSizeAndApplyMin());
        bytesSizeMaxField.textProperty().addListener((obs, oldText, newText) -> validateBytesSizeAndApplyMax());

        timestampFromTime.textProperty().addListener((obs, oldText, newText) -> validateTimestampFromTime());
        timestampToTime.textProperty().addListener((obs, oldText, newText) -> validateTimestampToTime());

        timestampFromDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && timestampToDate.getValue() != null && newDate.isAfter(timestampToDate.getValue())) {
                timestampFromDate.setValue(oldDate);
                return;
            }
            if(newDate != null && newDate.toString().length() > 0)
            {
                timestampFromTime.setDisable(false);
            }
            else
            {
                timestampFromTime.setDisable(true);
            }
            timestampToDate.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || (newDate != null && date.isBefore(newDate))) {
                        setDisable(true);
                        setStyle("-fx-background-color:rgb(155, 155, 155);"); // Optional: style disabled days
                    }
                }
            });
        });

        timestampToDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && timestampFromDate.getValue() != null && newDate.isBefore(timestampFromDate.getValue())) {
                timestampToDate.setValue(oldDate);
                return;
            }
            if(newDate != null && newDate.toString().length() > 0)
            {
                timestampToTime.setDisable(false);
            }
            else
            {
                timestampToTime.setDisable(true);
            }
            timestampFromDate.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || (newDate != null && date.isAfter(newDate))) {
                        setDisable(true);
                        setStyle("-fx-background-color:rgb(155, 155, 155);"); // Optional: style disabled days
                    }
                }
            });
        });

        timestampFromDate.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if(newText != null && newText.toString().length() > 0)
            {
                try {
                    LocalDate parsed = timestampFromDate.getConverter().fromString(newText);
                    timestampFromTime.setDisable(false);
                } catch (Exception e) {
                    timestampFromTime.setDisable(true);
                }
            }
            else
            {
                timestampFromTime.setDisable(true);
            }
        });
        
        timestampToDate.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if(newText != null && newText.toString().length() > 0)
            {
                try {
                    LocalDate parsed = timestampToDate.getConverter().fromString(newText);
                    timestampToTime.setDisable(false);
                } catch (Exception e) {
                    timestampToTime.setDisable(true);
                }
            }
            else
            {
                timestampToTime.setDisable(true);
            }
        });


        // final LocalDate[] timestampToDateOriginalValue = new LocalDate[1];
        // timestampToDate.setOnShowing(e -> {
        //     // Only if no date is already selected, temporarily set the value.
        //     if (timestampFromDate.getValue() != null) {
        //         timestampToDateOriginalValue[0] = timestampToDate.getValue();
        //         timestampToDate.setValue(timestampFromDate.getValue());
        //         Platform.runLater(() -> timestampToDate.getEditor().clear());
        //     }
        // });

        // timestampToDate.setOnHiding(e -> {
        //     // Check if the user actually selected a date.
        //     // For example, if the user never typed or picked a date,
        //     // the editor text may remain empty.
        //     if (timestampToDate.getEditor().getText().isEmpty()) {
        //         // Restore the original value (which is null)
        //         timestampToDate.setValue(timestampToDateOriginalValue[0]);
        //     }
        // });

        // final LocalDate[] timestampFromDateOriginalValue = new LocalDate[1];
        // timestampFromDate.setOnShowing(e -> {
        //     // Only if no date is already selected, temporarily set the value.
        //     if (timestampToDate.getValue() != null) {
        //         timestampFromDateOriginalValue[0] = timestampFromDate.getValue();
        //         timestampFromDate.setValue(timestampToDate.getValue());
        //         Platform.runLater(() -> timestampFromDate.getEditor().clear());
        //     }
        // });

        // timestampFromDate.setOnHiding(e -> {
        //     // Check if the user actually selected a date.
        //     // For example, if the user never typed or picked a date,
        //     // the editor text may remain empty.
        //     if (timestampFromDate.getEditor().getText().isEmpty()) {
        //         // Restore the original value (which is null)
        //         timestampFromDate.setValue(timestampFromDateOriginalValue[0]);
        //     }
        // });

        addResponseCodeBox();
    }

    public void onAddResponseStatusCodeBox() {
        if (responseStatusCodeVBox.getChildren().size() < MAX_COMBOBOXES) {
            addResponseCodeBox();
        }
        if (responseStatusCodeVBox.getChildren().size() >= MAX_COMBOBOXES) {
            addResponseStatusCodeButton.setDisable(true);
        }
    }

    private void addResponseCodeBox() {
        ComboBox<Integer> comboBox = new ComboBox<>(FilterController.httpCodes);
        comboBox.setPromptText("Select Code...");
        comboBox.setEditable(false);
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

        responseStatusCodeVBox.getChildren().add(comboBox);
    }

    
    private void updateSizeTextFieldsFromSlider() {
        long min = (long) bytesSizeRangeSlider.getLowValue();
        long max = (long) bytesSizeRangeSlider.getHighValue();

        bytesSizeMinField.setText(String.valueOf(min));
        bytesSizeMaxField.setText(String.valueOf(max));
        clearError(bytesSizeMinField, bytesSizeMinFieldErrorLabel);
        clearError(bytesSizeMaxField, bytesSizeMaxFieldErrorLabel);
    }

    private boolean validateTimestampFromTime() {
        if(timestampFromTime.isDisable()) {
            clearError(timestampFromTime, timestampFromTimeErrorLabel);
            return true;
        }
        try {
            String timeFromStr = timestampFromTime.getText();
            timeFromStr = timeFromStr == null ? "" : timeFromStr;
            LocalTime timeFrom = null;
            try {
                if(timestampFromTime != null && timeFromStr.length() > 0)
                {
                    if (timeFromStr.startsWith("24:")) {
                    }
                    else
                    {
                        timeFrom = LocalTime.parse(timeFromStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    }
                }
            } catch (Exception e) {
                timeFrom = null;
            }

            String timeToStr = timestampToTime.isDisable() ? null : timestampToTime.getText();
            LocalTime timeTo = null;
            try {
                if(timeToStr != null && timeToStr.length() > 0)
                {
                    if (timeToStr.startsWith("24:")) {
                    }
                    else
                    {
                        timeTo = LocalTime.parse(timeToStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    }
                }
            } catch (Exception e) {
                timeTo = null;
            }
            LocalDate timestampFromDateValue = timestampFromDate.getValue();
            LocalDate timestampToDateValue = timestampToDate.getValue();
            if(timeFrom != null && timeTo != null && timestampFromDateValue != null && timestampToDateValue != null)
            {
                LocalDateTime fromDateTime = LocalDateTime.of(timestampFromDateValue, timeFrom);
                LocalDateTime toDateTime = LocalDateTime.of(timestampToDateValue, timeTo);
                if(fromDateTime.isAfter(toDateTime) || toDateTime.isBefore(fromDateTime))
                {
                    showError(timestampFromTime, timestampFromTimeErrorLabel);
                    return false;
                }
            }
            clearError(timestampFromTime, timestampFromTimeErrorLabel);
            return true;
        } catch (Exception e) {
            showError(timestampFromTime, timestampFromTimeErrorLabel);
            return false;
        }
    }
    
    private boolean validateTimestampToTime() {
        if(timestampToTime.isDisable()) {
            clearError(timestampToTime, timestampToTimeErrorLabel);
            return true;
        }
        try {
            String timeToStr = timestampToTime.getText();
            timeToStr = timeToStr == null ? "" : timeToStr;
            LocalTime timeTo = null;
            try {
                if(timestampToTime != null && timeToStr.length() > 0)
                {
                    if (timeToStr.startsWith("24:")) {
                    }
                    else
                    {
                        timeTo = LocalTime.parse(timeToStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    }
                }
            } catch (Exception e) {
                timeTo = null;
            }

            String timeFromStr = timestampFromTime.isDisable() ? null : timestampFromTime.getText();
            LocalTime timeFrom = null;
            try {
                if(timeFromStr != null && timeFromStr.length() > 0)
                {
                    if (timeFromStr.startsWith("24:")) {
                    }
                    else
                    {
                        timeFrom = LocalTime.parse(timeFromStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    }
                }
            } catch (Exception e) {
                timeFrom = null;
            }
            LocalDate timestampToDateValue = timestampToDate.getValue();
            LocalDate timestampFromDateValue = timestampFromDate.getValue();
            if(timeTo != null && timeFrom != null && timestampToDateValue != null && timestampFromDateValue != null)
            {
                LocalDateTime toDateTime = LocalDateTime.of(timestampToDateValue, timeTo);
                LocalDateTime fromDateTime = LocalDateTime.of(timestampFromDateValue, timeFrom);
                if(toDateTime.isBefore(fromDateTime) || fromDateTime.isAfter(toDateTime))
                {
                    showError(timestampToTime, timestampToTimeErrorLabel);
                    return false;
                }
            }
            clearError(timestampToTime, timestampToTimeErrorLabel);
            return true;
        } catch (Exception e) {
            showError(timestampToTime, timestampToTimeErrorLabel);
            return false;
        }
    }

    private boolean validateBytesSizeAndApplyMin() {
        try {
            String minText = bytesSizeMinField.getText();
            long min;
            if(minText.length() > 0)
            {
                min = Long.parseLong(minText);
            }
            else
            {
                min = -1;
            }
            long max = (long) bytesSizeRangeSlider.getHighValue();
            if (min > max) {
                showError(bytesSizeMinField, bytesSizeMinFieldErrorLabel);
                return false;
            } else {
                bytesSizeRangeSlider.setLowValue(min);
                clearError(bytesSizeMinField, bytesSizeMinFieldErrorLabel);
                return true;
            }
        } catch (NumberFormatException e) {
            showError(bytesSizeMinField, bytesSizeMinFieldErrorLabel);
            return false;
        }
    }

    private boolean validateBytesSizeAndApplyMax() {
        try {
            String maxText = bytesSizeMaxField.getText();
            long max;
            if(maxText.length() > 0)
            {
                max = Long.parseLong(maxText);
            }
            else
            {
                max = 524288;
            }
            long min = (long) bytesSizeRangeSlider.getLowValue();
            if (max < min) {
                showError(bytesSizeMaxField, bytesSizeMaxFieldErrorLabel);
                return false;
            } else {
                bytesSizeRangeSlider.setHighValue(max);
                clearError(bytesSizeMaxField, bytesSizeMaxFieldErrorLabel);
                return true;
            }
        } catch (NumberFormatException e) {
            showError(bytesSizeMaxField, bytesSizeMaxFieldErrorLabel);
            return false;
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

    @Override
    public void setData(HashMap<String, Object> data) {
        // do something here
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