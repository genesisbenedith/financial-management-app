package csc335.app.controllers;

/**
 * Author(s): Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: SignInController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import csc335.app.App;
import csc335.app.FileIOManager;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        System.out.println("SignInController initialized.");

    }

    /* ------------------------------ Action Events ------------------------------ */

    /**
     * Handles switching to the SignUpView when "Sign Up" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void goToSignUp() throws IOException {
        System.out.println("Navigating to Sign-Up page...");

        // Load the Sign-Up view
        FXMLLoader signUpViewLoader = new FXMLLoader(getClass().getResource("/views/SignUpiew.fxml"));
        Parent rootContainer = signUpViewLoader.load();

        // Set app window to show Sign-Up scene
        Scene signUpScene = new Scene(rootContainer);
        App.setScene(signUpScene);
    }

    /**
     * Handles user authentication when "Sign In" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void logUserIn() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
            return;
        }

        // Authenticate user
        boolean isAuthenticated = FileIOManager.validateUserLogIn(username, password);
        if (isAuthenticated) {
            System.out.println("User authenticated successfully.");

            goToDashboard();

        } else {
            showAlert(AlertType.ERROR, "Authentication Failed", "Invalid username or password.");
        }

    }

    /* ------------------------------ Other Methods ------------------------------ */

    /**
     * Handles switching to the DashboardView.
     * 
     * @throws IOException
     */
    private void goToDashboard() throws IOException {
        System.out.println("Navigating to Dashboard page...");

        // Load the Dashboard view
        FXMLLoader dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
        Parent rootContainer = dashboardViewLoader.load();

        // Set app window to show Dashboard scene
        Scene dashboardScene = new Scene(rootContainer);
        App.setScene(dashboardScene);
    }

    /**
     * 
     * @param alertType
     * @param title
     * @param message
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
