package csc335.app.models;

import java.util.*;

/**
 * Author(s): Genesis Benedith
 * File: Budget.java
 * Description: 
 */

public class Budget {
    private Category category; // Budget category (e.g., Groceries, Transportation, etc.)
    private double limit;    // Budget limit for the category
    private List<Expense> budgetedExpenses; // List of expenses associated with budget

    /* ------------------------------ Constructor ------------------------------ */

    public Budget(Category category, double limit) {
        this.category = category;
        this.limit = limit;
        this.budgetedExpenses = new ArrayList<>();
    }

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

    /* ------------------------------ Getters and Setters ------------------------------ */

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getTotalSpent() {
        double totalSpent = 0.0;
        for (Expense expense : budgetedExpenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;
    }

    @Override
    public String toString() {
        return category + ": " + getTotalSpent() + "/" + limit;
    }
}