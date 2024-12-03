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
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BudgetController implements Subject, Initializable {
    @FXML
    private MFXNotificationCenter notificationCenter;

    @FXML
    private AnchorPane contentArea;

    // Spinners for each category
    @FXML
    private Spinner<Double> fSpinner;
    double currF = 0;
    @FXML
    private Spinner<Double> tSpinner;
    double currT = 0;
    @FXML
    private Spinner<Double> uSpinner;
    double currU = 0;
    @FXML
    private Spinner<Double> hSpinner;
    double currH = 0;
    @FXML
    private Spinner<Double> eSpinner;
    double currE = 0;
    @FXML
    private Spinner<Double> oSpinner;
    double currO = 0;

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
    private SidebarController navigation;

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

    private static final List<Observer> observers = new ArrayList<>();
    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the Budget Page!");

        UserSessionManager.getUserSessionManager();
        currentUser = UserSessionManager.getCurrentUser();

        initializeSpinners();
        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
    }

    public void initializeSpinners() {

        // Map<Category, Budget> budgets = currentUser.getBudgetsByCategory();
        // for (Category category : Category.values()) {
        //     Budget budget = budgets.get(category);
        //     switch (category) {
        //         case FOOD -> {
        //             fSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, budget.getLimit(), 5));
        //             foodProgress.setProgress(budget.getTotalSpent() / budget.getLimit());
        //             if (budget.isExceeded()) {
        //                 fAlert.setVisible(true);
        //             }
        //         }
        //         case ENTERTAINMENT -> {

        //         }
        //         case TRANSPORTATION -> {

        //         }
        //         case UTILITIES -> {

        //         }
        //         case HEALTHCARE -> {

        //         }
        //         case OTHER -> {

        //         }
        //     }
            
        // }

    }

    @FXML
    private double handleBudget(Category category, Spinner<Double> spinner, ProgressIndicator progress,
            ImageView alert) {
        alert.setVisible(false);

        Double value = (Double) spinner.getValue();
        if (value == null || value == 0) {
            SpinnerValueFactory<Double> valueF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0.0,
                    0.1);
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
        // saveBudgetToFile();
        return spinner.getValue();
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
    private void handleGoToDashboardClick() {
        ViewManager.getViewManager().loadView(View.DASHBOARD);
    }

    @FXML
    private void handleGoToBudgetClick() {
        ViewManager.getViewManager().loadView(View.BUDGET);
    }

    @FXML
    private void handleGoToLogoutClick() {
        ViewManager.getViewManager().loadView(View.LOGIN);
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
                        System.out.println(category + " has been updated with limit of " + budget.getLimit()
                                + " and total spent of " + budget.getTotalSpent());
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
                    System.out.println(category + " has been added with a limit of " + budget.getLimit()
                            + " and total spent of " + budget.getTotalSpent());
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