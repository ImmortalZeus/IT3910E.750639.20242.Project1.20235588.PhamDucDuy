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

public class ExplorerController implements DataReceiver<HashMap<String, Object>> {
    private static apacheFileParser aFP = new apacheFileParser();
    private static nginxFileParser nFP = new nginxFileParser();

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
                            PrimaryController.resetData();
                            ResultAggregator res = aFP.parse(selectedFile.getAbsolutePath());
                            return res;
                        }
                        else if(parser instanceof nginxFileParser)
                        {
                            PrimaryController.resetData();
                            ResultAggregator res = nFP.parse(selectedFile.getAbsolutePath());
                            return res;
                        }
                        // Process and upload the file to backend
                        // Example: BackendService.uploadLogFile(selectedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            main.App.closeLoadingStage();
                        });
                    }
                    return null;
                }
            };

            parseTask.setOnSucceeded(v -> {
                
                ResultAggregator parseTaskValue = parseTask.getValue();

                Task<HashMap<String, Object>> fetchDataTask = new Task<>() {
                    @Override
                    protected HashMap<String, Object> call() throws Exception {
                        return PrimaryController.prepareData(null);
                    }
                };

                fetchDataTask.setOnSucceeded(v2 -> {
                    HashMap<String, Object> fetchDataTaskValue = fetchDataTask.getValue();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("parseTaskValue", parseTaskValue);
                    data.put("fetchDataTaskValue", fetchDataTaskValue);
                    Platform.runLater(() -> {
                        main.App.closeLoadingStage();
                        Platform.runLater(() -> {
                            main.App.showParseResultStageStage(data);
                        });
                    });
                });

                Thread thread = new Thread(fetchDataTask);
                thread.setDaemon(true); // Allow JVM to exit if this is the only thread left
                thread.start();

            });

            parseTask.setOnFailed(e -> {
                System.err.println("Task failed: " + e.getSource().getException());
                
                // Ensure the loading stage closes on failure
                Platform.runLater(() -> {
                    main.App.closeLoadingStage();
                });
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