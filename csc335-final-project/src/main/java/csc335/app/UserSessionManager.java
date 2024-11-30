package csc335.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;

public class UserSessionManager extends Subject {
    
    private static UserSessionManager userSessionManager;
    private static User currentUser;

    /* ------------------------------ Constructor ------------------------------ */
    private UserSessionManager () {
        userSessionManager = null;
        currentUser = null;
    }

    /* ------------------------------ Getter Methods ------------------------------ */

    /**
     * 
     * @return
     */
    public static User getCurrentUser() {
        return !hasActiveUser() ? null : new User(currentUser.getUsername(),  currentUser.getEmail(), currentUser.getPassword());
    }

    private static UserSessionManager getInstance() {
        return userSessionManager;
    }

    public static String getUsername() {
        return currentUser.getUsername();
    }

    public static List<Budget> getBudgets() {
        return currentUser.getBudgets();
    }

    public static List<Expense> getExpenses() {
        return currentUser.getExpenses();
    }

    /* ------------------------------ Setter Methods ------------------------------ */

    public static void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        currentUser = user;
        getInstance().notifyObservers();
    }

    /* ------------------------------ Other Methods ------------------------------ */
    
    public static boolean hasActiveUser() {
        return !(currentUser == null);
    }

    /**
     * 
     */
    public void resetCurrentUser() {
        currentUser = null;
    }

    public void addExpense(Expense expense) {
        currentUser.addExpense(expense);
    }

    public void removeExpense(Expense expense) {
        currentUser.removeExpense(expense);
    }
}
