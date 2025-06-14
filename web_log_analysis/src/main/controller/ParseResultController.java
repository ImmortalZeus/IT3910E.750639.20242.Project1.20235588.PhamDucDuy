package controller;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.parsers.ResultAggregator;
import models.utils.hashMapCloner;

public class ParseResultController implements DataReceiver<HashMap<String, Object>> {

    private static ResultAggregator parseTaskValue = null;
    private static HashMap<String, Object> fetchDataTaskValue = null;

    @FXML private VBox parseResultVbox;
    @FXML private Label parseResultContentLabel;
    @FXML private Button continueButton;

    @FXML
    public void initialize() {
        parseResultVbox.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
    }
    @FXML
    public void onContinueButtonPressed() {
        Platform.runLater(() -> {
            main.MainApp.closeParseResultStage();
            Platform.runLater(() -> {
                main.MainApp.switchToDashboard(ParseResultController.fetchDataTaskValue);
            });
        });
    }
    protected static final void resetData() {
        ParseResultController.parseTaskValue = null;
        ParseResultController.fetchDataTaskValue = null;
    }
    @Override
    public void setData(HashMap<String, Object> data) {
        Object tmp_parseTaskValue = data.get("parseTaskValue");
        if(tmp_parseTaskValue != null && tmp_parseTaskValue instanceof ResultAggregator)
        {
            @SuppressWarnings("unchecked")
            ResultAggregator parseTaskValue2 = (ResultAggregator) tmp_parseTaskValue;
            
            ParseResultController.parseTaskValue = parseTaskValue2;

            parseResultContentLabel.setText("Parsed File!" + '\n' + 
                                            "✅ Succeed : " + String.valueOf(ParseResultController.parseTaskValue.getSucceed()) + " lines" + '\n' + 
                                            "❎ Fail : " + String.valueOf(ParseResultController.parseTaskValue.getFail()) + " lines"
                                            );
        }

        Object tmp_fetchDataTaskValue = data.get("fetchDataTaskValue");
        if(tmp_fetchDataTaskValue != null && tmp_fetchDataTaskValue instanceof HashMap<?, ?> map)
        {
            if(map.keySet().stream().allMatch(key -> key == null || key instanceof String) && map.values().stream().allMatch(value -> value == null || value instanceof Object))
            {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> fetchDataTaskValue = (HashMap<String, Object>) tmp_fetchDataTaskValue;
                
                ParseResultController.fetchDataTaskValue = hashMapCloner.deepCopy(fetchDataTaskValue);
            }
        }
        // do something here
    }
}
