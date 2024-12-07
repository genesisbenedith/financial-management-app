
package csc335.app.persistence;

import java.io.File;
import java.io.IOException;

import csc335.app.controllers.View;
import csc335.app.models.User;
import javafx.scene.control.Alert.AlertType;

// [ ] Complete file coment
/**
 * @author Genesis Benedith
 */

// [ ] Complete class coment
/**a
 * 
 */
public enum AccountManager {

    ACCOUNT; // The single instance of the enum

    public void saveUserAccount() {
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        Database.DATABASE.saveUserFile(currentUser);
    }

    /**
     * Sets up the user account upon registration,
     * ensuring that the given username & email is unique
     * 
     * @param username the username
     * @param email
     * @param password
     */
    public void setNewUser(String username, String email, String password) {

        /* Show error alert and void if username or email is taken */
        if (isEmailTaken(email)) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Email is already taken!");
            return;
        } else if (isUsernameTaken(username)) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Username is already taken!");
            return;
        }

        /* Encrypt password */
        String salt = Hasher.generateSalt();
        String hashedPassword = Hasher.hashPassword(password, salt);

        /*
         * Add the new user account to the database,
         * then set each category's budgets & expenses to the default,
         * then create a new file for the user's transaction history
         */
        try {
            Database.DATABASE.createNewUserAccount(username, email, hashedPassword, salt);
            /* Show success alert and load login view */
            System.out.println("\nUser account successfully created!\n");
            View.ALERT.showAlert(AlertType.INFORMATION, "Success", "User account successfully created!");
            View.LOGIN.loadView();

        } catch (IOException e) {
            // Cancel registration if there's an error adding account to database
            System.err.println("\nAn error occured. Registration aborted.\nPlease try again.");
        }

    }

    /**
     * Authenticates a user based on the login credentials in account database,
     * determines whether user passes authentication and starts the call to
     * load the dahsboard view, otherwise it shows error alerts and voids
     * 
     * @param username
     * @param password
     */
    public void authenticateUser(String username, String password) {
        /* Find user and get their login credentials */
        User user = Database.DATABASE.findUserAccount(username, "Username");

        /* Show error alert and void if username does not exist to any account */
        if (user == null) {
            View.ALERT.showAlert(AlertType.ERROR, "Error", "Username does not exist!");
            return;
        }

        /* Show alert and void if authentication failed */
        if (!user.isPasswordCorrect(password))
            View.ALERT.showAlert(AlertType.ERROR, "Authentication Failed", "Invalid username or password.");

        /* Set active user session and load dashboard view */
        UserSessionManager.SESSION.setCurrentUser(user);
            System.out.println("User authenticated. Session active. Loading dashboard now.");
            View.DASHBOARD.loadView();
       
    }

    /**
     * Checks if a username is already registered to an account
     * 
     * @author Genesis Benedith
     * @param username the username we are looking for
     * @return true if username is already in use, or false if otherwise
     */
    private boolean isUsernameTaken(String username) {
        return Database.DATABASE.findUserAccount(username, "Username") != null;
    }

    /**
     * Checks if a email is already registered to an account
     * 
     * @author Genesis Benedith
     * @param email the username we are looking for
     * @return true if email is already in use, or false if otherwise
     */
    private boolean isEmailTaken(String email) {
        return Database.DATABASE.findUserAccount(email, "Email") != null;
    }

    

    public void changeAvatarView(String imagePath) throws Exception {
        String username = UserSessionManager.SESSION.getCurrentUser().getUsername();
        if (imagePath != null && !imagePath.isEmpty()){
            try {
                Database.DATABASE.saveUserAvatarImage(imagePath, username);
            } catch (Exception e) {
                throw new Exception("An error occurred while saving image.");
            }
        }
    }

    public File exportFile() throws IOException{
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        return  Database.DATABASE.writeExpenseExport(currentUser.getUsername());
    }

    public void importFile(String fileName) throws IOException{
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        Database.DATABASE.readExpenseImport(fileName);

    }

}
