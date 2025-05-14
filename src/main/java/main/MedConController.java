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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedConController implements Initializable {

    @FXML
    private TableColumn<MedicalConsultation, Integer> ConsultColId;

    @FXML
    private TableColumn<MedicalConsultation, String> PatientnameIdcol;

    @FXML
    private TableColumn<MedicalConsultation, String> ConsultDateCol;

    @FXML
    private TableColumn<MedicalConsultation, String> ConsultTypeCol;

    @FXML
    private TableColumn<MedicalConsultation, String> DiagnosisCol;

    @FXML
    private TableColumn<MedicalConsultation, String> PresTreatCol;

    @FXML
    private TableView<MedicalConsultation> TableConsult;

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
    private Button btnCreatePresc;

    @FXML
    void CreatePresc(ActionEvent event) {
        // Get the selected consultation from the table
        MedicalConsultation selectedConsultation = TableConsult.getSelectionModel().getSelectedItem();
        if (selectedConsultation == null) {
            // Show an alert if no consultation is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Consultation Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a consultation from the table.");
            alert.showAndWait();
            return;
        }

        // Prompt for dosage
        TextInputDialog dosageDialog = new TextInputDialog();
        dosageDialog.setTitle("Create Prescription");
        dosageDialog.setHeaderText("Enter Prescription Details for Consultation ID: " + selectedConsultation.getConsultationId());
        dosageDialog.setContentText("Dosage:");
        Optional<String> dosageResult = dosageDialog.showAndWait();

        if (!dosageResult.isPresent() || dosageResult.get().isEmpty()) {
            return; // Cancelled or empty input
        }
        String dosage = dosageResult.get();

        // Prompt for frequency
        TextInputDialog frequencyDialog = new TextInputDialog();
        frequencyDialog.setTitle("Create Prescription");
        frequencyDialog.setHeaderText(null);
        frequencyDialog.setContentText("Frequency:");
        Optional<String> frequencyResult = frequencyDialog.showAndWait();

        if (!frequencyResult.isPresent() || frequencyResult.get().isEmpty()) {
            return; // Cancelled or empty input
        }
        String frequency = frequencyResult.get();

        // Prompt for duration
        TextInputDialog durationDialog = new TextInputDialog();
        durationDialog.setTitle("Create Prescription");
        durationDialog.setHeaderText(null);
        durationDialog.setContentText("Duration:");
        Optional<String> durationResult = durationDialog.showAndWait();

        if (!durationResult.isPresent() || durationResult.get().isEmpty()) {
            return; // Cancelled or empty input
        }
        String duration = durationResult.get();

        // Save prescription to the database
        String query = "INSERT INTO PRESCRIPTION (consultation_id, dosage, frequency, duration) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedConsultation.getConsultationId());
            preparedStatement.setString(2, dosage);
            preparedStatement.setString(3, frequency);
            preparedStatement.setString(4, duration);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Prescription Created");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Prescription successfully created for Consultation ID: " + selectedConsultation.getConsultationId());
                successAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while creating the prescription.");
            errorAlert.showAndWait();
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up column bindings
        ConsultColId.setCellValueFactory(new PropertyValueFactory<>("consultationId"));
        PatientnameIdcol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        ConsultDateCol.setCellValueFactory(new PropertyValueFactory<>("consultationDate"));
        ConsultTypeCol.setCellValueFactory(new PropertyValueFactory<>("consultationType"));
        DiagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        PresTreatCol.setCellValueFactory(new PropertyValueFactory<>("prescribedTreatment"));

        // Load data
        loadConsultations();
        
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

    private void loadConsultations() {
        ObservableList<MedicalConsultation> consultations = FXCollections.observableArrayList();

        String query
                = "SELECT mc.consultation_id, CONCAT(p.first_name, ' ', p.last_name) AS patient_name, "
                + "mc.consultation_date, mc.consultation_type, mc.diagnosis, mc.prescribed_treatment "
                + "FROM MEDICAL_CONSULTATION mc "
                + "JOIN PATIENTS p ON mc.id = p.id";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int consultationId = resultSet.getInt("consultation_id");
                String patientName = resultSet.getString("patient_name");
                String consultationDate = resultSet.getString("consultation_date");
                String consultationType = resultSet.getString("consultation_type");
                String diagnosis = resultSet.getString("diagnosis");
                String prescribedTreatment = resultSet.getString("prescribed_treatment");

                consultations.add(new MedicalConsultation(consultationId, patientName, consultationDate, consultationType, diagnosis, prescribedTreatment));
            }

            TableConsult.setItems(consultations);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load consultations.");
            alert.showAndWait();
        }
    }
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

}
