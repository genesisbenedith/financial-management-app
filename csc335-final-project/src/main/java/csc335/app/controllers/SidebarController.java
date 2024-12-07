package csc335.app.controllers;

// [ ] Finish file comment

/**
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

// [ ] Needs class comment
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

        currentUser = UserSessionManager.SESSION.getCurrentUser();
        dashboardPane.setOnMouseClicked(click -> { View.DASHBOARD.loadView(); });
        expensePane.setOnMouseClicked(click -> { View.EXPENSES.loadView(); });
        budgetPane.setOnMouseClicked(click -> { View.BUDGET.loadView(); });
        logoutPane.setOnMouseClicked(click -> {
            UserSessionManager.SESSION.resetCurrentUser();
            View.LOGIN.loadView();
        });
        System.out.println("Sidebar panel has been activated.");
        initializeUserInfo();

    }

    public void initializeUserInfo() {
        userAvatar.setInitials(currentUser.getUsername().substring(0,1).toUpperCase());
        username.setText(currentUser.getUsername());
        email.setText(currentUser.getEmail());

    }


}

