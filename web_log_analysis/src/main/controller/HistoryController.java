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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import models.logData.logData;
import models.mongoDB.mongoDB;
import javafx.scene.control.Label;

public class HistoryController implements DataReceiver<HashMap<String, Object>> {
    @FXML private void onDashboardButtonPressed() {
        main.App.showLoadingStage(null);
        Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
            @Override
            protected HashMap<String, Object> call() throws Exception {
                return PrimaryController.prepareData(null);
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

    @FXML private void onHistoryButtonPressed() {
        main.App.switchToHistory(null);
    }
    
    @FXML private void onExplorerButtonPressed() {
        main.App.switchToExplorer(null);;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        // do something here
    }

    private HBox createLogEntry(String fileName, int index) {
    
        HBox entryBox = new HBox(10);
        entryBox.getStyleClass().add("log-entry");
        entryBox.setAlignment(Pos.CENTER_LEFT);
        entryBox.setPadding(new Insets(10));
        
        Label indexLabel = new Label(String.valueOf(index));
        indexLabel.getStyleClass().add("log-index");

        Label nameLabel = new Label(fileName);
        nameLabel.getStyleClass().add("log-filename");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Button loadButton = new Button("Load");
        // loadButton.getStyleClass().add("load-button");
        // loadButton.setOnAction(e -> loadLogFile(fileName)); //Replace with loading logic

        Button deleteButton = new Button("🗑"); // Unicode trash can
        // deleteButton.getStyleClass().add("delete-button");
        // deleteButton.setOnAction(e -> deleteLogEntry(fileName, entryBox)); //Replace with deletion logic

        entryBox.getChildren().addAll(indexLabel, nameLabel, loadButton, deleteButton);
        return entryBox;
    }

}