package controller;

import javafx.fxml.FXML;

public class ExplorerController {

    @FXML private void onDashboardButtonPressed() {
        main.App.switchToDashboard();
    }

    @FXML private void onStreamButtonPressed() {
        main.App.switchToStream();
    }
    
    @FXML private void onExplorerButtonPressed() {
        main.App.switchToExplorer();;
    }
}
