package csc335.app.controllers;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.Map;

public class BudgetController {
    private final double max = 1e21;

    @FXML
    private AnchorPane contentArea;

    //Spinners for each category
    @FXML
    private Spinner<Double> fSpinner;
    @FXML
    private Spinner<Double> tSpinner;
    @FXML
    private Spinner<Double> uSpinner;
    @FXML
    private Spinner<Double> hSpinner;
    @FXML
    private Spinner<Double> eSpinner;
    @FXML
    private Spinner<Double> oSpinner;

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

    @FXML
    public void loadContent(String fxmlPath) {
        try {
            if (contentArea != null) {
                contentArea.getChildren().clear();
                Pane view = FXMLLoader.load(getClass().getResource(fxmlPath));
                contentArea.getChildren().add(view);

                // Add the panes to contentArea
                contentArea.getChildren().addAll(root, food, transportation, utilities, health, entertainment, other);
            } else {
                System.err.println("contentArea is null. Check FXML and Controller binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStart() {
        loadContent("/views/BudgetView.fxml");
    }

    @FXML
    public void initialize() {
        // Set up the spinners with value factories
        setupSpinner(fSpinner, Category.FOOD);
        setupSpinner(tSpinner, Category.TRANSPORTATION);
        setupSpinner(uSpinner, Category.UTILITIES);
        setupSpinner(hSpinner, Category.HEALTHCARE);
        setupSpinner(eSpinner, Category.ENTERTAINMENT);
        setupSpinner(oSpinner, Category.OTHER);
    }

    private void setupSpinner(Spinner<Double> spinner, Category category) {
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, max, 0.0, 0.1));

        // Listen for typed-in value changes
        spinner.getEditor().setOnKeyTyped(event -> {
            try {
                double typedValue = Double.parseDouble(spinner.getEditor().getText());
                handleTypedValue(category, typedValue);
            } catch (NumberFormatException ex) {
                System.err.println("Invalid typed input: " + spinner.getEditor().getText());
            }
        });

        // Listen for mouse presses (plus/minus buttons)
        spinner.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            double oldValue = spinner.getValue();
            spinner.increment(0); // Forces a refresh of the value
            double newValue = spinner.getValue();
            if (newValue > oldValue) {
                handlePlusButtonPressed(category, newValue);
            } else if (newValue < oldValue) {
                handleMinusButtonPressed(category, newValue);
            }
        });
    }

    private void handleTypedValue(Category category, double value) {
        System.out.println("Typed value for " + category + ": " + value);
        DashboardController.user.setBudget(category, value);
        updateProgressBar(category);
        handleWarning(category, value);
    }

    private void handlePlusButtonPressed(Category category, double newValue) {
        System.out.println("Plus button pressed for " + category + ": New value is " + newValue);
        DashboardController.user.setBudget(category, newValue);
        updateProgressBar(category);
        handleWarning(category, newValue);
    }

    private void handleMinusButtonPressed(Category category, double newValue) {
        System.out.println("Minus button pressed for " + category + ": New value is " + newValue);
        DashboardController.user.setBudget(category, newValue);
        updateProgressBar(category);
        handleWarning(category, newValue);
    }

    private void updateProgressBar(Category category) {
        double limit = DashboardController.user.getBudgets().get(category).getLimit();
        double spent = DashboardController.user.getBudgets().get(category).getSpent();
        double progress = (limit == 0) ? 0 : spent / limit;

        switch (category) {
            case FOOD:
                foodProgress.setProgress(progress);
                break;
            case TRANSPORTATION:
                transportationProgress.setProgress(progress);
                break;
            case UTILITIES:
                utilitiesProgress.setProgress(progress);
                break;
            case HEALTHCARE:
                healthProgress.setProgress(progress);
                break;
            case ENTERTAINMENT:
                entertainmentProgress.setProgress(progress);
                break;
            case OTHER:
                otherProgress.setProgress(progress);
                break;
            default:
                break;
        }
    }

    private void handleWarning(Category cat, double lim){
        double spent = DashboardController.user.getBudgets().get(cat).getSpent();
        if(spent != 0.0 && lim/spent >= 0.8){
            if(cat == Category.ENTERTAINMENT){
                ImageView img = (ImageView) entertainment.lookup("#eAlert");
                img.setVisible(true);
            }
            if(cat == Category.FOOD){
                ImageView img = (ImageView) food.lookup("#fAlert");
                img.setVisible(true);
            }
            if(cat == Category.HEALTHCARE){
                ImageView img = (ImageView) health.lookup("#hAlert");
                img.setVisible(true);
            }
            if(cat == Category.OTHER){
                ImageView img = (ImageView) other.lookup("#oAlert");
                img.setVisible(true);
            }
            if(cat == Category.UTILITIES){
                ImageView img = (ImageView) utilities.lookup("#uAlert");
                img.setVisible(true);
            }
            if(cat == Category.TRANSPORTATION){
                ImageView img = (ImageView) transportation.lookup("#tAlert");
                img.setVisible(true);
            }
        } else{
            if(cat == Category.ENTERTAINMENT){
                ImageView img = (ImageView) entertainment.lookup("#eAlert");
                img.setVisible(false);
            }
            if(cat == Category.FOOD){
                ImageView img = (ImageView) food.lookup("#fAlert");
                img.setVisible(false);
            }
            if(cat == Category.HEALTHCARE){
                ImageView img = (ImageView) health.lookup("#hAlert");
                img.setVisible(false);
            }
            if(cat == Category.OTHER){
                ImageView img = (ImageView) other.lookup("#oAlert");
                img.setVisible(false);
            }
            if(cat == Category.UTILITIES){
                ImageView img = (ImageView) utilities.lookup("#uAlert");
                img.setVisible(false);
            }
            if(cat == Category.TRANSPORTATION){
                ImageView img = (ImageView) transportation.lookup("#tAlert");
                img.setVisible(false);
            }
        }
    }

    @FXML
    private void saveBudgetToFile() {
        String username = DashboardController.user.getUsername();
        File userFile = new File(username + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             StringWriter fileContent = new StringWriter()) {

            String line;
            Map<Category, Budget> budgets = DashboardController.user.getBudgets();

            while ((line = reader.readLine()) != null) {
                boolean categoryUpdated = false;

                for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                    Category category = entry.getKey();
                    Budget budget = entry.getValue();

                    if (line.startsWith("Budget: " + category)) {
                        fileContent.append("Budget: ")
                                .append(category.toString())
                                .append(", ")
                                .append(budget.getLimit() + "")
                                .append(", ")
                                .append(budget.getSpent() + "")
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
                    fileContent.append("Budget: ")
                            .append(category.toString())
                            .append(", ")
                            .append(budget.getLimit() + "")
                            .append(", ")
                            .append(budget.getSpent() + "")
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
}