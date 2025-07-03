/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
/**
 * FXML Controller class
 *
 * @author admin
 */
public class DashboardController implements Initializable {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnFacRec;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnMC;

    @FXML
    private Button btnMP;

    @FXML
    private Button btnMed;

    @FXML
    private Button btnMedInv;

    @FXML
    private Button btnPatients;

    @FXML
    private Button btnPresc;

    @FXML
    private Button btnStdRec;
 
    
@FXML
private PieChart piechrt;

@FXML
private BarChart<String, Number> barchart;

@Override
public void initialize(URL url, ResourceBundle rb) {
    // Fetch data and populate charts
    loadPieChartData();
    loadBarChartData();
    setupYearDropdown();
}

private void loadPieChartData() {
    // Query to count students and faculty
    String query = "SELECT category, COUNT(*) AS count FROM PATIENTS GROUP BY category";

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        // ObservableList for the PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Calculate total number of patients
        int totalPatients = 0;
        while (resultSet.next()) {
            String category = resultSet.getString("category");
            int count = resultSet.getInt("count");
            totalPatients += count;

            // Add data to PieChart
            PieChart.Data pieData = new PieChart.Data(category + " (" + count + ")", count);
            
            // Set a custom label showing the category and count
            pieData.setName(category + " (" + count + ")");
            
            pieChartData.add(pieData);
        }

        // Set data to PieChart
        piechrt.setData(pieChartData);

        // You can also display the total number in a label or title if needed
        System.out.println("Total Patients: " + totalPatients);  // For debugging

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


@FXML private ChoiceBox<Integer> yearChoiceBox;

private void loadBarChartData() {
//    XYChart.Series<String, Number> series = new XYChart.Series<>();
//    series.setName("Medicines Given - " + selectedYear);
//
//    Map<Integer, Integer> monthCounts = new HashMap<>();
//    for (int i = 1; i <= 12; i++) {
//        monthCounts.put(i, 0);
//    }
//
//    String query = "SELECT MONTH(give_date) AS month, COUNT(*) AS total " +
//                   "FROM givemed WHERE YEAR(give_date) = ? " +
//                   "GROUP BY MONTH(give_date)";
//
//    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
//         PreparedStatement stmt = conn.prepareStatement(query)) {
//
//        stmt.setInt(1, selectedYear);
//        ResultSet rs = stmt.executeQuery();
//
//        while (rs.next()) {
//            int month = rs.getInt("month");
//            int count = rs.getInt("total");
//            monthCounts.put(month, count);
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return;
//    }
//
//    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
//                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//
//    for (int m = 1; m <= 12; m++) {
//        series.getData().add(new XYChart.Data<>(monthNames[m - 1], monthCounts.get(m)));
//    }
//
//    barchart.getData().clear();
//    barchart.getData().add(series);
//    // Query to count GiveMed records per category
//    String query = "SELECT p.category, COUNT(gm.give_med_id) AS count " +
//                   "FROM GiveMed gm " +
//                   "JOIN PATIENTS p ON gm.patient_id = p.id " +
//                   "GROUP BY p.category";
//
//    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
//         PreparedStatement preparedStatement = connection.prepareStatement(query);
//         ResultSet resultSet = preparedStatement.executeQuery()) {
//
//        // Create a series for BarChart
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("GiveMed Records");
//
//        while (resultSet.next()) {
//            String category = resultSet.getString("category");
//            int count = resultSet.getInt("count");
//
//            // Add data to BarChart series
//            series.getData().add(new XYChart.Data<>(category, count));
//        }
//
//        // Add series to the BarChart
//        barchart.getData().add(series);
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
} 
    private void setupYearDropdown() {
        int currentYear = Year.now().getValue();

        // Add years from current year going back 5 years
        for (int y = currentYear; y >= currentYear - 5; y--) {
            yearChoiceBox.getItems().add(y);
        }

        yearChoiceBox.setValue(currentYear); // default
        loadMedicineGivenPerMonth(currentYear); // load chart

        // Reload chart when year changes
        yearChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadMedicineGivenPerMonth(newVal);
            }
        });
    }
    private void loadMedicineGivenPerMonth(int selectedYear) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Medicines Given - " + selectedYear);

        // Default to 0 for all 12 months
        Map<Integer, Integer> monthCounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthCounts.put(i, 0);
        }

        String query = "SELECT MONTH(give_date) AS month, COUNT(*) AS total " +
                       "FROM givemed WHERE YEAR(give_date) = ? " +
                       "GROUP BY MONTH(give_date)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selectedYear);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("month");
                int count = rs.getInt("total");
                monthCounts.put(month, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int m = 1; m <= 12; m++) {
            series.getData().add(new XYChart.Data<>(monthNames[m - 1], monthCounts.get(m)));
        }

        barchart.getData().clear();
        barchart.getData().add(series);
    }


    @FXML
    void Dashboard(ActionEvent event) throws IOException {
        App.setRoot("Dashboard");  // This will load Dashboard.fxml
    }

    @FXML
    void FacRec(ActionEvent event) throws IOException {
        App.setRoot("FacRec");  // This will load FacRec.fxml
    }

    @FXML
    void Logout(ActionEvent event) throws IOException {
        App.setRoot("Login");  // This will load Login.fxml after logout
    }

    @FXML
    void MedCon(ActionEvent event) throws IOException {
        App.setRoot("MedCon");  // This will load MedCon.fxml
    }

    @FXML
    void MedInv(ActionEvent event) throws IOException {
        App.setRoot("MedInvt");  // This will load MedInv.fxml
    }

    @FXML
    void MedProf(ActionEvent event) throws IOException {
        App.setRoot("Medical_Profile");  // This will load Medical_Profile.fxml
    }

    @FXML
    void Medicine(ActionEvent event) throws IOException {
        App.setRoot("Medicine");  // This will load Medicine.fxml
    }

    @FXML

    void Patients(ActionEvent event) throws IOException {
        App.setRoot("AdminHome");  // This will load Patients.fxml
    }

    @FXML
    void Presc(ActionEvent event) throws IOException {
        App.setRoot("Presc");  // This will load Presc.fxml
    }

    @FXML
    void StdRec(ActionEvent event) throws IOException {
        App.setRoot("StdRec");  // This will load StdRec.fxml
    }
    
}
