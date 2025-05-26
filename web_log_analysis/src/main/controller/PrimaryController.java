package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.animation.FadeTransition;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;
import models.logData.logData;
import models.mongoDB.mongoDB;
import org.bson.Document;

import com.mongodb.client.FindIterable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrimaryController {

    @FXML private TableView<logData> logTable;
    @FXML private TableView<?> statusTable;
    @FXML private TableColumn<logData, Integer> indexColumn;
    @FXML private TableColumn<logData, String> dateColumn;
    @FXML private TableColumn<logData, String> timeColumn;
    @FXML private TableColumn<logData, String> ipColumn;
    @FXML private TableColumn<logData, String> userColumn;
    @FXML private TableColumn<logData, String> methodColumn;
    @FXML private TableColumn<logData, String> requestURLColumn;
    @FXML private TableColumn<logData, Integer> statusCodeColumn;
    @FXML private TableColumn<logData, Integer> bytesColumn;
    @FXML private TableColumn<logData, String> referrerColumn;
    @FXML private TableColumn<logData, String> countryColumn;
    @FXML private TableColumn<logData, String> regionColumn;
    @FXML private TableColumn<logData, String> cityColumn;
    @FXML private TableColumn<logData, String> browserColumn;
    @FXML private TableColumn<logData, String> osColumn;
    @FXML private TableColumn<logData, String> deviceColumn;
    @FXML private TableColumn<logData, String> agentColumn;

    @FXML private HBox tableSection;
    // @FXML private TextField searchField;

    // @FXML private DatePicker fromDatePicker;
    // @FXML private DatePicker toDatePicker;

    // @FXML private TextField fromTimeField;
    // @FXML private TextField toTimeField;

    // @FXML private Button applyFilterButton;

    // @FXML private HBox dateFilterBox;
    // @FXML private HBox timeFilterBox;

    // @FXML private HBox locationFilterBox;

    // @FXML private TextField countryField;
    // @FXML private TextField regionField;
    // @FXML private TextField cityField;

    
    // public void bindTableHeightsToScene(Scene scene) {
    //     tableSection.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));}

    // // MenuButton actions
    // @FXML private void onFilterIP() {
    //     resetFilterUI();
    //     showSearchField("Enter IP Address...");
    //     showApplyButton();
    // }
    
    // @FXML private void onTimestamp() {
    //     resetFilterUI();
    //     dateFilterBox.setVisible(true);
    //     dateFilterBox.setManaged(true);
    //     timeFilterBox.setVisible(true);
    //     timeFilterBox.setManaged(true);
    //     showApplyButton();
    // }
    
    // @FXML private void onStatus() {
    //     resetFilterUI();
    //     showSearchField("Enter Status Code...");
    //     showApplyButton();
    // }
    
    // @FXML private void onRequest() {
    //     resetFilterUI();
    //     showSearchField("Enter Request...");
    //     showApplyButton();
    // }
    
    // @FXML private void onSize() {
    //     resetFilterUI();
    //     showSearchField("Enter Size...");
    //     showApplyButton();
    // }
    
    // @FXML private void onReferrer() {
    //     resetFilterUI();
    //     showSearchField("Enter Referrer...");
    //     showApplyButton();
    // }
    
    // @FXML private void onFilterUserAgent() {
    //     resetFilterUI();
    //     showSearchField("Enter User Agent...");
    //     showApplyButton();
    // }
    
    // @FXML private void onLocation() {
    //     resetFilterUI();
    //     locationFilterBox.setVisible(true);
    //     locationFilterBox.setManaged(true);
    //     showApplyButton();
    // }
    
    // // Show a standard search field
    // private void showSearchField(String prompt) {
    //     searchField.setPromptText(prompt);
    //     searchField.setVisible(true);
    //     searchField.setManaged(true);
    //     searchField.requestFocus();
    // }

    // // Hide all other filters
    // private void resetFilterUI() {
    //     searchField.clear();
    //     searchField.setVisible(false);
    //     searchField.setManaged(false);
    
    //     fromDatePicker.setValue(null);
    //     toDatePicker.setValue(null);
    //     dateFilterBox.setVisible(false);
    //     dateFilterBox.setManaged(false);
    
    //     fromTimeField.clear();
    //     toTimeField.clear();
    //     timeFilterBox.setVisible(false);
    //     timeFilterBox.setManaged(false);
    
    //     countryField.clear();
    //     regionField.clear();
    //     cityField.clear();
    //     locationFilterBox.setVisible(false);
    //     locationFilterBox.setManaged(false);
    
    //     applyFilterButton.setVisible(false);
    //     applyFilterButton.setManaged(false);
    // }
    
    // private void showApplyButton() {
    //     applyFilterButton.setVisible(true);
    //     applyFilterButton.setManaged(true);
    // }
    

    // Placeholder until filtering is wired to backend
    @FXML
    private void onDashboardButtonPressed() {
        main.App.switchToDashboard();
    }

    @FXML
    private void onStreamButtonPressed() {
        main.App.switchToStream();
    }
    
    @FXML
    private void onExplorerButtonPressed() {
        main.App.switchToExplorer();
    }

    @FXML
    private void onFilterButtonPressed() {
        main.App.openFilterStage();
    }
    @FXML
    private void onRefreshButtonPressed() {
        // resetFilterUI();
        System.out.println("Filters cleared. Refreshed.");
    }
    @FXML
    private void onLeftButtonPressed() {
        System.out.println("Left button pressed.");
    }
    @FXML
    private void onRightButtonPressed() {
        System.out.println("Right button pressed.");
    }

    @FXML
    private PieChart pieChartCountry;

    @FXML
    private PieChart pieChartResponseStatus;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    public void initialize() {

        mongoDB mongodb = new mongoDB();

        Map<String, Integer> countryData = new HashMap<String, Integer>();
        ArrayList<Document> tmp = mongodb.aggregate("countryShort");
        for (Document doc : tmp) {
            Object key = doc.get("_id");
            int count = doc.getInteger("count", 0);
            countryData.put(key != null ? key.toString() : "-", count);
        }
        if (countryData == null || countryData.isEmpty()) return;

        int totalRequests = countryData.values().stream().mapToInt(Integer::intValue).sum();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : countryData.entrySet()) {
            String country = entry.getKey();
            int requests = entry.getValue();
            double percentage = (requests * 100.0) / totalRequests;

            String label = (country.equals("-") ? "Undefined" : country) + " (" + String.format("%.1f", percentage) + "%)";
            pieChartData.add(new PieChart.Data(label, requests));
        }

        pieChartCountry.setData(pieChartData);

        for (PieChart.Data data : pieChartCountry.getData()) {
            String country = data.getName();
            double percent = (data.getPieValue() / totalRequests) * 100;
            int actualCount = (int) data.getPieValue();

            // Create a styled label as a tooltip
            Label tooltipLabel = new Label(String.format("Country: %s\nPercentage: %.1f%%\nCount: %d", country, percent, actualCount));
            tooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            tooltipLabel.getStyleClass().add("piechart-tooltip");
            Popup popup = new Popup();
            popup.getContent().add(tooltipLabel);
            popup.setAutoHide(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), tooltipLabel);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Show tooltip on hover
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                tooltipLabel.setOpacity(0);
                popup.show(data.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                fadeIn.playFromStart();
            });

            // Hide tooltip on exit
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
        }

        Map<Integer, Integer> responseData = new HashMap<Integer, Integer>();
        ArrayList<Document> responseAgg = mongodb.aggregate("responseStatusCode");
        for (Document doc : responseAgg) {
            Object key = doc.get("_id");
            int count = doc.getInteger("count", 0);
            responseData.put((Integer)key, count);
        }
        if (responseData == null || responseData.isEmpty()) return;
        
        int totalResponses = responseData.values().stream().mapToInt(Integer::intValue).sum();
        
        ObservableList<PieChart.Data> responsePieChartData = FXCollections.observableArrayList();

        for (Map.Entry<Integer, Integer> entry : responseData.entrySet()) {
            int status = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalResponses;

            String label = status + " (" + String.format("%.1f", percentage) + "%)";
            responsePieChartData.add(new PieChart.Data(label, count));
        }

        pieChartResponseStatus.setData(responsePieChartData);

        for (PieChart.Data data : pieChartResponseStatus.getData()) {
            String status = data.getName();
            double percent = (data.getPieValue() / totalResponses) * 100;
            int actualCount = (int) data.getPieValue();

            Label restooltipLabel = new Label(String.format("Status: %s\nPercentage: %.1f%%\nCount: %d", status, percent, actualCount));
            restooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            restooltipLabel.getStyleClass().add("piechart-tooltip");
            Popup popup = new Popup();
            popup.getContent().add(restooltipLabel);
            popup.setAutoHide(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), restooltipLabel);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                restooltipLabel.setOpacity(0);
                popup.show(data.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                fadeIn.playFromStart();
            });

            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
        }
    
        indexColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getIndex()).asObject());
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTime()));
        timeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTime()));
        ipColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRemoteIp()));
        userColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRemoteUser()));
        methodColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRequestMethod()));
        requestURLColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRequestUrl()));
        statusCodeColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getResponseStatusCode()).asObject());
        bytesColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getBytes()).asObject());
        referrerColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReferrer()));
        countryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCountryLong()));
        regionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRegion()));
        cityColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCity()));
        browserColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBrowser()));
        osColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOS()));
        deviceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDevice()));
        agentColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAgent()));

        HashMap<String, Object> filter_rules = new HashMap<String, Object>();

        filter_rules.put("byIndex", true);
        Map<String, Integer> filterIndexMap = new HashMap<String, Integer>()
        {
            {
                put("byIndexStartValue", 1);
                put("byIndexEndValue", 500);
            }
        };
        List<Map<String, Integer>> arr = List.of(filterIndexMap);
        filter_rules.put("byIndexValue", arr);
        
        long startTime3 = System.nanoTime();
        ObservableList<logData> x = mongodb.filterWithSkipAndLimit(new HashMap<>(), null, null).into(FXCollections.observableArrayList());
        long stopTime3 = System.nanoTime();
        System.out.println(stopTime3 - startTime3);

        logTable.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        logTable.getStyleClass().add("log-table");
        logTable.setItems(x);

        lineChart.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        lineChart.getStyleClass().add("custom-line-chart");


        lineChart.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        lineChart.getStyleClass().add("custom-line-chart");


        // logTable.skinProperty().addListener((obs, oldSkin, newSkin) -> {
        //     ScrollBar verticalBar = findVerticalScrollBar(logTable);
        //     if (verticalBar != null) {
        //         final Integer dataMinIndex = 1;
        //         final Integer dataMaxIndex = Math.toIntExact(mongodb.count(new HashMap<>()));
        //         final ObservableList<logData> logTableItems = logTable.getItems();
        //         final AtomicReference<Integer> firstLogTableIndex = new AtomicReference<>(logTableItems.get(0).getIndex());
        //         final AtomicReference<Integer> lastLogTableIndex = new AtomicReference<>(logTableItems.get(logTableItems.size() - 1).getIndex());
        //         verticalBar.valueProperty().addListener((obs2, oldVal, newVal) -> {
        //             final Integer estimateNewVal = Long.valueOf(Math.round(newVal.doubleValue() / verticalBar.getMax() * (lastLogTableIndex.get() - firstLogTableIndex.get()) + firstLogTableIndex.get())).intValue();
        //             final Integer estimateOldVal = Long.valueOf(Math.round(oldVal.doubleValue() / verticalBar.getMax() * (lastLogTableIndex.get() - firstLogTableIndex.get()) + firstLogTableIndex.get())).intValue();
        //             if((newVal.doubleValue() / verticalBar.getMax() * 100 > 80 && estimateNewVal - estimateOldVal > 1) || (newVal.doubleValue() / verticalBar.getMax() * 100 < 20 && estimateOldVal - estimateNewVal > 1)) {
        //                 final Integer currentVal = estimateNewVal;
        //                 Integer fi = Math.max(dataMinIndex, currentVal - 500);
        //                 Integer se = Math.min(dataMaxIndex, currentVal + 500);

        //                 HashMap<String, Object> filter_rules2 = new HashMap<String, Object>();

        //                 filter_rules2.put("byIndex", true);

        //                 Map<String, Integer> filterIndexMap2 = new HashMap<String, Integer>()
        //                 {
        //                     {
        //                         put("byIndexStartValue", fi);
        //                         put("byIndexEndValue", se);
        //                     }
        //                 };
        //                 List<Map<String, Integer>> arr2 = List.of(filterIndexMap2);
        //                 filter_rules2.put("byIndexValue", arr2);
        //                 ObservableList<logData> x2 = mongodb.filter(filter_rules2).into(FXCollections.observableArrayList());
                        
        //                 logTable.setItems(x2);
        //                 logTableItems.clear();
        //                 logTableItems.addAll(x2);
        //                 firstLogTableIndex.set(logTableItems.get(0).getIndex());
        //                 lastLogTableIndex.set(logTableItems.get(logTableItems.size() - 1).getIndex());
                        
        //                 logTable.scrollTo((currentVal - fi));
        //             }
        //             // final Integer fi = Math.max(Double.valueOf(Math.floor(currentval/1000)).intValue() + 1, dataMinIndex);
        //             // final Integer se = Math.min(Double.valueOf(Math.floor(currentval/1000)).intValue() + 1, dataMaxIndex);
        //             // if (oldVal.doubleValue() < newVal.doubleValue() && newVal.doubleValue() == verticalBar.getMax()) {
        //             //     System.out.println("Scrolled to bottom. Loading more...");
                        
        //             //     final Integer currentIndex = logTable.getItems().get(0).getIndex();

        //             //     HashMap<String, Object> filter_rules2 = new HashMap<String, Object>();

        //             //     filter_rules2.put("byIndex", true);

        //             //     final Integer se = Math.min(currentIndex - 1 + 1000 + 1000, dataMaxIndex);
        //             //     Map<String, Integer> filterIndexMap2 = new HashMap<String, Integer>()
        //             //     {
        //             //         {
        //             //             put("byIndexStartValue", Math.max(se + 1 - 1000, dataMinIndex));
        //             //             put("byIndexEndValue", se);
        //             //         }
        //             //     };
        //             //     List<Map<String, Integer>> arr2 = List.of(filterIndexMap2);
        //             //     filter_rules2.put("byIndexValue", arr2);
        //             //     ObservableList<logData> x2 = mongodb.filter(filter_rules2).into(FXCollections.observableArrayList());
        //             //     logTable.setItems(x2);
        //             //     logTable.scrollTo(0);
        //             // }
        //             // else if (oldVal.doubleValue() > newVal.doubleValue() && newVal.doubleValue() == verticalBar.getMin()) {
        //             //     System.out.println("Scrolled to top. Loading more...");
                        
        //             //     final Integer currentIndex = logTable.getItems().get(0).getIndex();

        //             //     HashMap<String, Object> filter_rules2 = new HashMap<String, Object>();

        //             //     filter_rules2.put("byIndex", true);

        //             //     final Integer fi = Math.max(currentIndex - 1000, dataMinIndex);
        //             //     Map<String, Integer> filterIndexMap2 = new HashMap<String, Integer>()
        //             //     {
        //             //         {
        //             //             put("byIndexStartValue", Math.max(currentIndex - 1000, dataMinIndex));
        //             //             put("byIndexEndValue", Math.min(fi - 1 + 1000, dataMaxIndex));
        //             //         }
        //             //     };
        //             //     List<Map<String, Integer>> arr2 = List.of(filterIndexMap2);
        //             //     filter_rules2.put("byIndexValue", arr2);
        //             //     ObservableList<logData> x2 = mongodb.filter(filter_rules2).into(FXCollections.observableArrayList());
        //             //     logTable.setItems(x2);
        //             //     logTable.scrollTo(logTable.getItems().size() - 1);
        //             // }
        //         });
        //     }
        // });
    }

    private ScrollBar findVerticalScrollBar(TableView<?> table) {
        for (var node : table.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar sb && sb.getOrientation() == Orientation.VERTICAL) {
                return sb;
            }
        }
        return null;
    }

    public static Map<String, Integer> loadDataFromFile(String filePath) {
        Map<String, Integer> data = new HashMap<>();

        try (InputStream input = PrimaryController.class.getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            if (input == null) {
                System.err.println("Error reading data: file not found at " + filePath);
                return null;
            }
            String line = reader.readLine();
            if (line != null) {
                String[] entries = line.split(",");
                for (String entry : entries) {
                    String[] parts = entry.trim().split("=");
                    if (parts.length == 2) {
                        data.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
        return data;
    }

    

    // Apply timestamp filter — placeholder for now
    // @FXML
    // private void onApplyFilterButton() {
    //     String fromTime = normalizeTimeInput(fromTimeField.getText());
    //     String toTime = normalizeTimeInput(toTimeField.getText());

    //     System.out.println("Date Range: " + fromDatePicker.getValue() + " to " + toDatePicker.getValue());
    //     System.out.println("Time Range: " + fromTime + " to " + toTime);
    // }

    // // Ensures time format is HH:mm:ss with fallback values
    // private String normalizeTimeInput(String input) {
    //     if (input == null || input.trim().isEmpty()) return "00:00:00";

    //     String[] parts = input.trim().split(":");
    //     String hour = "00", minute = "00", second = "00";

    //     try {
    //         if (parts.length >= 1) hour = String.format("%02d", Integer.parseInt(parts[0]));
    //         if (parts.length >= 2) minute = String.format("%02d", Integer.parseInt(parts[1]));
    //         if (parts.length == 3) second = String.format("%02d", Integer.parseInt(parts[2]));
    //     } catch (NumberFormatException e) {
    //         System.out.println("Invalid time format. Defaulting to 00:00:00.");
    //     }

    //     return hour + ":" + minute + ":" + second;
    // }
}
