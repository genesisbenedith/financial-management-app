package csc335.app.controllers;

import java.io.IOException;
import csc335.app.UserSessionManager;
import csc335.app.models.User;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.Pane;

public class DashboardController {

    @FXML
    private NavController navigation;

    @FXML
    private AnchorPane contentArea;

    protected static User currentUser;

    @FXML 
    public static void initialize() throws IOException {
        currentUser = UserSessionManager.getCurrentUser();
        System.out.println("Welcome, " + currentUser.getUsername()); 
    }

    @FXML
    private void handleGoToDashboardClick() {
        AuthController.getInstance().loadContent("/views/DashboardView.fxml");
    }

    @FXML
    private void handleGoToBudgetClick() {
        AuthController.getInstance().loadContent("/views/BudgetView.fxml");
    }

    //TODO: The rest of the Clicks go here
}
