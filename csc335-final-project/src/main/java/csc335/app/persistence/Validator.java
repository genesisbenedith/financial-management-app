package csc335.app.persistence;

import java.util.regex.Pattern;

/**
 * Utility class for validating user input, such as email and password.
 * Provides methods to check the format and constraints of user credentials.
 * 
 * File: Validator.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public class Validator {

    /**
     * Determines whether a specified email is in an appropriate format for account registration.
     * 
     * Expected formats for email:
     * - user@subdomain.domain.tld
     * - user@domain.tld
     * - user-name@subdomain.domain.tld
     * - user_name@domain.tld
     * 
     * Examples of valid formats:
     * - wilbur@cs.arizona.edu
     * - wilma@arizona.edu
     * - wild-cats@arizona.edu
     * - wild_cats@catworks.arizona.edu
     * 
     * @param email the email to be registered to the account
     * @return true if the email is valid, false otherwise
     * @throws IllegalArgumentException if the email is null or empty
     */
    public static boolean isValidEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        // Regex pattern for validating email format
        String regex = "^[A-Za-z0-9]+([-_]?[A-Za-z]+)*@([A-Za-z]\\.){1,2}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    /**
     * Checks if the specified password meets the required constraints for account registration.
     * 
     * @param password the password to be validated
     * @return true if the password is valid, false otherwise
     * @throws IllegalArgumentException if the password is null
     */
    public static boolean isValidPassword(String password) throws IllegalArgumentException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        System.out.println(password);
        // Password is invalid if its length is less than 3 characters
        return (password.length() >= 3);
    }
}
