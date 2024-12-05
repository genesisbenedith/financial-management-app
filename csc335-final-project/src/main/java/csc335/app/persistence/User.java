package csc335.app.persistence;

import java.util.Collections;
import java.util.List;
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
public class User {
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
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

    public Budget findBudget(Category category) {
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

        sb.append("\n-------------------- Expenses --------------------\n");
        for (Budget budget : budgets) {
            sb.append(budget.toStringDetailed());
            sb.append("\n\t\tTotal Expenses: $").append(String.format("%.2f", budget.getTotalSpent())).append("\n");
        }

        return sb.toString();
    }

   
  

}