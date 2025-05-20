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
import java.io.IOException;

public class App extends Application {
    public static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(App.class.getResource("views.explorer.fxml"));
        mainScene = new Scene(root);
        stage.setScene(mainScene);
        stage.setTitle("Service Log Analyzer");
        stage.setMaximized(true);
        stage.show();
    }

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFile));
            Parent newRoot = loader.load();
            mainScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToDashboard() {
        switchScene("views.primary.fxml");
    }

    public static void switchToStream() {
        switchScene("views.stream.fxml");
    }

    public static void switchToExplorer() {
        switchScene("views.explorer.fxml");
    }

    // NEW: open the Filter window as a separate stage
    public static void openFilterStage() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml.filter.fxml"));
            Parent root = loader.load();

            Stage filterStage = new Stage();
            filterStage.setTitle("Filter Logs");
            filterStage.setScene(new Scene(root));
            filterStage.initOwner(primaryStage); // tie it to the main window
            filterStage.initModality(Modality.WINDOW_MODAL); // <-- modal behavior
            filterStage.setResizable(false); // optional
            filterStage.showAndWait(); // block main window until this one closes

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // @FXML
    // private void onBackButtonPressed(ActionEvent event) {
    //     try {
    //         Parent root = FXMLLoader.load(getClass().getResource("views/primary.fxml"));
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
        launch(args);
    }
}
