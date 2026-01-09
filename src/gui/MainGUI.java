package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the program selection window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgramSelection.fxml"));
        Parent root = loader.load();

        // Set up the scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Program Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}