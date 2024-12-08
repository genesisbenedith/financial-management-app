package csc335.app.models;

// ----------------------------------------------------------------------------
// File: User.java
// Author: Genesis Benedith
// Course: CSC 335 (Fall 2024)
// Description: This file defines the User class, representing a user account 
//              in the personal financial assistant app. It includes user 
//              information such as username, email, hashed password, salt, and 
//              associated budgets. The class provides methods for managing 
//              user data and validating credentials.
// ----------------------------------------------------------------------------

import java.util.Collections;
import java.util.List;

import csc335.app.persistence.Hasher;

/**
 * Represents a user in the personal financial assistant app.
 * Each user has a username, email, hashed password, salt, and a list of budgets 
 * for tracking expenses and financial goals.
 * 
 * Provides methods for managing user details, updating budgets, 
 * and validating user credentials.
 * 
 * @author Genesis Benedith
 */
public class User {

    // ------------------------------ Instance Variables ------------------------------
    
    //The username of the user account
    private String username;
    //The email address associated with the account
    private String email;
    // The hashed password for secure authentication
    private String hashedPassword;
    // The salt used for hashing the password
    private final String salt;
    // The list of budgets associated with the user 
    private List<Budget> budgets;

    // ------------------------------ Constructor ------------------------------

    /**
     * Constructs a User object with specified details.
     * 
     * @param username      the username for the account
     * @param email         the email address associated with the account
     * @param hashedPassword the hashed password for the account
     * @param salt          the salt used for password hashing
     * @param budgets       the list of budgets associated with the user
     */
    public User(String username, String email, String hashedPassword, String salt, List<Budget> budgets) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.budgets = budgets;
    }

    // ------------------------------ Getter Methods ------------------------------

    /**
     * Gets the username of the account.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the email address associated with the account.
     * 
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets an unmodifiable view of the user's budgets.
     * 
     * @return a list of budgets associated with the user
     */
    public List<Budget> getBudgets() {
        return Collections.unmodifiableList(budgets);
    }

    // ------------------------------ Setter Methods ------------------------------

    /**
     * Updates the user's email address.
     * 
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Updates the user's username.
     * 
     * @param username the new username
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    /**
     * Updates the user's hashed password.
     * 
     * @param password the new password (hashed)
     * @throws IllegalArgumentException if the password is null or empty
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.hashedPassword = password;
    }

    /**
     * Updates the user's list of budgets.
     * 
     * @param budgets the new list of budgets
     */
    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    // ------------------------------ Helper Methods ------------------------------

    /**
     * Validates the user's password by comparing it with the stored hashed password.
     * 
     * @param password the password provided for authentication
     * @return true if the password matches, false otherwise
     */
    public boolean isPasswordCorrect(String password) {
        return Hasher.matches(password, this.salt, this.hashedPassword);
    }

    /**
     * Gets the user's details, including their budgets and expenses, formatted as a string.
     * 
     * @return the user's details in a readable format
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------- User Budgets --------------------\n");
        for (Budget budget : budgets) {
            sb.append(budget.toStringDetailed());
            sb.append("\n\t\tTotal Expenses: $").append(String.format("%.2f", budget.getTotalSpent())).append("\n");
        }

        return sb.toString();
    }
}
