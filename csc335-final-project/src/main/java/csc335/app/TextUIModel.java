package csc335.app;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;

public class TextUIModel {

    private static User currentUser;

    protected TextUIModel() {
        currentUser = UserSessionManager.getCurrentUser();
    }

    /* ------------------------------ Getters ------------------------------ */

    protected String getUsername() {
        return currentUser.getUsername();
    }

    protected List<Budget> getAllBudgets() {
        return Collections.unmodifiableList(currentUser.getAllBudgets()); 
    }

    protected List<Expense> getAllExpenses() {
        return Collections.unmodifiableList(currentUser.getAllExpenses()); 
    }

    /* ------------------------------ Setters ------------------------------ */

    protected void setBudget(Category budgetCategory, double budgetLimit) {
       currentUser.setBudget(budgetCategory, budgetLimit);
    }

    /* ------------------------------ Other Methods ------------------------------ */

    protected void addExpense(LocalDate date, Category category, double amount, String description) {
        // Add expense to user
        Expense newExpense = new Expense(date, category, amount, description);
        currentUser.addExpense(newExpense);
        
        // Update user's budgets
        currentUser.updateUserBudgets();
    }

    /**
     * 
     * @param expenseNo
     * @param date
     * @param category
     * @param amount
     * @param description
     */
    protected void editExpense(int expenseNo, LocalDate date, Category category, double amount, String description) {
        Expense expense = currentUser.getAllExpenses().get(expenseNo);
        expense.setDate(date);
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setDescription(description);
    }

}
