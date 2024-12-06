package csc335.app.persistence;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Genesis Benedith
 */

/**
 * 
 */
public final class Hasher {

    private static final SecureRandom RANDOM_CRYPTO = new SecureRandom();
    private static final String HASH_ALGO = "SHA-256";

    protected static final String generateSalt() {
        byte[] bytes = new byte[64];
        RANDOM_CRYPTO.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 
     * @param password
     * @param salt
     * @return
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

     public static boolean matches(String password, String salt, String hashedPassword) {
        return hashedPassword.equals(hashPassword(password, salt));
     }
}