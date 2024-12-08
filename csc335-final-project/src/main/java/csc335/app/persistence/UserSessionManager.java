package csc335.app.persistence;

import csc335.app.models.User;

/**
 * Singleton class for managing the current user session in the application.
 * Provides functionality to set, retrieve, and reset the active user session.
 * Ensures there is always a valid user for session management.
 * 
 * File: UserSessionManager.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public enum UserSessionManager {
    
    SESSION; // A singleton instance of the app's session manager

    private User currentUser = null; // The current user logged into the session

    /**
     * Retrieves the current user of the session.
     * 
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user of the session.
     * 
     * @param user the user to set as the current session user
     * @throws IllegalArgumentException if the user is null
     */
    public void setCurrentUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("Current user cannot be set to null.");
        }
        currentUser = user;
    }
    
    /**
     * Checks if there is an active user in the session.
     * 
     * @return true if a user is active, false otherwise
     */
    public boolean hasActiveUser() {
        return !(currentUser == null);
    }

    /**
     * Resets the current user session to null, effectively logging the user out.
     */
    public void resetCurrentUser () {
        currentUser = null;
    }

}