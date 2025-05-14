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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Medical_ProfileController implements Initializable {

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

    @FXML
    private TableView<MedicalProfile> medicalProfileTable;

    @FXML
    private TableColumn<MedicalProfile, Integer> profileIdCol;

    @FXML
    private TableColumn<MedicalProfile, String> patientNameCol;

    @FXML
    private TableColumn<MedicalProfile, String> bloodTypeCol;

    @FXML
    private TableColumn<MedicalProfile, String> allergiesCol;

    @FXML
    private TableColumn<MedicalProfile, String> conditionsCol;

    @FXML
    private TableColumn<MedicalProfile, String> vaccinationsCol;

    private ObservableList<MedicalProfile> medicalProfiles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize table columns with the appropriate property names
        profileIdCol.setCellValueFactory(new PropertyValueFactory<>("profileId"));
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        allergiesCol.setCellValueFactory(new PropertyValueFactory<>("allergies"));
        conditionsCol.setCellValueFactory(new PropertyValueFactory<>("preExistingConditions"));
        vaccinationsCol.setCellValueFactory(new PropertyValueFactory<>("vaccinations"));

        // Load data into the table
        loadMedicalProfileData();
    }

    private void loadMedicalProfileData() {
        medicalProfiles = FXCollections.observableArrayList();
        String query = "SELECT mp.profile_id, CONCAT(p.first_name, ' ', p.last_name) AS name, "
                + "mp.blood_type, mp.allergies, mp.pre_existing_conditions, mp.vaccinations "
                + "FROM MEDICAL_PROFILE mp "
                + "JOIN PATIENTS p ON mp.id = p.id";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int profileId = resultSet.getInt("profile_id");
                String name = resultSet.getString("name");
                String bloodType = resultSet.getString("blood_type");
                String allergies = resultSet.getString("allergies");
                String preExistingConditions = resultSet.getString("pre_existing_conditions");
                String vaccinations = resultSet.getString("vaccinations");

                // Create and add the MedicalProfile object
                MedicalProfile medicalProfile = new MedicalProfile(
                        profileId, name, bloodType, allergies, preExistingConditions, vaccinations);
                medicalProfiles.add(medicalProfile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the data in the table view
        medicalProfileTable.setItems(medicalProfiles);
    }

}
