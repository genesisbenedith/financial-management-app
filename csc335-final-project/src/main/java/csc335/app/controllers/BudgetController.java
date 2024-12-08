package csc335.app.controllers;

/**
 * This class serves as the controller for the Budget View in a JavaFX application. 
 * It manages user interactions and updates budget information dynamically for 
 * different categories such as Transportation, Food, Utilities, and more. 
 * The controller handles validation, progress updates, and alert notifications 
 * for budget limits and expenditures.
 * File: BudgetController.java
 * @author: Chelina Obiang
 * 
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.BudgetTracker;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * 
 */
public class BudgetController implements Initializable {

    @FXML
    private Button notificationCenter;

    /* TextFields for each category */
    @FXML
    private TextField fText;
    
    @FXML
    private TextField tText;
    
    @FXML
    private TextField uText;
    
    @FXML
    private TextField hText;
    
    @FXML
    private TextField eText;
    
    @FXML
    private TextField oText;

    /* MFXProgressSpinners for each category */
    @FXML
    private MFXProgressSpinner foodProgress;
    
    @FXML
    private MFXProgressSpinner transportationProgress;

    @FXML
    private MFXProgressSpinner utilitiesProgress;
    
    @FXML
    private MFXProgressSpinner healthProgress;

    @FXML
    private MFXProgressSpinner entertainmentProgress;
    
    @FXML
    private MFXProgressSpinner otherProgress;

    /* Alert Images for each category */
    @FXML
    private ImageView tAlert;
    
    @FXML
    private ImageView eAlert;
    
    @FXML
    private ImageView uAlert;
    
    @FXML
    private ImageView hAlert;
    
    @FXML
    private ImageView fAlert;
    
    @FXML
    private ImageView oAlert;

    private static User currentUser; // The current user logged in
    private List<String> alerts;

    /**
     * Initializes the BudgetController by setting up the view components 
     * such as text fields, progress spinners, and alerts. It also loads 
     * the current user's budget data and configures the initial state 
     * of the Budget View.
     * 
     * @param location   The location of the FXML file.
     * @param resources  The resource bundle for the application.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerts = new ArrayList<>();
        System.out.println("Welcome to the Budget Panel!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        notificationCenter.setOnMouseClicked(event -> {
            handlebuttonClick();
            System.out.println("Button clicked!");
        });
        
        /* Setting up alerts & prompt texts */
        try {
            /* Set the visibility to all alerts as false */
            tAlert.setVisible(false);
            fAlert.setVisible(false);
            hAlert.setVisible(false);
            eAlert.setVisible(false);
            oAlert.setVisible(false);
            uAlert.setVisible(false);

            /* Set prompt texts for each category pane */
            setupPromptText(Category.FOOD, fText, foodProgress, fAlert);
            setupPromptText(Category.ENTERTAINMENT, eText, entertainmentProgress, eAlert);
            setupPromptText(Category.HEALTHCARE, hText, healthProgress, hAlert);
            setupPromptText(Category.UTILITIES, uText, utilitiesProgress, uAlert);
            setupPromptText(Category.TRANSPORTATION, tText, transportationProgress, tAlert);
            setupPromptText(Category.OTHER, oText, otherProgress, oAlert);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize BudgetController: " + e.getMessage());
        }
    }

    /**
     * Configures prompt text, progress indicators, and alert visibility for 
     * a specific budget category. It ensures that the UI reflects the current 
     * budget limits and progress.
     * 
     * @param category    The budget category being set up (e.g., Food, Transportation).
     * @param field       The text field for user input.
     * @param progressBar The progress spinner representing budget usage.
     * @param alert       The alert image displayed if the budget is exceeded.
     */
    private void setupPromptText(Category category, TextField field, MFXProgressSpinner progressBar, ImageView alert) {
        double limit = BudgetTracker.TRACKER.getBudgetLimit(category);
        if (limit != 0) {
            progressBar.setProgress(BudgetTracker.TRACKER.getBudgetProgress(category));
        }

        field.setPromptText(limit + "");
        if (BudgetTracker.TRACKER.isBudgetExceeded(category)) {
            alert.setVisible(true);
            View.ALERT.showAlert(AlertType.ERROR, "Alert", "You've reached your spending limit for " + category.name() + "!");
        }

        field.setEditable(true); // Set spinner to editable
    }

    /**
     * Handles the budget update process for a specific category. Validates user input, 
     * updates the budget limit, sets the progress spinner, and shows alerts if necessary.
     * 
     * @param category    The budget category to update.
     * @param field       The text field containing the new budget limit.
     * @param progress    The progress spinner to update.
     * @param alert       The alert image displayed if the budget is exceeded.
     */
    private void handleBudget(Category category, TextField field, MFXProgressSpinner progress, ImageView alert) {
        alert.setVisible(false);
        
        /* Validate entry for new budget value */
        Double value = 0.0;
        try {
            value = Double.valueOf(field.getText());
        } catch (NumberFormatException e) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "The input is not a valid number.");
            return;
        }

        /* Check if value is negative */
        if (value < 0) {
            System.err.println("Value cannot be negative. Entered value: " + value);
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            return;
        }

        /* Set budget to new value */
        BudgetTracker.TRACKER.updateLimit(category, value);
        System.out.println("The new value is now: " + Double.toString(value));

        /* Show alert if budget is exceeded */
        if (BudgetTracker.TRACKER.isBudgetExceeded(category)) {
            alert.setVisible(true);
        }

        Double totalSpent = BudgetTracker.TRACKER.getBudgetSpent(category);
        System.out.println(totalSpent / value >= 0.8);
        if (totalSpent / value >= 0.8) {
            alert.setVisible(true);
            if (totalSpent / value != 1){
            View.ALERT.showAlert(AlertType.ERROR, "Alert", "You've almost reached your spending limit for " + category.name() + "!");
            alerts.add("Alert: You've almost reached your spending limit for " + category.name());
            } else {
                View.ALERT.showAlert(AlertType.ERROR, "Alert", "You've reached/exceeded limit for " + category.name() + "!");
                alerts.add("Alert: You've reached/exceeded limit for " + category.name());
            }
        } else{
            alert.setVisible(false);
        }

        /* Set progress indicator for this budget category */
        Double fraction = BudgetTracker.TRACKER.getBudgetProgress(category);
        progress.setProgress(fraction); // Normalize for example (e.g., value out of 100)

    }

    /**
     * Handles the events for the transportation pane
     */
    @FXML
    private void handleTransport() {
        tText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.TRANSPORTATION, tText, transportationProgress, tAlert);
            }
        });
    }

    /**
     * Handles the events for the entertainment pane
     */
    @FXML
    private void handleEntertainment() {
        eText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.ENTERTAINMENT, eText, entertainmentProgress, eAlert);
            }
        });
    }

    /**
     * Handles the events for the utilities pane
     */
    @FXML
    private void handleUtilities() {
        uText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.UTILITIES, uText, utilitiesProgress, uAlert);
            }
        });
    }

    /**
     * Handles the events for the food pane
     */
    @FXML
    private void handleFood() {
        fText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.FOOD, fText, foodProgress, fAlert);
            }
        });
    }

    /**
     * Handles the events for the health pane
     */
    @FXML
    private void handleHealth() {
        hText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.HEALTHCARE, hText, healthProgress, hAlert);
            }
        });
    }

    /**
     * Handles the events for the other pane
     */
    @FXML
    private void handleOther() {
        oText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.OTHER, oText, otherProgress, oAlert);
            }
        });
    }
/**
 * This is a handler for when the notifications button is clicked, it will show all of the notifications for that session when you
 * log in and only shows the alerts for when you're close to and past the expeced budget.
 */
private void handlebuttonClick() {
    // Combine the alerts into a single string
    StringBuilder text = new StringBuilder();
    for (String str : alerts) {
        text.append(str).append("\n");
    }

    // Create and configure the alert dialog
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Alerts");
    alert.setHeaderText("Here are the alerts:");
    alert.setContentText(text.toString());

    // Show the alert and wait for user to close it
    alert.showAndWait();
}

}