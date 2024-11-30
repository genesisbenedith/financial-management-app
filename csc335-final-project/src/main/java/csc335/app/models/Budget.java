package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: Budget.java
 * Description: Model class that represents a budget for a user's expense category
 */

import java.util.*;

public class Budget {
    private Category category; // Budget category (e.g., Groceries, Transportation, etc.)
    private double limit;    // Budget limit for the category
    private List<Expense> budgetedExpenses; // List of expenses associated with budget

    /* ------------------------------ Constructor ------------------------------ */

    public Budget(Category category, double limit) {
        // TODO: Confirm if appropriate design for constructor
        setCategory(category);
        setLimit(limit);
        this.budgetedExpenses = new ArrayList<>();
    }

    /* ------------------------------ Getters ------------------------------ */

    public Category getCategory() {
        return category;
    }

    public double getLimit() {
        return limit;
    }

    public double getTotalSpent() {
        double totalSpent = 0.0;
        for (Expense expense : budgetedExpenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;
    }

    /* ------------------------------ Setters ------------------------------ */

    public void setLimit(double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }

        this.limit = limit;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /* ------------------------------ Other Methods ------------------------------ */

    public void addExpense(Expense newExpense) {
        budgetedExpenses.add(newExpense);
    }

    public void addExpenses(List<Expense> expenses) {
        budgetedExpenses.clear();
        budgetedExpenses.addAll(expenses);
    }

    public void removeExpense(Expense expense) {
        budgetedExpenses.remove(expense);
    }
    
    public boolean isExceeded() {
        return getTotalSpent() > limit;
    }
    
    @Override
    public String toString() {
        return category + ": " + getTotalSpent() + "/" + limit;
    }
}
