package csc335.app;

/**
 * Author(s): Genesis Benedith
 * File: App.java
 * Description: 
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App extends Application {

    /**
     * 
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Load the initial view (SignInView.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignInView.fxml"));
        Parent root = loader.load();

        // Set up the scene
        Scene scene = new Scene(root);

        // Configure and show the stage
        stage.setScene(scene);
        stage.setTitle("Financial Management App");
        stage.show();

        System.out.println("Application started...");
    }

    /*
     * 
     */
    public static void main(String[] args) {
        launch(args);
    }
}