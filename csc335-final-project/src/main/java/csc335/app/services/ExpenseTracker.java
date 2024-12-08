package csc335.app.services;

/**
 * Authors: Lauren Schroeder and Genesis Benedith
 * File: ExpenseTracker.java
 * Course: CSC 335 (Fall 2024)
 * Description: This file defines the ExpenseTracker singleton class, which 
 *              provides methods to manage and analyze user expenses. It 
 *              includes functionality for retrieving, filtering, sorting, 
 *              and calculating expenses across various criteria.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.UserSessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

/**
 * Singleton class to track and manage user expenses.
 * Provides methods for filtering, sorting, and calculating expenses based on
 * various criteria such as date, category, and range.
 */

public enum ExpenseTracker {

    TRACKER; // A singleton instance of a user's Expense Tracker

    private User currentUser; // Current user logged into the tracker

    /* ------------------------------ CONSTRUCTOR ------------------------------ */

    /**
     * Initializes the ExpenseTracker by loading the current user from the session.
     * 
     * @throws IllegalStateException if the current user cannot be accessed
     */
    private ExpenseTracker() {
        try {
            this.currentUser = UserSessionManager.SESSION.getCurrentUser();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access expense tracker for null user.");
        }
    }

    /*
     * ------------------------------ GETTER METHODS ------------------------------
     */

    /**
     * Retrieves all expenses for the current user.
     * 
     * @return an unmodifiable list of all expenses
     */
    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for (Budget budget : BudgetTracker.TRACKER.getBudgets()) {
            expenses.addAll(budget.getExpenses());
        }
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Adds a single expense to the appropriate budget.
     * 
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        BudgetTracker.TRACKER.findBudget(expense.getCategory()).addExpense(expense);
        AccountManager.ACCOUNT.saveUserAccount();
    }

    /**
     * Adds a list of expenses to their respective budgets.
     * 
     * @param expenses the list of expenses to add
     */
    public void addExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            addExpense(expense);
        }
    }

    /**
     * Removes an expense from the appropriate budget.
     * 
     * @param expense the expense to remove
     */
    public void removeExpense(Expense expense) {
        BudgetTracker.TRACKER.findBudget(expense.getCategory()).removeExpense(expense);
        AccountManager.ACCOUNT.saveUserAccount();
    }

    /*
     * ------------------------------ HELPER METHODS (CALCULATOR)  ------------------------------ */

    /**
     * Get's the total amount spent within a specific month and year
     * 
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return the total amount spent
     */
    public double calculateTotalExpenses(int month, int year) {
        double total = 0;
        for (Expense expense : filterExpenses(month, year)) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * 
     * 
     * @author Genesis Benedith
     * @param expenses list of transactions
     * @return total amount spent
     */
    public Double calculateTotalExpenses(List<Expense> expenses) {
        Double totalSpent = 0.0;
        for (Expense expense : expenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;

    }

    /**
     * Get's the total amount spent (no filter)
     * 
     * @param category
     * @return the total amount spent
     */
    public double calculateTotalExpenses() {
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
    public double calculateTotalExpenses(Category category) {
        double total = 0;
        for (Expense expense : filterExpenses(category)) {
            total += expense.getAmount();
        }
        return total;
    }

    public List<Expense> sortExpenses() {
        ObservableList<Expense> expenses = FXCollections.observableArrayList(ExpenseTracker.TRACKER.getExpenses());
        SortedList<Expense> sortedExpenses = new SortedList<>(expenses);
        sortedExpenses.setComparator(Comparator.comparing(Expense::getCalendarDate).reversed());
        return sortedExpenses;
    }

/* ------------------------------ HELPER METHODS (FILTER) ------------------------------ */
    /**
     * Filters the user's expenses within a specific category
     * 
     * @param category
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(Category category) {
        List<Expense> expenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            if (expense.getCategory().equals(category)) {
                expenses.add(expense);
            }
        }
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Filters the users expenses within a specific month and year
     * 
     * @author Genesis Benedith
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(int month, int year) {
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
     * Filters the users expenses within a specific month, year and category
     * 
     * @author Genesis Benedith
     * @param month    the month of the transaction
     * @param year     the year of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(int month, int year, Category category) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : filterExpenses(category)) {
            // Check the date of the expense
            if (expense.getCalendarDate().get(Calendar.MONTH) == month
                    && expense.getCalendarDate().get(Calendar.YEAR) == year) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    /**
     * Get's the users expenses within a specific day
     * 
     * @author Lauren Schroeder
     * @param day   the day of the transaction
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(int day, int month, int year) {
        List<Expense> dayExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            // Check the date of the expense
            if (expense.getCalendarDate().get(Calendar.MONTH) == month
                    && expense.getCalendarDate().get(Calendar.YEAR) == year
                    && expense.getCalendarDate().get(Calendar.DAY_OF_MONTH) == day) {
                dayExpenses.add(expense);
            }
        }
        return dayExpenses;
    }

    /**
     * Filters a list of expenses within a specific calendar month/year
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param category the calendar month
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(Calendar monthCal) {
        List<Expense> monthExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            if ((expense.getCalendarDate().get(Calendar.MONTH) == monthCal.get(Calendar.MONTH))
                    && (expense.getCalendarDate().get(Calendar.YEAR) == monthCal.get(Calendar.YEAR))) {
                monthExpenses.add(expense);
            }
        }
        return monthExpenses;
    }

    /**
     * Filters a list of expenses within a specific category
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param expenses    the list of expenses to filter through
     * @param category the category of the expenses we are looking for
     * @return a list of expenses
     */
    public List<Expense> filterExpenses(List<Expense> expenses, Category category) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getCategory() == category) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    /**
     * Get's the users expenses within a specific range and category
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> filterExpensesInRange(Calendar start, Calendar end, Category category) {
        return filterExpenses(filterExpensesInRange(start, end), category);
    }

    /**
     * Get's the users expenses within a specific range
     * 
     * @author Lauren Schroeder and Gennesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> filterExpensesInRange(Calendar start, Calendar end) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            if ((expense.getCalendarDate().after(start) || expense.getCalendarDate().equals(start))
                    && (expense.getCalendarDate().before(end) || expense.getCalendarDate().equals(end))) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

}