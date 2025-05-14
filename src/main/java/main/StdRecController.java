/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.ResultSet;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class StdRecController implements Initializable {
    @FXML
    private TableView<Patient> table; // Specify the type of data

    @FXML
    private TableColumn<Patient, String> PatientId;

    @FXML
    private TableColumn<Patient, String> Fname;

    @FXML
    private TableColumn<Patient, String> Lname;

    @FXML
    private TableColumn<Patient, String> Gender;

    @FXML
    private TableColumn<Patient, LocalDate> DateOfBirth;

    @FXML
    private TableColumn<Patient, Integer> Age;

    @FXML
    private TableColumn<Patient, String> Category;

    @FXML
    private TableColumn<Patient, String> Contact;

    @FXML
    private TableColumn<Patient, String> MedcialHistory;

    @FXML
    private TableColumn<Patient, String> HealthStatus;
    
    @FXML
    private TextField searchTF;
    
    ObservableList<String> categories = FXCollections.observableArrayList("Student", "Faculty");

    @FXML
    private TableColumn<Patient, Integer> Id; // Column for id

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Configure TableView columns
        PatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        Fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        DateOfBirth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Age.setCellValueFactory(new PropertyValueFactory<>("age"));
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        Contact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        MedcialHistory.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        HealthStatus.setCellValueFactory(new PropertyValueFactory<>("healthStatus"));
        Id.setCellValueFactory(new PropertyValueFactory<>("id")); // Display id in the table

        // Load data into TableView
        loadTableData();
    }

private void loadTableData() {
    ObservableList<Patient> patientList = FXCollections.observableArrayList();

    String query = "SELECT * FROM PATIENTS";
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); 
         PreparedStatement preparedStatement = connection.prepareStatement(query); 
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            // Fetch data including the 'id' field
            int id = resultSet.getInt("id"); // Fetch the id
            String patientId = resultSet.getString("patients_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            LocalDate birthDate = resultSet.getDate("birthdate").toLocalDate();
            int age = resultSet.getInt("age");
            String category = resultSet.getString("category");
            String contactInfo = resultSet.getString("contact_info");
            String medicalHistory = resultSet.getString("medical_history");
            String healthStatus = resultSet.getString("health_status");

            // Only add patients with "Student" category
            if ("Student".equalsIgnoreCase(category)) {
                // Create a Patient object with the id included
                Patient patient = new Patient(id, patientId, firstName, lastName, gender, birthDate, age, category, contactInfo, medicalHistory, healthStatus);
                patientList.add(patient);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    table.setItems(patientList);
}


    private void loadTableDatas(String searchTerm) {
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    String query;

    // Check if there is a search term and adjust the query accordingly
    if (searchTerm != null && !searchTerm.isEmpty()) {
        query = "SELECT * FROM PATIENTS WHERE first_name LIKE ? OR last_name LIKE ? OR patients_id LIKE ? AND category = 'Student'";
    } else {
        query = "SELECT * FROM PATIENTS WHERE category = 'Student'";
    }

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); 
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        // If searching, set the search term parameters
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);  // First Name
            preparedStatement.setString(2, searchPattern);  // Last Name
            preparedStatement.setString(3, searchPattern);  // Patient ID

        }
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Fetch data including the 'id' field
                int id = resultSet.getInt("id");
                String patientId = resultSet.getString("patients_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                LocalDate birthDate = resultSet.getDate("birthdate").toLocalDate();
                int age = resultSet.getInt("age");
                String category = resultSet.getString("category");
                String contactInfo = resultSet.getString("contact_info");
                String medicalHistory = resultSet.getString("medical_history");
                String healthStatus = resultSet.getString("health_status");

                // Only add patients with "Student" category
                if ("Student".equalsIgnoreCase(category)) {
                    // Create a Patient object with the id included
                    Patient patient = new Patient(id, patientId, firstName, lastName, gender, birthDate, age, category, contactInfo, medicalHistory, healthStatus);
                    patientList.add(patient);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Update the table view with the filtered list
    table.setItems(patientList);
}

    
        @FXML
    void Search(ActionEvent event) {
        // Get the search term entered by the user
        String searchTerm = searchTF.getText().trim();

        // If the search term is not empty, pass it to the loadTableData method
        if (!searchTerm.isEmpty()) {
            loadTableDatas(searchTerm);
        } else {
            // If no search term, load all data
            loadTableDatas(null);
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
