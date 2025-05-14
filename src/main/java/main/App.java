package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the Login.fxml file initially
        scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        // Load the new FXML file
        Parent root = loadFXML(fxml);
        // Set the root for the current scene
        scene.setRoot(root);
        // Dynamically adjust the scene size to match the new FXML layout
        scene.getWindow().setWidth(root.prefWidth(-1));
        scene.getWindow().setHeight(root.prefHeight(-1));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Ensure the FXML file is correctly located
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
