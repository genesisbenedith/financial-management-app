package csc335.app;

import csc335.app.controllers.View;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * The main entry point for the JavaFX application.
 * This class extends the JavaFX Application class and is responsible
 * for launching the application and displaying the initial view.

 * File: App.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public final class App extends Application {

    /**
     * Initializes the main user interface for the application.
     * This method is called when the JavaFX application is launched.
     * It loads the splash screen view to show the initial UI.
     * 
     * @param stage The primary stage for this application, onto which the application scene is set.
     */
    @Override
    public void start(Stage stage) {
        System.out.println("Starting application...");
        View.SPLASH.loadView();

    }

    /**
     * The main method which serves as the entry point for the application.
     * It launches the JavaFX application by calling the launch method of the Application class.
     * 
     * @param args Command line arguments passed to the application, if any.
     */
    public static void main(String[] args) {
        App.launch(args);
    }
}