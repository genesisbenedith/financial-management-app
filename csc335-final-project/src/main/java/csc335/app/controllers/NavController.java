package csc335.app.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import csc335.app.controllers.*;

public class NavController {
    @FXML
    private BudgetController BudgetFace;

    @FXML
    private DashboardController DashboardFace;

    //Need the Reports, Expenses, Logout

    @FXML
    private Pane anchor;

    @FXML
    private Pane dashboard;

    @FXML
    private Pane expense;

    @FXML
    private Pane budget;

    @FXML
    private Pane logout;

    @FXML
    private Pane reports;

    @FXML
    public Pane load(String fxml) {
        try {
            if (anchor != null) {
                // Clear current content
                anchor.getChildren().clear();

                // Load new view
                Pane view = FXMLLoader.load(NavController.class.getResource(fxml));
                anchor.getChildren().add(view);
                anchor.getChildren().addAll(dashboard, expense, budget, logout);
                initialize();
            } else {
                System.err.println("Anchor isn't available.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
                return anchor;
    }

    @FXML
    public void initialize() {
        // Make the panes clickable
        dashboard.setOnMouseClicked(event -> {
            handleClicked(dashboard);
        });

        expense.setOnMouseClicked(event -> {
            handleClicked(expense);
        });

        budget.setOnMouseClicked(event -> {
            handleClicked(budget);
        });

        reports.setOnMouseClicked(event -> {
            handleClicked(reports);
        });

        logout.setOnMouseClicked(event -> {
            handleClicked(logout);
        });
    }

    @FXML
    private void handleClicked(Pane target){
        if(target == dashboard){
            DashboardFace.handleStart();
        }
        if(target == expense){

        }
        if(target == budget){
            BudgetFace.handleStart();
        }
        if(target == reports){

        }
        if(target == logout){

        }
    }
}