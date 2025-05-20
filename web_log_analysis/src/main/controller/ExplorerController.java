package controller;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;

import javafx.fxml.Initializable;

public class ExplorerController implements Initializable {
    @FXML
    private ImageView hgna;
    @FXML
    private ImageView pdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image_pdd = new Image(Thread.currentThread().getContextClassLoader().getResource("resources/images/pdd.jpg").toExternalForm());
        pdd.setImage(image_pdd);
        Image image_hgna = new Image(Thread.currentThread().getContextClassLoader().getResource("resources/images/hgna.jpg").toExternalForm());
        hgna.setImage(image_hgna);
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