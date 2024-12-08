package csc335.app.controllers;

/**
 * @author Chelina Obiang
 * @author Genesis Benedith
 * File: SidebarController.java
 * Description: Controller class that controls the window and functions of the sidebar on each page
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
 * Manages the sidebar navigation in the application that allows users to switch
 * between different sections such as the dashboard, 
 * expenses, budget, and logout by clicking on the respective panes. 
 * The class also displays the current user's information, such as username, 
 * email, and an avatar, in the sidebar.
 * 
 * Key functions include:
 * - handling mouse click events to switch views (e.g., Dashboard, Expenses, Budget)
 * - logging out the current user and redirecting to the login view
 * - displaying the user's avatar, username, and email address in the sidebar
 * 
 * Implements the Initializable interface to ensure that the sidebar 
 * is properly set up when the view is initialized.
 */
public class SidebarController implements Initializable{

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
     * Initializes the sidebar panel & sets action listeners to handles
     * mouse click events that loads different views to the stage
     * 
     * also sets up the user information display in the sidebar, 
     * including the avatar, username, and email, based on the current logged-in user
     * 
     * @author Chelina Obiang
     * @author Genesis Benedith
     * 
     * @param location  The location of the FXML file (not used in this method).
     * @param resources The resource bundle for the application (not used in this method).
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
     * Initializes and displays the user's information in the sidebar by:
     * - setting the avatar to display the first letter of the username
     * - displaying the username in the sidebar
     * - displaying the user's email address
     * 
     * Retrieves the current user information from the user session and 
     * updates the sidebar labels and avatar accordingly.
     */
    public void initializeUserInfo() {
        userAvatar.setInitials(currentUser.getUsername().substring(0,1).toUpperCase());
        username.setText(currentUser.getUsername());
        email.setText(currentUser.getEmail());

    }


}