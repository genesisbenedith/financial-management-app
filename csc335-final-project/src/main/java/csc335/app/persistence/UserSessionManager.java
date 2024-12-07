package csc335.app.persistence;
/**
 * @author Genesis Benedith
 */

import csc335.app.models.User;

// [ ] Needs class comment
/**
 * 
 */
public enum UserSessionManager {
    
    SESSION; // A singleton instance of the app's session manager

    private User currentUser = null; 

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("Current user cannot be set to null.");
        }
        currentUser = user;
    }
    
    protected boolean hasActiveUser() {
        return !(currentUser == null);
    }

    public void resetCurrentUser () {
        currentUser = null;
    }

}