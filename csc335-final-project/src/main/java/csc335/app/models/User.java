package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: User.java
 * Description: Model class that represents a user account
 */

import java.io.File;
import java.time.LocalDate;

import java.util.*;

import csc335.app.FileIOManager;

public class User {
    private String username;
    private String email;
    private String password;
    private Map<Category, List<Expense>> categorizedExpenses; // Maps categories to a list of expenses
    private Map<Category, Budget> categorizedBudgets; // Maps categories to budgets

    /* ------------------------------ Constructor ------------------------------ */

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.categorizedExpenses = new HashMap<>();
        this.categorizedBudgets = new HashMap<>();
    }

    /* ------------------------------ Getters ------------------------------ */

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Expense> getExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (Map.Entry<Category, List<Expense>> expenseCategory : categorizedExpenses.entrySet()) {
            allExpenses.addAll(expenseCategory.getValue());
        }
        return Collections.unmodifiableList(allExpenses);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (Map.Entry<Category, List<Expense>> expenseCategory : categorizedExpenses.entrySet()) {
            allExpenses.addAll(expenseCategory.getValue());
        }
        return Collections.unmodifiableList(allExpenses);
    }

    public List<Budget> getBudgets() {
        List<Budget> budgets = new ArrayList<>();
        for (Category category : this.categorizedBudgets.keySet()) {
            budgets.add(this.categorizedBudgets.get(category));
        }
        return Collections.unmodifiableList(budgets);
    }

    public File getMonthlyReport(LocalDate reportDate) {        
        return FileIOManager.createUserReport(getUsername(), reportDate);
    }

    public List<Expense> getMonthExpenses(int month, int year) {
        List<Expense> monthExpenses = new ArrayList<>();
        for (Expense expense : getAllExpenses()) {
            // Check the date of the expense
            if (expense.getCalendarDate().get(Calendar.MONTH) == month
                    && expense.getCalendarDate().get(Calendar.YEAR) == year) {
                monthExpenses.add(expense);
            }
        }
        return monthExpenses;
    }

    public double getTotalExpenses() {
        double totalExpenses = 0;
        for (Expense expense : getAllExpenses()) {
            totalExpenses += expense.getAmount();
        }
        return totalExpenses;
    }

    public List<Expense> getCategoryExpenses(Category expenseCategory) {
        List<Expense> categoryExpenses = new ArrayList<>();
        if (categorizedExpenses.containsKey(expenseCategory)) {
            categoryExpenses.addAll(categorizedExpenses.get(expenseCategory));
        }
        return Collections.unmodifiableList(categoryExpenses);
    }

    public Map<Category, Budget> getCategorizedBudgets() {
        return Collections.unmodifiableMap(categorizedBudgets);
    }

    public Map<Category, List<Expense>> getCategorizedExpenses() {
        return Collections.unmodifiableMap(categorizedExpenses);
    }

    /* ------------------------------ Setters ------------------------------ */

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* ------------------------------ Other Methods ------------------------------ */

    public void addBudget(Budget budget) {
        if (categorizedBudgets.containsKey(budget.getCategory())) {
            // Update the budget for the category if it already exists in map
            this.categorizedBudgets.get(budget.getCategory()).setLimit(budget.getLimit());
        } else {
            // Set new budget for the category if it doesn't exist in map
            this.categorizedBudgets.put(budget.getCategory(), budget);
        }
    }

    public void addExpense(Expense expense) {
        // Check if expense category is already a key in map
        if (this.categorizedExpenses.containsKey(expense.getCategory())) {
            // Add expense to the category's list of expenses
            this.categorizedExpenses.get(expense.getCategory()).add(expense);
        } else {
            // Create a new list of expenses for category and add to map
            List<Expense> categoryExpenses = new ArrayList<>();
            categoryExpenses.add(expense);
            this.categorizedExpenses.put(expense.getCategory(), categoryExpenses);
        }

        // Update all budgets
        updateUserBudgets();
    }

    public void removeExpense(Expense expense) {
        // Remove the expense from its category list
        this.categorizedExpenses.get(expense.getCategory()).remove(expense);

        // Update the budget for the category after removing the expense
        updateUserBudgets();
    }

    public void updateUserBudgets() {
        // Update each budget with the most recent saved expenses
        for (Map.Entry<Category, Budget> categorizedBudget : this.categorizedBudgets.entrySet()) {
            categorizedBudget.getValue().addExpenses(getCategoryExpenses(categorizedBudget.getKey()));
        }
        
    }

    public void removeBudget(Category budgetCategory) {
        if (this.categorizedBudgets.containsKey(budgetCategory)) {
            this.categorizedBudgets.get(budgetCategory).setLimit(0);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information:\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Total Expenses: $").append(String.format("%.2f", getTotalExpenses())).append("\n");

        sb.append("\nBudgets:\n");
        if (this.categorizedBudgets.isEmpty()) {
            sb.append("No budgets set.\n");
        } else {
            for (Map.Entry<Category, Budget> entry : this.categorizedBudgets.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ")
                        .append(entry.getValue().getTotalSpent()).append("/")
                        .append(entry.getValue().getLimit()).append("\n");
            }
        }

        sb.append("\nExpenses:\n");
        if (getAllExpenses().isEmpty()) {
            sb.append("No expenses recorded.\n");
        } else {
            for (Expense expense : getAllExpenses()) {
                sb.append("  ").append(expense).append("\n");
            }
        }

        return sb.toString();
    }

}
