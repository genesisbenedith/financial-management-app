package csc335.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.persistence.AccountManager;
import csc335.app.utils.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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


    // [ ] Needs method comment and in-line comments
    /**
     * 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignUpController initialized.");
        
    }

    @FXML
    private void handleSignInClick(MouseEvent event) {
        ViewManager.INSTANCE.loadView(View.LOGIN);
    }

    // [ ] Needs in-line comments
    // EDIT method comment
    /**
     * Handles input validation for Sign-Up form when "Create Account" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void handleCreateAccountClick() {
        /* Show error alert and void if any field is null */
        if (emailField == null || usernameField == null || passwordField == null || confirmPasswordField == null) {
            ViewManager.INSTANCE.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        try {
            // Validate fields 
            if (validateFields()) {
                /* Get text from input fields */
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                AccountManager.REPOSITORY.setNewUser(username, email, password);
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
            ViewManager.INSTANCE.showAlert(AlertType.ERROR, "Error", "All fields are required.");
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
                ViewManager.INSTANCE.showAlert(AlertType.ERROR, "Error", "Password must be at least 3 characters long.");
                return false;
            }

            /* Show error alert and void if passwords do not match */
            if (!password.equals(confirmPassword)) {
                ViewManager.INSTANCE.showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
                return false;
            }

        } catch (IllegalArgumentException e) {
            ViewManager.INSTANCE.showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        return true;
    }

}