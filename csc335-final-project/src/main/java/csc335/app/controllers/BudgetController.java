package csc335.app.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class BudgetController implements Initializable {

    private static User currentUser;

    @FXML
    private MFXNotificationCenter notificationCenter;

    // TextFields for each category
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

    // Panes for each category
    @FXML
    private Pane transportation;
    @FXML
    private Pane utilities;
    @FXML
    private Pane health;
    @FXML
    private Pane other;
    @FXML
    private Pane entertainment;
    @FXML
    private Pane food;
    @FXML
    private Pane root;

    // ProgressBars for each category
    @FXML
    private ProgressIndicator foodProgress;
    @FXML
    private ProgressIndicator transportationProgress;
    @FXML
    private ProgressIndicator utilitiesProgress;
    @FXML
    private ProgressIndicator healthProgress;
    @FXML
    private ProgressIndicator entertainmentProgress;
    @FXML
    private ProgressIndicator otherProgress;


    // Alert Images for each category
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the Budget Panel!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        try {

            tAlert.setVisible(false);
            fAlert.setVisible(false);
            hAlert.setVisible(false);
            eAlert.setVisible(false);
            oAlert.setVisible(false);
            uAlert.setVisible(false);

            List<Budget> budgets = currentUser.getBudgets();

            System.out.println("USER'S INFO  ONCE THE BUDGET PAGE IS LOADED:\n" + currentUser.toString());

            for (Budget b : budgets) {
                System.out.println(b.toString());
            }

            setupPromptText(currentUser.findBudget(Category.FOOD), fText, foodProgress, fAlert);
            setupPromptText(currentUser.findBudget(Category.ENTERTAINMENT), eText, entertainmentProgress, eAlert);
            setupPromptText(currentUser.findBudget(Category.HEALTHCARE), hText, healthProgress, hAlert);
            setupPromptText(currentUser.findBudget(Category.UTILITIES), uText, utilitiesProgress, uAlert);
            setupPromptText(currentUser.findBudget(Category.TRANSPORTATION), tText, transportationProgress, tAlert);
            setupPromptText(currentUser.findBudget(Category.OTHER), oText, otherProgress, oAlert);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize BudgetController: " + e.getMessage());
        }

    }

    private void setupPromptText(Budget budg, TextField field, ProgressIndicator progressBar, ImageView alert) {
        if (budg != null) {
            if (budg.getLimit() != 0) {
                progressBar.setProgress(budg.getPercentage() / 100);
            }
            double limit = budg.getLimit();
            field.setPromptText(limit + "");
            if (budg.getPercentage() >= 80) {
                alert.setVisible(true);
            }
        }

        System.out.println(field.isEditable());
        // Set spinner to editable
        field.setEditable(true);
    }

    private void handleBudget(Category category, TextField field, ProgressIndicator progress, ImageView alert) {
        alert.setVisible(false);
        Double value = 0.0;
        try {
            value = Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            View.ALERT.showAlert(AlertType.ERROR, "Input error", "The input is not a number format");
            return;
        }

        System.out.println(value);
        // Check if value is negative
        if (value < 0) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            return;
        }

        // Set budget to new value
        Budget budget = currentUser.findBudget(category);
        currentUser.setBudget(category, value);
        System.out.println("The budget value for " + category.toString()+" is now: " + Double.toString(value));
        
        System.out.println(budget.getTotalSpent() / value >= 0.8);
        if (budget.getTotalSpent() / value >= 0.8) {
            alert.setVisible(true);
        } else{
            alert.setVisible(false);
        }
        
        Double fraction = budget.getPercentage() / 100;
        progress.setProgress(fraction); // Normalize for example (e.g., value out of 100)

    }

    // Individual handlers call the generalized method
    @FXML
    private void handleTransport() {
        tText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.TRANSPORTATION, tText, transportationProgress, tAlert);
            }
        });
    }

    @FXML
    private void handleEntertainment() {
        eText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.ENTERTAINMENT, eText, entertainmentProgress, eAlert);
            }
        });
    }

    @FXML
    private void handleUtilities() {
        uText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.UTILITIES, uText, utilitiesProgress, uAlert);
            }
        });
    }

    @FXML
    private void handleFood() {
        fText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.FOOD, fText, foodProgress, fAlert);
            }
        });
    }

    @FXML
    private void handleHealth() {
        hText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.HEALTHCARE, hText, healthProgress, hAlert);
            }
        });
    }

    @FXML
    private void handleOther() {
        oText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleBudget(Category.OTHER, oText, otherProgress, oAlert);
            }
        });
    }

}