package csc335.app.controllers;

/**
 * The SidebarController class is responsible for managing the behavior of the application's sidebar.
 * It listens for user actions on the sidebar buttons and loads the corresponding views.
 * It also handles user logout by resetting the user session and redirecting to the login page.
 * File: SidebarController.java
 * 
 * Authors: 
 * @author Chelina Obiang
 * @author Genesis Benedith
 */
import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class SidebarController implements Initializable{

    @FXML
    private Pane dashboardPane; // Button for navigating to the dashboard view

    @FXML
    private Pane expensePane; // Button for navigating to the expenses view

    @FXML
    private Pane budgetPane; // Button for navigating to the budget view

    @FXML
    private Pane reportPane; // Button for navigating to the report view

    @FXML
    private Pane logoutPane; // Button for logging out of the application

    /**
     * Initializes the sidebar panel and sets action listeners for mouse click events.
     * These listeners load different views or handle the logout process.
     * 
     * @param location  The location used to resolve relative paths for the root object (unused).
     * @param resources The resources used to localize the root object (unused).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
        // REVIEW Action listeners
        dashboardPane.setOnMouseClicked(_ -> { View.DASHBOARD.loadView(); });
        expensePane.setOnMouseClicked(_ -> { View.EXPENSES.loadView(); });
        budgetPane.setOnMouseClicked(_ -> { View.BUDGET.loadView(); });
        reportPane.setOnMouseClicked(_ -> { View.REPORT.loadView(); });
        logoutPane.setOnMouseClicked(_ -> {
            UserSessionManager.SESSION.resetCurrentUser();
            View.LOGIN.loadView();
        });
        System.out.println("Sidebar panel has been activated.");

    }
}

