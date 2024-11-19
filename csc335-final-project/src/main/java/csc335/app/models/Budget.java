package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * File: Budget.java
 * Description: 
 */

public class Budget {
    private Category category; // Budget category (e.g., Groceries, Transportation)
    private double limit;    // Budget limit for the category
    private double spent;    // Amount spent so far in the category

    /* ------------------------------ Constructor ------------------------------ */

    /**
     * 
     * @param category
     * @param limit
     */
    public Budget(Category category, double limit) {
        this.category = category;
        this.limit = limit;
        this.spent = 0.0;
    }

    /**
     * 
     * @param amount
     */
    public void addExpense(double amount) {
        this.spent += amount;
    }

    /**
     * 
     * @return
     */
    public boolean isExceeded() {
        return spent > limit;
    }

    /* ------------------------------ Getters and Setters ------------------------------ */

    /**
     * 
     * @return
     */
    public Category getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * 
     * @return
     */
    public double getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     */
    public void setLimit(double limit) {
        this.limit = limit;
    }

    /**
     * 
     * @return
     */
    public double getSpent() {
        return spent;
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return category + ": " + spent + "/" + limit;
    }
}
