package csc335.app.services;

import java.util.Calendar;
import java.util.List;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.UserSessionManager;
import csc335.app.utils.CalendarConverter;

/**
 * Singleton class for tracking and managing budgets for a user.
 * This class provides functionality to retrieve budgets, update spending limits, 
 * calculate budget progress, and verify if spending limits are exceeded.
 * 
 * File: BudgetTracker.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public enum BudgetTracker  {
    
    /** A singleton instance of a user's Expense Tracker */
    TRACKER;

    /** The current user logged in */
    private User currentUser;

    /**
     * Initializes the BudgetTracker by loading the current user from the session.
     * 
     * @throws IllegalStateException if the current user cannot be accessed
     */
    private BudgetTracker() {
        try {
            this.currentUser = UserSessionManager.SESSION.getCurrentUser();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access expense tracker for null user.");
        }
    }  

    /**
     * Retrieves all budgets for the current user.
     * 
     * @return a list of the user's budgets
     */
    protected List<Budget> getBudgets() {
        return currentUser.getBudgets();
    }

    /**
     * Finds the budget for a specific category.
     * 
     * @param category the category to find the budget for
     * @return the budget for the specified category, or null if not found
     */
    public Budget findBudget(Category category) {
        for (Budget budget : getBudgets()) {
            if (budget.getCategory().equals(category)) 
                return budget;
            }
        return null;
    }

    /**
     * Updates the spending limit for a specific category's budget.
     * 
     * @param category the category of the budget to update
     * @param value the new spending limit
     */
    public void updateLimit(Category category, Double value) {
        findBudget(category).setLimit(value);
        System.out.println("You changed the limit for " + category.name() + "! The new value is now: " + Double.toString(value) + ".");
        AccountManager.ACCOUNT.saveUserAccount();
    }

    /**
     * Updates the budget for a specific category with a new limit and expenses.
     * 
     * @param budget the updated budget to apply
     */
    public void updateBudget(Budget budget) {
        Budget oldBudget = findBudget(budget.getCategory());
        oldBudget.setLimit(budget.getLimit());
        oldBudget.addExpenses(budget.getExpenses());
        AccountManager.ACCOUNT.saveUserAccount();
    }

    /**
     * Checks if the spending limit for a specific category has been exceeded.
     * 
     * @param category the category to check
     * @return true if the budget limit is exceeded, false otherwise
     */
    public boolean isBudgetExceeded(Category category) {
        return isBudgetExceeded(category, CalendarConverter.INSTANCE.getCalendar());
    }

    /**
     * Checks if the spending limit for a specific category has been exceeded for a specific date.
     * 
     * @param category the category to check
     * @param calendar the calendar date to filter expenses
     * @return true if the budget limit is exceeded, false otherwise
     */
    public boolean isBudgetExceeded(Category category, Calendar calendar) {
        return findBudget(category).isExceeded(calendar);
    }

    /**
     * Calculates the progress of spending for a specific category.
     * 
     * @param category the category to calculate progress for
     * @return the percentage of the budget spent (out of 100)
     */
    public Double getBudgetProgress(Category category) {
        return getBudgetProgress(CalendarConverter.INSTANCE.getCalendar(), category);
    }

    public Double getBudgetProgress(Calendar calendar, Category category) {
        return findBudget(category).getPercentage(calendar) / 100;
    }

    /**
     * Retrieves the spending limit for a specific category's budget.
     * 
     * @param category the category to retrieve the limit for
     * @return the spending limit for the specified category
     */
    public Double getBudgetLimit(Category category) {
        return findBudget(category).getLimit();
    }

    /**
     * Calculates the total amount spent for a specific category.
     * 
     * @param category the category to calculate spending for
     * @return the total amount spent in the category
     */
    public Double getBudgetSpent(Category category) {
        return findBudget(category).getTotalSpent();
    }

    /**
     * Calculates the total spending limits for all budgets across all categories.
     * 
     * @return the total spending limit across all budgets
     */
    public Double getTotalBudgetLimits() {
        Double totalLimits = 0.0;
        for (Budget bud : getBudgets()){
            totalLimits += bud.getLimit();
        }
        return totalLimits;
    }
}
