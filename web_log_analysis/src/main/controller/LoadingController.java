package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.logData.logData;
import models.mongoDB.mongoDB;

public class LoadingController implements DataReceiver<HashMap<String, Object>> {
    @FXML private VBox loadingVbox;


    @FXML
    public void initialize() {
        loadingVbox.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
    }
    
    @Override
    public void setData(HashMap<String, Object> data) {
        // do something here
    }
}
