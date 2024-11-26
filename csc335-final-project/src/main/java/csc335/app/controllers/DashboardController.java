package csc335.app.controllers;

import csc335.app.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private NavController navigation;

    @FXML
    private AnchorPane contentArea;

    private User user;

 
    public void loadUserInformation(String username) {
        // Initialize the user with appropriate data
        this.user = new User(username, null, null);
    }

    @FXML
    public void loadContent(String fxml) {
        try {
            if (contentArea != null) {
                // Load the FXML and set it as the content of the content area
                Pane view = FXMLLoader.load(getClass().getResource(fxml));
                Pane sideBar = navigation.load(fxml);
                contentArea.getChildren().clear();
                contentArea.getChildren().add(view);
                contentArea.getChildren().addAll(sideBar); //all other panes need to be added as well
            } else {
                System.err.println("Content area is not initialized.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleStart() {
        loadContent("/views/DashboardView.fxml");
    }
}
