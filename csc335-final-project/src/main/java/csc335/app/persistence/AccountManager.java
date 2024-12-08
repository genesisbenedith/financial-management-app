package csc335.app.persistence;

import java.io.File;
import java.io.IOException;

import csc335.app.models.User;

/**
 * The `AccountManager` enum handles account-related operations, such as 
 * user registration, authentication, and account data management.
 * This is a singleton implementation using an enum, ensuring there is
 * only one instance of the `AccountManager` throughout the application.
 * 
 * Responsibilities:
 * - Validate and create new user accounts.
 * - Authenticate user login credentials.
 * - Save user data and export files related to user accounts.
 * - Check for the uniqueness of usernames and email addresses.
 * 
 * File: AccountManager.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 * @author Lauren Schroeder
 */
public enum AccountManager {

    /** The single instance of the `AccountManager` enum. */
    ACCOUNT;

    /**
     * Saves the current user's account data to the database.
     */
    public void saveUserAccount() {
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        Database.DATABASE.saveUserFile(currentUser);
    }

    /**
     * Registers a new user by creating an account with the provided
     * username, email, and password. Validates the uniqueness of the
     * username and email before proceeding.
     * 
     * @param username the username for the new account
     * @param email    the email for the new account
     * @param password the password for the new account
     * @return 0 if registration is successful, 1 if the email is taken,
     *         2 if the username is taken, or -1 if an error occurs
     */
    public int setNewUser(String username, String email, String password) {

        // Check if the email is already registered
        if (isEmailTaken(email)) {
            return 1;
        } 
        // Check if the username is already registered
        else if (isUsernameTaken(username)) {
            return 2;
        }

        // Encrypt the password with a generated salt
        String salt = Hasher.generateSalt();
        String hashedPassword = Hasher.hashPassword(password, salt);

        // Attempt to create the new user account in the database
        try {
            Database.DATABASE.createNewUserAccount(username, email, hashedPassword, salt);
            System.out.println("\nUser account successfully created!\n");
            return 0; // Registration successful
        } catch (IOException e) {
            System.err.println("\nAn error occurred. Registration aborted.\nPlease try again.");
            return -1; // Error occurred
        }
    }

    /**
     * Authenticates a user based on their login credentials. Validates the username
     * and password against the account database. If successful, starts the user 
     * session and loads the dashboard view.
     * 
     * @param username the username of the account to authenticate
     * @param password the password of the account to authenticate
     * @return 0 if authentication is successful, 1 if the username does not exist,
     *         or 2 if the password is incorrect
     */
    public int authenticateUser(String username, String password) {
        // Find the user account based on the username
        User user = Database.DATABASE.findUserAccount(username, "Username");

        // Check if the username is not registered
        if (user == null) {
            return 1; // Username does not exist
        }

        // Validate the password
        if (!user.isPasswordCorrect(password)) {
            return 2; // Incorrect password
        }

        // Set the current user session and load the dashboard view
        UserSessionManager.SESSION.setCurrentUser(user);
        System.out.println("User authenticated. Session active. Loading dashboard now.");
        return 0; // Authentication successful
    }

    /**
     * Checks if a username is already registered to an account.
     * 
     * @param username the username to check
     * @return true if the username is already in use, false otherwise
     */
    private boolean isUsernameTaken(String username) {
        return Database.DATABASE.findUserAccount(username, "Username") != null;
    }

    /**
     * Checks if an email is already registered to an account.
     * 
     * @param email the email to check
     * @return true if the email is already in use, false otherwise
     */
    private boolean isEmailTaken(String email) {
        return Database.DATABASE.findUserAccount(email, "Email") != null;
    }

    /**
     * Exports the current user's data as a file.
     * @author Lauren Schroeder
     * 
     * @return a File object representing the exported data
     * @throws IOException if an error occurs while writing the file
     */
    public File exportFile() throws IOException {
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        return Database.DATABASE.writeExpenseExport(currentUser.getUsername());
    }
}
