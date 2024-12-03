package csc335.app.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;

// [ ] Complete file coment
/**
 * @author Genesis Benedith
 */

// [ ] Complete class coment
/**
 * 
 */
public class User {
    private String username;
    private String email;
    private String hashedPassword;
    private final String salt;
    private final Map<Category, List<Expense>> expensesByCategory; // Maps categories to a list of expenses
    private final Map<Category, Budget> budgetsByCategory; // Maps categories to budgets

    /* ------------------------------ Constructor ------------------------------ */

    protected User(String username, String email, String hashedPassword, String salt) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.expensesByCategory = new HashMap<>();
        this.budgetsByCategory = new HashMap<>();
    }

    /* ------------------------------ Getters ------------------------------ */

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public Map<Category, List<Expense>> getExpensesByCategory() {
        return Collections.unmodifiableMap(expensesByCategory);
    }

    public Map<Category, Budget> getBudgetsByCategory() {
        return Collections.unmodifiableMap(budgetsByCategory);
    }

    /* ------------------------------ Setters ------------------------------ */

    public void setEmail(String email) {

        this.email = email;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.hashedPassword = password;
    }

    public void setBudgets() {
        for (Category category : Category.values()) {
            this.budgetsByCategory.put(category, new Budget(category, 0));
        }
    }

    /*
     * ------------------------------ Helper Methods ------------------------------
     */

    public void setBudget(Budget budget) {
        this.budgetsByCategory.put(budget.getCategory(), budget);
    }

    public void addExpense(Expense expense) {
        // Check if expense category is already a key in map
        if (this.expensesByCategory.containsKey(expense.getCategory())) {
            // Add expense to the category's list of expenses
            this.expensesByCategory.get(expense.getCategory()).add(expense);
        } else {
            // Create a new list of expenses for category and add to map
            List<Expense> categoryExpenses = new ArrayList<>();
            categoryExpenses.add(expense);
            this.expensesByCategory.put(expense.getCategory(), categoryExpenses);
        }
    }

    public void removeExpense(Expense expense) {
        // Remove the expense from its category list
        this.expensesByCategory.get(expense.getCategory()).remove(expense);

        // Update the budget for the category after removing the expense
        updateUserBudgets();
    }

    public void updateUserBudgets() {
        // Update each budget with the most recent saved expenses
        for (Category category : this.expensesByCategory.keySet()) {
            for (Expense expense : this.expensesByCategory.get(category)) {
                budgetsByCategory.get(category).addExpense(expense);
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information:\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Email: ").append(email).append("\n");

        sb.append("\nBudgets:\n");
        for (Map.Entry<Category, Budget> entry : this.budgetsByCategory.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ")
                    .append(entry.getValue().getTotalSpent()).append("/")
                    .append(entry.getValue().getLimit()).append("\n");
        }

        double totalSpent = 0.0;
        sb.append("\nExpenses:\n");

        Map<Category, List<Expense>> eMap = getExpensesByCategory();
        for (Category category : eMap.keySet()) {
            for (Expense expense : eMap.get(category)) {
                sb.append("  ").append(expense).append("\n");
            }
        }

        sb.append("Total Expenses: $").append(String.format("%.2f", totalSpent)).append("\n");

        return sb.toString();
    }

    /* ------------------------------ INACTIVE ------------------------------ */

    /**
     * Gets all of the user's expenses (no filter)
     * 
     * @return list of expenses
     */
    public List<Expense> getExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (List<Expense> expenses : this.expensesByCategory.values()) {
            allExpenses.addAll(expenses);
        }
        return allExpenses;
    }

    /**
     * Get's the user's expenses within a specific category
     * 
     * @param category
     * @return a list of expenses
     */
    public List<Expense> getExpenses(Category category) {
        return this.expensesByCategory.get(category);
    }

    /**
     * Get's the users expenses within a specific month and year
     * 
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return a list of expenses
     */
    public List<Expense> getExpenses(int month, int year) {
        List<Expense> monthExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            // Check the date of the expense
            if (expense.getCalendarDate().get(Calendar.MONTH) == month
                    && expense.getCalendarDate().get(Calendar.YEAR) == year) {
                monthExpenses.add(expense);
            }
        }
        return monthExpenses;
    }

    /**
     * Get's the total amount spent (no filter)
     * 
     * @param category
     * @return the total amount spent 
     */
    public double getTotalExpenses() {
        double total = 0;
        for (Expense expense : getExpenses()) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Get's the total amount spent within a specific category
     * 
     * @param category
     * @return the total amount spent 
     */
    public double getTotalExpenses(Category category) {
        double total = 0;
        for (Expense expense : getExpenses(category)) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Get's the total amount spent within a specific month and year
     * 
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return the total amount spent 
     */
    public double getTotalExpenses(int month, int year) {
        double total = 0;
        for (Expense expense : getExpenses(month, year)) {
            total += expense.getAmount();
        }
        return total;
    }

}
