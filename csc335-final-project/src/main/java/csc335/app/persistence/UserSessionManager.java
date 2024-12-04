package csc335.app.persistence;
/**
 * @author Genesis Benedith
 */

// [ ] Needs class comment
/**
 * 
 */
public enum UserSessionManager {
    
    INSTANCE; // Singleton instance

    private User currentUser = null; 

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) throws CloneNotSupportedException {
        if (user == null) {
            currentUser = null;
            return;
        }
        currentUser = (User)user.clone();
    }
    
    protected boolean hasActiveUser() {
        return !(currentUser == null);
    }

    public void resetCurrentUser () {
        currentUser = null;
    }

}
