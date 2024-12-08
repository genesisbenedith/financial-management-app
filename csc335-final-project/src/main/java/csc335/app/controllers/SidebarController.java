package csc335.app.controllers;

/**
 * This file defines the SidebarController class, which manages the sidebar panel 
 * in the application's graphical user interface. The sidebar allows navigation 
 * between different views, including Dashboard, Expenses, Budget, and Logout. 
 * The controller handles user interaction, such as mouse click events, and initializes 
 * user-related information displayed in the sidebar.
 * 
 * File: SidebarController.java
 * Course: CSC 335 (Fall 2024)
 * @author Chelina Obiang
 * @author Genesis Benedith
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.AvatarView;

import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The SidebarController class is responsible for managing the sidebar panel 
 * in the application. It initializes the panel, handles user interactions 
 * for navigation between views, and displays user-specific information such 
 * as username and email.
 * 
 * This class implements the Initializable interface to allow initialization 
 * logic to be executed when the controller is loaded.
 */
public class SidebarController implements Initializable {

    @FXML
    private Pane dashboardPane;

    @FXML
    private Pane expensePane;

    @FXML
    private Pane budgetPane;

    @FXML
    private Pane logoutPane;

    @FXML
    private AvatarView userAvatar;

    @FXML
    private Label username;

    @FXML
    private Label email;

    private static User currentUser;

    /**
     * Initializes the sidebar panel and sets up action listeners for mouse 
     * click events on the navigation panes. These events load the corresponding 
     * views into the application stage. Additionally, user information is 
     * initialized and displayed on the sidebar.
     * 
     * @author Chelina Obiang
     * @param location the location of the FXML resource
     * @param resources the resources used to localize the FXML resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        dashboardPane.setOnMouseClicked(_ -> { View.DASHBOARD.loadView(); });
        expensePane.setOnMouseClicked(_ -> { View.EXPENSES.loadView(); });
        budgetPane.setOnMouseClicked(_ -> { View.BUDGET.loadView(); });
        logoutPane.setOnMouseClicked(_ -> {
            UserSessionManager.SESSION.resetCurrentUser();
            View.LOGIN.loadView();
        });
        System.out.println("Sidebar panel has been activated.");
        initializeUserInfo();
    }

    /**
     * Initializes the user-specific information displayed on the sidebar. 
     * This includes setting the user's avatar initials, username, and email.
     * 
     * @author Genesis Benedith
     */
    public void initializeUserInfo() {
        userAvatar.setInitials(currentUser.getUsername().substring(0,1).toUpperCase());
        username.setText(currentUser.getUsername());
        email.setText(currentUser.getEmail());
    }
}
