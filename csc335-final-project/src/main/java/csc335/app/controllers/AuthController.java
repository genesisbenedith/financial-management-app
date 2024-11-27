package csc335.app.controllers;

/**
 * Author(s): Genesis Benedith
 * File: AuthController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import csc335.app.FileIOManager;
import csc335.app.UserSessionManager;
import csc335.app.models.User;

public class AuthController {

    @FXML
    private AnchorPane contentArea; // Pane representing the sign-in or sign-up or dashboard view

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    // Storing usernames and emails for existing accounts
    private static final Set<String> registeredUsernames = new HashSet<>();
    private static final Set<String> registeredEmails = new HashSet<>();

    @FXML
    public void initialize() {
        System.out.println("AuthController initialized.");
        System.out.println("contentArea: " + contentArea);
    }

    /**
     * Loads the specified FXML file into the contentArea.
     *
     * @param fxmlPath The path to the FXML file to load.
     */
    @FXML
    public void loadContent(String fxmlPath) {
        try {
            if (contentArea != null) {
                Pane view = FXMLLoader.load(getClass().getResource(fxmlPath));
                contentArea.getChildren().clear();
                contentArea.getChildren().add(view);
            } else {
                System.err.println("contentArea is null. Check FXML and Controller binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles switching to the SignUpView when "Sign Up" is clicked.
     */
    @FXML
    private void handleSignUpClick() {
        loadContent("/views/SignUpView.fxml");
    }

    /**
     * Handles switching to the SignInView when "Create Account" is clicked.
     */
    @FXML
    private void handleCreateAccountClick() {
        if (validateFields()) {
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Save user data to file
            if (FileIOManager.createUserFile(username, email, password)) {
                // For testing & debugging purposes
                showAlert(AlertType.INFORMATION, "Success", "Account created successfully!");

                // Switch to the SignInView
                loadContent("/views/SignInView.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user data.");
            }
        }
    }

    /**
     * Handles switching to the DashboardView when "Sign In" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void handleSignInClick() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Error", "Both username and password are required.");
            return;
        }

        boolean isAuthenticated = FileIOManager.authenticateUser(username, password);
        if (isAuthenticated) {
            System.out.println("User authenticated successfully.");
            User currentUser = null;

            try {
                currentUser = FileIOManager.loadUserData(username);
                UserSessionManager.setCurrentUser(currentUser);
            } catch (IOException e) {
                showErrorDialog("Error", "Failed to load user data.");
                e.printStackTrace();
            }
            
            System.out.println(currentUser.toString());
            
            // Load the dashboard view
            loadContent("/views/DashboardView.fxml");
        } else {
            showErrorDialog("Authentication Failed", "Invalid username or password.");
        }
    }

    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 
     * @return
     */
    private boolean validateFields() {
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

        // Check if email is already in use
        if (registeredEmails.contains(email)) {
            showAlert(AlertType.ERROR, "Error", "Email is already in use.");
            return false;
        }

        // Check if username is already taken
        if (registeredUsernames.contains(username)) {
            showAlert(AlertType.ERROR, "Error", "Username is already taken.");
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
