package csc335.app.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;

// [ ] Complete file coment
/**
 * @author Genesis Benedith
 */

// [ ] Complete class coment
/**
 * 
 */
public class User implements Cloneable {
    private String username;
    private String email;
    private String hashedPassword;
    private final String salt;
    private final List<Budget> budgets;

    /* ------------------------------ Constructor ------------------------------ */

    public User(String username, String email, String hashedPassword, String salt, List<Budget> budgets) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.budgets = budgets;
    }

    /* ------------------------------ Getters ------------------------------ */

    protected String getUsername() {
        return username;
    }

    protected String getEmail() {
        return email;
    }

    protected String getHashedPassword() {
        return hashedPassword;
    }

    protected String getSalt() {
        return salt;
    }

    public List<Budget> getBudgets() {
        return Collections.unmodifiableList(budgets);
    }


    /* ------------------------------ Setters ------------------------------ */

    public void setEmail(String email) {

        this.email = email;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.hashedPassword = password;
    }

    /**
     * Set the budget limit for a given category
     * 
     * @param category the category group of the budget
     * @param limit the new spending limit for the category
     */
    public void setBudget(Category category, double limit) {
        findBudget(category).setLimit(limit);
    }

    public void setBudget(Budget budget) {
        findBudget(budget.getCategory()).setLimit(budget.getLimit());
        findBudget(budget.getCategory()).addExpenses(budget.getExpenses());
    }

    /**
     * Set the budget limits for each category
     * included in a list of budgets
     * 
     * @param budgets the list of 
     */
    public void setBudgets(List<Budget> budgets) {
        for (Budget budget : budgets) {
            Budget foundBudget = findBudget(budget.getCategory());
            foundBudget.setLimit(budget.getLimit());
            foundBudget.addExpenses(budget.getExpenses());
        }

            
    }

    /**
     * 
     */
    public void addExpense(Expense expense) {
        findBudget(expense.getCategory()).addExpense(expense);
    }

    private Budget findBudget(Category category) {
        for (Budget budget : budgets) {
            if (budget.getCategory().equals(category)) 
                return budget;
            }
        return null;
    }

    /*
     * ------------------------------ Helper Methods ------------------------------
     */


    public void removeExpense(Expense expense) {
        findBudget(expense.getCategory()).removeExpense(expense);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information:\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Email: ").append(email).append("\n");

        sb.append("\n-------------------- Expenses --------------------");
        for (Budget budget : budgets) {
            sb.append(budget.toStringDetailed());
            sb.append("\n\t\tTotal Expenses: $").append(String.format("%.2f", budget.getTotalSpent())).append("\n");
        }

        return sb.toString();
    }

    /*
     * ------------------------------ Helper Methods ------------------------------
     */

    /**
     * Gets all of the user's expenses (no filter)
     * 
     * @return list of expenses
     */
    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for (Budget budget : budgets) {
            expenses.addAll(budget.getExpenses());
        }
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Get's the user's expenses within a specific category
     * 
     * @param category
     * @return a list of expenses
     */
    public List<Expense> getExpenses(Category category) {
        return Collections.unmodifiableList(this.findExpenses(category));
    }

    public List<Expense> findExpenses(Category category) {
        List<Expense> expenses = new ArrayList<>();
        for (Expense expense : this.getExpenses()) {
            if (expense.getCategory().equals(category)) {
                expenses.add(expense);
            }
        }
        return expenses;
    }

    // [ ] Change the helper "get expense" methods to "filter expenses"

    /**
     * Get's the users expenses within a specific month and year
     * 
     * @author Genesis Benedith
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return a list of expenses
     */
    public List<Expense> getExpenses(int month, int year) {
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
     * Get's the users expenses within a specific month, year and category
     * 
     * @author Genesis Benedith
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> getExpenses(int month, int year, Category category) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : getExpenses(category)) {
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
    public List<Expense> getExpenses(int day, int month, int year) {
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
     * Get's the users expenses within a specific range
     * 
     * @author Lauren Schroeder and Gennesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> getExpensesInRange(Calendar start, Calendar end) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : getExpenses()) {
            if ((expense.getCalendarDate().after(start) || expense.getCalendarDate().equals(start))
                    && (expense.getCalendarDate().before(end) || expense.getCalendarDate().equals(end))) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    /**
     * Filters a list of expenses within a specific category
     * 
     * @author Genesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
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

    // [ ] REMOVE THIS LATER - WILL BE REPLACED BY FILTEREXPENSES() ABOVE
    /** 
     * Get's the users expenses within a specific range
     * 
     * @author Lauren Schroeder and Genesis Benedith
     * @param start    the calendar start date of the transaction
     * @param end      the calendar end date of the transaction
     * @param category the category of the transaction
     * @return a list of expenses
     */
    public List<Expense> getExpensesInRange(Calendar start, Calendar end, Category category) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : getExpenses(category)) {
            if ((expense.getCalendarDate().after(start) || expense.getCalendarDate().equals(start))
                    && (expense.getCalendarDate().before(end) || expense.getCalendarDate().equals(end))) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
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
        for (Expense expense : getExpenses(category)) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Get's the total amount spent within a specific month and year
     * 
     * @param month the month of the transaction
     * @param year  the year of the transaction
     * @return the total amount spent
     */
    public double calculateTotalExpenses(int month, int year) {
        double total = 0;
        for (Expense expense : getExpenses(month, year)) {
            total += expense.getAmount();
        }
        return total;
    }

    @Override
    protected Object clone() 
        throws CloneNotSupportedException 
    { 
        return super.clone(); 
    } 

}