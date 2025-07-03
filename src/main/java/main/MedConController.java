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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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
                int patientId = selectedConsultation.getConsultationId(); 
                
                Map<String, Integer> medicineMap = new HashMap<>();
                List<String> medicineNames = new ArrayList<>();

                // Load all medicine names with their IDs
                String medQuery = "SELECT medicine_id, name FROM medicine";
                try (PreparedStatement stmt = connection.prepareStatement(medQuery);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        int medId = rs.getInt("medicine_id");
                        String medName = rs.getString("name");
                        medicineMap.put(medName, medId);
                        medicineNames.add(medName);
                        loadPrescriptions();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
//                    showError("Failed to load medicines.");
                    return;
                }

                boolean giveMore = true;

                while (giveMore) {
                    // Show ChoiceDialog with medicine names
                    ChoiceDialog<String> medDialog = new ChoiceDialog<>(medicineNames.get(0), medicineNames);
                    medDialog.setTitle("Give Medicine");
                    medDialog.setHeaderText("Select a medicine to give to the patient:");
                    medDialog.setContentText("Medicine:");

                    Optional<String> selectedName = medDialog.showAndWait();
                    if (selectedName.isEmpty()) break;

                    String medicineName = selectedName.get();
                    int medicineId = medicineMap.get(medicineName);

                    // Ask for quantity
                    TextInputDialog qtyDialog = new TextInputDialog();
                    qtyDialog.setTitle("Quantity");
                    qtyDialog.setHeaderText("Enter quantity for: " + medicineName);
                    qtyDialog.setContentText("Quantity:");

                    Optional<String> qtyResult = qtyDialog.showAndWait();
                    if (qtyResult.isEmpty()) break;

                    int quantity;
                    try {
                        quantity = Integer.parseInt(qtyResult.get());
                    } catch (NumberFormatException e) {
//                        showError("Invalid quantity.");
                        break;
                    }

                    // Insert into givemed
                    String giveQuery = "INSERT INTO givemed (patient_id, medicine_id, quantity, give_date) VALUES (?, ?, ?, NOW())";
                    try (PreparedStatement giveStmt = connection.prepareStatement(giveQuery)) {
                        giveStmt.setInt(1, selectedConsultation.getConsultationId());
                        giveStmt.setInt(2, medicineId);
                        giveStmt.setInt(3, quantity);
                        giveStmt.executeUpdate();
                        System.out.println(""+giveStmt);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        break;
                    }

                    // Ask if user wants to give another
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Give Another?");
                    confirm.setHeaderText("Do you want to give another medicine?");
                    confirm.setContentText(null);
                    confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                    Optional<ButtonType> result = confirm.showAndWait();
                    giveMore = result.isPresent() && result.get() == ButtonType.YES;
                }


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
        
        PrescTbale.setRowFactory(tv -> {
            TableRow<Prescription> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Prescription selected = row.getItem();
                    showPrescriptionReceipt(selected);
                }
            });
            return row;
        });

    }
    private void showPrescriptionReceipt(Prescription prescription) {
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        // Header section
        Label header = new Label("🧾 AMedic Health Center - Prescription Receipt");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        Label patient = new Label("👤 Patient: " + prescription.getPatientName());
        Label consultDate = new Label("📅 Consultation Date: " + prescription.getConsultationDate());
        Label consultType = new Label("🩺 Type: " + prescription.getConsultationType());
        Label diagnosis = new Label("🧠 Diagnosis: " + prescription.getDiagnosis());

        Label prescriptionInfo = new Label("📌 Dosage: " + prescription.getDosage()
                + "\n📌 Frequency: " + prescription.getFrequency()
                + "\n📌 Duration: " + prescription.getDuration());

        contentBox.getChildren().addAll(header, patient, consultDate, consultType, diagnosis, prescriptionInfo);

        // Section title
        Label givenLabel = new Label("💊 Medicines Given:");
        givenLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        contentBox.getChildren().add(givenLabel);

        String query = "SELECT gm.quantity, gm.give_date, m.name, m.brand FROM givemed gm JOIN medicine m ON gm.medicine_id = m.medicine_id WHERE gm.patient_id IN (SELECT id FROM patients WHERE CONCAT(first_name, ' ', last_name) = ?) ORDER BY gm.give_date DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, prescription.getPatientName());
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                String medName = rs.getString("name");
                String brand = rs.getString("brand");
                int qty = rs.getInt("quantity");
                String giveDate = rs.getString("give_date");

                VBox medBox = new VBox(2);
                medBox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-color: #ccc;");
                medBox.getChildren().addAll(
                    new Label("🔹 " + medName + " (Brand: " + brand + ")"),
                    new Label("   Quantity: " + qty),
                    new Label("   Given on: " + giveDate)
                );

                contentBox.getChildren().add(medBox);
            }

            if (!found) {
                Label noMeds = new Label("No medicines were given for this patient.");
                noMeds.setStyle("-fx-font-style: italic;");
                contentBox.getChildren().add(noMeds);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            contentBox.getChildren().add(new Label("⚠ Error retrieving medicine data."));
        }

        // Scrollable container
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(500, 500);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Prescription Receipt");
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
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
