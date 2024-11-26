package csc335.app;

/**
 * Author(s): Genesis Benedith
 * File: UserAuth.java
 * Description: 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserAuth {

    private static final String DATA_DIRECTORY = "data/users";

    public static boolean authenticateUser(String username, String password) {
        File userFile = new File(DATA_DIRECTORY, username + ".txt");
        if (!userFile.exists()) {
            return false; // Return false if user's file not found
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Password: ")) {
                    String storedPassword = line.substring(10); // Retrieve password
                    return storedPassword.equals(password); // Compare passwords and return true if pasword is correct
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    

}
