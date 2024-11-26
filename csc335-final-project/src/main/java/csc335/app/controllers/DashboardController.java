package csc335.app.controllers;

import csc335.app.models.User;
import javafx.fxml.FXML;
import csc335.app.controllers.NavController;

public class DashboardController {
    @FXML
    private NavController navigation;
    protected static User user;

    public static void loadUserInformation(String username) {
        user = new User(null, null, null);
    }

    
}
