package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

// [ ] Needs class comment
public class SplashController implements Initializable {

    @FXML
    private ImageView arrow; // JavaFX element that displays a button

    // [ ] Complete method comment

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to Finantra, the Personal Finance Assistant!\n \nClick the white arrow to continue.\n");

    }


    // Handling click event for the arrow image, redirecting client to login page
    @FXML
    private void handleArrowClick(MouseEvent event) {
        View.LOGIN.loadView();
    }

}