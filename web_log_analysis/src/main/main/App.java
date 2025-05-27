package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import models.exceptions.propertiesLoaderException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import controller.DataReceiver;
import controller.PrimaryController;
import models.logData.logData;

public class App extends Application {
    public static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(Thread.currentThread().getContextClassLoader().getResource("resources/views/explorer.fxml"));
        mainScene = new Scene(root);
        mainScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        stage.setScene(mainScene);
        stage.setTitle("Service Log Analyzer");
        stage.setMaximized(true);
        stage.show();
    }

    public static void switchScene(String fxmlFile, HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(fxmlFile));
            Parent newRoot = loader.load();

            if(data != null)
            {
                Object controller = loader.getController(); // ❌ This is null BEFORE load()
                if (controller instanceof DataReceiver) {
                    @SuppressWarnings("unchecked")
                    DataReceiver<HashMap<String, Object>> dataReceiver = (DataReceiver<HashMap<String, Object>>) controller;
                    dataReceiver.setData(data);
                }
            }

            mainScene.setRoot(newRoot);
            mainScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToDashboard(HashMap<String, Object> data) {
        switchScene("resources/views/primary.fxml", data);
    }
    
    public static void switchToStream(HashMap<String, Object> data) {
        switchScene("resources/views/stream.fxml", data);
    }

    public static void switchToExplorer(HashMap<String, Object> data) {
        switchScene("resources/views/explorer.fxml", data);
    }

    // NEW: open the Filter window as a separate stage
    public static void openFilterStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/filter.fxml"));
            Parent root = loader.load();

            if(data != null)
            {
                Object controller = loader.getController(); // ❌ This is null BEFORE load()
                if (controller instanceof DataReceiver) {
                    @SuppressWarnings("unchecked")
                    DataReceiver<HashMap<String, Object>> dataReceiver = (DataReceiver<HashMap<String, Object>>) controller;
                    dataReceiver.setData(data);
                }
            }

            Stage filterStage = new Stage();
            filterStage.setTitle("Filter Logs");
            Scene filterScene = new Scene(root);
            filterScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            filterStage.setScene(filterScene);
            filterStage.initOwner(primaryStage); // tie it to the main window
            filterStage.initModality(Modality.WINDOW_MODAL); // <-- modal behavior
            filterStage.setResizable(false); // optional
            filterStage.showAndWait(); // block main window until this one closes

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage loadingStage;

    public static void showLoadingStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/loading.fxml"));
            Parent root = loader.load();

            if(data != null)
            {
                Object controller = loader.getController(); // ❌ This is null BEFORE load()
                if (controller instanceof DataReceiver) {
                    @SuppressWarnings("unchecked")
                    DataReceiver<HashMap<String, Object>> dataReceiver = (DataReceiver<HashMap<String, Object>>) controller;
                    dataReceiver.setData(data);
                }
            }

            loadingStage = new Stage();
            loadingStage.setTitle("Loading...");
            Scene loadingScene = new Scene(root);
            loadingScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            loadingStage.setScene(loadingScene);
            loadingStage.initModality(Modality.WINDOW_MODAL);
            loadingStage.initOwner(primaryStage); 
            loadingStage.setResizable(false);
            loadingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void closeLoadingStage() {
        if (loadingStage != null && loadingStage.isShowing()) {
            Platform.runLater(() -> loadingStage.close());
            // loadingStage.close();
        }
    }

    
    // @FXML
    // private void onBackButtonPressed(ActionEvent event) {
    //     try {
    //         Parent root = FXMLLoader.load(Thread.currentThread().getContextClassLoader().getResource("/resources/views//primary.fxml"));
    //         Scene scene = new Scene(root);
            
    //         // Get the current stage from the event source
    //         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    //         stage.setScene(scene);
    //         stage.setTitle("Dashboard");
    //         stage.show();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        try {
            loadProperties(Thread.currentThread().getContextClassLoader().getResource("mongodb_config.properties").getPath());
        } catch (Exception e) {

        }

        launch(args);
    }
    private static void loadProperties(String filePath) throws propertiesLoaderException {
        try (FileInputStream input = new FileInputStream(filePath)) {
            Properties props = new Properties();
            props.load(input);
            
            // Set properties into system properties
            System.getProperties().putAll(props);

            System.out.println("Properties loaded into System properties!");
        } catch (Exception e) {
            throw new propertiesLoaderException();
        }
    }
}