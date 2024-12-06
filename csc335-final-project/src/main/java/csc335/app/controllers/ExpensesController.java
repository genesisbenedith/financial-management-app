package csc335.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;

/**
 * Author(s): Lauren Schroeder
 * File: ExpenseController.java
 * Description:
 */

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseButton;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.mfxcore.controls.Label;


public class ExpensesController implements Initializable, Subject{
    @FXML
    private AnchorPane contentArea;
    private static final String USER_DATA_DIRECTORY = "data/users";
    // private FlowPane mainPane, importPane, addNewPane, editPane, removePane;
    // private Scene mainScreen, importFile, addNewExpense, editExpense, removeExpense, progressBar;
    // private Button importFileButton, addNewExpenseButton, editExpenseButton, removeExpenseButton, dateFromButton, dateToButton, progressBarButton;
    // private Set<String> expenseCategories = new HashSet<>();
    private List<Expense> expenses = new ArrayList<>();
    // private double totalBudget = 0.0;
    // private double totalExpenses = 0.0;
    // private String username;
    
    // private Pane[] expensePanes;

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

    private static final List<Observer> observers = new ArrayList<>();
    private User currentUser;
    private Category categoryClicked;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized.");

        // Get current user
        currentUser = UserSessionManager.getCurrentUser();
        expenses = currentUser.getExpenses();
        loadExpenses(expenses);
        progressBarFill();
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
        ViewManager.getViewManager().loadView(View.EXPENSE);
    }

    @FXML
    private void downloadFileClick(){
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Export Expenses");
        chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        chooseFile.setInitialDirectory(new File("e:\\")); // You can set this to a default folder of your choice

        // Show save dialog and get the selected file
        File selectedFile = chooseFile.showSaveDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getAbsolutePath();
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt"; // Ensure the file has the correct extension
            }

            

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                // Write the header line
                writer.write("Date,Category,Amount,Description\n");

                // Iterate over the expenses and write each one to the file
                for (Expense expense : currentUser.getExpenses()) {
                    // Get expense data
                    String date = String.format("%04d-%02d-%02d", expense.getCalendarDate().get(Calendar.YEAR),
                            expense.getCalendarDate().get(Calendar.MONTH) + 1, expense.getCalendarDate().get(Calendar.DAY_OF_MONTH));
                    String category = expense.getCategory().toString(); // Assuming Category is an enum
                    double amount = expense.getAmount();
                    String description = expense.getDescription();

                    // Write the expense to the file as a CSV row
                    writer.write(String.format("%s,%s,%.2f,%s\n", date, category, amount, description));
                }

                System.out.println("Expenses exported successfully to " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "An error occurred while exporting the file.");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
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
        if (categoryClicked == null || clearButtonClick()){
            loadExpenses(currentUser.getExpensesInRange(startCal, endCal));
        }
        loadExpenses(currentUser.getExpensesInRange(startCal, endCal, categoryClicked)); // category will be the category clicked form the bar or the total expenses if none were selected

    }

    public void loadExpenses(List<Expense> expenses){
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
                            edit.setOnMouseClicked(e->{
                                if (e.getButton() == MouseButton.SECONDARY){
                                   ViewManager.getViewManager().loadView(View.EXPENSE);
                                    ExpenseController editExpense = new ExpenseController();
                                    editExpense.setContentText(expense); 
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
        
        Map<Category, Budget> categoryBudgets = UserSessionManager.getCurrentUser().getBudgetsByCategory();
        double totalBudget = 0.0;
        for (Budget bud : categoryBudgets.values()){
            totalBudget += bud.getLimit();
        }
        //Map<Category, List<Expense>> expensesByCategory = UserSessionManager.getCurrentUser().getExpensesByCategory();
        budgetHeader.setText("Total Budget");
        expensesHeader.setText("Total Expenses");
        totalExpensesAmt.setText("$" + totalBudget);
        totalExpensesAmt.setText("$" + UserSessionManager.getCurrentUser().getExpenses());
        for (Map.Entry<Category, Budget> entry : categoryBudgets.entrySet()) {
            Category category = entry.getKey();
            Budget budget = entry.getValue();
            double categoryExpenses = UserSessionManager.getCurrentUser().getTotalExpenses(category);

            // Calculate the percentage of total budget and expenses for this category
            double categoryPercentageOfTotal = budget.getLimit() / totalBudget;
            double categoryExpensePercentage = categoryExpenses / budget.getLimit();
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
                progressBarClicked(category, budget, categoryExpensePercentage);
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
    

    private void progressBarClicked(Category category, Budget budget, double categoryExpensePercentage){
        vBox.getChildren().clear();
        categoryClicked = category;
        budgetHeader.setText(categoryClicked.toString() + "Budget");
        expensesHeader.setText(categoryClicked.toString() + "Expenses");
        totalExpensesAmt.setText("$" + budget.getLimit());
        totalExpensesAmt.setText("$" + UserSessionManager.getCurrentUser().getExpenses(categoryClicked));
        // Load only the expenses for the specific category
        List<Expense> categoryExpenses = UserSessionManager.getCurrentUser().getExpensesByCategory().get(category);
        loadExpenses(categoryExpenses);
        
        double categoryExpensesAmount = UserSessionManager.getCurrentUser().getTotalExpenses(category);
        double categoryBudgetLimit = budget.getLimit();
        
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
        System.out.println("Category: " + category + ", Budget: $" + budget.getLimit() + ", Expenses: $" + UserSessionManager.getCurrentUser().getTotalExpenses(category));
    }

    private void updateCategoryExpensePercentage(double categoryExpensePercentage) {
        // Update the UI with the percentage, this can be displayed in a label or progress bar
        // Example:
        
        expensePercentageLabel.setText("Category spent: " + String.format("%.2f", categoryExpensePercentage * 100) + "%");
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