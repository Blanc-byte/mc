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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class MedInvtController implements Initializable {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnFacRec;

    @FXML
    private Button btnDel;

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
    private TableColumn<GiveMed, Integer> IDCol;

    @FXML
    private TableColumn<GiveMed, String> PatientCol;

    @FXML
    private TableColumn<GiveMed, String> MedicineNameCol;

    @FXML
    private TableColumn<GiveMed, Integer> QtyCol;

    @FXML
    private TableColumn<GiveMed, String> GiveDateCol;

    @FXML
    private TableView<GiveMed> InvMedTable;

    @FXML
    private ComboBox<String> PName;

    @FXML
    private ComboBox<String> nameMed;
    @FXML
    private TextField qty;

    @FXML
    private Button btnCreate;

    private void loadGiveMedData() {
        ObservableList<GiveMed> giveMedList = FXCollections.observableArrayList();
        String query = "SELECT gm.give_med_id, "
                + "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, "
                + "m.name AS medicine_name, "
                + "gm.quantity, "
                + "gm.give_date "
                + "FROM GiveMed gm "
                + "JOIN PATIENTS p ON gm.patient_id = p.id "
                + "JOIN MEDICINE m ON gm.medicine_id = m.medicine_id "
                + "ORDER BY gm.give_date DESC";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int giveMedId = resultSet.getInt("give_med_id");
                String patientName = resultSet.getString("patient_name");
                String medicineName = resultSet.getString("medicine_name");
                int quantity = resultSet.getInt("quantity");
                String giveDate = resultSet.getString("give_date");

                giveMedList.add(new GiveMed(giveMedId, patientName, medicineName, quantity, giveDate));
            }

            InvMedTable.setItems(giveMedList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load GiveMed data.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateMedicineComboBox();
        populatePatientComboBox();

        IDCol.setCellValueFactory(new PropertyValueFactory<>("giveMedId"));
        PatientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        MedicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        QtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        GiveDateCol.setCellValueFactory(new PropertyValueFactory<>("giveDate"));

        // Load data into the TableView
        loadGiveMedData();
    }

    private void populateMedicineComboBox() {
        ObservableList<String> medicines = FXCollections.observableArrayList();

        String query = "SELECT name FROM MEDICINE";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                medicines.add(resultSet.getString("name"));
            }

            nameMed.setItems(medicines);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populatePatientComboBox() {
        ObservableList<String> patients = FXCollections.observableArrayList();

        String query = "SELECT CONCAT(first_name, ' ', last_name) AS patient_name FROM PATIENTS";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                patients.add(resultSet.getString("patient_name"));
            }

            PName.setItems(patients);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Create(ActionEvent event) {
        String selectedMedicine = (String) nameMed.getValue();
        String selectedPatient = (String) PName.getValue();
        int givenQuantity;

        try {
            givenQuantity = Integer.parseInt(qty.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Quantity must be a valid number.");
            alert.showAndWait();
            return;
        }

        if (selectedMedicine == null || selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Data");
            alert.setHeaderText(null);
            alert.setContentText("Please select both medicine and patient.");
            alert.showAndWait();
            return;
        }

        // Fetch Medicine ID and Current Quantity
        String fetchMedicineQuery = "SELECT medicine_id, quantity_stock FROM MEDICINE WHERE name = ?";
        int medicineId = 0;
        int currentStock = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(fetchMedicineQuery)) {

            preparedStatement.setString(1, selectedMedicine);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                medicineId = resultSet.getInt("medicine_id");
                currentStock = resultSet.getInt("quantity_stock");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (currentStock < givenQuantity) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient Stock");
            alert.setHeaderText(null);
            alert.setContentText("Not enough stock. Please restock.");
            alert.showAndWait();
            return;
        }

        // Fetch Patient ID
        String fetchPatientQuery = "SELECT id FROM PATIENTS WHERE CONCAT(first_name, ' ', last_name) = ?";
        int patientId = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(fetchPatientQuery)) {

            preparedStatement.setString(1, selectedPatient);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                patientId = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update Medicine Stock
        String updateStockQuery = "UPDATE MEDICINE SET quantity_stock = ? WHERE medicine_id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(updateStockQuery)) {

            preparedStatement.setInt(1, currentStock - givenQuantity);
            preparedStatement.setInt(2, medicineId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert into GiveMed Table
        String insertGiveMedQuery = "INSERT INTO GiveMed (patient_id, medicine_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(insertGiveMedQuery)) {

            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, medicineId);
            preparedStatement.setInt(3, givenQuantity);

            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicine successfully given to the patient.");
            alert.showAndWait();
            loadGiveMedData();

            // Clear fields after success
            nameMed.setValue(null);
            PName.setValue(null);
            qty.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to record the transaction.");
            alert.showAndWait();
        }
    }

    @FXML
    void Del(ActionEvent event) {
        // Get the selected row from the TableView
        GiveMed selectedGiveMed = (GiveMed) InvMedTable.getSelectionModel().getSelectedItem();

        if (selectedGiveMed != null) {
            // Ask for confirmation before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this record?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Create the SQL DELETE query
                String deleteQuery = "DELETE FROM GiveMed WHERE give_med_id = ?";

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                    // Set the ID of the selected record to be deleted
                    preparedStatement.setInt(1, selectedGiveMed.getGiveMedId());

                    // Execute the delete query
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Show success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Record deleted successfully.");
                        successAlert.showAndWait();

                        // Refresh the TableView
                        loadGiveMedData();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Database Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete the record.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // If no row is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a record to delete.");
            alert.showAndWait();
        }
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
