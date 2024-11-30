package csc335.app.controllers;

import java.io.IOException;

import csc335.app.FileIOManager;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private AnchorPane contentArea; // Pane representing the dashboard view

    @FXML
private VBox sidebar;


    @FXML 
    public void initialize() throws IOException {
        System.out.println("Welcome, " + FileIOManager.getAuthenticatedUser());  
    }

}
