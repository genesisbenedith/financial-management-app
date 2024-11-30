package csc335.app.controllers;

/**
 * Author(s): Chelina Obiang and Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: SidebarController.java
 * Description:
 */


import java.io.IOException;

import csc335.app.App;
import csc335.app.FileIOManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class SidebarController {

    @FXML
    private VBox sidebar;

    /* ------------------------------ Action Events ------------------------------ */

    /**
     * Handles switching to the DashboardView when "Dashboard" is clicked.
     * 
     * @throws IOException
     */
    public void goToDashboard() throws IOException {
        System.out.println("Navigating to Dashboard page...");
        
        // Load the Dashboard view
        FXMLLoader dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
        Parent rootContainer = dashboardViewLoader.load();

        // Set app window to show Dashboard scene
        Scene dashboardScene = new Scene(rootContainer);
        App.setScene(dashboardScene);

    }

    /**
     * Handles switching to the BudgetView when "Budget" is clicked.
     * 
     * @throws IOException
     */
    public void goToBudget() throws IOException {
        System.out.println("Navigating to Budget page...");

        // Load the Budget view
        FXMLLoader budgetViewLoader = new FXMLLoader(getClass().getResource("/views/BudgetView.fxml"));
        Parent rootContainer = budgetViewLoader.load();

        // Set app window to show Budget scene
        Scene budgetScene = new Scene(rootContainer);
        App.setScene(budgetScene);
    }

    /**
     * Handles switching to the ExpensesView when "Expenses" is clicked.
     * 
     * @throws IOException
     */
    public void goToExpenses() throws IOException {
        System.out.println("Navigating to Expenses page...");
        
        // Load the Expenses view
        FXMLLoader expensesViewLoader = new FXMLLoader(getClass().getResource("/views/ExpensesView.fxml"));
        Parent rootContainer = expensesViewLoader.load();

        // Set app window to show Expenses scene
        Scene expensesScene = new Scene(rootContainer);
        App.setScene(expensesScene);
    }

    /**
     * Handles switching to the ReportsView when "Reports" is clicked.
     * 
     * @throws IOException
     */
    @FXML
    private void goToReports() throws IOException {
        System.out.println("Navigating to Reports page...");
        
        // Load the Reports view
        FXMLLoader reportsViewLoader = new FXMLLoader(getClass().getResource("/views/ReportsView.fxml"));
        Parent rootContainer = reportsViewLoader.load();

        // Set app window to show Reports scene
        Scene reportsScene = new Scene(rootContainer);
        App.setScene(reportsScene);
    }

    /**
     * Handles user session reset & switching to the MainView when "Log Out" is clicked.
     * 
     * @throws IOException
     */
    public void logOut() throws IOException {
        System.out.println("Logging out...");

        // Save user's changes & reset user session
        FileIOManager.saveUserData(FileIOManager.getAuthenticatedUser());
        FileIOManager.resetUserSession();
        
        // Load the Main view
        FXMLLoader reportsViewLoader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        Parent rootContainer = reportsViewLoader.load();

        // Set app window to show Main scene
        Scene reportsScene = new Scene(rootContainer);
        App.setScene(reportsScene);
    }
}
