package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.EnhancedPasswordField;

import csc335.app.persistence.AccountManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * The SignInController class manages the Sign-In view for the application.
 * It handles user input, validates credentials, and facilitates navigation
 * to the registration view or dashboard upon successful login.
 * 
 * File: SignInController.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public class SignInController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private EnhancedPasswordField passwordField;

    @FXML
    private Button signInLabel;

    @FXML
    private Label signUpLabel;

    /**
     * Initializes the Sign-In view. Sets up event listeners for the sign-in and
     * sign-up actions, and allows the "Enter" key to trigger the login process
     * from both username and password fields.
     * 
     * @param location  The location of the FXML resource.
     * @param resources The resources used to localize the FXML resource.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignInController initialized.");

        // Set custom echo character for password input
        passwordField.setEchoChar('★');

        // Trigger login on Enter key press in the password field
        passwordField.setOnKeyPressed(event1 -> {
            if (event1.getCode() == KeyCode.ENTER) {
                loginUser();
            }
        });

        // Trigger login on Enter key press in the username field
        usernameField.setOnKeyPressed(event2 -> {
            if (event2.getCode() == KeyCode.ENTER) {
                loginUser();
            }
        });

        // Trigger login on mouse click of the "Sign In" button
        signInLabel.setOnMouseClicked(event3 -> {
            loginUser();
        });

        // Navigate to the registration view when "Sign Up" label is clicked
        signUpLabel.setOnMouseClicked(event4 -> {
            View.REGISTER.loadView();
        });
    }

    /**
     * Authenticates the user based on the entered username and password.
     * If either field is empty, it displays an error alert. If both fields
     * are valid, it proceeds to authenticate the user through the 
     * AccountManager class.
     */
    private void loginUser() {
        // Variables to hold user input
        String username;
        String password;

        // Validate that both fields are non-null
        if (usernameField == null || passwordField == null) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
            return;
        }

        // Retrieve text from fields, ensuring no leading/trailing spaces
        username = usernameField.getText().trim();
        password = passwordField.getText().trim();

        // Validate that both fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
            return;
        }

        // Authenticate user using AccountManager
        int status = AccountManager.ACCOUNT.authenticateUser(username, password);

        // Provide feedback based on authentication success or failure
        switch (status) {
            case 0 -> {
                View.ALERT.showAlert(AlertType.INFORMATION, "Success", "Welcome back, " + username + "!");
                View.DASHBOARD.loadView(); // Navigate to the dashboard view
            }
            case 1 -> View.ALERT.showAlert(AlertType.ERROR, "Error", "Username does not exist!");
            case 2 -> View.ALERT.showAlert(AlertType.ERROR, "Authentication Failed", "Invalid username or password.");
            default -> View.ALERT.showAlert(AlertType.ERROR, "Error", "Unable to authenticate user. Please try again later.");
        }
    }
}
