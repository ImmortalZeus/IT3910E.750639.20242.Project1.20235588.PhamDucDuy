package controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
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
    @FXML private void onDashboardButtonPressed() {
        main.App.showLoadingStage(null);
        Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
            @Override
            protected HashMap<String, Object> call() throws Exception {
                return PrimaryController.prepareData(PrimaryController.filter_rules);
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