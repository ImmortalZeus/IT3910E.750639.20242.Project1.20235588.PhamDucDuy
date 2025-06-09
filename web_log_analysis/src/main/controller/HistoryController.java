package controller;

import java.text.DateFormat;
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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.logData.logData;
import models.mongoDB.mongoDB;
import models.mongoDB.mongoDBParseHistory;
import models.utils.dateToUTC;
import javafx.scene.control.Label;

public class HistoryController implements DataReceiver<HashMap<String, Object>> {
    protected static List<mongoDBParseHistory> collectionHistory = new ArrayList<>();

    @FXML private VBox logHistoryContainer;

    @FXML private void onDashboardButtonPressed() {
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

    @FXML private void onHistoryButtonPressed() {
        HashMap<String, Object> historyData = new HashMap<>();
        historyData.put("collectionHistory", PrimaryController.mongodb.getHistory().into(new ArrayList<mongoDBParseHistory>()));
        main.App.switchToHistory(historyData);
    }
    
    @FXML private void onExplorerButtonPressed() {
        main.App.switchToExplorer(null);
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        Platform.runLater(() -> {
            main.App.showLoadingStage(null);
            Object tmp_collectionHistory = data.get("collectionHistory");
            if(tmp_collectionHistory != null && tmp_collectionHistory instanceof ArrayList<?>)
            {
                ArrayList<?> tmp_collectionHistory2 = (ArrayList<?>) tmp_collectionHistory;
    
                if(tmp_collectionHistory2.stream().allMatch(item -> (item == null || item instanceof mongoDBParseHistory)))
                {
                    @SuppressWarnings("unchecked")
                    ArrayList<mongoDBParseHistory> collectionHistory2 = (ArrayList<mongoDBParseHistory>) tmp_collectionHistory2;
                    HistoryController.collectionHistory = collectionHistory2;
                }
            }
    
            for(mongoDBParseHistory e : HistoryController.collectionHistory) {
                logHistoryContainer.getChildren().add(this.createLogEntry(e.getCollectionName(), Date.from(Instant.ofEpochMilli(Long.parseLong(e.getCreatedAt()))), e.getFilePath()));
            }

            Platform.runLater(() -> {
                main.App.closeLoadingStage();
            });
        });
    }

    private HBox createLogEntry(String collectionName, Date date, String filePath) {
    
        HBox entryBox = new HBox(10);
        entryBox.getStyleClass().add("log-entry");
        entryBox.setAlignment(Pos.CENTER_LEFT);
        entryBox.setPadding(new Insets(10));
        
        Label collectionNameLabel = new Label("Collection Name : " + collectionName);
        collectionNameLabel.getStyleClass().add("log-collection-name");

        Label createdAtLabel = new Label("Created At : " + new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(date));
        createdAtLabel.getStyleClass().add("log-created-at");
        //HBox.setHgrow(createdAtLabel, Priority.ALWAYS);

        Label filePathLabel = new Label("File Path: " + filePath);
        filePathLabel.getStyleClass().add("log-file-path");
        //HBox.setHgrow(filePathLabel, Priority.ALWAYS);

        Button loadButton = new Button("Load");
        loadButton.getStyleClass().add("load-button");
        loadButton.setOnAction(e -> {
            PrimaryController.resetData();
            PrimaryController.mongodb = new mongoDB(collectionName);
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
        }); //Replace with loading logic

        Button deleteButton = new Button("Delete"); // Unicode trash can
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> {
            PrimaryController.mongodb.deleteCollection(collectionName);
            logHistoryContainer.getChildren().remove(entryBox);
            // deleteLogEntry(collectionName, entryBox);
        }); //Replace with deletion logic

        collectionNameLabel.setMinWidth(260.0);
        collectionNameLabel.setPrefWidth(260.0);

        createdAtLabel.setMinWidth(250.0);
        createdAtLabel.setPrefWidth(250.0);

        filePathLabel.setMinWidth(930);
        filePathLabel.setMaxWidth(930);
        filePathLabel.setPrefWidth(930);

        loadButton.setMinWidth(80);
        loadButton.setPrefWidth(80);

        deleteButton.setMinWidth(80);
        deleteButton.setPrefWidth(80);
        
        entryBox.getChildren().addAll(collectionNameLabel, createdAtLabel, filePathLabel, loadButton, deleteButton);
        return entryBox;
    }

}