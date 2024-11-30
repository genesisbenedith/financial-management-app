package csc335.app.controllers;

/**
 * Author(s): Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: SignUpController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.regex.Pattern;

import csc335.app.App;
import csc335.app.FileIOManager;

public class SignUpController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    /**
     * 
     */
    @FXML
    public void initialize() {
        System.out.println("SignUpController initialized.");
    }

    /**
     * Handles switching to the SignInView when "Sign In" is clicked or when method is invoked.
     * 
     * @throws IOException
     */
    @FXML
    private void goToSignIn() throws IOException {
        System.out.println("Navigating to Sign-In page...");

        // Load the Sign-In view
        FXMLLoader signInViewLoader = new FXMLLoader(getClass().getResource("/views/SignInView.fxml"));
        Parent rootContainer = signInViewLoader.load();

        // Set app window to show Sign-In scene
        Scene signInScene = new Scene(rootContainer);
        App.setScene(signInScene);
    }

    /**
     * Handles input validation for Sign-Up form when "Create Account" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void createUserAccount() throws IOException {
        String email = null;
        String username = null;
        String password = null;

        if (!validateFields()) {
            return;
        }

        email = emailField.getText().trim();
        username = usernameField.getText().trim();
        password = passwordField.getText().trim();

        // Check if email is already in use
        if (FileIOManager.isEmailTaken(email)) {
            showAlert(AlertType.ERROR, "Error", "Email is already in use.");
            return;
        }

        // Check if username is already taken
        if (FileIOManager.isUsernameTaken(username)) {
            showAlert(AlertType.ERROR, "Error", "Username is already taken.");
            return;
        }

        if (FileIOManager.createUserFile(username, email, password)) {
            showAlert(AlertType.INFORMATION, "Success", "Account created successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user data.");
        }

        goToSignIn();

    }

    /**
     * 
     * @return
     * @throws IOException
     */
    private boolean validateFields() throws IOException {
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Check if any field is empty
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(AlertType.ERROR, "Error", "Please enter a valid email address.");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
            return false;
        }

        return true;
    }

    /**
     * 
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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
