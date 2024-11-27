package csc335.app.models;

import java.io.File;
import java.time.LocalDate;

/**
 * Author(s): Genesis Benedith
 * File: User.java
 * Description: Model class that represents a user account
 */

import java.util.*;

public class User {
    private String username;
    private String email;
    private String password;
    private Map<Category, List<Expense>> categorizedExpenses; // Maps categories to a list of expenses
    private Map<Category, Budget> categorizedBudgets; // Maps categories to budgets
    private List<Report> monthlyReports; 

    /* ------------------------------ Constructor ------------------------------ */

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.categorizedExpenses = new HashMap<>();
        this.categorizedBudgets = new HashMap<>();
        this.monthlyReports = new ArrayList<>();
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

    public List<Expense> getAllExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (Map.Entry<Category, List<Expense>> expenseCategory : categorizedExpenses.entrySet()) {
            allExpenses.addAll(expenseCategory.getValue());
        }
        return Collections.unmodifiableList(allExpenses);
    }

    public List<Budget> getAllBudgets() {
        List<Budget> allBudgets = new ArrayList<>();
        for (Map.Entry<Category, Budget> budgetCategory : categorizedBudgets.entrySet()) {
            allBudgets.add(budgetCategory.getValue());
        }
        return Collections.unmodifiableList(allBudgets);
    }

    // public File getMonthlyReport(LocalDate reportDate) {
    //     for (Expense expense : monthlyReports) {
    //         if (report.getReportDate().equals(reportDate)) {
    //             report.update;
    //             return new File(report.getMonthlyReportPath());
    //         }
    //     }
    //     return null;
    // }

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

    public void setBudget(Category category, double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }

        if (categorizedBudgets.containsKey(category)) {
            // Update the budget for the category if it already exists in map
            this.categorizedBudgets.get(category).setLimit(limit);
        } else {
            // Create a new budget for the category if it doesn't exist in map
            this.categorizedBudgets.put(category, new Budget(category, limit));
        }
    }

    /*
     * ------------------------------ Other Methods ------------------------------
     */

    public void addExpense(Expense newExpense) {
        // Check if expense category is already a key in map
        if (this.categorizedExpenses.containsKey(newExpense.getCategory())) {
            // Add expense to the category's list of expenses
            this.categorizedExpenses.get(newExpense.getCategory()).add(newExpense);
        } else {
            // Create a new list of expenses for category and add to map
            List<Expense> categoryExpenses = new ArrayList<>();
            categoryExpenses.add(newExpense);
            this.categorizedExpenses.put(newExpense.getCategory(), categoryExpenses);
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