package csc335.app.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import csc335.app.models.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;

public enum ExpenseTracker {
    
    TRACKER;

    private static final User currentUser = UserSessionManager.SESSION.getCurrentUser();
    private static final List<Budget> userBudgets = currentUser.getBudgets();
    
    /**
     * Gets all of the user's expenses (no filter)
     * 
     * @return list of expenses
     */
    public static List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for (Budget budget : userBudgets) {
            expenses.addAll(budget.getExpenses());
        }
        return Collections.unmodifiableList(expenses);
    }

    /* ------------------------------ CALCULATOR (HELPER) METHODS ------------------------------ */
    
    /**
     * Get's the total amount spent within a specific month and year
     * 
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return the total amount spent
     */
    public static double calculateTotalExpenses(int month, int year) {
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
    public static Double calculateTotalExpenses(List<Expense> expenses) {
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
    public static double calculateTotalExpenses() {
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

    /* ------------------------------ FILTER (HELPER) METHODS ------------------------------ */

    /**
     * Filters the user's expenses within a specific category
     * 
     * @param category
     * @return a list of expenses
     */
    public static List<Expense> filterExpenses(Category category) {
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
    public static List<Expense> filterExpenses(int month, int year) {
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
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public static List<Expense> filterExpenses(int month, int year, Category category) {
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
    public static List<Expense> filterExpenses(int day, int month, int year) {
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
     * Filters a list of expenses within a specific category
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public static List<Expense> filterExpenses(List<Expense> expenses, Category category) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getCategory() == category) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    /** 
     * Get's the users expenses within a specific range
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public static List<Expense> filterExpensesInRange(Calendar start, Calendar end, Category category) {
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
    public static List<Expense> filterExpensesInRange(Calendar start, Calendar end) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            if ((expense.getCalendarDate().after(start) || expense.getCalendarDate().equals(start))&& (expense.getCalendarDate().before(end) || expense.getCalendarDate().equals(end))) {
                    filteredExpenses.add(expense);
                }
        }
        return filteredExpenses;
    }


}
