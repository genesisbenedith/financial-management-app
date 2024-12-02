/**
 * Author(s): Chelina Obiang
 * Course: CSC 335 (Fall 2024)
 * File: BudgetController.java
 * Description:
 */

package csc335.app.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BudgetControllerMINE implements Initializable, Subject {
    private final double max = 1e21;

    @FXML
    private AnchorPane contentArea;

    //Spinners for each category
    @FXML
    private Spinner<Double> fSpinner; double currF = 0;
    @FXML
    private Spinner<Double> tSpinner; double currT = 0;
    @FXML
    private Spinner<Double> uSpinner; double currU = 0;
    @FXML
    private Spinner<Double> hSpinner; double currH = 0;
    @FXML
    private Spinner<Double> eSpinner; double currE = 0;
    @FXML
    private Spinner<Double> oSpinner; double currO = 0;

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

    

    //Alert Images for each category
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

    // ProgressBars for each category
    @FXML
    private ProgressBar foodProgress;
    @FXML
    private ProgressBar transportationProgress;
    @FXML
    private ProgressBar utilitiesProgress;
    @FXML
    private ProgressBar healthProgress;
    @FXML
    private ProgressBar entertainmentProgress;
    @FXML
    private ProgressBar otherProgress;
    // @FXML
    // private Sidebar navigation;

    private static final List<Observer> observers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Budget Controller initialized.");
        addObserver(AccountRepository.getAccountRepository());
    }

    @FXML
private double handleBudget(Category category, Spinner<Double> spinner, ProgressIndicator progress, ImageView alert) {
    alert.setVisible(false);

            Double value = (Double) spinner.getValue();
            if (value == null || value == 0) {
                SpinnerValueFactory<Double> valueF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,100000.0, 0.0, 0.1);
                valueF.setValue(0.0);
                spinner.setValueFactory(valueF);
            }

            UserSessionManager.getCurrentUser().setBudget(new Budget(category, value));

            for (Budget b : UserSessionManager.getCurrentUser().getBudgetsByCategory().values()) {
                if (b.getCategory().equals(category)) {
                    if (b.isExceeded()) {
                        alert.setVisible(true);
                        progress.setProgress(Math.min(1.0, b.getTotalSpent() / b.getLimit()));
                    }
                    if (b.getLimit() < 0) {
                        showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
                        return 0;
                    }
                }
            }

            progress.setProgress(Math.min(1.0, value / 100)); // Normalize for example (e.g., value out of 100)
            saveBudgetToFile();
            return spinner.getValue();
        }

// Individual handlers call the generalized method
@FXML
private void handleTransport() {
    currT = handleBudget(Category.TRANSPORTATION, tSpinner, transportationProgress, tAlert);
}

@FXML
private void handleEntertainment() {
    currE = handleBudget(Category.ENTERTAINMENT, eSpinner, entertainmentProgress, eAlert);
}

@FXML
private void handleUtilities() {
    currU = handleBudget(Category.UTILITIES, uSpinner, utilitiesProgress, uAlert);
}

@FXML
private void handleFood() {
    currF = handleBudget(Category.FOOD, fSpinner, foodProgress, fAlert);
}

@FXML
private void handleHealth() {
    currH = handleBudget(Category.HEALTHCARE, hSpinner, healthProgress, hAlert);
}

@FXML
private void handleOther() {
    currO = handleBudget(Category.OTHER, oSpinner, otherProgress, oAlert);
}

    
    @FXML
    private void saveBudgetToFile() {
        String username = UserSessionManager.getCurrentUser().getUsername();
        File userFile = new File(username + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             StringWriter fileContent = new StringWriter()) {

            String line;
            Map<Category, Budget> budgets = UserSessionManager.getCurrentUser().getBudgetsByCategory();

            while ((line = reader.readLine()) != null) {
                boolean categoryUpdated = false;

                for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                    Category category = entry.getKey();
                    Budget budget = entry.getValue();

                    if (line.startsWith("Budget: " + category)) {
                        System.out.println(category + " has been updated with limit of " + budget.getLimit() + " and total spent of " + budget.getTotalSpent());
                        fileContent.append("Budget: ")
                                .append(category.toString())
                                .append(", ")
                                .append(budget.getLimit() + "")
                                .append(", ")
                                .append(budget.getTotalSpent() + "")
                                .append("\n");
                        categoryUpdated = true;
                        break;
                    }
                }

                if (!categoryUpdated) {
                    fileContent.append(line).append("\n");
                }
            }

            for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                Category category = entry.getKey();
                Budget budget = entry.getValue();

                if (!fileContent.toString().contains("Budget: " + category)) {
                    System.out.println(category + " has been added with a limit of " + budget.getLimit() + " and total spent of " + budget.getTotalSpent());
                    fileContent.append("Budget: ")
                            .append(category.toString())
                            .append(", ")
                            .append(budget.getLimit() + "")
                            .append(", ")
                            .append(budget.getTotalSpent() + "")
                            .append("\n");
                }
            }

            try (FileWriter writer = new FileWriter(userFile)) {
                writer.write(fileContent.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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


    // @FXML
    // public void initializeSpinners() {
    //     // Set up the spinners with value factories
    //     setupSpinner(fSpinner, Category.FOOD);
    //     setupSpinner(tSpinner, Category.TRANSPORTATION);
    //     setupSpinner(uSpinner, Category.UTILITIES);
    //     setupSpinner(hSpinner, Category.HEALTHCARE);
    //     setupSpinner(eSpinner, Category.ENTERTAINMENT);
    //     setupSpinner(oSpinner, Category.OTHER);
    // }

    // @FXML
    // private void setupSpinner(Spinner<Double> spinner, Category category) {
    //     spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, max, 0.0, 0.1));

    //     // Listen for typed-in value changes
    //     spinner.getEditor().setOnKeyTyped(event -> {
    //         try {
    //             double typedValue = Double.parseDouble(spinner.getEditor().getText());
    //             handleTypedValue(category, typedValue);
    //         } catch (NumberFormatException ex) {
    //             System.err.println("Invalid typed input: " + spinner.getEditor().getText());
    //         }
    //     });

    //     // Listen for mouse presses (plus/minus buttons)
    //     spinner.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
    //         double oldValue = spinner.getValue();
    //         spinner.increment(0); // Forces a refresh of the value
    //         double newValue = spinner.getValue();
    //         if (newValue > oldValue) {
    //             handlePlusButtonPressed(category, newValue);
    //         } else if (newValue < oldValue) {
    //             handleMinusButtonPressed(category, newValue);
    //         }
    //     });
    // }

    // private void handleTypedValue(Category category, double value) {
    //     System.out.println("Typed value for " + category + ": " + value);
    //     UserSessionManager.getUserSessionManager().getCurrentUser().setBudget(new Budget(category, value));
    //     updateProgressBar(category);
    //     handleWarning(category, value);
    // }

    // private void handlePlusButtonPressed(Category category, double newValue) {
    //     System.out.println("Plus button pressed for " + category + ": New value is " + newValue);
    //     UserSessionManager.getUserSessionManager().getCurrentUser().setBudget(new Budget(category, newValue));
    //     updateProgressBar(category);
    //     handleWarning(category, newValue);
    // }

    // private void handleMinusButtonPressed(Category category, double newValue) {
    //     System.out.println("Minus button pressed for " + category + ": New value is " + newValue);
    //     UserSessionManager.getUserSessionManager().getCurrentUser().setBudget(new Budget(category, newValue));
    //     updateProgressBar(category);
    //     handleWarning(category, newValue);
    // }

    // private void updateProgressBar(Category category) {
    //     double limit = UserSessionManager.getUserSessionManager().getCurrentUser().getBudgetsByCategory().get(category).getLimit();
    //     double spent = UserSessionManager.getUserSessionManager().getCurrentUser().getBudgetsByCategory().get(category).getTotalSpent();
    //     double progress = (limit == 0) ? 0 : spent / limit;

    //     switch (category) {
    //         case FOOD:
    //             foodProgress.setProgress(progress);
    //             break;
    //         case TRANSPORTATION:
    //             transportationProgress.setProgress(progress);
    //             break;
    //         case UTILITIES:
    //             utilitiesProgress.setProgress(progress);
    //             break;
    //         case HEALTHCARE:
    //             healthProgress.setProgress(progress);
    //             break;
    //         case ENTERTAINMENT:
    //             entertainmentProgress.setProgress(progress);
    //             break;
    //         case OTHER:
    //             otherProgress.setProgress(progress);
    //             break;
    //         default:
    //             break;
    //     }
    // }

    // private void handleWarning(Category cat, double lim){
    //     double spent = UserSessionManager.getUserSessionManager().getCurrentUser().getBudgetsByCategory().get(cat).getTotalSpent();
    //     if(spent != 0.0 && lim/spent >= 0.8){
    //         if(cat == Category.ENTERTAINMENT){
    //             ImageView img = (ImageView) entertainment.lookup("#eAlert");
    //             img.setVisible(true);
    //         }
    //         if(cat == Category.FOOD){
    //             ImageView img = (ImageView) food.lookup("#fAlert");
    //             img.setVisible(true);
    //         }
    //         if(cat == Category.HEALTHCARE){
    //             ImageView img = (ImageView) health.lookup("#hAlert");
    //             img.setVisible(true);
    //         }
    //         if(cat == Category.OTHER){
    //             ImageView img = (ImageView) other.lookup("#oAlert");
    //             img.setVisible(true);
    //         }
    //         if(cat == Category.UTILITIES){
    //             ImageView img = (ImageView) utilities.lookup("#uAlert");
    //             img.setVisible(true);
    //         }
    //         if(cat == Category.TRANSPORTATION){
    //             ImageView img = (ImageView) transportation.lookup("#tAlert");
    //             img.setVisible(true);
    //         }
    //     } else{
    //         if(cat == Category.ENTERTAINMENT){
    //             ImageView img = (ImageView) entertainment.lookup("#eAlert");
    //             img.setVisible(false);
    //         }
    //         if(cat == Category.FOOD){
    //             ImageView img = (ImageView) food.lookup("#fAlert");
    //             img.setVisible(false);
    //         }
    //         if(cat == Category.HEALTHCARE){
    //             ImageView img = (ImageView) health.lookup("#hAlert");
    //             img.setVisible(false);
    //         }
    //         if(cat == Category.OTHER){
    //             ImageView img = (ImageView) other.lookup("#oAlert");
    //             img.setVisible(false);
    //         }
    //         if(cat == Category.UTILITIES){
    //             ImageView img = (ImageView) utilities.lookup("#uAlert");
    //             img.setVisible(false);
    //         }
    //         if(cat == Category.TRANSPORTATION){
    //             ImageView img = (ImageView) transportation.lookup("#tAlert");
    //             img.setVisible(false);
    //         }
    //     }
    // }

    // @FXML
    // private void saveBudgetToFile() {
    //     String username = UserSessionManager.getUserSessionManager().getCurrentUser().getUsername();
    //     File userFile = new File(username + ".txt");

    //     try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
    //          StringWriter fileContent = new StringWriter()) {

    //         String line;
    //         Map<Category, Budget> budgets = UserSessionManager.getUserSessionManager().getCurrentUser().getBudgetsByCategory();

    //         while ((line = reader.readLine()) != null) {
    //             boolean categoryUpdated = false;

    //             for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
    //                 Category category = entry.getKey();
    //                 Budget budget = entry.getValue();

    //                 if (line.startsWith("Budget: " + category)) {
    //                     fileContent.append("Budget: ")
    //                             .append(category.toString())
    //                             .append(", ")
    //                             .append(budget.getLimit() + "")
    //                             .append(", ")
    //                             .append(budget.getTotalSpent() + "")
    //                             .append("\n");
    //                     categoryUpdated = true;
    //                     break;
    //                 }
    //             }

    //             if (!categoryUpdated) {
    //                 fileContent.append(line).append("\n");
    //             }
    //         }

    //         for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
    //             Category category = entry.getKey();
    //             Budget budget = entry.getValue();

    //             if (!fileContent.toString().contains("Budget: " + category)) {
    //                 fileContent.append("Budget: ")
    //                         .append(category.toString())
    //                         .append(", ")
    //                         .append(budget.getLimit() + "")
    //                         .append(", ")
    //                         .append(budget.getTotalSpent() + "")
    //                         .append("\n");
    //             }
    //         }

    //         try (FileWriter writer = new FileWriter(userFile)) {
    //             writer.write(fileContent.toString());
    //         }

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

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