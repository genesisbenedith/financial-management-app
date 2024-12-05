package csc335.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;

/**
 * Author(s): Lauren Schroeder
 * File: ExpenseController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseButton;
//import scala.collection.immutable.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Locale;
import java.util.Optional;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import io.github.palexdev.materialfx.builders.control.DatePickerBuilder;
import io.github.palexdev.materialfx.controls.BoundTextField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.cell.MFXDateCell;
import io.github.palexdev.mfxcore.controls.Label;


public class ExpensesController implements Initializable, Subject{
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
    private Pane expenseTemplate;

    @FXML
    private MFXScrollPane expenseList;

    @FXML
    private VBox vBox;

    @FXML
    private Label amountChild;

    @FXML
    private Label categoryChild;

    @FXML
    private Label dateChild;

    @FXML
    private Label summaryChild;

    @FXML
    private ImageView edit;

    @FXML
    private ImageView delete;

    @FXML
    private HBox percentBar;

    @FXML
    private Pane foodBar;

    @FXML
    private Pane enterntainmentBar;

    @FXML
    private Pane transportBar;

    @FXML
    private Pane utilityBar;

    @FXML
    private Pane healthcareBar;

    @FXML
    private Pane otherBar;

    private static final List<Observer> observers = new ArrayList<>();
    private User currentUser;
    private Expense expense;
    private String currentDate;
    private String currentCategory;
    private String currentAmount;
    private String currentSummary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized.");

        // Get current user
        currentUser = UserSessionManager.getCurrentUser();
        expenses = currentUser.getExpenses();
        loadExpenses(expenses);
        addObserver(AccountRepository.getAccountRepository());
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
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

    @FXML
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

    @FXML
    private void addNewExpenseClick(){
        ExpenseController addExpense = new ExpenseController();
        ViewManager.getViewManager().loadView(View.EXPENSE);
    }

    private void editExpenseClick(){
        // use add expense popup but with different title and the information already filled in, just editable
    }

    @FXML
    private void dateFrom(){
        startDate = dateFrom.getValue();

        if (startDate != null && dateTo.getValue() == null){
            showAlert(AlertType.ERROR, "Error", "End date must be chosen.");
            return;
        }
        else if (dateTo.getValue().isBefore(startDate)){
            showAlert(AlertType.ERROR, "Error", "End date has to be after start date.");
            return;
        }
        
        endDate = dateTo.getValue();
        if(startDate !=  null){
            dateTo.show();
        }
    }

    @FXML
    private void dateTo(){
        // has to be greater than start date
        if (dateFrom.getValue() == null){
            showAlert(AlertType.ERROR, "Error", "start date must be chosen before end date.");
            return;
        }
        startDate = dateFrom.getValue();
        endDate = dateTo.getValue();
        Calendar startCal = Calendar.getInstance(Locale.getDefault());
        startCal.set(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
        Calendar endCal = Calendar.getInstance(Locale.getDefault());
        endCal.set(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());
        if (!progressBarClick() || clearButton){
            loadExpenses(currentUser.getExpensesInRange(startCal, endCal));
        }
        loadExpenses(currentUser.getExpensesInRange(startCal, endCal, category)); // category will be te category clicked form the bar or the total expenses if none were selected

    }

    private void loadExpenses(List<Expense> expenses){
        // dateChild.setText("");
        // summaryChild.setText("");
        // categoryChild.setText("");
        // amountChild.setText("");
        

        for (Expense expense : expenses) {
            Pane clonedPane = new Pane();

            for (Node node : expenseTemplate.getChildren()) {

                if (node.getId() == "dateChild") {
                    Label originalLabel = (Label) node;
                    // Date to string

                    Label clonedDateLabel = new Label(expense.getStringDate()); // Put date as string here
                    clonedDateLabel.setStyle(originalLabel.getStyle());
                    clonedDateLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedPane);
                }

                if (node.getId() == "summaryChild") {
                    Label originalLabel = (Label) node;

                    Label clonedSumLabel = new Label(expense.getDescription()); 
                    clonedSumLabel.setStyle(originalLabel.getStyle());
                    clonedSumLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedPane);
                }

                if (node.getId() == "categoryChild") {
                    Label originalLabel = (Label) node;

                    Label clonedCatLabel = new Label(expense.getCategory().toString()); 
                    clonedCatLabel.setStyle(originalLabel.getStyle());
                    clonedCatLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedPane);
                }

                if (node.getId() == "amountChild") {
                    Label originalLabel = (Label) node;

                    Label clonedAmtLabel = new Label("$" + String.valueOf(expense.getAmount())); 
                    clonedAmtLabel.setStyle(originalLabel.getStyle());
                    clonedAmtLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedPane);
                }

                if(node.getId() == "edit"){
                    edit.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                        if(newValue){
                            edit.setVisible(true);
                        }
                        else{
                            edit.setVisible(false);
                        }
                    });
                }

                if(node.getId() == "delete"){
                    delete.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                        if(newValue){
                            delete.setVisible(true);
                            delete.setOnMouseClicked(e ->{
                                if (e.getButton() == MouseButton.SECONDARY){
                                    Alert alert = new Alert(AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation Dialog Box");
                                    alert.setHeaderText("Please Confirm!");
                                    alert.setContentText("Are you sure want to delete expense?");
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if(result.get() == ButtonType.OK){
                                        vBox.getChildren().remove(clonedPane);
                                        currentUser.removeExpense(expense);
                                        notifyObservers();
                                    }
                                }
                            });
                        }
                        else{
                            delete.setVisible(false);
                        }
                    });
                }

            }
            vBox.getChildren().add(clonedPane);
        }
        
        
    }

    private void progressBarFill(){
        // make it hoverable with the label and percentage of the total expenses from that catefory visible when hovered over
        // and then it will be clickable and the bar will change to that category and the percentage of the budget for that category already set in expenses will show up
        // clear will send you back to the total budget
        
        for (Budget b : UserSessionManager.getCurrentUser().getBudgetsByCategory().values()) {
            if (b.getCategory().equals(category)) {
                if (b.isExceeded()) {
                    alert.setVisible(true);
                    progress.setProgress(Math.min(1.0, b.getTotalSpent() / b.getLimit()));
                }
                if (b.getLimit() < 0) {
                    showAlert(AlertType.ERROR, "Error", "Budget cannot be set below zero.");
                    return 0;
                }
            }
        }
    }

    private void progressBarClick(){
        
    }

    // [ ] Needs method comment
    /**
     * 
     * @param alertType
     * @param title
     * @param message
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}