package csc335.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import csc335.app.App;
import csc335.app.controllers.*;

public class NavController {
    
    @FXML
    private BudgetController BudgetFace;

    @FXML
    private DashboardController DashboardFace;

    // Need the Reports, Expenses, Logout

    @FXML
    private VBox sidebar;

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

    private Pane[] panes = { reports, dashboard, logout, expense, budget };

    protected void goToDashboard() throws IOException {
        System.out.println("Navigating to Dashboard...");

        // Load the dashboard view
        FXMLLoader dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
        Parent rootContainer = dashboardViewLoader.load();

        // Set app window to show dashboard scene
        Scene dashboardScene = new Scene(rootContainer);
        App.setScene(dashboardScene);

    }

    public void goToBudgets() throws IOException {
        System.out.println("Navigating to Budgets...");

        // Load the dashboard view
        FXMLLoader budgetViewLoader = new FXMLLoader(getClass().getResource("/views/BudgetView.fxml"));
        Parent rootContainer = budgetViewLoader.load();

        // Set app window to show budget scene
        Scene budgetScene = new Scene(rootContainer);
        App.setScene(budgetScene);
    }

    public void goToTransactions() throws IOException {
        System.out.println("Navigating to Expenses...");

    }

    @FXML
    private void goToReports() {

    }

    public void signOut() {
        System.out.println("Signing out...");
        // Add sign-out logic here
    }

}