package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: Budget.java
 * Description: Model class that represents a budget for a user's expense category
 */

import java.util.ArrayList;
import java.util.List;

import csc335.app.Category;

public class Budget {
    private final Category category; // Budget category (e.g., Groceries, Transportation, etc.)
    private double limit;    // Budget limit for the category
    private final List<Expense> budgetedExpenses; // List of expenses associated with budget

    /* ------------------------------ Constructor ------------------------------ */

    public Budget(Category category, double limit) {
        this.category = category;
        this.limit = limit;
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

    /* ------------------------------ Helper Methods ------------------------------ */

    public double getPercentage(){
        return (this.getTotalSpent() / this.getLimit()) * 100;
    }

    public void addExpense(Expense newExpense) {
        this.budgetedExpenses.add(newExpense);
    }

    public void addExpenses(List<Expense> expenses) {
        this.budgetedExpenses.clear();
        this.budgetedExpenses.addAll(expenses);
    }

    public void removeExpense(Expense expense) {
        this.budgetedExpenses.remove(expense);
    }
    
    public boolean isExceeded() {
        return getTotalSpent() > this.limit;
    }
    
    @Override
    public String toString() {
        return String.join(",", category.toString(), Double.toString(this.limit));
    }
}
