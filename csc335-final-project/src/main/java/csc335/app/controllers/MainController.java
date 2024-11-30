package csc335.app.controllers;

import java.io.IOException;

import csc335.app.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {

    public void goToSignIn() throws IOException {
        System.out.println("Navigating to Sign-In page...");
        
        // Load the Sign-In view
        FXMLLoader signInViewLoader = new FXMLLoader(getClass().getResource("/views/SignInView.fxml"));
        Parent rootContainer = signInViewLoader.load();

        // Set app window to show Sign-In scene
        Scene signInScene = new Scene(rootContainer);
        App.setScene(signInScene);

    }

}
