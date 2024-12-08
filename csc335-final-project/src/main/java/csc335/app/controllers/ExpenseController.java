package csc335.app.controllers;

/**
 * @author Lauren Schroeder
 * Course: CSC 335 (Fall 2024)
 * File: ExpenseController.java
 * Description: Controller class that controls the popup window that allows for
 *              adding and editing expenses from the user
 */
// ---------------imports--------------------
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.persistence.AccountManager;
import csc335.app.services.ExpenseTracker;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;

/**
 * Handles the UI logic for adding and editing expenses within the application by
 * managing the interaction between the user and the system when creating or modifying an expense, including setting 
 * values for the amount, category, description, and date of an expense. The controller interacts with other components 
 * like the ExpensesController and ExpenseTracker to ensure that changes are properly reflected in the system and the 
 * user interface.
 * 
 * Supports the following features:
 * - adding a new expense with validation for amount, category, and description
 * - editing an existing expense, including pre-filling the form with the expense's current values
 * - handling user interactions for saving or canceling the addition/editing of an expense
 * - validating and formatting numeric input with commas as the user types in the amount field
 * - providing the user with error or success alerts for certain actions (e.g., invalid input, successful expense addition)
 * 
 * Implements the Initializable interface, initializing UI components and setting up event listeners when the view is loaded.
 */
public class ExpenseController implements Initializable{
    // -----------fields from the view---------------------
    @FXML
    private TextField amountField;

    @FXML
    private MFXDatePicker currentDate;

    @FXML
    private TextField expenseSummary;

    @FXML
    private MFXButton save;

    @FXML
    private MFXButton cancel;

    @FXML
    private MFXComboBox<String> expenseCategory;

    @FXML
    private Label title;

    //-----------------instance variables--------------------------------
    /**
     * selectedCategory: The category selected by the user from the expense category list.
     * This represents the type of expense (e.g., "Food", "Entertainment").
     * 
     * exController: The controller responsible for managing expense-related operations.
     * This controller interacts with the model to handle expenses and update the view.
     */
    private String selectedCategory;
    private ExpensesController exController;
    
    //----------------------------methods------------------------------------

    /**
     * loads the window and the information/visuals needed with their implementations
     * @param location   The location of the FXML file.
     * @param resources  The resource bundle for the application.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized");
        onCancelClick();
        handleAddExpenseClick();
        addCategories();
        //List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
    }

    /**
     * Displays an alert with the given type, title, and message
     * @param alertType the type of the alert (e.g., ERROR, INFORMATION)
     * @param title the title of the alert window
     * @param message the content message to display in the alert
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Converts a LocalDate to a Calendar instance
     * @param date the LocalDate to convert
     * @return the corresponding Calendar instance
     */
    public Calendar localDateToCalenderDate(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        return calendar;
    }

    /**
     * Adds predefined expense categories to the expenseCategory ComboBox and sets a listener
     * to handle category selection.
     */
    public void addCategories(){
        ObservableList<String> list = FXCollections.observableArrayList("Food", "Entertainment", "Transportation", "Utilities", "Healthcare", "Other");
        expenseCategory.setItems(list);
        expenseCategory.setPromptText("Select Category"); // Shows default text when nothing is selected
    
        // Add selection listener
        expenseCategory.setOnAction(_ -> {
            selectedCategory = expenseCategory.getValue();
        });
        
    }

    /**
     * Sets the ExpensesController instance for this ExpenseController
     * @param controller the ExpensesController instance
     */
    public void setExController(ExpensesController controller) {
        this.exController = controller;
    }

    /**
     * retrieves the selected category from the expenseCategory ComboBox
     * @return the selected category as a String
     */
    public String getSelectedCategory() {
        return expenseCategory.getValue();
    }

    /**
     * sets the selected category in the expenseCategory ComboBox
     * @param category the category to be selected
     */
    public void setSelectedCategory(String category) {
        expenseCategory.setValue(category);
    }

    /**
     * Adds numeric validation with comma formatting to a TextField and 
     * only allows valid numbers and formats them with commas as the user types.
     * @param textField the TextField to add validation and formatting to
     * @throws ParseException if there is an error parsing the input text
     */
    public void addNumericValidationWithCommas(TextField textField) {
        // Define a number formatter for formatting numbers with commas
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);

        // Create a TextFormatter to handle both input validation and formatting
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change; // Allow clearing the field
            }

            try {
                // Strip out commas for internal parsing
                String plainText = change.getControlNewText().replace(",", "");

                // Check if the plain text is a valid number with optional decimal
                if (plainText.matches("\\d*\\.?\\d*")) {
                    // Parse the number and reformat with commas
                    Number parsedNumber = format.parse(plainText);
                    String formattedText = format.format(parsedNumber);

                    // Set the new value in the control with formatted text
                    change.setText(formattedText);
                    change.setRange(0, change.getControlText().length()); // Replace the entire text
                    return change; // Accept the change with formatted text
                }
            } catch (ParseException e) {
            }
            return null; // Reject any invalid input
        });

        // Apply the TextFormatter to the TextField
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Handles the click event for adding an expense by validating the input fields,
     * creating a new Expense instance, and adding it to the ExpenseTracker.
     * @throws NumberFormatException if the amount field contains an invalid number
     */
    public void handleAddExpenseClick(){
        save.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (amountField == null) {
                showAlert(AlertType.ERROR, "Error", "All fields are required.");
                return;
            }
            try {
                double amount = Double.parseDouble(amountField.getText().substring(1));
                addNumericValidationWithCommas(amountField);
                if (amount <= 0) {
                    showAlert(AlertType.ERROR, "Error", "Amount must be greater than 0.");
                    return;
                }
                //showAlert(AlertType.INFORMATION, "Success", "Expense added successfully.");
                Category category = Category.valueOf(selectedCategory.trim().toUpperCase());

                // Save expense to user
                
                ExpenseTracker.TRACKER.addExpense(new Expense(localDateToCalenderDate(currentDate.getValue()), category, amount, expenseSummary.getText()));
                AccountManager.ACCOUNT.saveUserAccount();
                View.EXPENSE.closePopUpWindow();
                exController.loadExpenses(ExpenseTracker.TRACKER.getExpenses());
                
                // [ ] close popup
            } catch (NumberFormatException t) {
                showAlert(AlertType.ERROR, "Error", "Invalid amount format.");
            }
                    
            }
        });
       
    }

    /**
     * Handles the click event for canceling the expense addition closing the popup window when clicked
     */
    public void onCancelClick(){
        cancel.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                View.EXPENSES.loadView();
            }
        });
    }

    /**
     * sets the content of the expense form for editing an existing expense
     * @param expense the Expense instance to be edited
     */
    public void setContentText(Expense expense){
        title.setText("Edit Expense");
        amountField.setText(String.valueOf(expense.getAmount()));
        currentDate.setValue(expense.getCalendarDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        expenseSummary.setText(expense.getDescription());
        expenseCategory.setValue(expense.getCategory().toString());
    }

}