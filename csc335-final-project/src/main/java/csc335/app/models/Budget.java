package csc335.app.models;


import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;

// ----------------------------------------------------------------------------
// File: Budget.java
// Author: Genesis Benedith
// Description: This file defines the Budget class, representing a financial 
//              budget assigned to a specific category. Each budget includes 
//              a spending limit, a list of expenses, and methods for tracking 
//              and managing financial data related to that category.
// ----------------------------------------------------------------------------

/**
 * Represents a financial budget assigned to a specific category.
 * Each budget has a spending limit, tracks associated expenses, and provides 
 * methods for calculating totals, percentages, and determining if the limit 
 * has been exceeded.
 * 
 * The class also includes functionality for managing the list of expenses 
 * within the budget.
 * 
 * @author Genesis Benedith
 */
public class Budget {

     // ------------------------------ Instance Variables ------------------------------
    
    /** The category this budget is assigned to */
    private final Category category; 
    /** The spending limit for this budget */
    private double limit; 
    /** The list of expenses allocated to this budget */
    private final List<Expense> expenses;

    // ------------------------------ Constructor ------------------------------

    /**
     * Constructs a Budget object with the specified category, spending limit, and list of expenses.
     * 
     * @param category the category for this budget
     * @param limit    the spending limit for the budget
     * @param expenses the list of expenses assigned to this budget
     */
    public Budget(Category category, double limit, List<Expense> expenses) {
        this.category = category;
        this.limit = limit;
        this.expenses = expenses;
    }

    // ------------------------------ Getters ------------------------------

    /**
     * Gets the category for this budget.
     * 
     * @return the category assigned to the budget
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Gets the spending limit for this budget.
     * 
     * @return the maximum allowable spending for the budget
     */
    public double getLimit() {
        return limit;
    }

   /**
     * Gets the list of expenses assigned to this budget.
     * 
     * @return an unmodifiable list of transactions for the budget
     */
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(this.expenses);
    }

    // ------------------------------ Setters ------------------------------

    /**
     * Updates the spending limit for this budget.
     * 
     * @param limit the new spending limit
     * @throws IllegalArgumentException if the limit is less than 0
     */
    public void setLimit(double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }
        this.limit = limit;
    }

    /* ------------------------------ Helper Methods ------------------------------ */

   /**
     * Calculates the total amount spent within the budget
     * for the current month.
     * 
     * @return the total spending
     */
    public double getTotalSpent() {
        return getTotalSpent(CalendarConverter.INSTANCE.getCalendar());
    }

    /**
     * Calculates the total amount spent within the budget
     * for a specific month.
     * 
     * @param the calendr month for the spending limit
     * @return the total spending
     */
    public double getTotalSpent(Calendar calendar) {
        double totalSpent = 0.0;
        for (Expense expense : this.expenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;
    }

    /**
     * Calculates the percentage of the budget used based on the spending limit.
     * 
     * @return the percentage of the budget spent
     */
    public double getPercentage(){
        return getPercentage(CalendarConverter.INSTANCE.getCalendar());
    }

    /**
     * Calculates the percentage of the budget used for a specific month, based on the provided calendar date.
     * 
     * @param calendar the calendar date to filter expenses by month
     * @return the percentage of the budget spent for the specified month
     */
    public double getPercentage(Calendar calendar){
        double percentage = (this.getTotalSpent(calendar) / this.getLimit()) * 100;
        return Math.round(percentage * 10.0) / 100.0;
    }

    /**
     * Adds a new expense to the budget if it matches the budget's category.
     * 
     * @param newExpense the expense to add
     */
    public void addExpense(Expense newExpense) {
        if (newExpense.getCategory().equals(this.category)) {
            this.expenses.add(newExpense);
        }
    }

    /**
     * Adds a list of expenses to the budget.
     * Each expense is checked to ensure it matches the category of the budget before being added.
     * 
     * @param expenses the list of expenses to add
     */
    public void addExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            this.addExpense(expense);
        }
    }

    /**
     * Sets the list of expenses for this budget.
     * Replaces the current list with a new list, including only those matching the budget's category.
     * 
     * @param expenses the new list of expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
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
     * Checks if the spending for the current month has exceeded the budget limit.
     * 
     * @param calendar the current date to filter expenses by month
     * @return true if the limit is exceeded, false otherwise
     */
    public boolean isExceeded(Calendar calendar) {
        List<Expense> expensesInCurrentMonth = ExpenseTracker.TRACKER.filterExpenses(calendar);
        Double totalSpentInCurrentMonth = ExpenseTracker.TRACKER.calculateTotalExpenses(expensesInCurrentMonth);
        return totalSpentInCurrentMonth > this.limit;
    }
    
    /**
     * Formats the budget details as "Category,Limit".
     * 
     * @return the budget as a comma-separated string
     */
    @Override
    public String toString() {
        return String.join(",", this.getCategory().toString(), Double.toString(this.getLimit()));
    }

    /**
     * Formats the budget with detailed information, including category, limit, and expenses.
     * 
     * @return the detailed budget as a formatted string
     */
    public String toStringDetailed() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.category.name());
        builder.append(" CATEGORY\n");
        builder.append("\tBudget: (Category,Limit)\n");
        builder.append("\t-> Budget: ");
        builder.append(this.toString());
        for (Expense expense : expenses) {
            builder.append("\n\t\tExpense: ");
            builder.append(expense.toString());
        }
        return builder.toString();
    }
}
