package csc335.app.persistence;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating salts, hashing passwords, and verifying hashed passwords.
 * Ensures secure password storage and authentication using SHA-256 hashing.

 * File: Hasher.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public final class Hasher {

    // SecureRandom instance for generating cryptographic salts
    private static final SecureRandom RANDOM_CRYPTO = new SecureRandom();

    // Hashing algorithm used for password hashing
    private static final String HASH_ALGO = "SHA-256";

    /**
     * Generates a random cryptographic salt for password hashing.
     * 
     * @return a Base64-encoded salt string
     */
    protected static final String generateSalt() {
        byte[] bytes = new byte[64];
        RANDOM_CRYPTO.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Hashes a password with the provided salt using SHA-256.
     * 
     * @param password the password to hash
     * @param salt     the salt to apply to the password
     * @return the Base64-encoded hashed password
     * @throws RuntimeException if the hashing algorithm is not found
     */
    protected static final String hashPassword(String password, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGO);
            String saltedPassword = password + salt;
            byte[] hash = messageDigest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found -> " + e.getMessage());
        }
     }

     /**
     * Verifies if a hashed password matches the provided plain text password and salt.
     * 
     * @param password       the plain text password to verify
     * @param salt           the salt applied during hashing
     * @param hashedPassword the stored hashed password
     * @return true if the password matches, false otherwise
     */
     public static boolean matches(String password, String salt, String hashedPassword) {
        return hashedPassword.equals(hashPassword(password, salt));
     }
}
