package csc335.app.models;

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
    private List<Expense> expenses;
    private Map<Category, Budget> budgets; // Maps category names to budget limits
    private List<Report> financialReports;

    /* ------------------------------ Constructor ------------------------------ */

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.expenses = new ArrayList<>();
        this.budgets = new HashMap<>();
        this.financialReports = new ArrayList<>();
    }

    /*
     * ------------------------------ Getters and Setters
     * ------------------------------
     */

    /**
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * 
     * @param expense
     */
    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    /**
     * 
     * @param expense
     */
    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
    }

    /**
     * 
     * @return
     */
    public Map<Category, Budget> getBudgets() {
        return budgets;
    }

    /**
     * 
     * @param category
     * @param limit
     */
    public void setBudget(Category category, double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }

        if (budgets.containsKey(category)) {
            // Update the budget for the category if it already exists
            budgets.get(category).setLimit(limit);
        } else {
            // Create a new budget for the category if it doesn't exist
            budgets.put(category, new Budget(category, limit));
        }
    }

    /**
     * 
     * @param category
     * @param amount
     */
    public void addExpenseToBudget(Category category, double amount) {
        if (budgets.containsKey(category)) {
            budgets.get(category).addExpense(amount);
        }
    }

    /**
     * 
     * @param category
     */
    public void removeBudget(Category category) {
        this.budgets.remove(category);
    }

    /**
     * 
     * @return
     */
    public List<Report> getFinancialReports() {
        return financialReports;
    }

    /**
     * 
     * @param report
     */
    public void addFinancialReport(Report report) {
        this.financialReports.add(report);
    }

    /**
     * 
     * @return
     */
    public double getTotalExpenses() {
        double totalExpenses = 0;
        for (Expense expense : this.expenses) {
            totalExpenses += expense.getAmount();
        }
        return totalExpenses;
    }

    /**
     * 
     * @return
     */
    Map<Category, Double> getCategoryExpenses() {
        Map<Category, Double> categoryExpenses = new HashMap<>();
        for (Expense expense : this.expenses) {
            categoryExpenses.put(
                    expense.getCategory(),
                    categoryExpenses.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        return categoryExpenses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information:\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Total Expenses: $").append(String.format("%.2f", getTotalExpenses())).append("\n");

        sb.append("\nBudgets:\n");
        if (budgets.isEmpty()) {
            sb.append("No budgets set.\n");
        } else {
            for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ")
                        .append(entry.getValue().getSpent()).append("/")
                        .append(entry.getValue().getLimit()).append("\n");
            }
        }

        sb.append("\nExpenses:\n");
        if (expenses.isEmpty()) {
            sb.append("No expenses recorded.\n");
        } else {
            for (Expense expense : expenses) {
                sb.append("  ").append(expense).append("\n");
            }
        }

        sb.append("\nFinancial Reports:\n");
        if (financialReports.isEmpty()) {
            sb.append("No reports generated.\n");
        } else {
            for (Report report : financialReports) {
                sb.append("  ").append(report).append("\n");
            }
        }

        return sb.toString();
    }

}
