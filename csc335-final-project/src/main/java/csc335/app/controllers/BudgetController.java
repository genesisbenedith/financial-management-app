package csc335.app.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Subject;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

public class BudgetController implements Subject, Initializable{

    private static User currentUser;

    @FXML
    private MFXNotificationCenter notificationCenter;

    //Spinners for each category
    @FXML
    private Spinner<Double> fSpinner = new Spinner<Double>(); double currF = 0;
    @FXML
    private Spinner<Double> tSpinner = new Spinner<Double>(); double currT = 0;
    @FXML
    private Spinner<Double> uSpinner = new Spinner<Double>(); double currU = 0;
    @FXML
    private Spinner<Double> hSpinner = new Spinner<Double>(); double currH = 0;
    @FXML
    private Spinner<Double> eSpinner = new Spinner<Double>(); double currE = 0;
    @FXML
    private Spinner<Double> oSpinner = new Spinner<Double>(); double currO = 0;

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

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Welcome to the Budget Panel!");
    try {
        currentUser = UserSessionManager.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Current user is null.");
        }

        tAlert.setVisible(false);
        fAlert.setVisible(false);
        hAlert.setVisible(false);
        eAlert.setVisible(false);
        oAlert.setVisible(false);
        uAlert.setVisible(false);

        Map<Category, Budget> budgets = currentUser.getBudgetsByCategory();
        if (budgets == null || budgets.isEmpty()) {
            System.err.println("No budgets found for current user.");
        }

        System.out.println("USER'S INFO  ONCE THE BUDGET PAGE IS LOADED:\n" + currentUser.toString());

        for (Budget b : budgets.values()) {
            System.out.println(b.toString());
    }

        setupSpinnerAndProgress(budgets.get(Category.FOOD), fSpinner, foodProgress, fAlert);
        setupSpinnerAndProgress(budgets.get(Category.ENTERTAINMENT), eSpinner, entertainmentProgress, eAlert);
        setupSpinnerAndProgress(budgets.get(Category.HEALTHCARE), hSpinner, healthProgress, hAlert);
        setupSpinnerAndProgress(budgets.get(Category.UTILITIES), uSpinner, utilitiesProgress, uAlert);
        setupSpinnerAndProgress(budgets.get(Category.TRANSPORTATION), tSpinner, transportationProgress, tAlert);
        setupSpinnerAndProgress(budgets.get(Category.OTHER), oSpinner, otherProgress, oAlert);

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to initialize BudgetController: " + e.getMessage());
    }
    }

    private void setupSpinnerAndProgress(Budget budg, Spinner<Double> spinner, ProgressIndicator progressBar, ImageView alert) {
    if (budg != null) {
        double limit = budg.getLimit();
        if (limit > 0) {
            DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(0, limit, 5);
            spinner.setValueFactory(factory);
            progressBar.setProgress(budg.getTotalSpent() / limit);
        } else {
            
            DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(0, 0, 5);
            spinner.setValueFactory(factory);progressBar.setProgress(0);
        }
        if (budg.isExceeded()) {
            alert.setVisible(true);
        }
    } else {
        DoubleSpinnerValueFactory factory = new DoubleSpinnerValueFactory(0, 0, 5);
        spinner = new Spinner<Double>();
        spinner.setValueFactory(factory);
        progressBar.setProgress(0);
        alert.setVisible(false);
    }

    System.out.println(spinner.isEditable());
    // Set spinner to editable
    spinner.setEditable(true);
    spinner.editableProperty();
}

    private double handleBudget(Category category, Spinner<Double> spinner, ProgressIndicator progress, ImageView alert) {
        alert.setVisible(false);
                if (spinner.getValueFactory() == null) {
                    System.out.println("This spinner's factory is null.");
                }
                
                System.out.println(spinner.getEditor().getText());
                // Convert user input to double 
                String inputValue = spinner.getEditor().getText();
                SpinnerValueFactory<Double> factory = spinner.getValueFactory();
                Double newLimit = factory.getConverter().fromString(inputValue);
                factory.setValue(newLimit);

                currentUser.setBudget(new Budget(category, newLimit));
                System.out.println("The new value is now: " + Double.toString(newLimit));

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
    
                progress.setProgress(newLimit / 100); // Normalize for example (e.g., value out of 100)
                // saveBudgetToFile();
                return newLimit;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addObserver'");
    }
    @Override
    public void removeObserver(Observer observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeObserver'");
    }
    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyObservers'");
    }

}