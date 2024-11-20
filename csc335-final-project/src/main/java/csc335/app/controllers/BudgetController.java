package csc335.app.controllers;
import csc335.app.models.Budget;
import csc335.app.models.Category;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class BudgetController {
    double max = 1e21;
    @FXML
    private Pane root;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Pane food;
    
    @FXML
    private Spinner<Double> fSpinner;
    @FXML
    private Pane transport;

    @FXML
    private Spinner<Double> tSpinner;

    @FXML
    private Pane utilities;

    @FXML
    private Spinner<Double> uSpinner;

    @FXML
    private Pane health;

    @FXML
    private Spinner<Double> hSpinner;

    @FXML
    private Pane entertain;

    @FXML
    private Spinner<Double> eSpinner;

    @FXML
    private Pane other;

    @FXML
    private Spinner<Double> oSpinner;

    @FXML
    public void loadContent(String fxmlPath) {
        try {
            if (contentArea != null) {
                contentArea.getChildren().clear();
                Pane view = FXMLLoader.load(getClass().getResource(fxmlPath));
                contentArea.getChildren().add(view);
                root = (Pane) view.lookup("#root");
                food = (Pane) view.lookup("#food");
                transport = (Pane) view.lookup("#transport");
                utilities = (Pane) view.lookup("#utilities");
                health = (Pane) view.lookup("#health");
                entertain = (Pane) view.lookup("#entertain");
                other = (Pane) view.lookup("#other");

                // Add the panes to contentArea
                contentArea.getChildren().addAll(root, food, transport, utilities, health, entertain, other);
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
    private void handleCreateBudget(){
        if(fSpinner.getValue() != null){
            double f = fSpinner.getValue();
            DashboardController.user.setBudget(Category.FOOD, f);
        }

        if(eSpinner.getValue() != null){
            double e = eSpinner.getValue();
            DashboardController.user.setBudget(Category.ENTERTAINMENT, e);
        }

        if(hSpinner.getValue() != null){
            double h = hSpinner.getValue();
            DashboardController.user.setBudget(Category.HEALTHCARE, h);
        }

        if(oSpinner.getValue() != null){
            double o = oSpinner.getValue();
            DashboardController.user.setBudget(Category.OTHER, o);
        }

        if(tSpinner.getValue() != null){
            double t = tSpinner.getValue();
            DashboardController.user.setBudget(Category.TRANSPORTATION, t);
        }

        if(uSpinner.getValue() != null){
            double u = uSpinner.getValue();
            DashboardController.user.setBudget(Category.UTILITIES, u);
        }

    }

    @FXML
    private boolean saveBudgettoFile() {
        String username = DashboardController.user.getUsername();
        File userFile = new File(username + ".txt");
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            StringBuilder fileContent = new StringBuilder(); //building the budget string
            String line;
            Map<Category, Budget> budgets = DashboardController.user.getBudgets();
    
            while ((line = reader.readLine()) != null) {
                boolean categoryUpdated = false;
    
                for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                    Category category = entry.getKey();
                    Budget budget = entry.getValue();
    
                    // Check if the current line is for the category
                    if (line.startsWith("Budget: " + category.toString())) {
                        fileContent.append("Budget: ")
                                   .append(category.toString())
                                   .append(" ,")
                                   .append(budget.getLimit())
                                   .append(", ")
                                   .append(budget.getSpent())
                                   .append("\n");
                        categoryUpdated = true;
                        break;
                    }
                }
    
                if (!categoryUpdated) {
                    // If the category is not updated, keep the original line
                    fileContent.append(line).append("\n");
                }
            }
    
            reader.close();
    
            // Add any new categories that were not found in the file
            for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                Category category = entry.getKey();
                Budget budget = entry.getValue();
                if (!fileContent.toString().contains("Budget: " + category.toString())) {
                    fileContent.append("Budget: ")
                               .append(category.toString())
                               .append(" ,")
                               .append(budget.getLimit())
                               .append(", ")
                               .append(budget.getSpent())
                               .append("\n");
                }
            }
    
            // Write the updated content back to the file
            FileWriter writer = new FileWriter(userFile);
            writer.write(fileContent.toString());
            writer.close();
    
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}
