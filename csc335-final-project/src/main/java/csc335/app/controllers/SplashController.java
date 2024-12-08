package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/** 
 * The SplashController class manages the splash screen of the application. 
 * It displays a welcome message and handles navigation to the login page 
 * when the user clicks the arrow button. This class implements the Initializable interface to set up the splash screen 
 * when the application launches.
 * 
 * File: SplashController.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public class SplashController implements Initializable {

    @FXML
    private ImageView arrow; // JavaFX element that displays a button

    /**
     * This method is called automatically when the splash screen is loaded. 
     * It displays a welcome message in the console to greet the user 
     * and provides instructions for proceeding.
     * 
     * @param location  The location of the FXML file (not used here).
     * @param resources The resources used for localization (not used here).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to Finantra, the Personal Finance Assistant!\n \nClick the white arrow to continue.\n");
        arrow.setOnMouseClicked(_ -> {
            View.LOGIN.loadView();
        });
    }

}