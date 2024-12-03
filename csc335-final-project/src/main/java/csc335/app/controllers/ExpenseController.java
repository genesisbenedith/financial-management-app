package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.models.Expense;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import com.gluonhq.charm.glisten.control.DropdownButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public class ExpenseController implements Initializable{
    @FXML
    private TextField amountField;

    @FXML
    private MFXDatePicker currentDate;

    //@FXML
    //private DropdownButton expenseCategory;

    @FXML
    private TextField expenseSummary;

    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized");
        UserSessionManager.getUserSessionManager();
        currentUser = UserSessionManager.getCurrentUser();
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

    public void handleAddExpenseClick(){
        if (amountField == null) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert(AlertType.ERROR, "Error", "Amount must be greater than 0.");
                return;
            }
            //showAlert(AlertType.INFORMATION, "Success", "Expense added successfully.");
            
            // Save expense to user

            currentUser.addExpense(new Expense(localDateToCalenderDate(currentDate.getValue()), null, amount, expenseSummary.getText()));


            // [ ] close popup
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid amount format.");
        }
    }

    

}
