package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

// [ ] Needs file comment
/**
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Observer {

    // [ ] Needs method comment
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome, ");
    }


    // [ ] Needs method comment
    @Override
    public void update() {
        // [ ]: Update 6-month summary bar chart

        // [ ]: Update expense/budget summary pie chart

        // [ ]: Update recent expenses panel

        // [ ]: Update notification inbox
    }

    

}
