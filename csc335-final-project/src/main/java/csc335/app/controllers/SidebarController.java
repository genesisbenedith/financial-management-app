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
import javafx.scene.input.MouseEvent;
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
     * @author Chelina Obiang
     * @author Genesis Benedith
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
        // dashboardPane.setOnMouseClicked(click -> ViewManager.INSTANCE.loadView(View.DASHBOARD));
        // expensePane.setOnMouseClicked(click -> ViewManager.INSTANCE.loadView(View.EXPENSE));
        // budgetPane.setOnMouseClicked(click -> ViewManager.INSTANCE.loadView(View.BUDGET));
        // reportPane.setOnMouseClicked(click -> ViewManager.INSTANCE.loadView(View.REPORT));
        // logoutPane.setOnMouseClicked(click -> handleLogOutPaneClicked());
        System.out.println("Sidebar panel has been activated.");

    }

    @FXML
    private void handleBudgetPaneClick(MouseEvent event) {
        ViewManager.INSTANCE.loadView(View.BUDGET);
    }

    @FXML
    private void handleExpensePaneClick(MouseEvent event) {
        ViewManager.INSTANCE.loadView(View.EXPENSE);
    }

    @FXML
    private void handleReportPaneClick(MouseEvent event) {
        ViewManager.INSTANCE.loadView(View.REPORT);
    }

    @FXML
    private void handleDashboardPaneClick(MouseEvent event) {
        ViewManager.INSTANCE.loadView(View.DASHBOARD);
    }

    @FXML
    private void handleLogOutPaneClick() {
        UserSessionManager.INSTANCE.resetCurrentUser();
        ViewManager.INSTANCE.loadView(View.LOGIN);
    }

   
}