package csc335.app.controllers;
/**
 * @author Chelina Obiang
 * @author Genesis Benedith
 * File: SignInController.java
 * Description: Controller class that controls the window and functions of the sign in view
 */
import java.io.IOException;
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
 * Handles the logic and behavior for the sign-in 
 * view of the application while it manages user interactions such as entering 
 * the username and password, submitting the sign-in form, and transitioning 
 * to the dashboard view upon successful authentication. It also
 * provides a way to navigate to the sign-up view for new users.
 * 
 * Key functions include:
 * - handling user input for username and password fields.
 * - authenticating the user when the "Sign In" button is clicked or the Enter 
 *   key is pressed.
 * - transitioning to the registration view if the user opts to sign up.
 * 
 * Implements the Initializable interface to ensure that the sign-in 
 * panel is properly set up when the view is initialized.
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
     * Initializes the sign-in panel & handles
     * mouse click events that loads the dashboard after sign on is successful
     * 
     * @param location  The location of the FXML file (not used in this method).
     * @param resources The resource bundle for the application (not used in this method).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignInController initialized.");

        passwordField.setEchoChar('★');
        passwordField.setOnKeyPressed(event1 -> {
            if (event1.getCode() == KeyCode.ENTER) {
                loginUser();
            }
        });

        usernameField.setOnKeyPressed(event2 -> {
            if (event2.getCode() == KeyCode.ENTER) {
                loginUser();
            }
        });

        signInLabel.setOnMouseClicked(event3 -> { loginUser(); });
        signUpLabel.setOnMouseClicked(event4 -> { View.REGISTER.loadView(); });
    }

    /**
     * Handles user authentication when sign-in button is clicked or the 
     * enter key is pressed in the username or password fields. Then retrieves 
     * the entered username and password, validates that they are not empty, and 
     * attempts to authenticate the user using the `AccountManager`'s authentication 
     * service.
     * 
     * If the username or password fields are empty, an error alert is displayed 
     * prompting the user to fill in both fields.
     * 
     * @throws IOException if there is an issue during authentication or the loading 
     * of the next view.
     */
    private void loginUser() {
        String username;
        String password;

        /* Show error alert and void if fields are null or empty */
        if (usernameField == null || passwordField == null) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
            return;
        } else {
            username = usernameField.getText().trim();
            password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                View.ALERT.showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
                return;
            }
        }

        username = usernameField.getText().trim();
        password = passwordField.getText().trim();

        // Authenticate user
        AccountManager.ACCOUNT.authenticateUser(username, password);

    }

}