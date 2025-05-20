package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.animation.FadeTransition;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;


public class PrimaryController {

    @FXML private TableView<?> logTable;
    @FXML private TableView<?> statusTable;

    @FXML private HBox tableSection;
    // @FXML private TextField searchField;

    // @FXML private DatePicker fromDatePicker;
    // @FXML private DatePicker toDatePicker;

    // @FXML private TextField fromTimeField;
    // @FXML private TextField toTimeField;

    // @FXML private Button applyFilterButton;

    // @FXML private HBox dateFilterBox;
    // @FXML private HBox timeFilterBox;

    // @FXML private HBox locationFilterBox;

    // @FXML private TextField countryField;
    // @FXML private TextField regionField;
    // @FXML private TextField cityField;

    
    // public void bindTableHeightsToScene(Scene scene) {
    //     tableSection.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));}

    // // MenuButton actions
    // @FXML private void onFilterIP() {
    //     resetFilterUI();
    //     showSearchField("Enter IP Address...");
    //     showApplyButton();
    // }
    
    // @FXML private void onTimestamp() {
    //     resetFilterUI();
    //     dateFilterBox.setVisible(true);
    //     dateFilterBox.setManaged(true);
    //     timeFilterBox.setVisible(true);
    //     timeFilterBox.setManaged(true);
    //     showApplyButton();
    // }
    
    // @FXML private void onStatus() {
    //     resetFilterUI();
    //     showSearchField("Enter Status Code...");
    //     showApplyButton();
    // }
    
    // @FXML private void onRequest() {
    //     resetFilterUI();
    //     showSearchField("Enter Request...");
    //     showApplyButton();
    // }
    
    // @FXML private void onSize() {
    //     resetFilterUI();
    //     showSearchField("Enter Size...");
    //     showApplyButton();
    // }
    
    // @FXML private void onReferrer() {
    //     resetFilterUI();
    //     showSearchField("Enter Referrer...");
    //     showApplyButton();
    // }
    
    // @FXML private void onFilterUserAgent() {
    //     resetFilterUI();
    //     showSearchField("Enter User Agent...");
    //     showApplyButton();
    // }
    
    // @FXML private void onLocation() {
    //     resetFilterUI();
    //     locationFilterBox.setVisible(true);
    //     locationFilterBox.setManaged(true);
    //     showApplyButton();
    // }
    
    // // Show a standard search field
    // private void showSearchField(String prompt) {
    //     searchField.setPromptText(prompt);
    //     searchField.setVisible(true);
    //     searchField.setManaged(true);
    //     searchField.requestFocus();
    // }

    // // Hide all other filters
    // private void resetFilterUI() {
    //     searchField.clear();
    //     searchField.setVisible(false);
    //     searchField.setManaged(false);
    
    //     fromDatePicker.setValue(null);
    //     toDatePicker.setValue(null);
    //     dateFilterBox.setVisible(false);
    //     dateFilterBox.setManaged(false);
    
    //     fromTimeField.clear();
    //     toTimeField.clear();
    //     timeFilterBox.setVisible(false);
    //     timeFilterBox.setManaged(false);
    
    //     countryField.clear();
    //     regionField.clear();
    //     cityField.clear();
    //     locationFilterBox.setVisible(false);
    //     locationFilterBox.setManaged(false);
    
    //     applyFilterButton.setVisible(false);
    //     applyFilterButton.setManaged(false);
    // }
    
    // private void showApplyButton() {
    //     applyFilterButton.setVisible(true);
    //     applyFilterButton.setManaged(true);
    // }
    

    // Placeholder until filtering is wired to backend
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
        main.App.switchToExplorer();
    }

    @FXML
    private void onFilterButtonPressed() {
        main.App.openFilterStage();
    }
    @FXML
    private void onRefreshButtonPressed() {
        // resetFilterUI();
        System.out.println("Filters cleared. Refreshed.");
    }

    @FXML
    private PieChart pieChart;

    private final String DATA_FILE = "/data_placeholder/country_request.txt"; // fixed file name and added leading slash

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    public void initialize() {
        Map<String, Integer> countryData = loadDataFromFile(DATA_FILE);
        if (countryData == null || countryData.isEmpty()) return;

        int totalRequests = countryData.values().stream().mapToInt(Integer::intValue).sum();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : countryData.entrySet()) {
            String country = entry.getKey();
            int requests = entry.getValue();
            double percentage = (requests * 100.0) / totalRequests;

            String label = (country.equals("-") ? "Undefined" : country) + " (" + String.format("%.1f", percentage) + "%)";
            pieChartData.add(new PieChart.Data(label, requests));
        }

        pieChart.setData(pieChartData);

        for (PieChart.Data data : pieChart.getData()) {
            String country = data.getName();
            double percent = (data.getPieValue() / totalRequests) * 100;
            int actualCount = (int) data.getPieValue();

            // Create a styled label as a tooltip
            Label tooltipLabel = new Label(String.format("Country: %s\nPercentage: %.1f%%\nCount: %d", country, percent, actualCount));
            tooltipLabel.getStyleClass().add("piechart-tooltip");
            tooltipLabel.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("resources/css/style.css").toExternalForm());
            Popup popup = new Popup();
            popup.getContent().add(tooltipLabel);
            popup.setAutoHide(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), tooltipLabel);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Show tooltip on hover
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                tooltipLabel.setOpacity(0);
                popup.show(data.getNode(), event.getScreenX() + 10, event.getScreenY() + 10);
                fadeIn.playFromStart();
            });

            // Hide tooltip on exit
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
        }

    }

    public static Map<String, Integer> loadDataFromFile(String filePath) {
        Map<String, Integer> data = new HashMap<>();

        try (InputStream input = PrimaryController.class.getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            if (input == null) {
                System.err.println("Error reading data: file not found at " + filePath);
                return null;
            }
            String line = reader.readLine();
            if (line != null) {
                String[] entries = line.split(",");
                for (String entry : entries) {
                    String[] parts = entry.trim().split("=");
                    if (parts.length == 2) {
                        data.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
        return data;
    }

    

    // Apply timestamp filter — placeholder for now
    // @FXML
    // private void onApplyFilterButton() {
    //     String fromTime = normalizeTimeInput(fromTimeField.getText());
    //     String toTime = normalizeTimeInput(toTimeField.getText());

    //     System.out.println("Date Range: " + fromDatePicker.getValue() + " to " + toDatePicker.getValue());
    //     System.out.println("Time Range: " + fromTime + " to " + toTime);
    // }

    // // Ensures time format is HH:mm:ss with fallback values
    // private String normalizeTimeInput(String input) {
    //     if (input == null || input.trim().isEmpty()) return "00:00:00";

    //     String[] parts = input.trim().split(":");
    //     String hour = "00", minute = "00", second = "00";

    //     try {
    //         if (parts.length >= 1) hour = String.format("%02d", Integer.parseInt(parts[0]));
    //         if (parts.length >= 2) minute = String.format("%02d", Integer.parseInt(parts[1]));
    //         if (parts.length == 3) second = String.format("%02d", Integer.parseInt(parts[2]));
    //     } catch (NumberFormatException e) {
    //         System.out.println("Invalid time format. Defaulting to 00:00:00.");
    //     }

    //     return hour + ":" + minute + ":" + second;
    // }
}
