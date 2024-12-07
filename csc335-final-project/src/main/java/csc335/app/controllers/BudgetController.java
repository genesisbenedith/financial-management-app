package csc335.app.controllers;

/**
 * Author: Chelina Obiang
 * File: BudgetController.java
 * Description:
 */

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.BudgetTracker;
import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * 
 */
public class BudgetController implements Initializable {

    @FXML
    private MFXNotificationCenter notificationCenter;

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

    /***
     * 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the Budget Panel!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        
        /* Setting up alerts & prompt texts */
        try {
            /* Set the visibility to all alerts as false */
            tAlert.setVisible(false);
            fAlert.setVisible(false);
            hAlert.setVisible(false);
            eAlert.setVisible(false);
            oAlert.setVisible(false);
            uAlert.setVisible(false);

            /* TODO: REMOVE LATER! THIS BLOCK IS FOR TESTING PURPOSES */
            List<Budget> budgets = currentUser.getBudgets();
            System.out.println("USER'S INFO  ONCE THE BUDGET PAGE IS LOADED:\n" + currentUser.toString());
            for (Budget b : budgets) {
                System.out.println(b.toString());
            }

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
     * 
     * @param category
     * @param field
     * @param progressBar
     * @param alert
     */
    private void setupPromptText(Category category, TextField field, MFXProgressSpinner progressBar, ImageView alert) {
        progressBar.getRanges1().add(NumberRange.of(0.0, 0.1));
        double limit = BudgetTracker.TRACKER.getBudgetLimit(category);
        if (limit != 0) {
            progressBar.setProgress(BudgetTracker.TRACKER.getBudgetProgress(category));
        }

        field.setPromptText(limit + "");
        if (BudgetTracker.TRACKER.isBudgetExceeded(category)) {
            alert.setVisible(true);
            System.out.print("here");
        }

        System.out.println(field.isEditable());
        field.setEditable(true); // Set spinner to editable
    }

    /**
     * 
     * 
     * @param category
     * @param field
     * @param progress
     * @param alert
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

}