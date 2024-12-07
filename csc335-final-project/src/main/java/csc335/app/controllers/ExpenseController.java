package csc335.app.controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseController implements Initializable{
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

    private User currentUser;
    private String selectedCategory;
    private ExpensesController exController;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        onCancelClick();
        handleAddExpenseClick();
        //List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
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

    public Calendar localDateToCalenderDate(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        return calendar;
    }

    public void addCategories(){
        ObservableList<String> list = FXCollections.observableArrayList("Food", "Entertainment", "Transportation", "Utilities", "Healthcare", "Other");
        expenseCategory.setItems(list);
        selectedCategory = (String) expenseCategory.getValue();
        
    }

    public void addNumericValidationWithCommas(TextField textField) {
        // Define a number formatter for formatting numbers with commas
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US); // US format for commas

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
                e.printStackTrace();
            }
            return null; // Reject any invalid input
        });

        // Apply the TextFormatter to the TextField
        textField.setTextFormatter(textFormatter);
    }

    // use this for the save click
    public void handleAddExpenseClick(){
        if (amountField == null) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText());
            addNumericValidationWithCommas(amountField);
            if (amount <= 0) {
                showAlert(AlertType.ERROR, "Error", "Amount must be greater than 0.");
                return;
            }
            //showAlert(AlertType.INFORMATION, "Success", "Expense added successfully.");
            Category category = Category.valueOf(selectedCategory.trim().toUpperCase());

            // Save expense to user
            
            ExpenseTracker.TRACKER.addExpense(new Expense(localDateToCalenderDate(currentDate.getValue()), category, amount, expenseSummary.getText()));

            exController.loadExpenses(ExpenseTracker.TRACKER.getExpenses());
            onSaveClick();

            // [ ] close popup
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid amount format.");
        }
    }

    public void onCancelClick(){
        cancel.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                View.EXPENSES.loadView();
            }
        });
    }

    public void onSaveClick(){
        save.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                View.EXPENSES.loadView();
            }
        });
    }

    public void setContentText(Expense expense){
        title.setText("Edit Expense");
        amountField.setText(String.valueOf(expense.getAmount()));
        currentDate.setValue(expense.getCalendarDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        expenseSummary.setText(expense.getDescription());
        expenseCategory.setValue(expense.getCategory().toString());
    }

}