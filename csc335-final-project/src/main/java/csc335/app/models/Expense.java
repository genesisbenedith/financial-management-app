package csc335.app.models;

// [ ] Finish this file comment
/**
 * @author Genesis Benedith
 * File: Expense.java
 * Description: 
 */


import java.util.Calendar;


/**
 * This class represents a transaction made by a user
 */
public class Expense {

    /* Instance Variables */
    private Calendar date;       
    private Category category;    
        private double amount;   
        private String description; 
    
        // ------------------------------ Constructor ------------------------------
    
        // [ ] Finish this constructor comment 
        /**
         * 
         * 
         * @param date the date of the transaction
         * @param category the category type of the transaction  
         * @param amount the amount of the transaction
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
         * Gets the date of the transaction
         * 
         * @return the date of the transaction
         */
        public Calendar getCalendarDate() {
            return date;
        }
    
        /**
         * Gets the category of the transaction
         * 
         * @return the category the transaction belongs to
         */
        public Category getCategory() {
            return category;
        }
    
        /**
         * Gets the amount of the transaction
         * 
         * @return the transaction amount
         */
        public double getAmount() {
            return amount;
        }
    
        /**
         * Gets the description of the transaction
         * 
         * @return the transacation description
         */
        public String getDescription() {
            return description;
        }
    
        // ------------------------------ Setter Methods ------------------------------
    
        /**
         * Updates the date of the transaction
         * 
         * @param date the new date 
         */
        public void setDate(Calendar date) {
            this.date = date;
        }
    
        public void setCategory(Category category){
            this.category = category;
    }

    /**
     * Updates the amount of the transacation
     * 
     * @param amount the new amount 
     * @throws IllegalArgumentException if the new amount is less than 0
     */
    public void setAmount(double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Transacation amount cannot be negative.");
        }
        this.amount = amount;
    }

    /**
     * Updates the description of the tramsaction
     * 
     * @param description the new description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // ------------------------------ Helper Methods ------------------------------

    /**
     * Converts the transaction date to "YYYY-MM-DD" format
     * 
     * @return a formatted version of the transaction date
     */
    public String getStringDate() {
        Calendar calendar = this.getCalendarDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Calendar months are 0-indexed
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    /**
     * Formats the transaction as YYYY-MM-DD,Category,Amount,Description
     * 
     * @return the transaction as comma-separated values
     */
    @Override
    public String toString() {
        return String.join(",", getStringDate(), this.getCategory().toString(), Double.toString(this.amount), this.description);
    }
}