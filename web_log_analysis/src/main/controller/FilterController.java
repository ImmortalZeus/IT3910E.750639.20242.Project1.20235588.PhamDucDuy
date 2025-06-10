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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static String ipAddressFieldBackup = null;
    @FXML private Label ipAddressFieldErrorLabel;

    @FXML private DatePicker timestampFromDate;
    private static LocalDate timestampFromDateBackup = null;
    @FXML private TextField timestampFromTime;
    private static String timestampFromTimeBackup = null;
    @FXML private Label timestampFromTimeErrorLabel;

    @FXML private DatePicker timestampToDate;
    private static LocalDate timestampToDateBackup = null;
    @FXML private TextField timestampToTime;
    private static String timestampToTimeBackup = null;
    @FXML private Label timestampToTimeErrorLabel;

    @FXML private TextField countryField;
    private static String countryFieldBackup = null;
    @FXML private TextField regionField;
    private static String regionFieldBackup = null;
    @FXML private TextField cityField;
    private static String cityFieldBackup = null;

    @FXML private FlowPane requestMethodFlowPane;
    private static ArrayList<String> requestMethodFlowPaneBackup = null;

    @FXML private TextField userField;
    private static String userFieldBackup = null;

    @FXML private VBox responseStatusCodeVBox;
    private static ArrayList<Integer> responseStatusCodeVBoxBackup = null;

    @FXML private Button addResponseStatusCodeButton;

    @FXML private TextField bytesSizeMinField;
    private static String bytesSizeMinFieldBackup = null;
    @FXML private Label bytesSizeMinFieldErrorLabel;

    @FXML private TextField bytesSizeMaxField;
    private static String bytesSizeMaxFieldBackup = null;
    @FXML private Label bytesSizeMaxFieldErrorLabel;
    
    @FXML private StackPane bytesSizeSliderStack;
    @FXML private RangeSlider bytesSizeRangeSlider;
    private static SimpleEntry<Integer, Integer> bytesSizeRangeSliderBackup = null;
    @FXML private Label minBytesSizeRangeSliderLabel;
    @FXML private Label maxBytesSizeRangeSliderLabel;
    
    @FXML private TextField requestURLField;
    private static String requestURLFieldBackup = null;

    @FXML private TextField osField;
    private static String osFieldBackup = null;
    @FXML private TextField browserField;
    private static String browserFieldBackup = null;
    @FXML private TextField deviceField;
    private static String deviceFieldBackup = null;

    @FXML private TextField referrerField;
    private static String referrerFieldBackup = null;

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
            ipAddressField.setText("");
            timestampFromDate.setValue(null);
            timestampFromTime.setText("");
            timestampToDate.setValue(null);
            timestampToTime.setText("");
            countryField.setText("");
            regionField.setText("");
            cityField.setText("");
            for (Node node : requestMethodFlowPane.getChildren()) {
                if (node instanceof ToggleButton) {
                    ToggleButton toggle = (ToggleButton) node;
                    toggle.setSelected(false);
                }
            }
            userField.setText("");
            responseStatusCodeVBox.getChildren().clear();
            this.addResponseCodeBox(null);
            bytesSizeRangeSlider.setLowValue(Double.valueOf(0));
            bytesSizeRangeSlider.setHighValue(Double.valueOf(2147483647));
            bytesSizeMinField.setText("");
            bytesSizeMaxField.setText("");
            requestURLField.setText("");
            osField.setText("");
            browserField.setText("");
            deviceField.setText("");
            referrerField.setText("");
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
            String ipAddressValue = ipAddressField.getText().trim().length() > 0 ? ipAddressField.getText().trim() : null;
            ipAddressFieldBackup = ipAddressValue;
            
            LocalDate timestampFromDateValue = timestampFromDate.getValue();
            timestampFromDateBackup = timestampFromDateValue;
            LocalTime timestampFromTimeValue;
            try {
                timestampFromTimeValue = timestampFromTime.isDisable() ? null : LocalTime.parse(timestampFromTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                timestampFromTimeValue = null;
            }
            timestampFromTimeBackup = timestampFromTimeValue == null ? null : String.valueOf(timestampFromTimeValue);
    
            LocalDate timestampToDateValue = timestampToDate.getValue();
            timestampToDateBackup = timestampToDateValue;
            LocalTime timestampToTimeValue;
            try {
                timestampToTimeValue = timestampToTime.isDisable() ? null : LocalTime.parse(timestampToTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                timestampToTimeValue = null;
            }
            timestampToTimeBackup = timestampToTimeValue == null ? null : String.valueOf(timestampToTimeValue);

            String countryValue = countryField.getText().trim().length() > 0 ? countryField.getText().trim() : null;
            countryFieldBackup = countryValue;
            String regionValue = regionField.getText().trim().length() > 0 ? regionField.getText().trim() : null;
            regionFieldBackup = regionValue;
            String cityValue = cityField.getText().trim().length() > 0 ? cityField.getText().trim() : null;
            cityFieldBackup = cityValue;

            ArrayList<String> requestMethodValue = new ArrayList<>();
            for (Node node : requestMethodFlowPane.getChildren()) {
                if (node instanceof ToggleButton) {
                    ToggleButton toggle = (ToggleButton) node;
                    if(toggle.isSelected())
                    {
                        requestMethodValue.add(toggle.getText().trim());
                    }
                }
            }
            requestMethodFlowPaneBackup = requestMethodValue.isEmpty() ? null : requestMethodValue;
    
            ArrayList<Integer> responseStatusCodeValue = new ArrayList<>();
            for (Node node : responseStatusCodeVBox.getChildren()) {
                if (node instanceof ComboBox<?>) {
                    ComboBox<?> combo = (ComboBox<?>) node;
                    // Check if the ComboBox contains Integer values
                    if (combo.getItems().stream().allMatch(item -> item == null || item instanceof Integer)) {
                        @SuppressWarnings("unchecked")
                        ComboBox<Integer> intCombo = (ComboBox<Integer>) combo;
                        Integer value = intCombo.getValue();
                        if (value != null) {
                            responseStatusCodeValue.add(value);
                        }
                    }
                }
            }
            responseStatusCodeVBoxBackup = responseStatusCodeValue.isEmpty() ? null : responseStatusCodeValue;

            String userValue = userField.getText().trim().length() > 0 ? userField.getText().trim() : null;
            userFieldBackup = userValue;

            Integer minBytesSizeValue = Double.valueOf(bytesSizeRangeSlider.getLowValue()).intValue();
            Integer maxBytesSizeValue = Double.valueOf(bytesSizeRangeSlider.getHighValue()).intValue();
            bytesSizeRangeSliderBackup = (minBytesSizeValue.equals(0) && maxBytesSizeValue.equals(2147483647)) ? null : new SimpleEntry<>(minBytesSizeValue, maxBytesSizeValue);
            bytesSizeMinFieldBackup = minBytesSizeValue.equals(0) ? null : String.valueOf(minBytesSizeValue);
            bytesSizeMaxFieldBackup = maxBytesSizeValue.equals(2147483647) ? null : String.valueOf(maxBytesSizeValue);

            String requestURLValue = requestURLField.getText().trim().length() > 0 ? requestURLField.getText().trim() : null;
            requestURLFieldBackup = requestURLValue;
            
            String osValue = osField.getText().trim().length() > 0 ? osField.getText().trim() : null;
            osFieldBackup = osValue;
            String browseValue = browserField.getText().trim().length() > 0 ? browserField.getText().trim() : null;
            browserFieldBackup = browseValue;
            String deviceValue = deviceField.getText().trim().length() > 0 ? deviceField.getText().trim() : null;
            deviceFieldBackup = deviceValue;

            String referrerValue = referrerField.getText().trim().length() > 0 ? referrerField.getText().trim() : null;
            referrerFieldBackup = referrerValue;

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
                filter_rules.put("byRemoteIpValue", new ArrayList<>(Arrays.asList(ipAddressValue)));
            }
            if(timestampFromDateValue != null || timestampToDateValue != null)
            {
                filter_rules.put("byPeriod", true);
                HashMap<String, Date> byPeriodValueHashMap = new HashMap<>();
                LocalDateTime fromDateTime = timestampFromDateValue != null ? LocalDateTime.of(timestampFromDateValue, timestampFromTimeValue == null ? LocalTime.of(0, 0, 0) : timestampFromTimeValue) : null;
                LocalDateTime toDateTime = timestampToDateValue != null ? LocalDateTime.of(timestampToDateValue, timestampToTimeValue == null ? LocalTime.of(23, 59, 59) : timestampToTimeValue) : null;

                if(fromDateTime != null) byPeriodValueHashMap.put("byPeriodStartValue", (Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant())));
                if(toDateTime != null) byPeriodValueHashMap.put("byPeriodEndValue", (Date.from(toDateTime.atZone(ZoneId.systemDefault()).toInstant())));
                
                filter_rules.put("byPeriodValue", new ArrayList<>(Arrays.asList(byPeriodValueHashMap)));
            }
            if(countryValue != null)
            {
                filter_rules.put("byCountryLong", true);
                filter_rules.put("byCountryLongValue", new ArrayList<>(Arrays.asList(countryValue)));
            }
            if(regionValue != null)
            {
                filter_rules.put("byRegion", true);
                filter_rules.put("byRegionValue", new ArrayList<>(Arrays.asList(regionValue)));
            }
            if(cityValue != null)
            {
                filter_rules.put("byCity", true);
                filter_rules.put("byCityValue", new ArrayList<>(Arrays.asList(cityValue)));
            }
            if(requestMethodValue != null && !requestMethodValue.isEmpty())
            {
                filter_rules.put("byRequestMethod", true);
                filter_rules.put("byRequestMethodValue", new ArrayList<>(requestMethodValue));
            }
            if(responseStatusCodeValue != null && !responseStatusCodeValue.isEmpty())
            {
                filter_rules.put("byResponseStatusCode", true);
                filter_rules.put("byResponseStatusCodeValue", new ArrayList<>(responseStatusCodeValue));
            }
            if(userValue != null)
            {
                filter_rules.put("byRemoteUser", true);
                filter_rules.put("byRemoteUserValue", new ArrayList<>(Arrays.asList(userValue)));
            }
            if((minBytesSizeValue != null || maxBytesSizeValue != null) && (!(minBytesSizeValue.equals(0) && maxBytesSizeValue.equals(2147483647))))
            {
                filter_rules.put("byBytes", true);
                HashMap<String, Integer> byBytesValueHashMap = new HashMap<>();

                if(minBytesSizeValue != null) byBytesValueHashMap.put("byBytesStartValue", minBytesSizeValue);
                if(maxBytesSizeValue != null) byBytesValueHashMap.put("byBytesEndValue", maxBytesSizeValue);
                
                filter_rules.put("byBytesValue", new ArrayList<>(Arrays.asList(byBytesValueHashMap)));
            }
            if(requestURLValue != null)
            {
                filter_rules.put("byRequestUrl", true);
                filter_rules.put("byRequestUrlValue", new ArrayList<>(Arrays.asList(requestURLValue)));
            }
            if(osValue != null)
            {
                filter_rules.put("byOS", true);
                filter_rules.put("byOSValue", new ArrayList<>(Arrays.asList(osValue)));
            }
            if(browseValue != null)
            {
                filter_rules.put("byBrowser", true);
                filter_rules.put("byBrowserValue", new ArrayList<>(Arrays.asList(browseValue)));
            }
            if(deviceValue != null)
            {
                filter_rules.put("byDevice", true);
                filter_rules.put("byDeviceValue", new ArrayList<>(Arrays.asList(deviceValue)));
            }
            if(referrerValue != null)
            {
                filter_rules.put("byReferrer", true);
                filter_rules.put("byReferrerValue", new ArrayList<>(Arrays.asList(referrerValue)));
            }

            PrimaryController.resetData();

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

    private final Integer MAX_COMBOBOXES = 3;
    private static final ObservableList<Integer> httpCodes = FXCollections.observableArrayList(
        100, 101, 102, 103,
        200, 201, 202, 203, 204, 205, 206, 207, 208, 226,
        300, 301, 302, 303, 304, 305, 306, 307, 308,
        400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 421, 422, 423, 424, 425, 426, 428, 429, 431, 451,
        500, 501, 502, 503, 504, 505, 506, 507, 508, 510, 511
    );
    
    public static final void resetData() {
        FilterController.ipAddressFieldBackup = null;
        FilterController.timestampFromDateBackup = null;
        FilterController.timestampFromTimeBackup = null;
        FilterController.timestampToDateBackup = null;
        FilterController.timestampToTimeBackup = null;
        FilterController.countryFieldBackup = null;
        FilterController.regionFieldBackup = null;
        FilterController.cityFieldBackup = null;
        FilterController.requestMethodFlowPaneBackup = null;
        FilterController.userFieldBackup = null;
        FilterController.responseStatusCodeVBoxBackup = null;
        FilterController.bytesSizeMinFieldBackup = null;
        FilterController.bytesSizeMaxFieldBackup = null;
        FilterController.bytesSizeRangeSliderBackup = null;
        FilterController.requestURLFieldBackup = null;
        FilterController.osFieldBackup = null;
        FilterController.browserFieldBackup = null;
        FilterController.deviceFieldBackup = null;
        FilterController.referrerFieldBackup = null;
    }

    @FXML
    public void initialize() {
        ipAddressField.textProperty().addListener((obs, oldText, newText) -> validateipAddressField());

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

        timestampFromTime.disableProperty().addListener((obs, oldValue, newValue) -> validateTimestampFromTime());
        timestampToTime.disableProperty().addListener((obs, oldValue, newValue) -> validateTimestampToTime());

        timestampFromDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && timestampToDate.getValue() != null && newDate.isAfter(timestampToDate.getValue())) {
                timestampFromDate.setValue(oldDate);
            }
            else
            {
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
            }
        });

        timestampToDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && timestampFromDate.getValue() != null && newDate.isBefore(timestampFromDate.getValue())) {
                timestampToDate.setValue(oldDate);
            }
            else
            {
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
            }
        });

        timestampFromDate.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if(newText != null && newText.toString().length() > 0)
            {
                try {
                    LocalDate parsed = timestampFromDate.getConverter().fromString(newText);
                    if(parsed != null) {
                        if(!parsed.equals(timestampToDate.getValue()))
                        {
                            timestampFromDate.setValue(parsed);
                        }
                        else
                        {
                            timestampFromTime.setDisable(false);
                        }
                    }
                    else
                    {
                        timestampFromTime.setDisable(true);
                    }
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
                    if(parsed != null) {
                        if(!parsed.equals(timestampToDate.getValue()))
                        {
                            timestampToDate.setValue(parsed);
                        }
                        else
                        {
                            timestampToTime.setDisable(false);
                        }
                    }
                    else
                    {
                        timestampToTime.setDisable(true);
                    }
                } catch (Exception e) {
                    timestampToTime.setDisable(true);
                }
            }
            else
            {
                timestampToTime.setDisable(true);
            }
        });

        if(ipAddressFieldBackup != null) ipAddressField.setText(ipAddressFieldBackup);
        if(timestampFromDateBackup != null) timestampFromDate.setValue(timestampFromDateBackup);
        if(timestampFromTimeBackup != null) timestampFromTime.setText(timestampFromTimeBackup);
        if(timestampToDateBackup != null) timestampToDate.setValue(timestampToDateBackup);
        if(timestampToTimeBackup != null) timestampToTime.setText(timestampToTimeBackup);
        if(countryFieldBackup != null) countryField.setText(countryFieldBackup);
        if(regionFieldBackup != null) regionField.setText(regionFieldBackup);
        if(cityFieldBackup != null) cityField.setText(cityFieldBackup);
        if(requestMethodFlowPaneBackup != null) {
            for (Node node : requestMethodFlowPane.getChildren()) {
                if (node instanceof ToggleButton) {
                    ToggleButton toggle = (ToggleButton) node;
                    if(requestMethodFlowPaneBackup.contains(toggle.getText().trim()))
                    {
                        toggle.setSelected(true);
                    }
                }
            }
        }
        if(userFieldBackup != null) userField.setText(userFieldBackup);
        if(responseStatusCodeVBoxBackup != null) {
            for(Integer e: responseStatusCodeVBoxBackup)
            {
                this.addResponseCodeBox(e);
            }
        }
        else
        {
            this.addResponseCodeBox(null);
        }
        if(bytesSizeMinFieldBackup != null) bytesSizeMinField.setText(bytesSizeMinFieldBackup);
        if(bytesSizeMaxFieldBackup != null) bytesSizeMaxField.setText(bytesSizeMaxFieldBackup);
        if(bytesSizeRangeSliderBackup != null) {
            bytesSizeRangeSlider.setLowValue(Double.valueOf(bytesSizeRangeSliderBackup.getKey()));
            bytesSizeRangeSlider.setHighValue(Double.valueOf(bytesSizeRangeSliderBackup.getValue()));
        }
        if(requestURLFieldBackup != null) requestURLField.setText(requestURLFieldBackup);
        if(osFieldBackup != null) osField.setText(osFieldBackup);
        if(browserFieldBackup != null) browserField.setText(browserFieldBackup);
        if(deviceFieldBackup != null) deviceField.setText(deviceFieldBackup);
        if(referrerFieldBackup != null) referrerField.setText(referrerFieldBackup);
    }

    public void onAddResponseStatusCodeBox() {
        if (responseStatusCodeVBox.getChildren().size() < MAX_COMBOBOXES) {
            this.addResponseCodeBox(null);
        }
        if (responseStatusCodeVBox.getChildren().size() >= MAX_COMBOBOXES) {
            addResponseStatusCodeButton.setDisable(true);
        }
    }

    private void addResponseCodeBox(Integer val) {
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

        if(val != null) comboBox.setValue(val);

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

    private boolean validateipAddressField() {
        if(ipAddressField.isDisable()) {
            clearError(ipAddressField, ipAddressFieldErrorLabel);
            return true;
        }
        try {
            String ipAddressStr = ipAddressField.getText().trim();
            ipAddressStr = ipAddressStr == null ? "" : ipAddressStr;
            if(ipAddressStr.length() == 0)
            {
                clearError(ipAddressField, ipAddressFieldErrorLabel);
                return true;
            }
            String regex = "^" + "(?<RemoteIp>-|(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){3})" + "$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ipAddressStr);
            if(matcher.matches())
            {
                clearError(ipAddressField, ipAddressFieldErrorLabel);
                return true;
            }
            else
            {
                showError(ipAddressField, ipAddressFieldErrorLabel);
                return false;
            }
        } catch (Exception e) {
            showError(ipAddressField, ipAddressFieldErrorLabel);
            return false;
        }
    }

    private boolean validateTimestampFromTime() {
        if(timestampFromTime.isDisable()) {
            clearError(timestampFromTime, timestampFromTimeErrorLabel);
            return true;
        }
        try {
            String timeFromStr = timestampFromTime.getText().trim();
            timeFromStr = timeFromStr == null ? "" : timeFromStr;
            if(timeFromStr.length() == 0)
            {
                clearError(timestampFromTime, timestampFromTimeErrorLabel);
                return true;
            }
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

            if(timeFrom == null) {
                showError(timestampFromTime, timestampFromTimeErrorLabel);
                return false;
            }

            String timeToStr = timestampToTime.isDisable() ? null : timestampToTime.getText().trim();
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
            String timeToStr = timestampToTime.getText().trim();
            timeToStr = timeToStr == null ? "" : timeToStr;
            if(timeToStr.length() == 0)
            {
                clearError(timestampToTime, timestampToTimeErrorLabel);
                return true;
            }
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
            
            if(timeTo == null) {
                showError(timestampToTime, timestampToTimeErrorLabel);
                return false;
            }

            String timeFromStr = timestampFromTime.isDisable() ? null : timestampFromTime.getText().trim();
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
            String minText = bytesSizeMinField.getText().trim();
            long min;
            if(minText.length() > 0)
            {
                min = Long.parseLong(minText);
            }
            else
            {
                min = 0;
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
            String maxText = bytesSizeMaxField.getText().trim();
            long max;
            if(maxText.length() > 0)
            {
                max = Long.parseLong(maxText);
            }
            else
            {
                max = 2147483647;
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