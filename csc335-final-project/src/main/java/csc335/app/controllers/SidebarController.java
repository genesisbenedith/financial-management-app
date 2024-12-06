package csc335.app.controllers;

// [ ] Finish file comment

/**
 * @author Chelina Obiang
 * @author Genesis Benedith
 */
import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

// [ ] Needs class comment
public class SidebarController implements Initializable{

    @FXML
    private Pane dashboardPane;

    @FXML
    private Pane expensePane;

    @FXML
    private Pane budgetPane;

    @FXML
    private Pane reportPane;

    @FXML
    private Pane logoutPane;

    // [ ] Complete method comment
    /**
     * Initializes the sidebar panel & sets action listeners to handles
     * mouse click events that loads different views to the stage
     * 
     * @author Chelina Obiang
     * @author Genesis Benedith
     * 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        

        // [ ]: Change the color of the button that is assigned to the current view
        /** For example, if the client is currently on the dashboard view, and the color
         * of the dashboard button is black, then the buttons on the sidebar for
         * every other view should be purple, etc.
         */

        
        // REVIEW Action listeners
        dashboardPane.setOnMouseClicked(click -> { View.DASHBOARD.loadView(); });
        expensePane.setOnMouseClicked(click -> { View.EXPENSES.loadView(); });
        budgetPane.setOnMouseClicked(click -> { View.BUDGET.loadView(); });
        reportPane.setOnMouseClicked(click -> { View.REPORT.loadView(); });
        logoutPane.setOnMouseClicked(click -> {
            UserSessionManager.SESSION.resetCurrentUser();
            View.LOGIN.loadView();
        });
        System.out.println("Sidebar panel has been activated.");

    }
}
