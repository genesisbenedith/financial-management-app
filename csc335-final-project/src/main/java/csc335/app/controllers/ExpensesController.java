package csc335.app.controllers;

/**
 * @author Lauren Schroeder
 * Course: CSC 335 (Fall 2024)
 * File: ExpensesController.java
 * Description: Controller class that controls the window and functions of the Expenses page
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.BudgetTracker;
import csc335.app.services.ExpenseTracker;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class ExpensesController implements Initializable {
    
    // fields from the view
    @FXML
    private AnchorPane contentArea;

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

    @FXML
    private Label expensePercentageLabel;

    @FXML
    private Label budgetHeader;

    @FXML
    private Label expensesHeader;

    @FXML
    private Label totalBudgetAmt;

    @FXML
    private Label totalExpensesAmt;

    @FXML
    private Pane downloadFile;

    private User currentUser;
    private Category categoryClicked;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized.");

        // Get current user
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
        loadExpenses(expenses);
        progressBarFill();
    }


    @FXML
    private void importFileClick(){
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Import File");
        chooseFile.setInitialDirectory(new File("user.home"));
        //Label label = new Label("no files selected");
        chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File selectedFile = chooseFile.showOpenDialog(null);
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
                    calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                    Expense expense = new Expense(calendar, category, amount, description);
                    ExpenseTracker.TRACKER.addExpense(expense);
                    List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
                    loadExpenses(expenses);
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
    private void downloadFileClick(){
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Export Expenses");
        chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        chooseFile.setInitialDirectory(new File("e:\\")); // You can set this to a default folder of your choice

        // Show save dialog and get the selected file
        File selectedFile = chooseFile.showSaveDialog(null);
        if (selectedFile != null) {
            // Ensure the file has the correct .txt extension
            String targetFilePath = selectedFile.getAbsolutePath();
            if (!targetFilePath.endsWith(".txt")) {
                targetFilePath += ".txt";
            }
            try {
                
                // Get the file generated by the export method
                File fileToDownload = AccountManager.ACCOUNT.exportFile(); // This file is generated elsewhere

                // Copy the file to the user's selected location
                Files.copy(fileToDownload.toPath(), new File(targetFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File downloaded successfully to: " + targetFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                View.ALERT.showAlert(AlertType.ERROR, "Error", "An error occurred while exporting the file.");
            }
        } 
        else {
            System.out.println("File selection cancelled.");
        }
    }

    @FXML
    private void addNewExpenseClick(){
        View.EXPENSE.loadView();
    }

    // @FXML
    // private void editExpenseClick(){
    //     // use add expense popup but with different title and the information already filled in, just editable
    //     ViewManager.getViewManager().loadView(View.EXPENSE);
    //     ExpenseController editExpense = new ExpenseController();
    //     editExpense.setContentText(expense);
    // }

    @FXML
    private void dateFrom(){
        startDate = dateFrom.getValue();

        if (startDate != null && dateTo.getValue() == null){
            View.ALERT.showAlert(AlertType.ERROR, "Error", "End date must be chosen.");
            return;
        }
        else if (dateTo.getValue().isBefore(startDate)){
            View.ALERT.showAlert(AlertType.ERROR, "Error", "End date has to be after start date.");
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
            View.ALERT.showAlert(AlertType.ERROR, "Error", "start date must be chosen before end date.");
            return;
        }
        startDate = dateFrom.getValue();
        endDate = dateTo.getValue();
        Calendar startCal = Calendar.getInstance(Locale.getDefault());
        startCal.set(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
        Calendar endCal = Calendar.getInstance(Locale.getDefault());
        endCal.set(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());
        if (categoryClicked == null || clearButtonClick()){
            loadExpenses(ExpenseTracker.TRACKER.filterExpensesInRange(startCal, endCal));
        }
        loadExpenses(ExpenseTracker.TRACKER.filterExpensesInRange(startCal, endCal, categoryClicked)); // category will be the category clicked form the bar or the total expenses if none were selected

    }

    public void loadExpenses(List<Expense> expenses){
        // dateChild.setText("");
        // summaryChild.setText("");
        // categoryChild.setText("");
        // amountChild.setText("");
        if (vBox.getChildren().size() > 1) {
            vBox.getChildren().retainAll(vBox.getChildren().get(0));
        }
        vBox.getChildren().clear();


        for (Expense expense : expenses) {
            Pane clonedPane = new Pane();

            for (Node node : expenseTemplate.getChildren()) {

                if (node.getId() == "dateChild") {
                    Label originalLabel = (Label) node;
                    // Date to string

                    Label clonedDateLabel = new Label(expense.getStringDate()); // Put date as string here
                    clonedDateLabel.setStyle(originalLabel.getStyle());
                    clonedDateLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().addAll(clonedDateLabel);
                }

                if (node.getId() == "summaryChild") {
                    Label originalLabel = (Label) node;

                    Label clonedSumLabel = new Label(expense.getDescription()); 
                    clonedSumLabel.setStyle(originalLabel.getStyle());
                    clonedSumLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedSumLabel);
                }

                if (node.getId() == "categoryChild") {
                    Label originalLabel = (Label) node;

                    Label clonedCatLabel = new Label(expense.getCategory().toString()); 
                    clonedCatLabel.setStyle(originalLabel.getStyle());
                    clonedCatLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedCatLabel);
                }

                if (node.getId() == "amountChild") {
                    Label originalLabel = (Label) node;

                    Label clonedAmtLabel = new Label("$" + String.valueOf(expense.getAmount())); 
                    clonedAmtLabel.setStyle(originalLabel.getStyle());
                    clonedAmtLabel.setFont(originalLabel.getFont());
                    clonedPane.getChildren().add(clonedAmtLabel);
                }

                if(node.getId() == "edit"){
                    edit.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                        if(newValue){
                            edit.setVisible(true);
                            edit.setOnMouseClicked(e->{
                                if (e.getButton() == MouseButton.SECONDARY){
                                   View.EXPENSE.loadView();

                                   // FIX -> THIS WON'T WORK
                                    // ExpenseController editExpense = new ExpenseController();
                                    // editExpense.setContentText(expense); 
                                }
                            });
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
                                        ExpenseTracker.TRACKER.removeExpense(expense);
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

    @FXML
    private boolean clearButtonClick(){
        progressBarFill();
        return true;
    }

    private void progressBarFill(){
        // make it hoverable with the label and percentage of the total expenses from that catefory visible when hovered over
        // and then it will be clickable and the bar will change to that category and the percentage of the budget for that category already set in expenses will show up
        // clear will send you back to the total budget
        //double totalExpenses = UserSessionManager.getCurrentUser().getTotalExpenses();
        double totalBudgetLimits = BudgetTracker.TRACKER.getTotalBudgetLimits();
        //Map<Category, List<Expense>> expensesByCategory = UserSessionManager.getCurrentUser().getExpensesByCategory();
        budgetHeader.setText("Total Budget");
        expensesHeader.setText("Total Expenses");
        totalBudgetAmt.setText("$" + totalBudgetLimits);
        totalExpensesAmt.setText("$" + ExpenseTracker.TRACKER.calculateTotalExpenses());
        for (Category category : Category.values()) {
            double categoryExpenses = ExpenseTracker.TRACKER.calculateTotalExpenses(category);

            // Calculate the percentage of total budget and expenses for this category
            double categoryPercentageOfTotal = BudgetTracker.TRACKER.getBudgetLimit(category) / totalBudgetLimits;
            double categoryExpensePercentage = categoryExpenses / BudgetTracker.TRACKER.getBudgetLimit(category);
            double totalWidth = percentBar.getWidth();
            
            // Create a Region or Pane to represent this category's budget
            Pane categoryBar = new Pane();
            categoryBar.setPrefWidth(categoryPercentageOfTotal * totalWidth); // Width relative to total budget
            categoryBar.setStyle(category.getDefaultColor()); // Reverts to default color
            categoryBar.setStyle(category.getDefaultColor() + ";");
            categoryBar.setStyle(Category.valueOf(category.toString().toUpperCase()).getDefaultColor());
            
            // pieNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
            //     addHoverEffect(nodeStyle, pieNode, Category.valueOf(pieSlice.getName().toUpperCase()));
            // });
               
            // Set hover event to show percentage spent for this category
            categoryBar.setOnMouseEntered(event -> {
                addHoverEffect(categoryBar, Category.valueOf(category.toString()));
                Tooltip.install(categoryBar, new Tooltip(category + ":\n$" + categoryExpensePercentage));
            });

            categoryBar.setOnMouseClicked(event -> {
                System.out.println("Clicked: " + category + " -> " + categoryExpensePercentage);
                progressBarClicked(category, categoryExpensePercentage);
            });
    
            // Add the category bar to the main bar layout (HBox, etc.)
            percentBar.getChildren().add(categoryBar);
        }
    }

    private void addHoverEffect(Node node, Category category) {
        
        node.setOnMouseEntered(event -> {
            node.setStyle("-fx-bar-fill: " + category.getHoverColor() + ";");
            node.setStyle(category.getHoverColor() + ";");
        });

        node.setOnMouseExited(event -> {
            node.setStyle(category.getDefaultColor()); // Reverts to default color
            node.setStyle(category.getDefaultColor() + ";"); // Reverts to default color
        });
    }
    

    private void progressBarClicked(Category category, double categoryExpensePercentage){
        vBox.getChildren().clear();
        categoryClicked = category;
        
        // Load only the expenses for the specific category
        List<Expense> categoryExpenses = ExpenseTracker.TRACKER.filterExpenses(category);
        loadExpenses(categoryExpenses);
        
        double categoryExpensesAmount = ExpenseTracker.TRACKER.calculateTotalExpenses(category);
        double categoryBudgetLimit = BudgetTracker.TRACKER.getBudgetLimit(category);
        budgetHeader.setText(categoryClicked.toString() + "Budget");
        expensesHeader.setText(categoryClicked.toString() + "Expenses");
        totalBudgetAmt.setText("$" + categoryBudgetLimit);
        totalExpensesAmt.setText("$" + categoryExpensesAmount);
        
        // Calculate the percentage of the budget used for this category
        double categoryUsagePercentage = categoryExpensesAmount / categoryBudgetLimit;

        // Get the HBox where we want to display the percentage bar
        HBox percentBar = this.percentBar; // Assuming you have this HBox defined

        // Clear the current content of the HBox before adding the new pane
        percentBar.getChildren().clear();

        // Create a new colored Pane that represents the percentage used of the budget
        Pane categoryBar = new Pane();
        categoryBar.setPrefWidth(percentBar.getWidth() * categoryUsagePercentage); // Set the width relative to the percentage used
        categoryBar.setStyle("-fx-background-color: " + category.getDefaultColor()); // Use the default color of the category
        addHoverEffect(categoryBar, category);
        
        // Optionally, add a Tooltip or label to show the percentage used
        Tooltip.install(categoryBar, new Tooltip(String.format("%.2f", categoryUsagePercentage * 100) + "%"));

        // Add the category bar to the HBox
        percentBar.getChildren().add(categoryBar);

        // Optionally, if you want to update the UI with the exact percentage used
        updateCategoryExpensePercentage(categoryUsagePercentage);
        
        // You can also update the display to show this category's budget breakdown if needed
        System.out.println("Category: " + category + ", Budget: $" + BudgetTracker.TRACKER.getBudgetLimit(category) + ", Expenses: $" + ExpenseTracker.TRACKER.calculateTotalExpenses(category));
    }

    private void updateCategoryExpensePercentage(double categoryExpensePercentage) {
        // Update the UI with the percentage, this can be displayed in a label or progress bar
        // Example:
        
        expensePercentageLabel.setText("Category spent: " + String.format("%.2f", categoryExpensePercentage * 100) + "%");
    }

}