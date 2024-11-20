package csc335.app.controllers;

/**
 * Author(s): Lauren Schroeder
 * File: ExpenseController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ExpenseController {
    @FXML
    private AnchorPane contentArea;

    /**
     * 
     * @param fxmlPath
     */
    public void loadPage(String fxmlPath){
        try {
            if (contentArea != null) {
                Pane view = FXMLLoader.load(getClass().getResource(fxmlPath));
                contentArea.getChildren().clear();
                contentArea.getChildren().add(view);
            } else {
                System.err.println("contentArea is null. Check FXML and Controller binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData(){
        
    }

    private void importFileClick(){

    }

    private void addNewExpenseClick(){
        
    }

    private void editExpenseClick(){
        
    }

    private void removeExpenseClick(){
        
    }

    private void dateFrom(){
        
    }

    private void dateTo(){
        
    }

    private void progressBarFill(){
        
    }

    private void progressBarClick(){
        
    }
}
