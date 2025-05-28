package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.logData.logData;
import models.mongoDB.mongoDB;
import models.parsers.ResultAggregator;
import models.parsers.fileParsers.apacheFileParser;
import models.parsers.fileParsers.nginxFileParser;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bson.Document;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExplorerController implements DataReceiver<HashMap<String, Object>> {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();
    
    private Integer currentPage = 0;
    private Integer maxPage = 1;
    private Integer rowsPerPage = 1000;
    private mongoDB mongodb = new mongoDB();

    @FXML
    private ImageView hgna;
    @FXML
    private ImageView pdd;

    @FXML
    private Button uploadApacheButton;

    @FXML
    private Button uploadNginxButton;

    @FXML
    public void initialize() {
        Image image_pdd = new Image(Thread.currentThread().getContextClassLoader().getResource("resources/images/pdd.jpg").toExternalForm());
        pdd.setImage(image_pdd);
        Image image_hgna = new Image(Thread.currentThread().getContextClassLoader().getResource("resources/images/hgna.jpg").toExternalForm());
        hgna.setImage(image_hgna);

        uploadApacheButton.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        uploadApacheButton.getStyleClass().add("upload-button");

        uploadNginxButton.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        uploadNginxButton.getStyleClass().add("upload-button");
    }

    private void onUploadButtonPressed(ActionEvent event, Object parser) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Log File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Log Files", "*.log", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Get the current window from the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            
            main.App.showLoadingStage(null); // Show loading screen

            Task<ResultAggregator> parseTask = new Task<>() {
                @Override
                protected ResultAggregator call() throws Exception {
                    try {
                        // Simulate log parsing (replace with real logic)
                        System.out.println("Selected log file: " + selectedFile.getAbsolutePath());
                        if(parser instanceof apacheFileParser)
                        {
                            ResultAggregator res = aFP.parse(selectedFile.getAbsolutePath());
                            return res;
                        }
                        else if(parser instanceof nginxFileParser)
                        {
                            ResultAggregator res = nFP.parse(selectedFile.getAbsolutePath());
                            return res;
                        }
                        // Process and upload the file to backend
                        // Example: BackendService.uploadLogFile(selectedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        main.App.closeLoadingStage();
                    }
                    return null;
                }
            };

            parseTask.setOnSucceeded(v -> {
                ResultAggregator parseTaskValue = parseTask.getValue();

                Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
                    @Override
                    protected HashMap<String, Object> call() throws Exception {
                        try {
                            // Simulate log parsing (replace with real logic)
                            HashMap<String, Object> data = new HashMap<String, Object>();
                            
                            ExplorerController.this.currentPage = 0;
                            ExplorerController.this.maxPage = Double.valueOf(Math.floor(Double.valueOf(ExplorerController.this.mongodb.count(PrimaryController.filter_rules)) / Double.valueOf(ExplorerController.this.rowsPerPage))).intValue();
                            
                            ObservableList<logData> logTableData = ExplorerController.this.mongodb.filterWithSkipAndLimit(PrimaryController.filter_rules, ExplorerController.this.currentPage, ExplorerController.this.rowsPerPage).into(FXCollections.observableArrayList());
                            data.put("logTableData", logTableData);


                            Map<String, Integer> countryData = new HashMap<String, Integer>();
                            ArrayList<Document> countryAgg = ExplorerController.this.mongodb.aggregate(PrimaryController.filter_rules, "countryShort");
                            for (Document doc : countryAgg) {
                                Object key = doc.get("_id");
                                int count = doc.getInteger("count", 0);
                                countryData.put(key != null ? key.toString() : "-", count);
                            }

                            data.put("countryData", countryData);
                            
                            
                            Map<Integer, Integer> responseStatusData = new HashMap<Integer, Integer>();
                            ArrayList<Document> responseStatusAgg = ExplorerController.this.mongodb.aggregate(PrimaryController.filter_rules, "responseStatusCode");
                            for (Document doc : responseStatusAgg) {
                                Object key = doc.get("_id");
                                int count = doc.getInteger("count", 0);
                                responseStatusData.put((Integer)key, count);
                            }

                            data.put("responseStatusData", responseStatusData);
                        
                            try {
                                Map<String, Integer> lineChartData = new HashMap<String, Integer>();
                                final Integer SEGMENT_COUNT = 8;
                                
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.ENGLISH);
                                Date minDate = sdf.parse((String) ExplorerController.this.mongodb.getMin(PrimaryController.filter_rules, "time").get("min"));
                                Date maxDate = sdf.parse((String) ExplorerController.this.mongodb.getMax(PrimaryController.filter_rules, "time").get("max"));;
                                
                                Instant minDateInstant = minDate.toInstant();
                                Instant maxDateInstant = maxDate.toInstant();
                                
                                Duration totalDuration = Duration.between(minDateInstant, maxDateInstant);
                                Duration period = totalDuration.dividedBy(SEGMENT_COUNT);

                                List<Date> checkpoints = new ArrayList<>();
                                checkpoints.add(minDate);
                                for (int i = 1; i < SEGMENT_COUNT; i++) {
                                    checkpoints.add(Date.from(minDateInstant.plus(period.multipliedBy(i))));
                                }
                                checkpoints.add(maxDate);

                                for (int i = 0; i < SEGMENT_COUNT; i++) {
                                    int j = i + 1;
                                    Date start = checkpoints.get(i);
                                    Date end = checkpoints.get(j);
                                    HashMap<String, Object> filter_rules2 = new HashMap<>(PrimaryController.filter_rules);

                                    filter_rules2.put("byPeriod", true);
                                    HashMap<String, Date> byPeriodValueHashMap = new HashMap<>();

                                    byPeriodValueHashMap.put("byPeriodStartValue", start);
                                    byPeriodValueHashMap.put("byPeriodEndValue", end);
                                    
                                    filter_rules2.put("byPeriodValue", Arrays.asList(byPeriodValueHashMap));
                                    Integer cnt = ExplorerController.this.mongodb.count(filter_rules2);
                                    lineChartData.put("Dur " + String.valueOf(j), cnt);

                                    data.put("lineChartData", lineChartData);
                                }
                            } catch (Exception e) {
                            }

                            data.put("filter_rules", PrimaryController.filter_rules);

                            data.put("currentPage", ExplorerController.this.currentPage);
                            data.put("maxPage", ExplorerController.this.maxPage);
                            data.put("rowsPerPage", ExplorerController.this.rowsPerPage);

                            return data;
                            // Process and upload the file to backend
                            // Example: BackendService.uploadLogFile(selectedFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                fetchDataTask.setOnSucceeded(v2 -> {
                    HashMap<String, Object> data = fetchDataTask.getValue();
                    main.App.switchToDashboard(data);
                    main.App.closeLoadingStage();
                });

                Thread thread = new Thread(fetchDataTask);
                thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
                thread.start();

            });

            parseTask.setOnFailed(e -> {
                System.err.println("Task failed: " + e.getSource().getException());
                
                // Ensure the loading stage closes on failure
                Platform.runLater(() -> main.App.closeLoadingStage());
            });

            Thread thread = new Thread(parseTask);
            thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
            thread.start();
        }
    }

    @FXML
    private void onUploadApacheButtonPressed(ActionEvent event) {
        onUploadButtonPressed(event, aFP);
    }
    @FXML
    private void onUploadNginxButtonPressed(ActionEvent event) {
        onUploadButtonPressed(event, nFP);
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        // do something here
    }
}