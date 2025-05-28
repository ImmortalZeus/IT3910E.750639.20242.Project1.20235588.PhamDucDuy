package controller;

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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import models.logData.logData;
import models.mongoDB.mongoDB;

public class StreamController implements DataReceiver<HashMap<String, Object>> {
    private mongoDB mongodb = new mongoDB();

    @FXML private void onDashboardButtonPressed() {
        main.App.showLoadingStage(null);
        Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
            @Override
            protected HashMap<String, Object> call() throws Exception {
                try {
                    // Simulate log parsing (replace with real logic)
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    
                    if(PrimaryController.currentPage.equals(-1)) PrimaryController.currentPage = 0;
                    if(PrimaryController.maxPage.equals(-1)) PrimaryController.maxPage = Double.valueOf(Math.floor(Double.valueOf(StreamController.this.mongodb.count(PrimaryController.filter_rules)) / Double.valueOf(PrimaryController.rowsPerPage))).intValue();
                    
                    ObservableList<logData> logTableData = StreamController.this.mongodb.filterWithSkipAndLimit(PrimaryController.filter_rules, PrimaryController.currentPage, PrimaryController.rowsPerPage).into(FXCollections.observableArrayList());
                    data.put("logTableData", logTableData);


                    Map<String, Integer> countryData = new HashMap<String, Integer>();
                    ArrayList<Document> countryAgg = StreamController.this.mongodb.aggregate(PrimaryController.filter_rules, "countryShort");
                    for (Document doc : countryAgg) {
                        Object key = doc.get("_id");
                        int count = doc.getInteger("count", 0);
                        countryData.put(key != null ? key.toString() : "-", count);
                    }

                    data.put("countryData", countryData);
                    
                    
                    Map<Integer, Integer> responseStatusData = new HashMap<Integer, Integer>();
                    ArrayList<Document> responseStatusAgg = StreamController.this.mongodb.aggregate(PrimaryController.filter_rules, "responseStatusCode");
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
                        Date minDate = sdf.parse((String) StreamController.this.mongodb.getMin(PrimaryController.filter_rules, "time").get("min"));
                        Date maxDate = sdf.parse((String) StreamController.this.mongodb.getMax(PrimaryController.filter_rules, "time").get("max"));;
                        
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
                            Integer cnt = StreamController.this.mongodb.count(filter_rules2);
                            lineChartData.put("Dur " + String.valueOf(j), cnt);

                            data.put("lineChartData", lineChartData);
                        }
                    } catch (Exception e) {

                    }

                    data.put("filter_rules", PrimaryController.filter_rules);

                    data.put("currentPage", PrimaryController.currentPage);
                    data.put("maxPage", PrimaryController.maxPage);
                    data.put("rowsPerPage", PrimaryController.rowsPerPage);

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
    }

    @FXML private void onStreamButtonPressed() {
        main.App.switchToStream(null);
    }
    
    @FXML private void onExplorerButtonPressed() {
        main.App.switchToExplorer(null);;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        // do something here
    }
}