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
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminHomeController implements Initializable {

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
    private TableColumn<Patient, String> dateAdded;

    @FXML
    private TextField FnameTF, LnameTF, contactTF, pIdTF, idTF;

    @FXML 
    private TextArea MedhisTF, remark;
    
    @FXML
    private TextField searchTF;

    @FXML
    private ComboBox<String> CategorycomboBox, healthStatusCom;

    @FXML
    private DatePicker DoBTF;

    @FXML
    private CheckBox Femalechkbx, Malechkbx;

    @FXML
    private TableColumn<Patient, Void> remarks;

    ObservableList<String> categories = FXCollections.observableArrayList("Student", "Faculty","Staff");
    ObservableList<String> healthStatusCat = FXCollections.observableArrayList("Below Normal", "Normal","Abnormal");

    @FXML
    private TableColumn<Patient, Integer> Id; // Column for id

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MedhisTF.setWrapText(true);remark.setWrapText(true);
        
        Malechkbx.setOnAction(e -> {
            if (Malechkbx.isSelected()) Femalechkbx.setSelected(false);
        });

        Femalechkbx.setOnAction(e -> {
            if (Femalechkbx.isSelected()) Malechkbx.setSelected(false);
        });
        
        // Populate ComboBox with category options
        CategorycomboBox.setItems(categories);
        healthStatusCom.setItems(healthStatusCat);

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
        Id.setCellValueFactory(new PropertyValueFactory<>("id")); 
        dateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded")); 

        loadMedicalProfileData();
        
        // Load data into TableView
        loadTableData();
        
//        table.setRowFactory(tv -> {
//            TableRow<Patient> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    Patient clickedPatient = row.getItem();
//                    int patientId = clickedPatient.getId();
//                    showMedicalProfileDialog(patientId);
//                }
//            });
//            return row;
//        });


    }
    private ObservableList<Patient> fullPatientList = FXCollections.observableArrayList();

    @FXML
    private void onSearchKeyReleased(KeyEvent event) {
        String filter = searchTF.getText().toLowerCase();

        FilteredList<Patient> filteredData = new FilteredList<>(fullPatientList, patient -> {
            if (filter == null || filter.isEmpty()) {
                return true;
            }

            return patient.getFirstName().toLowerCase().contains(filter) ||
                   patient.getLastName().toLowerCase().contains(filter) ||
                   patient.getPatientId().toLowerCase().contains(filter) ||
                   patient.getGender().toLowerCase().contains(filter) ||
                   patient.getCategory().toLowerCase().contains(filter) ||
                   patient.getContactInfo().toLowerCase().contains(filter) ||
                   patient.getMedicalHistory().toLowerCase().contains(filter) ||
                   patient.getHealthStatus().toLowerCase().contains(filter);
        });

        table.setItems(filteredData);
    }

    private void showMedicalProfileDialog(int patientId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "")) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM medical_profile WHERE id = ? ORDER BY created_at DESC");
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            List<MedicalProfile> profiles = new ArrayList<MedicalProfile>();
            while (rs.next()) {
                MedicalProfile profile = new MedicalProfile(
                    rs.getInt("profile_id"),
                    rs.getString("id"),
                    rs.getString("blood_type"),
                    rs.getString("allergies"),
                    rs.getString("pre_existing_conditions"),
                    rs.getString("vaccinations"),
                    rs.getString("created_at")
                );
                profiles.add(profile);
            }

            if (profiles.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Records");
                alert.setHeaderText("No medical profiles found.");
                alert.showAndWait();
            } else {
                // Create content VBox
                VBox contentBox = new VBox(10);
                contentBox.setPadding(new Insets(10));

                for (MedicalProfile profile : profiles) {
                    VBox card = new VBox(5);
                    card.setStyle("-fx-padding: 10; -fx-background-color: #f8f8f8; -fx-border-color: #ccc; -fx-border-radius: 5;");
                    card.getChildren().addAll(
                        new Label("Blood Type: " + profile.getBloodType()),
                        new Label("Allergies: " + profile.getAllergies()),
                        new Label("Pre-existing Conditions: " + profile.getPreExistingConditions()),
                        new Label("Vaccinations: " + profile.getVaccinations()),
                        new Label("Created_at: " + profile.getCreatedAt())
                    );
                    contentBox.getChildren().add(card);
                }

                // Make scrollable
                ScrollPane scrollPane = new ScrollPane(contentBox);
                scrollPane.setFitToWidth(true);
                scrollPane.setPrefViewportHeight(400); // dialog height

                // Create Dialog
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Medical Profiles");
                dialog.getDialogPane().setContent(scrollPane);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                dialog.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML private Label FacultyCount,StudentCount,StaffCount, Overall;
    int facultyCount=0,studentCount=0,staffCount=0;
    private void loadTableData() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        String query = "SELECT * FROM PATIENTS ORDER BY date_added DESC";
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
                String studremarks = resultSet.getString("remarks");
                String dateadded = resultSet.getString("date_added");
                if(category.equals("Faculty")){
                    facultyCount++;
                }else if(category.equals("Student")){
                    studentCount++;
                }else if(category.equals("Staff")){
                    staffCount++;
                }
                // Create a Patient object with the id included
                Patient patient = new Patient(id, patientId, firstName, lastName, gender, birthDate, 
                        age, category, contactInfo, medicalHistory, healthStatus, studremarks, dateadded);
                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyCount.setText("Faculty: "+facultyCount);
        StudentCount.setText("Student: "+studentCount);
        StaffCount.setText("Staff: "+staffCount);
        Overall.setText("Overall: "+(staffCount+studentCount+facultyCount));
        table.setItems(patientList);
        fullPatientList.addAll(patientList);
        table.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>() {
                @Override
                protected void updateItem(Patient patient, boolean empty) {
                    super.updateItem(patient, empty);

                    if (patient == null || empty) {
                        setStyle(""); 
                    } else {
                        if (ids.contains(String.valueOf(patient.getId()))) {
                            setStyle("-fx-background-color: #ffe6e6;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Patient clickedPatient = row.getItem();
                    int patientId = clickedPatient.getId();
                    showMedicalProfileDialog(patientId);
                }
            });

            return row;
        });
        remarks.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, viewButton, deleteButton); // Add spacing between buttons

            {
                viewButton.setStyle("-fx-background-color: #87CEFA; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");

                viewButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    showRemarkDialog(patient); // pass patient object now
                });


                deleteButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    deleteRemark(patient); // This is the method you will define
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Patient patient = getTableView().getItems().get(getIndex());
                if (patient.getRemarks() != null && !patient.getRemarks().isBlank()) {
                    setGraphic(buttonBox);
                } else {
                    setGraphic(null);
                }
            }
        });


    }
    private void deleteRemark(Patient patient) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Are you sure you want to delete this remark?");
        confirm.setContentText("This cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("UPDATE patients SET remarks = NULL WHERE id = ?")) {

                stmt.setInt(1, patient.getId());
                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    loadTableData();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Database Error");
                error.setHeaderText("Could not delete remark.");
                error.setContentText(ex.getMessage());
                error.showAndWait();
            }
        }
    }

    private void showRemarkDialog(Patient patient) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Patient Remark");
        dialog.setHeaderText("View or Edit Remark");

        // Set button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create a TextArea to allow multi-line editing
        TextArea remarkArea = new TextArea();
        remarkArea.setWrapText(true);
        remarkArea.setPrefRowCount(5);
        remarkArea.setText(patient.getRemarks() != null ? patient.getRemarks() : "");

        dialog.getDialogPane().setContent(remarkArea);

        // Convert result when Save is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return remarkArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newRemark -> {
            // Update database
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("UPDATE patients SET remarks = ? WHERE id = ?")) {

                stmt.setString(1, newRemark);
                stmt.setInt(2, patient.getId());
                int updated = stmt.executeUpdate();

                if (updated > 0) {
                    loadTableData();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Database Error");
                error.setHeaderText("Could not update remark.");
                error.setContentText(ex.getMessage());
                error.showAndWait();
            }
        });
    }


    
    private void loadTableDatas(String searchTerm) {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();
        String query;

        // Check if there is a search term and adjust the query accordingly
        if (searchTerm != null && !searchTerm.isEmpty()) {
            query = "SELECT * FROM PATIENTS WHERE first_name LIKE ? OR last_name LIKE ? OR patients_id LIKE ? OR category LIKE ?";
        } else {
            query = "SELECT * FROM PATIENTS";
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // If searching, set the search term parameters
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                preparedStatement.setString(1, searchPattern);  // First Name
                preparedStatement.setString(2, searchPattern);  // Last Name
                preparedStatement.setString(3, searchPattern);  // Patient ID
                preparedStatement.setString(4, searchPattern);  // category
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
                    String studremarks = resultSet.getString("remarks");
                    String dateadded = resultSet.getString("date_added");

                    // Create a Patient object with the id included
                    Patient patient = new Patient(id, patientId, firstName, lastName, gender, birthDate,
                            age, category, contactInfo, medicalHistory, healthStatus, studremarks, dateadded);
                    patientList.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the table view with the filtered list
        table.setItems(patientList);
        table.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>() {
                @Override
                protected void updateItem(Patient patient, boolean empty) {
                    super.updateItem(patient, empty);

                    if (patient == null || empty) {
                        setStyle(""); // reset style
                    } else {
                        if (ids.contains(String.valueOf(patient.getId()))) {
                            setStyle("-fx-background-color: #ffe6e6;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Patient clickedPatient = row.getItem();
                    int patientId = clickedPatient.getId();
                    showMedicalProfileDialog(patientId);
                }
            });

            return row;
        });

    }

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnDelete;

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
    private Button btnPatients;

    @FXML
    private Button btnPresc;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnStdRec;

    @FXML
    private Button btnMedC;

    @FXML
    private Button btnMp;

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
    void Add(ActionEvent event) {
        String patientId = pIdTF.getText();
        String firstName = FnameTF.getText();
        String lastName = LnameTF.getText();
        String gender = null;
        String studremark = remark.getText()+"";

        if (Malechkbx.isSelected()) {
            gender = "Male";
        } else if (Femalechkbx.isSelected()) {
            gender = "Female";
        }

        LocalDate birthDate = DoBTF.getValue();
        String category = CategorycomboBox.getValue();
        String contactInfo = contactTF.getText();
        String medicalHistory = MedhisTF.getText();
        String healthStatus = healthStatusCom.getValue();

        // Check for missing or invalid input
        if (patientId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            gender == null || birthDate == null ||
            category == null || contactInfo.isEmpty() ||
            medicalHistory.isEmpty() || healthStatus.isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
            return;
        }
        // Validate Philippine contact number
        if (!contactInfo.matches("^(09\\d{9}|\\+639\\d{9})$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Contact Number", "Please enter a valid Philippine mobile number (e.g., 09123456789 or +639123456789).");
            return;
        }
        
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        // Save to database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", "")) {
            String query = "INSERT INTO PATIENTS (patients_id, first_name, last_name, gender, birthdate, age, category, contact_info, medical_history, health_status, remarks) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, gender);
            preparedStatement.setDate(5, Date.valueOf(birthDate));
            preparedStatement.setInt(6, age);
            preparedStatement.setString(7, category);
            preparedStatement.setString(8, contactInfo);
            preparedStatement.setString(9, medicalHistory);
            preparedStatement.setString(10, healthStatus);
            preparedStatement.setString(11, studremark);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient details added successfully.");
                loadTableData();
                ClearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add patient details.");
        }
    }


    void ClearFields() {
        pIdTF.clear();
        FnameTF.clear();
        LnameTF.clear();
        Malechkbx.setSelected(false);
        Femalechkbx.setSelected(false);
        DoBTF.setValue(null);
        CategorycomboBox.setValue(null);
        healthStatusCom.setValue(null);
        contactTF.clear();
        MedhisTF.clear();
    }

    @FXML
    void Delete(ActionEvent event) {
        // Get the selected patient from the table
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Confirm deletion with the user (you can add a dialog for confirmation)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Patient");
            alert.setHeaderText("Are you sure you want to delete this patient?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Perform the deletion
                String deleteQuery = "DELETE FROM PATIENTS WHERE id = ?";

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                    preparedStatement.setInt(1, selectedPatient.getId());
                    int result1 = preparedStatement.executeUpdate();
                    if (result1 > 0) {
                        // Reload data to reflect changes
                        loadTableData();
                        // Optionally show a success message
                    } else {
                        // Optionally show an error message
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void Edit(ActionEvent event) {
        // Get the selected patient from the table
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Populate the fields with the selected patient's details
            pIdTF.setText(selectedPatient.getPatientId());
            FnameTF.setText(selectedPatient.getFirstName());
            LnameTF.setText(selectedPatient.getLastName());

            // Set gender (assuming you have checkboxes for Male/Female)
            if (selectedPatient.getGender().equals("Female")) {
                Femalechkbx.setSelected(true);
            } else {
                Malechkbx.setSelected(true);
            }

            // Set birthdate
            DoBTF.setValue(selectedPatient.getBirthDate());

            // Set other fields
            CategorycomboBox.setValue(selectedPatient.getCategory());
            healthStatusCom.setValue(selectedPatient.getHealthStatus());
            contactTF.setText(selectedPatient.getContactInfo());
            MedhisTF.setText(selectedPatient.getMedicalHistory());
        }
    }

    @FXML
    void Save(ActionEvent event) {
        // Validate if the necessary fields are filled
        if (pIdTF.getText().isEmpty() || FnameTF.getText().isEmpty() || LnameTF.getText().isEmpty()
                || contactTF.getText().isEmpty() ) {
            // Show an error alert if fields are missing
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all the fields.");
            return;
        }

        // Get the selected patient from the table to update
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Prepare the SQL query to update the patient data
            String updateQuery = "UPDATE PATIENTS SET patients_id = ?, first_name = ?, last_name = ?, gender = ?, birthdate = ?, age = ?, category = ?, contact_info = ?, medical_history = ?, health_status = ? WHERE id = ?";

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                // Set the updated values from the form fields
                preparedStatement.setString(1, pIdTF.getText());
                preparedStatement.setString(2, FnameTF.getText());
                preparedStatement.setString(3, LnameTF.getText());
                preparedStatement.setString(4, Femalechkbx.isSelected() ? "Female" : "Male"); // Set gender based on checkbox
                LocalDate birthDate = DoBTF.getValue(); 
                preparedStatement.setDate(5, Date.valueOf(birthDate));
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                preparedStatement.setInt(6, age);
                preparedStatement.setString(7, CategorycomboBox.getValue());
                preparedStatement.setString(8, contactTF.getText());
                preparedStatement.setString(9, MedhisTF.getText());
                preparedStatement.setString(10, healthStatusCom.getValue());
                preparedStatement.setInt(11, selectedPatient.getId());

                // Execute the update
                int result = preparedStatement.executeUpdate();

                // Check if the update was successful and show an alert
                if (result > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient details updated successfully.");
                    // Reload the table data to reflect changes
                    loadTableData();
                    ClearFields(); // Clear fields after successful insertion
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patient details.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving the data.");
            }
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void MedicalProfile(ActionEvent event) {
        // Get the selected patient from the table
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            // Show an alert if no patient is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient from the table.");
            alert.showAndWait();
            return;
        }

        // Valid blood types
        Set<String> validBloodTypes = new HashSet<>(Arrays.asList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));

        String bloodType = null;

        while (true) {
            TextInputDialog bloodTypeDialog = new TextInputDialog();
            bloodTypeDialog.setTitle("Medical Profile");
            bloodTypeDialog.setHeaderText("Enter Medical Profile for " + selectedPatient.getFirstName() + " " + selectedPatient.getLastName());
            bloodTypeDialog.setContentText("Blood Type (A+, A-, B+, B-, AB+, AB-, O+, O-):");

            Optional<String> bloodTypeResult = bloodTypeDialog.showAndWait();

            if (!bloodTypeResult.isPresent() || bloodTypeResult.get().trim().isEmpty()) {
                return; // Cancelled or empty input
            }

            String input = bloodTypeResult.get().toUpperCase().trim();

            if (validBloodTypes.contains(input)) {
                bloodType = input;
                break;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid Blood Type");
                alert.setContentText("Please enter a valid blood type: A+, A-, B+, B-, AB+, AB-, O+, or O-.");
                alert.showAndWait();
            }
        }

        // bloodType now contains a valid value


        // Additional dialogs for allergies, conditions, vaccinations
        TextInputDialog allergiesDialog = new TextInputDialog();
        allergiesDialog.setTitle("Medical Profile");
        allergiesDialog.setHeaderText(null);
        allergiesDialog.setContentText("Allergies:");
        Optional<String> allergiesResult = allergiesDialog.showAndWait();

        String allergies = allergiesResult.orElse("");

        TextInputDialog conditionsDialog = new TextInputDialog();
        conditionsDialog.setTitle("Medical Profile");
        conditionsDialog.setHeaderText(null);
        conditionsDialog.setContentText("Pre-Existing Conditions:");
        Optional<String> conditionsResult = conditionsDialog.showAndWait();

        String preExistingConditions = conditionsResult.orElse("");

        TextInputDialog vaccinationsDialog = new TextInputDialog();
        vaccinationsDialog.setTitle("Medical Profile");
        vaccinationsDialog.setHeaderText(null);
        vaccinationsDialog.setContentText("Vaccinations:");
        Optional<String> vaccinationsResult = vaccinationsDialog.showAndWait();

        String vaccinations = vaccinationsResult.orElse("");

        // Save the collected data to the MEDICAL_PROFILE table
        String query = "INSERT INTO MEDICAL_PROFILE (id, blood_type, allergies, pre_existing_conditions, vaccinations) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedPatient.getId()); // Reference to PATIENTS(id)
            preparedStatement.setString(2, bloodType);
            preparedStatement.setString(3, allergies);
            preparedStatement.setString(4, preExistingConditions);
            preparedStatement.setString(5, vaccinations);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Medical Profile Saved");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Medical profile successfully saved for " + selectedPatient.getFirstName() + " " + selectedPatient.getLastName());
                successAlert.showAndWait();
                loadMedicalProfileData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while saving the medical profile.");
            errorAlert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Medical Consultation");
        alert.setHeaderText("Do you want to proceed with medical consultation?");
        alert.setContentText("This action will open the MedicalConsult process.");

        // Customize button types
        ButtonType proceedButton = new ButtonType("Proceed");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(proceedButton, cancelButton);

        // Show dialog and wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == proceedButton) {
            runMedicalConsult();
        }
    }
    private void runMedicalConsult() {
        // Get the selected patient from the table
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            // Show an alert if no patient is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient from the table.");
            alert.showAndWait();
            return;
        }

        // Create a DatePicker
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("YYYY-MM-DD");

        // Build a custom dialog
        Dialog<LocalDate> consultationDateDialog = new Dialog<>();
        consultationDateDialog.setTitle("Medical Consultation");
        consultationDateDialog.setHeaderText("Enter Medical Consultation Date for " 
            + selectedPatient.getFirstName() + " " + selectedPatient.getLastName());

        // Set content: DatePicker inside a VBox
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Consultation Date:"), datePicker);
        consultationDateDialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        consultationDateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result when OK is clicked
        consultationDateDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datePicker.getValue();
            }
            return null;
        });

        // Show the dialog and wait for result
        Optional<LocalDate> consultationDateResult = consultationDateDialog.showAndWait();

        // Check if a date was selected
        if (!consultationDateResult.isPresent() || consultationDateResult.get() == null) {
            return; // User cancelled or left it empty
        }

        // Convert to string if needed
        String consultationDate = consultationDateResult.get().toString();

        // Create the dropdown (ComboBox) with predefined options
        ComboBox<String> consultationTypeCombo = new ComboBox<>();
        consultationTypeCombo.getItems().addAll("Regular Checkup", "Emergency");
        consultationTypeCombo.setPromptText("Select Consultation Type");

        // Create the custom dialog
        Dialog<String> consultationTypeDialog = new Dialog<>();
        consultationTypeDialog.setTitle("Medical Consultation");
        consultationTypeDialog.setHeaderText("Select Consultation Type");

        // Set the dialog content
        VBox content2 = new VBox(10);
        content2.getChildren().addAll(new Label("Consultation Type:"), consultationTypeCombo);
        consultationTypeDialog.getDialogPane().setContent(content2);

        // Add OK and Cancel buttons
        consultationTypeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result when OK is clicked
        consultationTypeDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return consultationTypeCombo.getValue();
            }
            return null;
        });

        // Show the dialog and wait for result
        Optional<String> consultationTypeResult = consultationTypeDialog.showAndWait();
        String consultationType = consultationTypeResult.orElse("");


        // Diagnosis Dialog
        TextInputDialog diagnosisDialog = new TextInputDialog();
        diagnosisDialog.setTitle("Medical Consultation");
        diagnosisDialog.setHeaderText(null);
        diagnosisDialog.setContentText("Diagnosis:");
        Optional<String> diagnosisResult = diagnosisDialog.showAndWait();

        String diagnosis = diagnosisResult.orElse("");

        // Create the dropdown (ComboBox) with options
        ComboBox<String> treatmentCombo = new ComboBox<>();
        treatmentCombo.getItems().addAll("Follow Up (1 WEEK)", "No Follow Up");
        treatmentCombo.setPromptText("Select Prescribed Treatment");

        // Create the custom dialog
        Dialog<String> treatmentDialog = new Dialog<>();
        treatmentDialog.setTitle("Medical Consultation");
        treatmentDialog.setHeaderText("Select Prescribed Treatment");

        // Set the dialog content
        VBox content3 = new VBox(10);
        content3.getChildren().addAll(new Label("Remarks:"), treatmentCombo);
        treatmentDialog.getDialogPane().setContent(content3);

        // Add OK and Cancel buttons
        treatmentDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Optional: Disable OK until an item is selected
        Node okButton = treatmentDialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        treatmentCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(newVal == null || newVal.isEmpty());
        });

        // Convert the result when OK is clicked
        treatmentDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return treatmentCombo.getValue();
            }
            return null;
        });

        // Show dialog and get result
        Optional<String> treatmentResult = treatmentDialog.showAndWait();
        String prescribedTreatment = treatmentResult.orElse("");


        // Save the collected data to the MEDICAL_CONSULTATION table
        String query = "INSERT INTO MEDICAL_CONSULTATION (id, consultation_date, consultation_type, diagnosis, prescribed_treatment) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedPatient.getId()); // Reference to PATIENTS(id)
            preparedStatement.setString(2, consultationDate);
            preparedStatement.setString(3, consultationType);
            preparedStatement.setString(4, diagnosis);
            preparedStatement.setString(5, prescribedTreatment);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Consultation Saved");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Medical consultation successfully saved for " + selectedPatient.getFirstName() + " " + selectedPatient.getLastName());
                successAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while saving the medical consultation.");
            errorAlert.showAndWait();
        }
    }


    @FXML
    void MedicalConsult(ActionEvent event) {
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (ids.contains(String.valueOf(selectedPatient.getId()))) {
            runMedicalConsult();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("No Medical Profile");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Create a medical profile for the student to proceed.");
            errorAlert.showAndWait();
        }
    }
    

    ObservableList<String> ids = FXCollections.observableArrayList();
    private void loadMedicalProfileData() {
        String query = "SELECT mp.id,mp.profile_id, CONCAT(p.first_name, ' ', p.last_name) AS name, "
                + "mp.blood_type, mp.allergies, mp.pre_existing_conditions, mp.vaccinations, mp.created_at "
                + "FROM MEDICAL_PROFILE mp "
                + "JOIN PATIENTS p ON mp.id = p.id";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amedic", "root", ""); 
        PreparedStatement preparedStatement = connection.prepareStatement(query); 
        ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String Id = resultSet.getString("id");
                ids.add(Id);
                System.out.println(ids);
                System.out.println(Id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    @FXML
    void Clear(ActionEvent event) {

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
