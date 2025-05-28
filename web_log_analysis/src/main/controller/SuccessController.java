package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class SuccessController {
    @FXML private VBox successVbox;

    @FXML
    public void initialize() {
        successVbox.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
    }
    @FXML
    public void onContinueButtonPressed() {
        main.App.switchToDashboard(null);
    }

}
