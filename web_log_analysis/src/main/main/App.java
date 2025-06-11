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

public class App {
    public static void main(String[] args) {
        try {
            loadProperties(Thread.currentThread().getContextClassLoader().getResourceAsStream("mongodb_config.properties"));
        } catch (Exception e) {

        }
        Application.launch(MainApp.class, args);
    }

    private static void loadProperties(InputStream inpStream) throws propertiesLoaderException {
        try {
            Properties props = new Properties();
            props.load(inpStream);
            
            // Set properties into system properties
            System.getProperties().putAll(props);

            secureLogger.info("Properties loaded into System properties!");
        } catch (Exception e) {
            System.out.println(e);
            throw new propertiesLoaderException();
        }
    }
}