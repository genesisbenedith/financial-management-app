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

    private static Stage appWindowStage;

    /**
     * 
     */
    @Override
    public void start(Stage stage) throws Exception {

        setStage(stage);

        // Load the Main view
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        Parent rootContainer = mainViewLoader.load();

        // Set up & configure the scene for the app window
        Scene scene = new Scene(rootContainer);
        setScene(scene);
        appWindowStage.setTitle("Financial Management App");
        appWindowStage.show();

        System.out.println("Application is now running...");
    }

    public static void setStage(Stage stage) {
        appWindowStage = stage;
    }

    public static void setScene(Scene scene) {
        if (appWindowStage != null) {
            appWindowStage.setScene(scene);
        } else {
            throw new IllegalStateException("App occurred an error. The stage is not initialized!");
        }
    }

    

    /*
     * 
     */
    public static void main(String[] args) {
        launch(args);
    }
}
