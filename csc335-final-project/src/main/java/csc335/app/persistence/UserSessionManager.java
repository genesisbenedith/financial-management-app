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
    
    SESSION; // Singleton instance

    private User currentUser = null; 

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        if (user == null) {
            currentUser = null;
            return;
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
