package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.File;

public class ExplorerController {
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
        uploadApacheButton.setStyle("upload-button");

        uploadNginxButton.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
        uploadNginxButton.setStyle("upload-button");
    }

    @FXML
    private void onUploadButtonPressed(ActionEvent event) {
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
            System.out.println("Selected log file: " + selectedFile.getAbsolutePath());

            // TODO: Process and upload the file to backend
            // Optional: show loading screen while processing
            main.App.switchToDashboard(); // if you want to show results after upload
        }
    }

    @FXML
    private void onDashboardButtonPressed() {
        main.App.switchToDashboard();
    }

    @FXML
    private void onStreamButtonPressed() {
        main.App.switchToStream();
    }
    
    @FXML
    private void onExplorerButtonPressed() {
        main.App.switchToExplorer();;
    }
}