package csc335.app.controllers;

// [ ] Complete file comment
/**
 * @author Genesis Benedith
 */
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

// [ ] Needs class comment
public class SplashController {

    @FXML
    private ImageView arrow; // JavaFX element that displays a button

    @FXML
    /**
     * 
     * @param location
     * @param resources
     */
    // [ ] Complete method comment
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to Finantra, the Personal Finance Assistant!\n Click the white arrow to continue.");
        
        // Handling click event for the arrow image, redirecting client to login page
        arrow.setOnMouseClicked(click -> ViewManager.getViewManager().loadView(View.LOGIN));
    }

}
