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
// import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.ProgressIndicator;
// import javafx.scene.control.Spinner;
// import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
// import javafx.util.StringConverter;

public class BudgetController implements Subject, Initializable{

    private static User currentUser;

    @FXML
    private MFXNotificationCenter notificationCenter;

    //Spinners for each category
    @FXML
    private TextField fText; double currF = 0;
    @FXML
    private TextField tText; double currT = 0;
    @FXML
    private TextField uText; double currU = 0;
    @FXML
    private TextField hText; double currH = 0;
    @FXML
    private TextField eText; double currE = 0;
    @FXML
    private TextField oText; double currO = 0;

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

        tAlert.setVisible(false);
        fAlert.setVisible(false);
        hAlert.setVisible(false);
        eAlert.setVisible(false);
        oAlert.setVisible(false);
        uAlert.setVisible(false);

        Map<Category, Budget> budgets = currentUser.getBudgetsByCategory();

        System.out.println("USER'S INFO  ONCE THE BUDGET PAGE IS LOADED:\n" + currentUser.toString());

        for (Budget b : budgets.values()) {
            System.out.println(b.toString());
    }

        setupPromptText(budgets.get(Category.FOOD), fText, foodProgress, fAlert);
        setupPromptText(budgets.get(Category.ENTERTAINMENT), eText, entertainmentProgress, eAlert);
        setupPromptText(budgets.get(Category.HEALTHCARE), hText, healthProgress, hAlert);
        setupPromptText(budgets.get(Category.UTILITIES), uText, utilitiesProgress, uAlert);
        setupPromptText(budgets.get(Category.TRANSPORTATION), tText, transportationProgress, tAlert);
        setupPromptText(budgets.get(Category.OTHER), oText, otherProgress, oAlert);

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to initialize BudgetController: " + e.getMessage());
    }
    }

    private void setupPromptText(Budget budg, TextField field, ProgressIndicator progressBar, ImageView alert) {
    if (budg != null) {
        double limit = budg.getLimit();
        field.setPromptText(limit + "");
        if (budg.isExceeded()) {
            alert.setVisible(true);
        }
    }

    System.out.println(field.isEditable());
    // Set spinner to editable
    field.setEditable(true);
}

    private double handleBudget(Category category, TextField field, ProgressIndicator progress, ImageView alert) {
        alert.setVisible(false);
        Double value = 0.0;
        try
        {
          value = Double.parseDouble(field.getText());
        }
        catch(NumberFormatException e)
        {
          showAlert(AlertType.ERROR, "Input error", "The input is not a number format");
          return value;
        }
                
                System.out.println(value);
                // Convert user input to double 

                currentUser.setBudget(new Budget(category, value));
                System.out.println("The new value is now: " + Double.toString(value));
                Double fraction = 0.0;
                for (Budget b : currentUser.getBudgetsByCategory().values()) {
                    if (b.getCategory().equals(category)) {
                        fraction = b.getPercentage() / 100;
                        if (b.isExceeded()) {
                            alert.setVisible(true);
                        }
                        if (b.getLimit() < 0) {
                            showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
                            return 0;
                        }
                    }
                }
    
                progress.setProgress(fraction); // Normalize for example (e.g., value out of 100)
                // saveBudgetToFile();
                return value;
            }

// Individual handlers call the generalized method
@FXML
private void handleTransport() {
    tText.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.ENTER) {
            currT = handleBudget(Category.TRANSPORTATION, tText, transportationProgress, tAlert);
        }
    });
}

@FXML
private void handleEntertainment() {
    currE = handleBudget(Category.ENTERTAINMENT, eText, entertainmentProgress, eAlert);
}

@FXML
private void handleUtilities() {
    currU = handleBudget(Category.UTILITIES, uText, utilitiesProgress, uAlert);
}

@FXML
private void handleFood() {
    currF = handleBudget(Category.FOOD, fText, foodProgress, fAlert);
}

@FXML
private void handleHealth() {
    currH = handleBudget(Category.HEALTHCARE, hText, healthProgress, hAlert);
}

@FXML
private void handleOther() {
    currO = handleBudget(Category.OTHER, oText, otherProgress, oAlert);
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