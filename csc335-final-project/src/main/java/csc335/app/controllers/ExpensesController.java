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
import javafx.scene.layout.VBox;
//import scala.collection.immutable.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

//import javax.faces.event.ActionEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import csc335.app.Category;
import csc335.app.models.Expense;
import csc335.app.persistence.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.mfxcore.controls.Label;


public class ExpensesController implements Initializable{
    @FXML
    private AnchorPane contentArea;
    private static final String USER_DATA_DIRECTORY = "data/users";
    private FlowPane mainPane, importPane, addNewPane, editPane, removePane;
    private Scene mainScreen, importFile, addNewExpense, editExpense, removeExpense, progressBar;
    private Button importFileButton, addNewExpenseButton, editExpenseButton, removeExpenseButton, dateFromButton, dateToButton, progressBarButton;
    private Set<String> expenseCategories = new HashSet<>();
    private List<Expense> expenses = new ArrayList<>();
    private double totalBudget = 0.0;
    private double totalExpenses = 0.0;
    private String username;
    
    private Pane[] expensePanes;

    @FXML
    private MFXButton clearButton;

    @FXML
    private MFXDatePicker dateFrom;
    LocalDate startDate;

    @FXML
    private MFXDatePicker dateTo;
    LocalDate endDate;

    @FXML
    private Pane expensePane;

    @FXML
    private MFXScrollPane expenseList;

    @FXML
    private VBox vBox;

    @FXML
    private Label amountText;

    @FXML
    private Label categoryText;

    @FXML
    private Label dateText;

    @FXML
    private Label summaryText;

    private User currentUser;
    private String currentDate;
    private String currentCategory;
    private String currentAmount;
    private String currentSummary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized.");
    }

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

    // @Override
    // public void addObserver(Observer observer) {
    //     observers.add(observer);
    // }

    // @Override
    // public void removeObserver(Observer observer) {
    //     observers.remove(observer);
    // }

    // @Override
    // public void notifyObservers() {
    //     for (Observer observer : observers) {
    //         observer.update();
    //     }
    // }

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

    private void importFileClick(){
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Import File");
        chooseFile.setInitialDirectory(new File("e:\\"));
        //Label label = new Label("no files selected");
        File selectedFile = chooseFile.showSaveDialog(null);
        if (selectedFile != null) {
            // Process through the selected file
            //File file = new File(selectedFile.getAbsolutePath());
            String fileName = selectedFile.getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Process the line
                    String[] expenseInfo = line.split(",");
                    // Defining date, category, amount, and description for expense
                    String[] date = expenseInfo[0].trim().split("-");
                    Category category = Category.valueOf(expenseInfo[1].trim().toUpperCase());
                    double amount = Double.parseDouble(expenseInfo[2].trim());
                    String description = expenseInfo[3].trim();

                    // Defining calendar for expense date
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                    Expense expense = new Expense(calendar, category, amount, description);
                    currentUser.addExpense(expense);
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }

            //label.setText(selectedFile.getAbsolutePath() + "  selected");
        }
        else {
            System.out.println("File selection cancelled.");
        }

    }

    private void addNewExpenseClick(){
        ExpenseController addExpense = new ExpenseController();
        ViewManager.getViewManager().loadView(View.EXPENSE);
    }

    private void editExpenseClick(){
        
    }

    private void removeExpenseClick(){
        
    }

    private void dateFrom(){
        startDate = dateFrom.getValue();
    }

    private void dateTo(){
        endDate = dateTo.getValue();
    }

    private void addExpensesToScroll(){
        Pane newLoadedPane;
        try {
            newLoadedPane = FXMLLoader.load(getClass().getResource("../views/ExpensePane.fxml"));
            dateText = "";

            vBox.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private void progressBarFill(){
        
    }

    private void progressBarClick(){
        
    }
}
