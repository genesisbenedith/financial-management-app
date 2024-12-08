package csc335.app.controllers;

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

public class SignInController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private EnhancedPasswordField passwordField;

    @FXML
    private Button signInLabel;

    @FXML
    private Label signUpLabel;

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

    // EDIT method comment and in-line comments
    /**
     * Handles user authentication when "Sign In" is clicked.
     * 
     * @throws IOException
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