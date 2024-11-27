package csc335.app.controllers;

import java.io.IOException;
import csc335.app.UserSessionManager;
import csc335.app.models.User;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    private static User currentUser;

    @FXML
    private AnchorPane contentArea; // Pane representing the dashboard view

    @FXML 
    public void initialize() throws IOException {
        currentUser = UserSessionManager.getCurrentUser();
        System.out.println("Welcome, " + currentUser.getUsername());  
    }

    

}
