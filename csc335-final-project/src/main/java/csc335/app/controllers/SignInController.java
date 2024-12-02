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

public class SignInController implements Subject, Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label signUpLabel;

    private static final List<Observer> observers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("SignInController initialized.");
        AccountManager.getAccountManager();
        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
    }

    @FXML
    private void handleSignUpClick(MouseEvent event) {
        ViewManager.getViewManager().loadView(View.REGISTER);
    }
    

    // EDIT method comment and in-line comments
    /**
     * Handles user authentication when "Sign In" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void handleSignInButtonClick() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Both username and password are required.");
            return;
        }

        
        // Authenticate user
        boolean isAuthenticated = AccountManager.authenticateUser(username, password);
        
        if (isAuthenticated) {
            System.out.println("User authenticated successfully.");
            ViewManager.getViewManager().loadView(View.DASHBOARD);

        } else {
            showAlert(AlertType.ERROR, "Authentication Failed", "Invalid username or password.");
        }

    }

    // [ ] Finish method comment
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
