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


    // [ ] Needs method comment and in-line comments
    /**
     * 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignUpController initialized.");

        signInLabel.setOnMouseClicked(event -> { View.LOGIN.loadView(); });
        
    }

    @FXML
    private void handleCreateAccountClick() {
        registerUser();
    }

    // [ ] Needs in-line comments
    // EDIT method comment
    /**
     * Handles input validation for Sign-Up form when "Create Account" is clicked.
     * 
     * @throws IOException
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

                int status = AccountManager.ACCOUNT.setNewUser(username, email, password);
                switch (status) {
                    case 0:
                        View.ALERT.showAlert(AlertType.INFORMATION, "Success", "User account successfully created!");
                        View.LOGIN.loadView();
                        break;
                    case 1:
                        View.ALERT.showAlert(AlertType.ERROR, "Error", "Email is already taken!");
                        break;
                    case 2:
                        View.ALERT.showAlert(AlertType.ERROR, "Error", "Username is already taken!");
                        break;
                    case -1:
                        break;
                    default:
                        break;
                }
                
            }
        } catch (IOException e) {
            System.err.println("Unable to create this account.");
        }
        

    }

    // [ ] Needs method comment
    // EDIT in-line comments
    /**
     * 
     * @return
     * @throws IOException
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