package csc335.app.services;

// import java.lang.reflect.Constructor;

/**
 * Description: Manages and tracks the user's budget data and updates.
 * File: BudgetTracker.java
 * 
 * Authors: 
 * @author Chelina Obiang
 * @author Genesis Benedith
 */

import java.util.List;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;

/**
 * Singleton class for tracking and managing budgets for a user.
 */
public enum BudgetTracker  {
    
    TRACKER; // A singleton instance of a user's Expense Tracker

    private User currentUser; // The current user logged in

    /**
     * Constructor initializes the current user.
     * Throws an exception if no user is found.
     * 
     * @author Genesis Benedith
     */
    private BudgetTracker() {
        try {
            this.currentUser = UserSessionManager.SESSION.getCurrentUser();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access expense tracker for null user.");
        }
    }  

    /**
     * Retrieve all budgets for the current user
     * 
     * @author Genesis Benedith
     * @return a list the user's budgets
     */
    protected List<Budget> getBudgets() {
        return currentUser.getBudgets();
    }

    /**
     * Finds the budget for a specific category
     * 
     * @author Genesis Benedith
     * @param category
     * @return
     */
    public Budget findBudget(Category category) {
        for (Budget budget : getBudgets()) {
            if (budget.getCategory().equals(category)) 
                return budget;
            }
        return null;
    }

    /**
     * Updates the limit of a budget for a specific category
     * 
     * @author Chelina Obiang
     * @param category the category of the budget that is being updated
     * @param value the new limit for the budget
     */
    public void updateLimit(Category category, Double value) {
        findBudget(category).setLimit(value);
        System.out.println("The new value is now: " + Double.toString(value));
    }

    /**
     * Resets the budget for a given category
     * with pre-filled data
     * 
     * @author Genesis Benedith
     * @param category the category group of the budget
     * @param limit the new spending limit for the category
     */
   
     public void updateBudget(Budget budget) {
        findBudget(budget.getCategory()).setLimit(budget.getLimit());
        findBudget(budget.getCategory()).addExpenses(budget.getExpenses());
    }

    /**
     * Checks if the spending limit (budget) for a specific category 
     * has been exceeded
     * 
     * @author Chelina Obiang
     * @param category
     * @return true if limit exceeded, false if otherwise
     */
    public boolean isBudgetExceeded(Category category) {
        return findBudget(category).isExceeded();
    }

    /**
     * Calculate the spending progress for a given category
     * 
     * @author Chelina Obiang
     * @param category the category of the spending budget
     * @return the progress out of 100
     */
    public Double getBudgetProgress(Category category) {
        return findBudget(category).getPercentage() / 100;
    }

    /**
     * Gets the budget's spending limit for a specific category 
     * 
     * @author Chelina Obiang
     * @param category
     * @return
     */
    public Double getBudgetLimit(Category category) {
        return findBudget(category).getLimit();
    }

    /**
     * Retrieves the total amount spent for a specific category.
     * 
     * @param category the category of the budget
     * @return the total amount spent in the category
     */
    public Double getBudgetSpent(Category category) {
        return findBudget(category).getTotalSpent();
    }

    /**
     * Calculates the total budget limits for all categories.
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
