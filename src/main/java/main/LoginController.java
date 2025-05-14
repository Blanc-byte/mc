/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class LoginController implements Initializable {
    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField pfPasswprd;

    @FXML
    private TextField tfUsername;

 @FXML
    void Login(ActionEvent event) {
        // Get the entered username and password
        String username = tfUsername.getText();
        String password = pfPasswprd.getText();

        // Check if the credentials match Admin/admin123
        if ("Admin".equals(username) && "admin123".equals(password)) {
            try {
                // Navigate to AdminHome.fxml
                App.setRoot("Dashboard");
            } catch (IOException e) {
                // Handle errors if AdminHome.fxml fails to load
                showAlert("Error", "Failed to load the AdminHome screen.");
            }
        } else {
            // Show error alert if credentials are incorrect
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
