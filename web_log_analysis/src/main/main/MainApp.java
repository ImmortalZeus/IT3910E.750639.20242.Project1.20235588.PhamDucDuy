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
import javafx.stage.StageStyle;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import models.exceptions.propertiesLoaderException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import controller.DataReceiver;
import controller.PrimaryController;
import models.logData.logData;
import models.logger.secureLogger;

public class MainApp extends Application {
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

    private static void setData(FXMLLoader loader, HashMap<String, Object> data)
    {
        if(data != null)
        {
            Object controller = loader.getController(); // ❌ This is null BEFORE load()
            if (controller instanceof DataReceiver) {
                @SuppressWarnings("unchecked")
                DataReceiver<HashMap<String, Object>> dataReceiver = (DataReceiver<HashMap<String, Object>>) controller;
                dataReceiver.setData(data);
            }
        }
    }

    public static void switchScene(String fxmlFile, HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(fxmlFile));
            Parent newRoot = loader.load();

            setData(loader, data);

            mainScene.setRoot(newRoot);
            mainScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public static void switchToDashboard(HashMap<String, Object> data) {
        switchScene("resources/views/primary.fxml", data);
    }
    
    public static void switchToHistory(HashMap<String, Object> data) {
        switchScene("resources/views/history.fxml", data);
    }

    public static void switchToExplorer(HashMap<String, Object> data) {
        switchScene("resources/views/explorer.fxml", data);
    }

    // NEW: open the Filter window as a separate stage
    public static Stage filterStage;
    
    public static void openFilterStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/filter.fxml"));
            Parent root = loader.load();

            setData(loader, data);

            filterStage = new Stage();
            filterStage.setTitle("Filter Logs");
            Scene filterScene = new Scene(root);
            filterScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            filterStage.setScene(filterScene);
            filterStage.initOwner(primaryStage); // tie it to the main window
            filterStage.initModality(Modality.WINDOW_MODAL); // <-- modal behavior
            filterStage.setResizable(false); // optional
            filterStage.showAndWait(); // block main window until this one closes

        } catch (IOException e) {
        }
    }

    public static void closeFilterStage() {
        if (filterStage != null && filterStage.isShowing()) {
            filterStage.close();
            // filterStage.close();
        }
    }

    public static Stage loadingStage;

    public static void showLoadingStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/loading.fxml"));
            Parent root = loader.load();

            setData(loader, data);

            loadingStage = new Stage();
            loadingStage.initStyle(StageStyle.UNDECORATED);
            loadingStage.setTitle("Loading...");
            Scene loadingScene = new Scene(root);
            loadingScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            loadingStage.setScene(loadingScene);
            loadingStage.initModality(Modality.WINDOW_MODAL);
            loadingStage.initOwner(primaryStage); 
            loadingStage.setResizable(false);
            loadingStage.show();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }


    public static void closeLoadingStage() {
        if (loadingStage != null && loadingStage.isShowing()) {
            loadingStage.close();
            // loadingStage.close();
        }
    }

    public static Stage invalidFilterStage;

    public static void showInvalidFilterStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/invalid.fxml"));
            Parent newRoot = loader.load();

            setData(loader, data);

            invalidFilterStage = new Stage();
            invalidFilterStage.initStyle(StageStyle.UNDECORATED);
            invalidFilterStage.setTitle("Invalid Filter!");
            Scene invalidScene = new Scene(newRoot);
            invalidScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            invalidFilterStage.setScene(invalidScene);
            invalidFilterStage.initModality(Modality.WINDOW_MODAL);
            invalidFilterStage.initOwner(filterStage); 
            invalidFilterStage.setResizable(false);
            invalidFilterStage.showAndWait();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public static void closeInvalidFilterStage() {
        if (invalidFilterStage != null && invalidFilterStage.isShowing()) {
            invalidFilterStage.close();
            // invalidFilterStage.close();
        }
    }

    public static Stage parseResultStage;

    public static void showParseResultStageStage(HashMap<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("resources/views/parseResult.fxml"));
            Parent newRoot = loader.load();

            setData(loader, data);

            parseResultStage = new Stage();
            parseResultStage.initStyle(StageStyle.UNDECORATED);
            parseResultStage.setTitle("Invalid Filter!");
            Scene parseResultScene = new Scene(newRoot);
            parseResultScene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            parseResultStage.setScene(parseResultScene);
            parseResultStage.initModality(Modality.WINDOW_MODAL);
            parseResultStage.initOwner(primaryStage); 
            parseResultStage.setResizable(false);
            parseResultStage.showAndWait();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public static void closeParseResultStage() {
        if (parseResultStage != null && parseResultStage.isShowing()) {
            parseResultStage.close();
            // parseResultStage.close();
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

    // private static void loadProperties(InputStream inpStream) throws propertiesLoaderException {
    //     try {
    //         Properties props = new Properties();
    //         props.load(inpStream);
            
    //         // Set properties into system properties
    //         System.getProperties().putAll(props);

    //         secureLogger.info("Properties loaded into System properties!");
    //     } catch (Exception e) {
    //         System.out.println(e);
    //         throw new propertiesLoaderException();
    //     }
    // }
}