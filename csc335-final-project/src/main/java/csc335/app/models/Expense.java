package csc335.app.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import csc335.app.Category;

/**
 * Author(s): Genesis Benedith
 * File: Expense.java
 * Description: Model class that represents an expense for the user
 */

public class Expense {
    private Calendar date;        // Date in format "YYYY-MM-DD"
    private Category category;    // Expense category (e.g., Groceries, Entertainment)
    private double amount;      // Transaction amount
    private String description; // Brief description of the transaction

    /* ------------------------------ Constructor ------------------------------ */


    public Expense(Calendar date, Category category, double amount, String description) {
        // [ ]: Confirm if appropriate design for constructor
        setDate(date);
        setCategory(category);
        setAmount(amount);
        setDescription(description);
    }

    /* ------------------------------ Getters and Setters ------------------------------ */

    /**
     * 
     * @return
     */
    public Calendar getCalendarDate() {
        return date;
    }

    public String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
        String formattedDate = formatter.format(date.getTime());
        return formattedDate;
    }

    /**
     * 
     * @param date
     */
    public void setDate(Calendar date) {
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
        return getStringDate() + "," + category + "," + amount + "," + description;
    }
}

