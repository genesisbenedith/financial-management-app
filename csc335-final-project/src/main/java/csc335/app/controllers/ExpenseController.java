package csc335.app.controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import csc335.app.Category;
import csc335.app.models.Expense;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.mfxcore.controls.Label;
//import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseController implements Initializable, Subject{
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
    private static final List<Observer> observers = new ArrayList<>();
    private String selectedCategory;
    private ExpensesController exController;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized");
        UserSessionManager.getUserSessionManager();
        currentUser = UserSessionManager.getCurrentUser();
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

    public void loadPage(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadPage'");
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
    @FXML
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

            currentUser.addExpense(new Expense(localDateToCalenderDate(currentDate.getValue()), category, amount, expenseSummary.getText()));
            notifyObservers();

            exController.loadExpenses(currentUser.getExpenses());

            // [ ] close popup
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid amount format.");
        }
    }

    @FXML
    public void onCancelClick(){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void setContentText(Expense expense){
        title.setText("Edit Expense");
        amountField.setText(String.valueOf(expense.getAmount()));
        currentDate.setValue(expense.getCalendarDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        expenseSummary.setText(expense.getDescription());
        expenseCategory.setValue(expense.getCategory().toString());
    }

}
