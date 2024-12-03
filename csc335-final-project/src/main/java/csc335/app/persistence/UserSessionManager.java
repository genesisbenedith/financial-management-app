package csc335.app.persistence;
/**
 * @author Genesis Benedith
 */

// [ ] Needs class comment
/**
 * 
 */
public final class UserSessionManager {
    
    private static UserSessionManager manager = null;  
    private static User currentUser; 
    
    /**
     * Private constructor prevents instantiation from other classes
     */
    private UserSessionManager () {
    }

    /**
     * 
     * @return
     */
    public static User getCurrentUser() {
        if (currentUser == null) {
            return null;
        }
        return new User(currentUser.getUsername(), currentUser.getEmail(), "", "");
    }

    public static UserSessionManager getUserSessionManager() {
        if (manager == null) {
            manager = new UserSessionManager();
        }
        return manager;
    }

    protected static void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        currentUser = user;
    }
    
    public static boolean hasActiveUser() {
        return !(currentUser == null);
    }

    /**
     * 
     */
    public void resetCurrentUser() {
        currentUser = null;
    }

}
