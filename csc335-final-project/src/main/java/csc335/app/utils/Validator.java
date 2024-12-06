package csc335.app.utils;

import java.util.regex.Pattern;

public class Validator {

    /**
     * Determines whether or not a specified email is in an appropriate format for the account 
     * 
     * Expected formats for email
     * -> user@subdomain.domain.tld
     * -> user@domain.tld
     * -> user-name@subdomain.domain.tld
     * -> user_name@domain.tld
     * 
     * Valid format examples
     * -> wilbur@cs.arizona.edu
     * -> wilma@arizona.edu
     * -> wild-cats@arizona.edu
     * -> wild_cats@catworks.arizona.edu
     * 
     * @param email the email to be registered to the account
     * @return true if email is valid or false otherwise
     * @throws IllegalArgumentException
     */
    public static boolean isValidEmail(String email) throws IllegalArgumentException {
        // Throwing exception if 
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        // Building regex pattern for expected email format
        String regex = "^[A-Za-z0-9]+([-_]?[A-Za-z]+)*@([A-Za-z]\\.){1,2}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    /**
     * 
     * @param email
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isValidPassword(String password) throws IllegalArgumentException {
        // Throwing exception if 
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        return (password.length() < 3);
    }
}
