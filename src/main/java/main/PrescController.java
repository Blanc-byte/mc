/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrescController implements Initializable {

    @FXML
    private Button btnCreatePresc;

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
    private Button btnStdRec;

    @FXML
    private TableView<Prescription> PrescTbale;  

    @FXML
    private TableColumn<Prescription, Integer> PrescriptionId;

    @FXML
    private TableColumn<Prescription, String> PatientName;

    @FXML
    private TableColumn<Prescription, String> ConsultName;

    @FXML
    private TableColumn<Prescription, String> ConsultationDate;

    @FXML
    private TableColumn<Prescription, String> Diagnosis;

    @FXML
    private TableColumn<Prescription, String> Dosage;

    @FXML
    private TableColumn<Prescription, String> Frequency;

    @FXML
    private TableColumn<Prescription, String> Duration;
    
    private void loadPrescriptions() {
        ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();

        String query
                = "SELECT p.prescription_id, "
                + "       CONCAT(pt.first_name, ' ', pt.last_name) AS patient_name, "
                + "       mc.consultation_type, mc.consultation_date, mc.diagnosis, "
                + "       p.dosage, p.frequency, p.duration "
                + "FROM PRESCRIPTION p "
                + "JOIN MEDICAL_CONSULTATION mc ON p.consultation_id = mc.consultation_id "
                + "JOIN PATIENTS pt ON mc.id = pt.id";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int prescriptionId = resultSet.getInt("prescription_id");
                String patientName = resultSet.getString("patient_name");
                String consultationType = resultSet.getString("consultation_type");
                String consultationDate = resultSet.getString("consultation_date");
                String diagnosis = resultSet.getString("diagnosis");
                String dosage = resultSet.getString("dosage");
                String frequency = resultSet.getString("frequency");
                String duration = resultSet.getString("duration");

                prescriptions.add(new Prescription(prescriptionId, patientName, consultationType, consultationDate, diagnosis, dosage, frequency, duration));
            }

            PrescTbale.setItems(prescriptions);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load prescriptions.");
            alert.showAndWait();
        }
    }    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Bind columns to model properties
        PrescriptionId.setCellValueFactory(new PropertyValueFactory<>("prescriptionId"));
        PatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        ConsultName.setCellValueFactory(new PropertyValueFactory<>("consultationType"));
        ConsultationDate.setCellValueFactory(new PropertyValueFactory<>("consultationDate"));
        Diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        Dosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        Frequency.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        Duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        
        // Load prescriptions into the table
        loadPrescriptions();
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
