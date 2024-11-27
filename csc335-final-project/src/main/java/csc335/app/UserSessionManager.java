package csc335.app;

import csc335.app.models.User;

public class UserSessionManager {
    private static User currentUser;

    /**
     * 
     * @return
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 
     * @param user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void resetCurrentUser() {
        currentUser = null;
    }
}