package csc335.app.controllers;

import java.io.IOException;
import csc335.app.UserSessionManager;
import csc335.app.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    // @FXML
    // public void loadContent(String fxml) {
    //     try {
    //         if (contentArea != null) {
    //             // Load the FXML and set it as the content of the content area
    //             Pane view = FXMLLoader.load(getClass().getResource(fxml));
    //             Pane sideBar = navigation.load(fxml);
    //             contentArea.getChildren().clear();
    //             contentArea.getChildren().add(view);
    //             if (sideBar != null) {
    //                 contentArea.getChildren().add(sideBar);
    //             }
    //         } else {
    //             System.err.println("Content area is not initialized.");
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // @FXML
    // protected void handleStart() {
    //     loadContent("/views/DashboardView.fxml");
    // }
}
