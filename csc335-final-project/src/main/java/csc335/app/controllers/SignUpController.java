package csc335.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.persistence.AccountManager;
import csc335.app.persistence.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * Controller for handling user sign-up functionality.
 * 
 * This class manages the user sign-up form, including email, username, password, and confirm password fields.
 * It handles user input validation and calls the necessary methods to register the user.
 * 
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
    private Label createAccountLabel;


    /**
     * Sets up event listeners for the Sign-Up form.
     * 
     * When the user clicks "Create Account," the registerUser method is called.
     * When the user clicks "Sign In," the login view is shown.
     * 
     * @param location the location of the FXML file.
     * @param resources the resources for localization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignUpController initialized.");

        createAccountLabel.setOnMouseClicked(_ -> { registerUser(); });
        signInLabel.setOnMouseClicked(_ -> { View.LOGIN.loadView(); });
        
    }

    /**
     * Handles the user registration process when "Create Account" is clicked.
     * 
     * Validates the input fields and if valid, creates a new user.
     * If any field is empty or invalid, an error message is displayed.
     * 
     * @throws IOException if there is an error during user registration.
     */
    private void registerUser() {
        /* Show error alert and void if any field is null */
        if (emailField == null || usernameField == null || passwordField == null || confirmPasswordField == null) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        try {
            // Validate fields 
            if (validateFields()) {
                /* Get text from input fields */
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                AccountManager.ACCOUNT.setNewUser(username, email, password);
            }
        } catch (IOException e) {
            System.err.println("Unable to create this account.");
        }

    }

    /**
     * Validates the fields in the Sign-Up form.
     * 
     * Checks if all fields are filled in, if the password is valid, and if the passwords match.
     * If any validation fails, an error message is shown.
     * 
     * @return true if all fields are valid, false otherwise.
     * @throws IOException if there is an error during validation.
     */
    private boolean validateFields() throws IOException {
        /* Get text from input fields */
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        /* Show alert if any field is empty */
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        /* Valid the form fields */
        try {
            // boolean validEmail = Validator.isValidEmail(email);
            // if (!validEmail) {
            // showAlert(AlertType.ERROR, "Error", "Please enter a valid email address.");
            // return false;
            // }

            /* Show error alert and void if password is too short */
            boolean validPassword = Validator.isValidPassword(password);
            if (!validPassword) {
                View.ALERT.showAlert(AlertType.ERROR, "Error", "Password must be at least 3 characters long.");
                return false;
            }

            /* Show error alert and void if passwords do not match */
            if (!password.equals(confirmPassword)) {
                View.ALERT.showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
                return false;
            }

        } catch (IllegalArgumentException e) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        return true;
    }

}