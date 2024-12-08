package csc335.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.persistence.AccountManager;
import csc335.app.persistence.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * The SignUpController class manages the sign-up view, allowing new users to create accounts.
 * It handles form validation, user interaction, and communication with the AccountManager
 * to persist user data. It also enables navigation to the login view if users wish to return.
 * 
 * This class implements the Initializable interface to perform setup when the controller is loaded.
 * 
 * File: SignUpController.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public class SignUpController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label signInLabel;

    @FXML
    private Button createAccountButton;

    /**
     * Initializes the Sign-Up view. Sets up event listeners, such as the ability to navigate
     * back to the login view when the "Sign In" label is clicked.
     * 
     * @param location the location of the FXML resource
     * @param resources the resources used to localize the FXML resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignUpController initialized.");

        // Add a click listener to the "Sign In" label to load the login view
        signInLabel.setOnMouseClicked(event -> {
            View.LOGIN.loadView();
        });
    }

    /**
     * Handles the "Create Account" button click event. Triggers the user registration process.
     */
    @FXML
    private void handleCreateAccountClick() {
        registerUser();
    }

    /**
     * Registers a new user by validating input fields and interacting with the AccountManager
     * to persist the new user account. Displays appropriate alerts based on success or failure.
     */
    private void registerUser() {
        // Ensure all input fields are present before proceeding
        if (emailField == null || usernameField == null || passwordField == null || confirmPasswordField == null) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        try {
            // Validate the form fields
            if (validateFields()) {
                // Retrieve input values from the fields
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();

                // Attempt to create a new user account
                int status = AccountManager.ACCOUNT.setNewUser(username, email, password);

                // Handle different status outcomes
                switch (status) {
                    case 0 -> {
                        View.ALERT.showAlert(AlertType.INFORMATION, "Success", "User account successfully created!");
                        View.LOGIN.loadView();
                    }
                    case 1 -> View.ALERT.showAlert(AlertType.ERROR, "Error", "Email is already taken!");
                    case 2 -> View.ALERT.showAlert(AlertType.ERROR, "Error", "Username is already taken!");
                    case -1 -> {
                    }
                    default -> {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to create this account.");
        }
    }

    /**
     * Validates the form fields for creating a new user account. Ensures all fields are filled,
     * the email format is valid, the password meets length requirements, and the passwords match.
     * 
     * @return true if the fields are valid, false otherwise
     * @throws IOException if an input/output error occurs during validation
     */
    private boolean validateFields() throws IOException {
        // Retrieve input values from the fields
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Check if any field is empty and show an alert if true
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        try {
            // Check if the password meets minimum length requirements
            boolean validPassword = Validator.isValidPassword(password);
            if (!validPassword) {
                View.ALERT.showAlert(AlertType.ERROR, "Error", "Password must be at least 3 characters long.");
                return false;
            }

            // Check if the password and confirm password match
            if (!password.equals(confirmPassword)) {
                View.ALERT.showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
                return false;
            }
        } catch (IllegalArgumentException e) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Invalid input.");
            return false;
        }

        return true;
    }
}
