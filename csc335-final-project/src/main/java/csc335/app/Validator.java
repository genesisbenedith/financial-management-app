package csc335.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import csc335.app.models.User;

public class Validator {
    private void validateEmail(String email) {
        // Throwing exception if 
        if (email == null | email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        // Building regex pattern for expected email format
        String regex = "^[A-Za-z0-9]+([-_]?[A-Za-z]+)*@([A-Za-z]\\.){1,2}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);

        // Check if 
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * [ ]: IMPLEMENT HASHING
     * 
     * @param username
     * @param password
     * @throws Exception
     */
    public static void validateUserLogIn(String username, String password) throws Exception {
        // Open file that contains all registered users
        File userFile = new File(DATABASE_DIRECTORY + "users", username + ".txt");
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            // Read each line in user's file
            String line;
            while ((line = br.readLine()) != null) {
                // Look for the line that contains the user's password
                if (line.startsWith("Password: ")) {

                    // Check is password match
                    String storedPassword = line.substring(10);
                    if (storedPassword.equals(password)) {

                        // Load user account
                        try {
                            User authenticatedUser = loadUserAccount(username);
                            authenticatedUser = loadUserTransactions(authenticatedUser);
                            UserSessionManager.setCurrentUser(authenticatedUser);
                        } catch (IllegalArgumentException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                    }
                }
            }
            System.err.println("Incorrect password.");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File cannot be opened or read: " + e.getMessage());
        }
    }
}
