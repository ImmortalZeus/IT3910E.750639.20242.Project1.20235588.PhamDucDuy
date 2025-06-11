package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import models.logData.logData;
import models.mongoDB.mongoDB;
import models.mongoDB.mongoDBParseHistory;
import models.utils.dateToUTC;
import models.utils.hashMapCloner;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrimaryController implements DataReceiver<HashMap<String, Object>> {
    private static Integer currentPage = -1;
    private static Integer maxPage = -1;
    private static Integer entriesCount = -1;
    private static Integer rowsPerPage = 1000;
    private static HashMap<String, Object> filter_rules = new HashMap<>();
    private static mongoDB mongodb = new mongoDB();

    @FXML private TableView<logData> logTable;
    @FXML private TableView<?> statusTable;
    @FXML private TableColumn<logData, String> indexColumn;
    @FXML private TableColumn<logData, String> dateTimeColumn;
    @FXML private TableColumn<logData, String> ipColumn;
    @FXML private TableColumn<logData, String> userColumn;
    @FXML private TableColumn<logData, String> methodColumn;
    @FXML private TableColumn<logData, String> requestURLColumn;
    @FXML private TableColumn<logData, String> statusCodeColumn;
    @FXML private TableColumn<logData, String> bytesColumn;
    @FXML private TableColumn<logData, String> referrerColumn;
    @FXML private TableColumn<logData, String> countryColumn;
    @FXML private TableColumn<logData, String> regionColumn;
    @FXML private TableColumn<logData, String> cityColumn;
    @FXML private TableColumn<logData, String> browserColumn;
    @FXML private TableColumn<logData, String> osColumn;
    @FXML private TableColumn<logData, String> deviceColumn;
    @FXML private TableColumn<logData, String> agentColumn;

    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    @FXML private Label pageNumText;
    @FXML private Button prevButton;
    @FXML private Button nextButton;

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
    
    public static final void resetData() {
        PrimaryController.currentPage = -1;
        PrimaryController.maxPage = -1;
        PrimaryController.entriesCount = -1;
        PrimaryController.rowsPerPage = 1000;
        PrimaryController.filter_rules = new HashMap<>();
    }

    // Placeholder until filtering is wired to backend
    protected static HashMap<String, Object> prepareData(HashMap<String, Object> fr /* filter_rules */) {
        boolean isFrEqualNull = fr == null;
        if(isFrEqualNull)
        {
            fr = hashMapCloner.deepCopy(PrimaryController.filter_rules);
        }
        try {
            // Simulate log parsing (replace with real logic)
            HashMap<String, Object> data = new HashMap<>();
            
            if(!isFrEqualNull || PrimaryController.currentPage.equals(-1)) PrimaryController.currentPage = 0;
            if(!isFrEqualNull || PrimaryController.entriesCount.equals(-1)) PrimaryController.entriesCount = PrimaryController.mongodb.count(fr);
            if(!isFrEqualNull || PrimaryController.maxPage.equals(-1)) PrimaryController.maxPage = (int) (Math.ceil(((double)(PrimaryController.entriesCount)) / ((double)(PrimaryController.rowsPerPage))));
            
            if(entriesCount <= 0) return (new HashMap<String, Object>());

            ObservableList<logData> logTableData = PrimaryController.mongodb.filterWithSkipAndLimit(fr, PrimaryController.currentPage, PrimaryController.rowsPerPage).into(FXCollections.observableArrayList());
            data.put("logTableData", logTableData);


            Map<String, Integer> countryData = new HashMap<>();
            ArrayList<Document> countryAgg = PrimaryController.mongodb.aggregate(fr, "countryShort");
            for (Document doc : countryAgg) {
                Object key = doc.get("_id");
                Integer count = doc.getInteger("count", 0);
                countryData.put(key != null ? key.toString() : "-", count);
            }

            data.put("countryData", countryData);
            
            
            Map<Integer, Integer> responseStatusData = new HashMap<>();
            ArrayList<Document> responseStatusAgg = PrimaryController.mongodb.aggregate(fr, "responseStatusCode");
            for (Document doc : responseStatusAgg) {
                Object key = doc.get("_id");
                Integer count = doc.getInteger("count", 0);
                responseStatusData.put((Integer)key, count);
            }

            data.put("responseStatusData", responseStatusData);
            
            try {
                Integer SEGMENT_COUNT = Math.min(8, PrimaryController.entriesCount);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.ENGLISH);
                Date minDate = (Date) PrimaryController.mongodb.getMin(fr, "time").get("min");
                Date maxDate = (Date) PrimaryController.mongodb.getMax(fr, "time").get("max");
                minDate = Date.from(minDate.toInstant().truncatedTo(ChronoUnit.SECONDS));
                maxDate = Date.from(maxDate.toInstant().plusSeconds(1).truncatedTo(ChronoUnit.SECONDS));
                
                Instant minDateInstant = minDate.toInstant();
                Instant maxDateInstant = maxDate.toInstant();
                
                Duration totalDuration = Duration.between(minDateInstant, maxDateInstant);

                while(SEGMENT_COUNT > 0) {
                    try {
                        ArrayList<SimpleEntry<String, Integer>> lineChartData = new ArrayList<>();
                        
                        Duration period = totalDuration.dividedBy(SEGMENT_COUNT);
        
                        List<Date> checkpoints = new ArrayList<>();
                        checkpoints.add((minDate));
                        for (Integer i = 1; i < SEGMENT_COUNT; i++) {
                            checkpoints.add((Date.from(minDateInstant.plus(period.multipliedBy(i)).truncatedTo(ChronoUnit.SECONDS))));
                        }
                        checkpoints.add((maxDate));
        
        
                        ArrayList<Document> bucketRes = PrimaryController.mongodb.bucket(fr, "time", checkpoints);
        
                        Map<Object, Integer> bucketCountsMap = new HashMap<>();
                        for (Document doc : bucketRes) {
                            bucketCountsMap.put(doc.get("_id"), doc.getInteger("count"));
                        }
        
                        for (Integer i = 0; i < checkpoints.size() - 1; i++) {
                            Date bucketStart = checkpoints.get(i);
                            bucketCountsMap.putIfAbsent(bucketStart, 0);
                        }
        
                        for(Integer i = 0; i < checkpoints.size() - 1; i++)
                        {
                            SimpleEntry<String, Integer> entry  = new SimpleEntry<>("From: " + new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(checkpoints.get(i)) + '\n' + "To: " + new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(checkpoints.get(i + 1)), bucketCountsMap.get(checkpoints.get(i)));
                            lineChartData.add(entry);
                        }
                        data.put("lineChartData", lineChartData);
                        break;
                    } catch (Exception e2) {
                    }
                    SEGMENT_COUNT -= 1;
                }
            } catch (Exception e) {
            }

            data.put("filter_rules", fr);

            data.put("currentPage", PrimaryController.currentPage);
            data.put("maxPage", PrimaryController.maxPage);
            data.put("entriesCount", PrimaryController.entriesCount);
            data.put("rowsPerPage", PrimaryController.rowsPerPage);

            return data;
        } catch (Exception e) {
        }
        return (new HashMap<String, Object>());
    }
    @FXML
    private void onDashboardButtonPressed() {
        Platform.runLater(() -> {
            main.App.showLoadingStage(null);
            Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
                @Override
                protected HashMap<String, Object> call() throws Exception {
                    return PrimaryController.prepareData(null);
                }
            };
    
            fetchDataTask.setOnSucceeded(v2 -> {
                HashMap<String, Object> data = fetchDataTask.getValue();
                Platform.runLater(() -> {
                    main.App.closeLoadingStage();
                    Platform.runLater(() -> {
                        main.App.switchToDashboard(data);
                    });
                });
            });
    
            Thread thread = new Thread(fetchDataTask);
            thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
            thread.start();
        });
    }

    @FXML
    private void onHistoryButtonPressed() {
        HashMap<String, Object> historyData = new HashMap<>();
        historyData.put("collectionHistory", PrimaryController.mongodb.getHistory().into(new ArrayList<mongoDBParseHistory>()));
        main.App.switchToHistory(historyData);
    }
    
    @FXML
    private void onExplorerButtonPressed() {
        main.App.switchToExplorer(null);
    }

    @FXML
    private void onFilterButtonPressed() {
        main.App.openFilterStage(null);
    }
    @FXML
    private void onClearFilterButtonPressed() {
        Platform.runLater(() -> {
            PrimaryController.resetData();
            FilterController.resetData();
            
            main.App.showLoadingStage(null);
            Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
                @Override
                protected HashMap<String, Object> call() throws Exception {
                    return PrimaryController.prepareData(null);
                }
            };
    
            fetchDataTask.setOnSucceeded(v2 -> {
                HashMap<String, Object> data = fetchDataTask.getValue();
                Platform.runLater(() -> {
                    main.App.closeLoadingStage();
                    Platform.runLater(() -> {
                        main.App.switchToDashboard(data);
                    });
                });
            });
    
            Thread thread = new Thread(fetchDataTask);
            thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
            thread.start();
        });
    }
    @FXML
    private void onPrevButtonPressed() {
        // System.out.println("Prev button pressed.");
        PrimaryController.currentPage = PrimaryController.maxPage.equals(0) ? 0 : (PrimaryController.currentPage - 1 + PrimaryController.maxPage) % PrimaryController.maxPage;
        ObservableList<logData> x = PrimaryController.mongodb.filterWithSkipAndLimit(PrimaryController.filter_rules, PrimaryController.currentPage * 1000, PrimaryController.rowsPerPage).into(FXCollections.observableArrayList());
        logTable.setItems(x);
        pageNumText.setText("Found : " + String.valueOf(PrimaryController.entriesCount) + " entries" + "  |  " + "Page : " + String.valueOf(PrimaryController.maxPage.equals(0) ? 0 : PrimaryController.currentPage + 1) + "/" + String.valueOf(PrimaryController.maxPage));
    }
    @FXML
    private void onNextButtonPressed() {
        // System.out.println("Next button pressed.");
        PrimaryController.currentPage = PrimaryController.maxPage.equals(0) ? 0 : (PrimaryController.currentPage + 1 + PrimaryController.maxPage) % PrimaryController.maxPage;
        ObservableList<logData> x = PrimaryController.mongodb.filterWithSkipAndLimit(PrimaryController.filter_rules, PrimaryController.currentPage * 1000, PrimaryController.rowsPerPage).into(FXCollections.observableArrayList());
        logTable.setItems(x);
        pageNumText.setText("Found : " + String.valueOf(PrimaryController.entriesCount) + " entries" + "  |  " + "Page : " + String.valueOf(PrimaryController.maxPage.equals(0) ? 0 : PrimaryController.currentPage + 1) + "/" + String.valueOf(PrimaryController.maxPage));
    }

    public void setData(HashMap<String, Object> data) {
        Platform.runLater(() -> {
            main.App.showLoadingStage(null);
            Object tmp_filter_rules = data.get("filter_rules");
            if(tmp_filter_rules != null && tmp_filter_rules instanceof HashMap<?, ?> map)
            {
                if(map.keySet().stream().allMatch(key -> key == null || key instanceof String) && map.values().stream().allMatch(value -> value == null || value instanceof Object))
                {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> filter_rules = (HashMap<String, Object>) tmp_filter_rules;
                    
                    PrimaryController.filter_rules = hashMapCloner.deepCopy(filter_rules);
                }
            }

            Object tmp_currentPage = data.get("currentPage");
            if(tmp_currentPage != null && tmp_currentPage instanceof Integer)
            {
                PrimaryController.currentPage = (Integer) tmp_currentPage;
            }

            Object tmp_maxPage = data.get("maxPage");
            if(tmp_maxPage != null && tmp_maxPage instanceof Integer)
            {
                PrimaryController.maxPage = (Integer) tmp_maxPage;
            }

            Object tmp_entriesCount = data.get("entriesCount");
            if(tmp_entriesCount != null && tmp_entriesCount instanceof Integer)
            {
                PrimaryController.entriesCount = (Integer) tmp_entriesCount;
            }
        
            Object tmp_rowsPerPage = data.get("rowsPerPage");
            if(tmp_rowsPerPage != null && tmp_rowsPerPage instanceof Integer)
            {
                PrimaryController.rowsPerPage = (Integer) tmp_rowsPerPage;
            }

            Object tmp_logTableData = data.get("logTableData");
            if(tmp_logTableData != null && tmp_logTableData instanceof ObservableList<?>)
            {
                ObservableList<?> tmp_logTableData2 = (ObservableList<?>) tmp_logTableData;

                if(tmp_logTableData2.stream().allMatch(item -> item == null || item instanceof logData))
                {
                    @SuppressWarnings("unchecked")
                    ObservableList<logData> logTableData = (ObservableList<logData>) tmp_logTableData2;
                    logTable.setItems(logTableData);
                }
            }

            Object tmp_countryData = data.get("countryData");
            if(tmp_countryData != null && tmp_countryData instanceof HashMap<?, ?> map)
            {
                if(map.keySet().stream().allMatch(key -> key == null || key instanceof String) && map.values().stream().allMatch(value -> value == null || value instanceof Integer))
                {
                    @SuppressWarnings("unchecked")
                    Map<String, Integer> countryData = (Map<String, Integer>) tmp_countryData;

                    Integer totalRequests = countryData.values().stream().mapToInt(Integer::intValue).sum();

                    if(totalRequests > 0)
                    {
                        ObservableList<PieChart.Data> pieChartCountryData = FXCollections.observableArrayList();
    
                        for (Map.Entry<String, Integer> entry : countryData.entrySet()) {
                            String country = entry.getKey();
                            Integer requests = entry.getValue();
                            double percentage = (requests * 100.0) / totalRequests;
    
                            String label = (country == null ? "Undefined" : country) + " (" + String.format("%.1f", percentage) + "%)";
                            pieChartCountryData.add(new PieChart.Data(label, requests));
                        }
    
                        pieChartCountry.setData(pieChartCountryData);
    
                        Platform.runLater(() -> {
                            for (PieChart.Data piedata : pieChartCountryData) {
                                String country = piedata.getName();
                                double percent = (piedata.getPieValue() / totalRequests) * 100;
                                Integer actualCount = Double.valueOf(piedata.getPieValue()).intValue();
        
                                // Create a styled label as a tooltip
                                Label tooltipLabel = new Label(String.format("Country: %s\nPercentage: %.1f%%\nCount: %d", country, percent, actualCount));
                                tooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
                                tooltipLabel.getStyleClass().add("piechart-tooltip");
                                Popup popup = new Popup();
                                popup.getContent().add(tooltipLabel);
                                popup.setAutoHide(true);
        
                                FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.millis(200), tooltipLabel);
                                fadeIn.setFromValue(0);
                                fadeIn.setToValue(1);
        
                                // Show tooltip on hover
                                piedata.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                                    tooltipLabel.setOpacity(0);
                                    popup.show(piedata.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                                    fadeIn.playFromStart();
                                });
        
                                // Hide tooltip on exit
                                piedata.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
                            }
                        });
                    }
                }
            }

            Object tmp_responseStatusData = data.get("responseStatusData");
            if(tmp_responseStatusData != null && tmp_responseStatusData instanceof HashMap<?, ?> map)
            {
                if(map.keySet().stream().allMatch(key -> key == null || key instanceof Integer) && map.values().stream().allMatch(value -> value == null || value instanceof Integer))
                {
                    @SuppressWarnings("unchecked")
                    Map<Integer, Integer> responseStatusData = (Map<Integer, Integer>) tmp_responseStatusData;

                    Integer totalResponses = responseStatusData.values().stream().mapToInt(Integer::intValue).sum();
           
                    if(totalResponses > 0)
                    {
                        ObservableList<PieChart.Data> pieChartResponseStatusData = FXCollections.observableArrayList();
    
                        for (Map.Entry<Integer, Integer> entry : responseStatusData.entrySet()) {
                            Integer status = entry.getKey();
                            Integer count = entry.getValue();
                            double percentage = (count * 100.0) / totalResponses;
    
                            String label = status + " (" + String.format("%.1f", percentage) + "%)";
                            pieChartResponseStatusData.add(new PieChart.Data(label, count));
                        }
    
                        pieChartResponseStatus.setData(pieChartResponseStatusData);
    
                        Platform.runLater(() -> {
                            for (PieChart.Data piedata : pieChartResponseStatus.getData()) {
                                String status = piedata.getName();
                                double percent = (piedata.getPieValue() / totalResponses) * 100;
                                Integer actualCount = Double.valueOf(piedata.getPieValue()).intValue();
        
                                Label restooltipLabel = new Label(String.format("Status Code: %s\nPercentage: %.1f%%\nCount: %d", status, percent, actualCount));
                                restooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
                                restooltipLabel.getStyleClass().add("piechart-tooltip");
                                Popup popup = new Popup();
                                popup.getContent().add(restooltipLabel);
                                popup.setAutoHide(true);
        
                                FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.millis(200), restooltipLabel);
                                fadeIn.setFromValue(0);
                                fadeIn.setToValue(1);
        
                                piedata.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                                    restooltipLabel.setOpacity(0);
                                    popup.show(piedata.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                                    fadeIn.playFromStart();
                                });
        
                                piedata.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
                            }
                        });
                    }
                }
            }

            Object tmp_lineChartData = data.get("lineChartData");
            if(tmp_lineChartData != null && tmp_lineChartData instanceof ArrayList<?> list)
            {
                if(list.stream().allMatch(item -> item instanceof SimpleEntry<?, ?> &&
                            (((SimpleEntry<?, ?>) item).getKey() == null || ((SimpleEntry<?, ?>) item).getKey() instanceof String) &&
                            (((SimpleEntry<?, ?>) item).getValue() == null || ((SimpleEntry<?, ?>) item).getValue() instanceof Integer)))
                {
                    @SuppressWarnings("unchecked")
                    ArrayList<SimpleEntry<String, Integer>> lineChartData = (ArrayList<SimpleEntry<String, Integer>>) tmp_lineChartData;

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    Integer minCnt = -1;
                    Integer maxCnt = -1;
                    for (SimpleEntry<String, Integer> entry : lineChartData) {
                        String label = entry.getKey();
                        Integer cnt = entry.getValue();

                        minCnt = minCnt.equals(-1) ? cnt : Math.min(minCnt, cnt);
                        maxCnt = minCnt.equals(-1) ? cnt : Math.max(maxCnt, cnt);

                        series.getData().add(new XYChart.Data<>(label, cnt));
                    }

                    yAxis.setAutoRanging(false);
                    Integer delta = maxCnt - minCnt;
                    if(delta > 1000)
                    {
                        delta = 1000;
                    }
                    else if (delta > 500)
                    {
                        delta = 500;
                    }
                    else if (delta > 100)
                    {
                        delta = 100;
                    }
                    else
                    {
                        delta = 20;
                    }
                    Integer lowerBound = Math.max(0, minCnt - delta);
                    Integer upperBound = Math.max(0, maxCnt + delta);
                    yAxis.setLowerBound(lowerBound);
                    yAxis.setUpperBound(upperBound);
                    yAxis.setTickUnit(Math.max(((upperBound) - (lowerBound)) / 15, 10));

                    lineChart.getData().add(series);

                    Platform.runLater(() -> {
                        // Loop through each data point
                        for (XYChart.Series<String, Number> series2 : lineChart.getData()) {
                            for (XYChart.Data<String, Number> dataPoint : series2.getData()) {
                                Label linecharttooltipLabel = new Label(dataPoint.getXValue() + "\n" + "Count: " + dataPoint.getYValue().toString());
                                linecharttooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
                                linecharttooltipLabel.getStyleClass().add("piechart-tooltip");
                                Popup popup = new Popup();
                                popup.getContent().add(linecharttooltipLabel);
                                popup.setAutoHide(true);

                                FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.millis(200), linecharttooltipLabel);
                                fadeIn.setFromValue(0);
                                fadeIn.setToValue(1);
        
                                dataPoint.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                                    linecharttooltipLabel.setOpacity(0);
                                    popup.show(dataPoint.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                                    fadeIn.playFromStart();
                                });
        
                                dataPoint.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
                            }
                        }
                    });
                }
            }

            pageNumText.setText("Found : " + String.valueOf(PrimaryController.entriesCount) + " entries" + "  |  " + "Page : " + String.valueOf(PrimaryController.maxPage.equals(0) ? 0 : PrimaryController.currentPage + 1) + "/" + String.valueOf(PrimaryController.maxPage));
            Platform.runLater(() -> {
                main.App.closeLoadingStage();
            });
        });
    }

    @FXML
    private PieChart pieChartCountry;

    @FXML
    private PieChart pieChartResponseStatus;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    public void initialize() {
        pieChartCountry.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        
        pieChartResponseStatus.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());

        lineChart.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        lineChart.getStyleClass().add("custom-line-chart");

        pieChartCountry.setLegendVisible(false);
        pieChartResponseStatus.setLegendVisible(false);
        lineChart.setLegendVisible(false);

        // logTable.getSelectionModel().setCellSelectionEnabled(true);
        logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        logTable.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        logTable.getStyleClass().add("log-table");

        indexColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getIndex())));
        dateTimeColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getTime() == null ? "null" : (new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss")).format(cell.getValue().getTime()))));
        ipColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRemoteIp())));
        userColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRemoteUser())));
        methodColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRequestMethod())));
        requestURLColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf((cell.getValue().getRequestUrl()))));
        statusCodeColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getResponseStatusCode())));
        bytesColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getBytes())));
        referrerColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getReferrer())));
        countryColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCountryLong())));
        regionColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRegion())));
        cityColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCity())));
        browserColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getBrowser())));
        osColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getOS())));
        deviceColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getDevice())));
        agentColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAgent())));
    }

    // private void printAllNodes(Parent parent) {
    //     for (Node node : parent.getChildrenUnmodifiable()) {
    //         System.out.println(node.getClass().getSimpleName() + ": " + node);
    //         if (node instanceof Parent p) {
    //             printAllNodes(p);
    //         }
    //     }
    // }

    // private ScrollBar findVerticalScrollBar(TableView<?> table) {
    //     for (var node : table.lookupAll(".scroll-bar")) {
    //         if (node instanceof ScrollBar sb && sb.getOrientation().equals(Orientation.VERTICAL)) {
    //             return sb;
    //         }
    //     }
    //     return null;
    // }

    

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
