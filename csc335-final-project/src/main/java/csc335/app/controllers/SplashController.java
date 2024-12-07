package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/** 
 * The SplashController class manages the splash screen of the application. 
 * It displays a welcome message and handles navigation to the login page 
 * when the user clicks the arrow button. This class implements the Initializable interface to set up the splash screen 
 * when the application launches.
 * File: SplashController.java
 * 
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

    }

    /**
     * Handles the click event for the arrow button. 
     * When the user clicks the arrow, this method loads the login page.
     * 
     * @param event The MouseEvent triggered when the arrow is clicked.
     */
    @FXML
    private void handleArrowClick(MouseEvent event) {
        View.LOGIN.loadView();
    }

}