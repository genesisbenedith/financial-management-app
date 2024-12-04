package csc335.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Subject;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.AccountRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignUpController implements Initializable, Subject{

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

    private static final List<Observer> observers = new ArrayList<>();
    

    // [ ] Needs method comment and in-line comments
    /**
     * 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignUpController initialized.");
        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
    }

    @FXML
    private void handleSignInClick(MouseEvent event) {
        ViewManager.getViewManager().loadView(View.LOGIN);
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
        // Check if any field is empty
        if (emailField == null || usernameField == null || passwordField == null || confirmPasswordField == null) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        try {
            if (!validateFields())
                return;
        } catch (IOException e) {
            return;
        }

        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        String registrationStatus = AccountManager.getAccountManager().registerAccount(username, email, password);

        if (registrationStatus == "Success"){
            showAlert(AlertType.INFORMATION, registrationStatus, "Account created successfully!");
            ViewManager.getViewManager().loadView(View.LOGIN);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", registrationStatus);
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
        try {
            // boolean validEmail = Validator.isValidEmail(email);
            // boolean validPassword = Validator.isValidPassword(password);
            
            // if (!validEmail) {
            //     showAlert(AlertType.ERROR, "Error", "Please enter a valid email address."); 
            //     return false;
            // }

            // if (!validPassword) {
            //     showAlert(AlertType.ERROR, "Error", "Password must be at least 5 characters long.");
            //     return false;
            // }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
                return false;
            }
            
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        return true;
    }

    
    // [ ] Needs method comment
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

}
