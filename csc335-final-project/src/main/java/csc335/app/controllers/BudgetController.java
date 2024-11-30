package csc335.app.controllers;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
// import io.github.palexdev.materialfx.notifications.MFXNotification;
// import io.github.palexdev.materialfx.notifications.MFXNotificationCenterHandler;
import io.github.palexdev.materialfx.controls.MFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.Map;

public class BudgetController {
    @FXML
    private MFXNotificationCenter notificationCenter;

    @FXML
    private AnchorPane contentArea;

    //Spinners for each category
    @FXML
    private MFXSpinner<Double> fSpinner;
    @FXML
    private MFXSpinner<Double> tSpinner;
    @FXML
    private MFXSpinner<Double> uSpinner;
    @FXML
    private MFXSpinner<Double> hSpinner;
    @FXML
    private MFXSpinner<Double> eSpinner;
    @FXML
    private MFXSpinner<Double> oSpinner;

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
    @FXML
    private NavController navigation;

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


    @FXML
    private void handleTransport(){
        tAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.TRANSPORTATION, tSpinner.getValue()); //(double) Integer.parseInt(tSpinner.getPromptText())
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.TRANSPORTATION)){
                if(b.isExceeded()){
                    //Make a Notification
                    tAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        transportationProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            transportationProgress.setProgress(tSpinner.getValue());
            saveBudgetToFile();
        }
    }

    @FXML
    private void handleEntertainment(){
        eAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.ENTERTAINMENT, eSpinner.getValue());
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.ENTERTAINMENT)){
                if(b.isExceeded()){
                    //Make a Notification
                    eAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        entertainmentProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            entertainmentProgress.setProgress(eSpinner.getValue());
            saveBudgetToFile();
        }
    }

    @FXML
    private void handleUtilities(){
        uAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.UTILITIES, uSpinner.getValue());
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.UTILITIES)){
                if(b.isExceeded()){
                    //Make a Notification
                    uAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        utilitiesProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            utilitiesProgress.setProgress(uSpinner.getValue());
            saveBudgetToFile();
        }
    }

    @FXML
    private void handleFood(){
        fAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.FOOD, fSpinner.getValue());
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.FOOD)){
                if(b.isExceeded()){
                    //Make a Notification
                    fAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        foodProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            foodProgress.setProgress(fSpinner.getValue());
            saveBudgetToFile();
        }
    }

    @FXML
    private void handleHealth(){
        hAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.HEALTHCARE, hSpinner.getValue());
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.HEALTHCARE)){
                if(b.isExceeded()){
                    //Make a Notification
                    hAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        healthProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            healthProgress.setProgress(hSpinner.getValue());
            saveBudgetToFile();
        }
    }

    @FXML
    private void handleOther(){
        oAlert.setVisible(false);
        DashboardController.currentUser.setBudget(Category.OTHER, oSpinner.getValue());
        for(Budget b: DashboardController.currentUser.getAllBudgets()){
            if(b.getCategory().equals(Category.OTHER)){
                if(b.isExceeded()){
                    //Make a Notification
                    oAlert.setVisible(true);
                    if(b.getTotalSpent()/b.getLimit() >= 1.0){
                        otherProgress.setProgress(1.0);
                    }
            }
            else if(b.getLimit() < 0){
                showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
            }
        }
            otherProgress.setProgress(oSpinner.getValue());
            saveBudgetToFile();
        }
    }

    // private void handlePlusButtonPressed(Category category, double newValue) {
    //     System.out.println("Plus button pressed for " + category + ": New value is " + newValue);
    //     DashboardController.currentUser.setBudget(category, newValue);
    //     updateProgressBar(category);
    //     handleWarning(category, newValue);
    // }

    // private void handleMinusButtonPressed(Category category, double newValue) {
    //     System.out.println("Minus button pressed for " + category + ": New value is " + newValue);
    //     DashboardController.currentUser.setBudget(category, newValue);
    //     updateProgressBar(category);
    //     handleWarning(category, newValue);
    // }

    @FXML
    private void handleGoToDashboardClick() {
        AuthController.getInstance().loadContent("/views/DashboardView.fxml");
    }

    @FXML
    private void handleGoToBudgetClick() {
        AuthController.getInstance().loadContent("/views/BudgetView.fxml");
    }

    @FXML
    private void handleGoToLogoutClick(){
        AuthController.getInstance().loadContent("/views/LogOutView.fxml");
    }
    
    @FXML
    private void saveBudgetToFile() {
        String username = DashboardController.currentUser.getUsername();
        File userFile = new File(username + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             StringWriter fileContent = new StringWriter()) {

            String line;
            Map<Category, Budget> budgets = DashboardController.currentUser.getCategorizedBudgets();

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

}