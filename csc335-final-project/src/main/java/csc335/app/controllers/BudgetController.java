package csc335.app.controllers;
import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class BudgetController {
    double max = 1e21;
    @FXML
    private AnchorPane contentArea;

    @FXML
    private Pane food;
    Budget b_food = new Budget(Category.FOOD, 0.0);
    
    @FXML
    private Spinner<Double> fSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    private Pane transport;
    Budget b_transport = new Budget(Category.TRANSPORTATION, 0.0);

    @FXML
    private Spinner<Double> tSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    private Pane utilities;
    Budget b_utility = new Budget(Category.UTILITIES, 0.0);

    @FXML
    private Spinner<Double> uSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    private Pane health;
    Budget b_health = new Budget(Category.HEALTHCARE, 0.0);

    @FXML
    private Spinner<Double> hSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    private Pane entertain;
    Budget b_entertain = new Budget(Category.ENTERTAINMENT, 0.0);

    @FXML
    private Spinner<Double> eSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    private Pane other;
    Budget b_other = new Budget(Category.OTHER, 0.0);

    @FXML
    private Spinner<Double> oSpinner = new Spinner<>(0.0, max, 0.0, 100.0);

    @FXML
    public void loadContent(String fxmlPath) {
        try {
            if (contentArea != null) {
                contentArea.getChildren().clear();
                Pane food = FXMLLoader.load(getClass().getResource(fxmlPath));
                Pane transport = FXMLLoader.load(getClass().getResource(fxmlPath));
                Pane utilities = FXMLLoader.load(getClass().getResource(fxmlPath));
                Pane health = FXMLLoader.load(getClass().getResource(fxmlPath));
                Pane entertain = FXMLLoader.load(getClass().getResource(fxmlPath));
                Pane other = FXMLLoader.load(getClass().getResource(fxmlPath));
                contentArea.getChildren().add(food);
                contentArea.getChildren().add(transport);
                contentArea.getChildren().add(utilities);
                contentArea.getChildren().add(health);
                contentArea.getChildren().add(entertain);
                contentArea.getChildren().add(other);
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
            b_food.setLimit(f);
        }

        if(eSpinner.getValue() != null){
            double e = eSpinner.getValue();
            b_entertain.setLimit(e);
        }

        if(hSpinner.getValue() != null){
            double h = hSpinner.getValue();
            b_health.setLimit(h);
        }

        if(oSpinner.getValue() != null){
            double o = oSpinner.getValue();
            b_other.setLimit(o);
        }

        if(tSpinner.getValue() != null){
            double t = tSpinner.getValue();
            b_transport.setLimit(t);
        }

        if(uSpinner.getValue() != null){
            double u = uSpinner.getValue();
            b_utility.setLimit(u);
        }

    }

    @FXML
    private void saveBudgettoFile(){
        File userFile = File(this.User.getUsername());
        try(FileWriter writer = new FileWriter(this.User.getUsername())){
            
        } catch (){

        }

    }

}
