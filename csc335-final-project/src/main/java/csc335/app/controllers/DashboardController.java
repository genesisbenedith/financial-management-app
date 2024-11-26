package csc335.app.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc335.app.UserSessionManager;
import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
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
