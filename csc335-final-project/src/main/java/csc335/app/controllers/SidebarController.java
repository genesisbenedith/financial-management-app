package csc335.app.controllers;

// [ ] Finish file comment

/**
 * @author Chelina Obiang
 * @author Genesis Benedith
 */
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

// [ ] Needs class comment
public class SidebarController {

    @FXML
    private VBox sidebar; // [ ] Either remove this field or use it

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

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        

        // [ ]: Change the color of the button that is assigned to the current view
        /** For example, if the client is currently on the dashboard view, and the color
         * of the dashboard button is black, then the buttons on the sidebar for
         * every other view should be purple, etc.
         */

        
        // REVIEW Action listeners
        dashboardPane.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.DASHBOARD));
        expensePane.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.EXPENSE));
        budgetPane.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.BUDGET));
        reportPane.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.REPORT));
        // [ ] Implement logout feature in @see  
        logoutPane.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.LOGOUT));
        System.out.println("Sidebar panel has been activated.");

    }

   
}
