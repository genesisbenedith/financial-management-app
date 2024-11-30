package csc335.app.controllers;

import csc335.app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LogOutController {
    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private void initialize(){
        System.out.print("You have successfully logged out!");
        DashboardController.currentUser = null;
    }

    @FXML
    private void handleLogIn(){
        try {
        App app = new App();
        app.start((Stage) yesButton.getScene().getWindow());
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void handleClose(){
        Platform.exit();
    }
    
}
