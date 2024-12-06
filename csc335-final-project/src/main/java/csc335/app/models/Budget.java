package csc335.app.models;



/**
 * @author Genesis Benedith
 * Course: CSC 335 (Fall 2024)
 * File: Budget.java
 * Description: Model class that represents a budget for a user's expense category
 */

import java.util.Collections;
import java.util.List;

public class Budget {

    /*  */
    private final Category category; 
    private double limit; 
    private final List<Expense> expenses;

    /* ------------------------------ Constructor ------------------------------ */

    /**
     * 
     * 
     * @param category
     * @param limit
     * @param expenses
     */
    public Budget(Category category, double limit, List<Expense> expenses) {
        this.category = category;
        this.limit = limit;
        this.expenses = expenses;
    }

    
    /** 
     * Gets the budget's category 
     * 
     * @return the category assigned for all the expenses in this group
     */
    /* ------------------------------ Getters ------------------------------ */

    public Category getCategory() {
        return category;
    }

    
    /** 
     * Gets the spending limit for this category group
     * 
     * @return the max amount the user should spend 
     * for this category
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Gets a list of the expenses allocated towards this category group
     * 
     * @return a list of transactions
     */
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(this.expenses);
    }

    /* ------------------------------ Setters ------------------------------ */

    public void setLimit(double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }
        this.limit = limit;
    }

    /* ------------------------------ Helper Methods ------------------------------ */

    /**
     * Calculates the total amount spent in the category group
     * 
     * @return the total spent towards this budget
     */
    public double getTotalSpent() {
        double totalSpent = 0.0;
        for (Expense expense : this.expenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;
    }

    /**
     * Calculates the percentage of the amount spent towards the
     * category group 
     * 
     * @return
     */
    public double getPercentage() {
        return (this.getTotalSpent() / limit) * 10;
    }

    /**
     * Adds a new expense to this budget if it is within
     * this category group
     * 
     * @param newExpense
     */
    public void addExpense(Expense newExpense) {
        if (newExpense.getCategory().equals(this.category)) {
            this.expenses.add(newExpense);
        }
    }

    /**
     * Clears all expenses from the list and sets the list
     * to another list of expenses. Onlys adds the 
     * expenses in the new list that have the same category
     * as the budget
     *   
     * @param expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
        for (Expense expense : expenses) {
            this.addExpense(expense);
        }
        
    }

    public void addExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            this.addExpense(expense);
        }
        
    }

    /**
     * Removes an expense from the list of transactions that are 
     * assigned to this budget. 
     * 
     * NOTE: Should only be used when a user edits the category of an
     * expense, therefore specifying the need to remove the expense
     * from the budget's POV
     * @param expense the transcation to remove from this budget
     */
    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
    }
    
    /**
     * Checks whether or not the user reached their spending 
     * limit
     * 
     * @return true if max reached, false if otherwise
     */
    public boolean isExceeded() {
        return this.getTotalSpent() > this.limit;
    }
    
    /**
     * Formats the budget as Category,Limit
     * 
     * @return the budget details as comma-separated values
     */
    @Override
    public String toString() {
        return String.join(",", this.getCategory().toString(), Double.toString(this.getLimit()));
    }

    /**
     * 
     * @return
     */
    public String toStringDetailed() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.category.name());
        builder.append(" CATEGORY\n");
        builder.append("\tBudget: (Category,Limit)\n");
        builder.append("\t-> Budget: ");
        builder.append(this.toString());
        for (Expense expense : expenses) {
            builder.append("\n\t\t");
            builder.append(expense.toString());
        }
        return builder.toString();
    }
}