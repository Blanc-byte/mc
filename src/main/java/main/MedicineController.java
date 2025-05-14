/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class MedicineController implements Initializable {

    @FXML
    private TextArea Desc;

    @FXML
    private DatePicker ExpiryDate;

    @FXML
    private TextField MedicineQty;

    @FXML
    private TextField medicineName;
    
    @FXML
    private TextField brandName;

    @FXML
    private TableView<Medicine> MedicineTable;

    @FXML
    private TableColumn<Medicine, Integer> medicineidcol;

    @FXML
    private TableColumn<Medicine, String> Namecol;

    @FXML
    private TableColumn<Medicine, Integer> Qtycol;

    @FXML
    private TableColumn<Medicine, String> desccol;
    
    @FXML
    private TableColumn<Medicine, String> BrandCol;

    @FXML
    private TableColumn<Medicine, Date> expcol;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnEdit;

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
    private Button btnDelete;

    @FXML
    private Button btnPatients;

    @FXML
    private Button btnPresc;

    @FXML
    private Button btnSaveEdit;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnStdRec;

    @FXML
    private TextField searchtf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up column bindings
        medicineidcol.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        Namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        BrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        Qtycol.setCellValueFactory(new PropertyValueFactory<>("quantityStock"));
        desccol.setCellValueFactory(new PropertyValueFactory<>("description"));
        expcol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        // Load data from the database
        loadMedicineData();

        // Add dynamic search
        searchtf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadMedicineData();
            } else {
                ObservableList<Medicine> filteredMedicines = FXCollections.observableArrayList();

                for (Medicine medicine : MedicineTable.getItems()) {
                    if (medicine.getName().toLowerCase().contains(newValue.toLowerCase())
                            || (medicine.getDescription() != null && medicine.getDescription().toLowerCase().contains(newValue.toLowerCase()))) {
                        filteredMedicines.add(medicine);
                    }
                }

                MedicineTable.setItems(filteredMedicines);
            }
        });
    }

    
    List<String> expiringMedicines = new ArrayList<>();
    List<String> lowStockMedicines = new ArrayList<>();
    int totalNotifs=0;
    @FXML private Label notifNumbers;
    
    private void loadMedicineData() {
        ObservableList<Medicine> medicines = FXCollections.observableArrayList();

        String query = "SELECT * FROM MEDICINE";

        
        LocalDate today = LocalDate.now();
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int medicineId = resultSet.getInt("medicine_id");
                String name = resultSet.getString("name");
                String brand = resultSet.getString("brand");
                String description = resultSet.getString("description");
                int quantityStock = resultSet.getInt("quantity_stock");
                Date expiryDate = resultSet.getDate("expiry_date");

                medicines.add(new Medicine(medicineId, name, description, quantityStock, expiryDate,brand));
                
                LocalDate expiryLocalDate = expiryDate.toLocalDate();
                if (expiryLocalDate.isBefore(today.plusDays(6))) {
                    expiringMedicines.add(name + " (Brand: " + brand + ", Expiry: " + expiryLocalDate + ")");
                }

                if (quantityStock <= 10) {
                    lowStockMedicines.add(name + " (Brand: " + brand + ", Stock: " + quantityStock + ")");
                }
                totalNotifs = expiringMedicines.size() + lowStockMedicines.size();
                
            }
            notifNumbers.setText(totalNotifs+"");

            MedicineTable.setItems(medicines);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load medicine data.");
            alert.showAndWait();
        }
    }
    
    @FXML
    void showNotifications() {
        if (expiringMedicines.isEmpty() && lowStockMedicines.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Notifications");
            alert.setHeaderText(null);
            alert.setContentText("No medicines are expiring soon or low in stock.");
            alert.showAndWait();
            return;
        }

        StringBuilder notificationMessage = new StringBuilder();

        if (!expiringMedicines.isEmpty()) {
            notificationMessage.append("⚠️ Expiring Medicines (within 5 days):\n");
            for (String med : expiringMedicines) {
                notificationMessage.append("- ").append(med).append("\n");
            }
            notificationMessage.append("\n");
        }

        if (!lowStockMedicines.isEmpty()) {
            notificationMessage.append("📉 Low Stock Medicines (≤ 10 units):\n");
            for (String med : lowStockMedicines) {
                notificationMessage.append("- ").append(med).append("\n");
            }
        }

        Alert combinedAlert = new Alert(Alert.AlertType.WARNING);
        combinedAlert.setTitle("Medicine Notifications");
        combinedAlert.setHeaderText("Attention Required");
        combinedAlert.setContentText(notificationMessage.toString());
        combinedAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        combinedAlert.showAndWait();
    }


    @FXML
    void ADD(ActionEvent event) {
        // Retrieve the values from the input fields
        String name = medicineName.getText();
        String brand = brandName.getText();
        String description = Desc.getText();
        int quantityStock;
        Date expiryDate;

        // Validate the inputs
        try {
            quantityStock = Integer.parseInt(MedicineQty.getText()); // Convert quantity to integer
            expiryDate = java.sql.Date.valueOf(ExpiryDate.getValue()); // Convert DatePicker value to Date
        } catch (NumberFormatException e) {
            // Handle invalid quantity input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Quantity must be a number.");
            alert.showAndWait();
            return;
        } catch (NullPointerException e) {
            // Handle null date input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Expiry date cannot be empty.");
            alert.showAndWait();
            return;
        }

        // SQL query to insert the data into the MEDICINE table
        String query = "INSERT INTO MEDICINE (name, description, quantity_stock, expiry_date, brand) VALUES (?, ?, ?, ?, ?)";

        // Execute the SQL query
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the query
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, quantityStock);
            preparedStatement.setDate(4, expiryDate);
            preparedStatement.setString(5, brand);

            // Execute the update and check if the record was added successfully
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Medicine Added Successfully");

                successAlert.setContentText("The medicine details have been saved to the database.");
                loadMedicineData();
                successAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText("Error while saving the medicine");
            errorAlert.setContentText("An error occurred while saving the medicine details.");
            errorAlert.showAndWait();
        }
    }

    private void clearFields() {
        medicineName.clear();
        brandName.clear();
        Desc.clear();
        MedicineQty.clear();
        ExpiryDate.setValue(null);
    }

    @FXML
    void EDIT(ActionEvent event) {
        Medicine selectedMedicine = MedicineTable.getSelectionModel().getSelectedItem();

        if (selectedMedicine != null) {
            medicineName.setText(selectedMedicine.getName());
            brandName.setText(selectedMedicine.getBrand());
            Desc.setText(selectedMedicine.getDescription());
            MedicineQty.setText(String.valueOf(selectedMedicine.getQuantityStock()));
            ExpiryDate.setValue(selectedMedicine.getExpiryDate().toLocalDate());
        } else {
            clearFields();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a medicine to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    void SAVEEDIT(ActionEvent event) {
        Medicine selectedMedicine = MedicineTable.getSelectionModel().getSelectedItem();

        if (selectedMedicine != null) {
            String newName = medicineName.getText();
            String newBrand = brandName.getText();
            String newDescription = Desc.getText();
            int newQuantity;
            try {
                newQuantity = Integer.parseInt(MedicineQty.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Quantity must be a valid number.");
                alert.showAndWait();
                return;
            }
            LocalDate newExpiryDate = ExpiryDate.getValue();

            String updateQuery = "UPDATE MEDICINE SET name = ?, description = ?, quantity_stock = ?, expiry_date = ?, brand = ? WHERE medicine_id = ?";

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newDescription);
                preparedStatement.setInt(3, newQuantity);
                preparedStatement.setDate(4, Date.valueOf(newExpiryDate));
                preparedStatement.setInt(5, selectedMedicine.getMedicineId());
                preparedStatement.setString(6, newBrand);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Medicine details updated successfully.");
                    alert.showAndWait();

                    // Refresh the TableView
                    loadMedicineData();

                    // Clear the fields
                    clearFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update medicine details.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a medicine to save edits.");
            alert.showAndWait();
        }
    }

    @FXML
    void DELETE(ActionEvent event) {
        Medicine selectedMedicine = MedicineTable.getSelectionModel().getSelectedItem();

        if (selectedMedicine != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this medicine?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String deleteQuery = "DELETE FROM MEDICINE WHERE medicine_id = ?";

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                    preparedStatement.setInt(1, selectedMedicine.getMedicineId());
                    int rowsDeleted = preparedStatement.executeUpdate();

                    if (rowsDeleted > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Medicine deleted successfully.");
                        alert.showAndWait();

                        // Refresh the TableView
                        loadMedicineData();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete medicine.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a medicine to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void Search(ActionEvent event) {
        String searchText = searchtf.getText().toLowerCase();

        if (searchText.isEmpty()) {
            // Reload all data if search field is empty
            loadMedicineData();
        } else {
            ObservableList<Medicine> filteredMedicines = FXCollections.observableArrayList();

            for (Medicine medicine : MedicineTable.getItems()) {
                if (medicine.getName().toLowerCase().contains(searchText)
                        || (medicine.getDescription() != null && medicine.getDescription().toLowerCase().contains(searchText))) {
                    filteredMedicines.add(medicine);
                }
            }

            MedicineTable.setItems(filteredMedicines);
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
