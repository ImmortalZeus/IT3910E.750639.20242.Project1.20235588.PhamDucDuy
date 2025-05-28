package controller;

import javafx.fxml.FXML;

public class SuccessController {
    
    @FXML
    public void onContinueButtonPressed() {
        main.App.switchToDashboard(null);
    }

}
