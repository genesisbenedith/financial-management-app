package csc335.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import csc335.app.models.Expense;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseController implements Initializable, Subject{
    @FXML
    private TextField amountField;

    @FXML
    private MFXDatePicker currentDate;

    //@FXML
    //private DropdownButton expenseCategory;

    @FXML
    private TextField expenseSummary;

    private User currentUser;
    private static final List<Observer> observers = new ArrayList<>();

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
            notifyObservers();

            // [ ] close popup
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid amount format.");
        }
    }

    

}
