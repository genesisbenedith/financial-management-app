package csc335.app.controllers;

/**
 * Author(s): Lauren Schroeder
 * File: ExpenseController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import scala.collection.immutable.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.event.ActionEvent;

import java.util.ArrayList;
//import java.util.List;
import csc335.app.UserAuth;


public class ExpenseController {
    @FXML
    private AnchorPane contentArea;
    private static final String USER_DATA_DIRECTORY = "data/users";
    private FlowPane mainPane, importPane, addNewPane, editPane, removePane;
    private Scene mainScreen, importFile, addNewExpense, editExpense, removeExpense, dateFrom, dateTo, progressBar;
    private Button importFileButton, addNewExpenseButton, editExpenseButton, removeExpenseButton, dateFromButton, dateToButton, progressBarButton;
    private Set<String> expenseCategories = new HashSet<>();
    private List<Expense> expenses = new ArrayList<>();
    private double totalBudget = 0.0;
    private double totalExpenses = 0.0;
    private String username;

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
                Pane importFile = new Pane("importFile");
                importFile.setOnMouseClicked((MouseEvent click)->{
                    importFileClick(click, ActionEvent);
                });
            } else {
                System.err.println("contentArea is null. Check FXML and Controller binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param expenses
     * @param totalBudget
     * @param totalExpenses
     */
    private void saveData(String username, List expenses, String totalBudget, String totalExpenses) {
        File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.write("Expenses: " + expenses + "\n");
            writer.write("Total Budget: " + totalBudget + "\n");
            writer.write("Total Expenses: " + totalExpenses + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importFileClick(ActionEvent click, ActionEvent fileEnter){

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
