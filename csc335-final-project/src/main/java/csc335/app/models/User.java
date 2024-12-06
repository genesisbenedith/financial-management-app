package csc335.app.models;

// [ ] Complete file coment
/**
 * Author: Genesis Benedith
 * File: User.java
 * Description:
 */

import java.util.Collections;
import java.util.List;

import com.dlsc.gemsfx.AvatarView;

import csc335.app.persistence.Hasher;

// [ ] Complete class coment
/**
 * 
 */
public class User {
    private String username;
    private String email;
    private String hashedPassword;
    private final String salt;
    private List<Budget> budgets;
    private AvatarView avatar = null;

    /* ------------------------------ Constructor ------------------------------ */

    /**
     * 
     * @param username
     * @param email
     * @param hashedPassword
     * @param salt
     * @param budgets
     */
    public User(String username, String email, String hashedPassword, String salt, List<Budget> budgets) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.budgets = budgets;
        this.avatar = null;
    }

    /*
     * ------------------------------ Getter Methods ------------------------------
     */

    /**
     * Gets the username for the account
     * 
     * @return account username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the email associated with the account
     * 
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's budgets
     * 
     * @return a list of the user's budgets
     */
    public List<Budget> getBudgets() {
        return Collections.unmodifiableList(budgets);
    }

    /**
     * Get the user's avatar view
     * 
     * @return an avatar view for the user
     */
    public AvatarView getAvatar() {
        return avatar;
    }

    /*
     * ------------------------------ Setter Methods ------------------------------
     */

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
     * Set the user's current list of budgets
     * with another list of budgets, essentially
     * overwriting the category, limit, and expenses
     * for each budget stored on the user account
     * 
     * @param budgets the list of budgets
     */
    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    /**
     * 
     * @param avatar
     */
    public void setAvatar(AvatarView avatar) {
        this.avatar = avatar;
    }

    /*
     * ------------------------------ Helper Methods ------------------------------
     */

    /**
     * Validates the user's credentials and
     * checks if a given password is correct
     *  
     * @param password the password used in attempt to authenticate user 
     * @return true if the password is correct, false if otherwise
     */
    public boolean isPasswordCorrect(String password) {
        return Hasher.matches(password, this.salt, this.hashedPassword);
    }

    /**
     * Gets the user's details in a structured format.
     * Includes the username, email, budgets, and expenses
     * 
     * @return the user's formatted details
     */
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