package csc335.app.models;

// ----------------------------------------------------------------------------
// File: Expense.java
// Author: Genesis Benedith
// Course: CSC 335 (Fall 2024)
// Description: This file defines the Expense class, representing a financial 
//              transaction made by a user. The class includes details such as 
//              the transaction date, category, amount, and description. 
//              It provides getter and setter methods to access and modify these 
//              details and includes helper methods for formatted output.
// ----------------------------------------------------------------------------

import java.util.Calendar;

/**
 * Represents a transaction made by a user. 
 * Each transaction is categorized and includes a date, amount, and description.
 * 
 * @author Genesis Benedith
 */
public class Expense {

    // ------------------------------ Instance Variables ------------------------------
    
    /** The date of the transaction */
    private Calendar date;       
    /** The category the transaction belongs to */
    private Category category;    
    /** The amount of the transaction */
    private double amount;   
    /** A brief summary of the transaction */
    private String description; 
    
    // ------------------------------ Constructor ------------------------------
    
    /**
     * Initializes an Expense object with the specified details.
     * 
     * @param date        the date of the transaction
     * @param category    the category type of the transaction  
     * @param amount      the amount of the transaction
     * @param description a brief summary of the transaction
     */
    public Expense(Calendar date, Category category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }
    
    // ------------------------------ Getter Methods ------------------------------
    
    /**
     * Gets the date of the transaction.
     * 
     * @return the date of the transaction
     */
    public Calendar getCalendarDate() {
        return date;
    }
    
    /**
     * Gets the category of the transaction.
     * 
     * @return the category the transaction belongs to
     */
    public Category getCategory() {
        return category;
    }
    
    /**
     * Gets the amount of the transaction.
     * 
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Gets the description of the transaction.
     * 
     * @return the transaction description
     */
    public String getDescription() {
        return description;
    }
    
    // ------------------------------ Setter Methods ------------------------------
    
    /**
     * Updates the date of the transaction.
     * 
     * @param date the new date 
     */
    public void setDate(Calendar date) {
        this.date = date;
    }
    
    /**
     * Updates the category of the transaction.
     * 
     * @param category the new category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Updates the amount of the transaction.
     * 
     * @param amount the new amount 
     * @throws IllegalArgumentException if the new amount is less than 0
     */
    public void setAmount(double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative.");
        }
        this.amount = amount;
    }

    /**
     * Updates the description of the transaction.
     * 
     * @param description the new description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // ------------------------------ Helper Methods ------------------------------

    /**
     * Converts the transaction date to "YYYY-MM-DD" format.
     * 
     * @return a formatted version of the transaction date
     */
    public String getStringDate() {
        Calendar calendar = this.getCalendarDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-indexed
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    /**
     * Formats the transaction details as "YYYY-MM-DD,Category,Amount,Description".
     * 
     * @return the transaction details as a comma-separated string
     */
    @Override
    public String toString() {
        return String.join(",", 
            getStringDate(), 
            this.getCategory().toString(), 
            Double.toString(this.amount), 
            this.description
        );
    }
}
