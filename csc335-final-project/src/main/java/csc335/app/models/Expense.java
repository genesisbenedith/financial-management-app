package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * File: Expense.java
 * Description: Model class that represents an expense for the user
 */

public class Expense {
    private String date;        // Date in format "YYYY-MM-DD"
    private Category category;    // Expense category (e.g., Groceries, Entertainment)
    private double amount;      // Transaction amount
    private String description; // Brief description of the transaction

    /* ------------------------------ Constructor ------------------------------ */


    public Expense(String date, Category category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    /* ------------------------------ Getters and Setters ------------------------------ */

    /**
     * 
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

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
    public double getAmount() {
        return amount;
    }

    /**
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @ return
     */
    @Override
    public String toString() {
        return date + "," + category + "," + amount + "," + description;
    }
}

