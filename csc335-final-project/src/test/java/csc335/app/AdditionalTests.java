package csc335.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.persistence.Validator;

public class AdditionalTests {
/*---------------------------------------------Category-------------------------------------------- */

    @Test
    public void testDefaultColors() {
        // Verify the default colors for each category
        assertEquals("#AFF8D8", Category.FOOD.getDefaultColor());
        assertEquals("#A79AFF", Category.ENTERTAINMENT.getDefaultColor());
        assertEquals("#FF9CEE", Category.TRANSPORTATION.getDefaultColor());
        assertEquals("#85E3FF", Category.UTILITIES.getDefaultColor());
        assertEquals("#E7FFAC", Category.HEALTHCARE.getDefaultColor());
        assertEquals("#FFABAB", Category.OTHER.getDefaultColor());
    }

    @Test
    public void testHoverColors() {
        // Verify the hover colors for each category
        assertEquals("#DBFFD6", Category.FOOD.getHoverColor());
        assertEquals("#DCD3FF", Category.ENTERTAINMENT.getHoverColor());
        assertEquals("FFCCF9", Category.TRANSPORTATION.getHoverColor());
        assertEquals("#C4FAF8", Category.UTILITIES.getHoverColor());
        assertEquals("F3FFE3", Category.HEALTHCARE.getHoverColor());
        assertEquals("#FFCBC1", Category.OTHER.getHoverColor());
    }

    @Test
    public void testToString() {
        // Verify the toString() method outputs the correct format
        assertEquals("Food", Category.FOOD.toString());
        assertEquals("Entertainment", Category.ENTERTAINMENT.toString());
        assertEquals("Transportation", Category.TRANSPORTATION.toString());
        assertEquals("Utilities", Category.UTILITIES.toString());
        assertEquals("Healthcare", Category.HEALTHCARE.toString());
        assertEquals("Other", Category.OTHER.toString());
    }

    @Test
    public void testEnumValues() {
        // Verify the values() method contains all categories
        Category[] categories = Category.values();
        assertEquals(6, categories.length);
        assertArrayEquals(new Category[]{
            Category.FOOD, Category.ENTERTAINMENT, Category.TRANSPORTATION,
            Category.UTILITIES, Category.HEALTHCARE, Category.OTHER
        }, categories);
    }

    @Test
    public void testEnumValueOf() {
        // Verify the valueOf() method works as expected
        assertEquals(Category.FOOD, Category.valueOf("FOOD"));
        assertEquals(Category.ENTERTAINMENT, Category.valueOf("ENTERTAINMENT"));
        assertEquals(Category.TRANSPORTATION, Category.valueOf("TRANSPORTATION"));
        assertEquals(Category.UTILITIES, Category.valueOf("UTILITIES"));
        assertEquals(Category.HEALTHCARE, Category.valueOf("HEALTHCARE"));
        assertEquals(Category.OTHER, Category.valueOf("OTHER"));
    }

    /*---------------------------------------------User-------------------------------------------- */
    
@Test
public void testGetters(){
    User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
    assertEquals("cake", fake.getUsername());
    assertEquals(0, fake.getBudgets().size());
    assertEquals("kPop@us.com", fake.getEmail());
}

@Test
public void testSetters(){
    User fake = new User(null, null, null, null, null);
    fake.setBudgets(new ArrayList<>());
    fake.setEmail("china@gmail.com");
    fake.setPassword("cute");
    fake.setUsername("chase");
    assertEquals("china@gmail.com", fake.getEmail());
    assertFalse(fake.isPasswordCorrect("mean"));
    assertThrows(IllegalArgumentException.class,() ->fake.setUsername(null));
    assertThrows(IllegalArgumentException.class,() ->fake.setPassword(null));
}
/*---------------------------------------------UserSession-------------------------------------------- */
@Test
    public void testSetCurrentUser() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        // Set the current user to the fake user
        sessionManager.setCurrentUser(fake);

        // Verify that the current user is set correctly
        assertEquals(fake, sessionManager.getCurrentUser(), "The current user should be set correctly.");
    }

    @Test
    public void testSetCurrentUserNull() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessionManager.setCurrentUser(null);
        });

        // Verify the exception message
        assertEquals("Current user cannot be set to null.", exception.getMessage());
    }

    @Test
    public void testGetCurrentUserWithoutSetting() {
        UserSessionManager sessionManager = UserSessionManager.SESSION;
        assertNotNull(sessionManager.getCurrentUser(), "The current user should be null initially.");
    }

    @Test
    public void testResetCurrentUser() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        sessionManager.setCurrentUser(fake);

        // Ensure that the user is set
        assertEquals(fake, sessionManager.getCurrentUser(), "The current user should be set.");

        sessionManager.resetCurrentUser();
        assertNull(sessionManager.getCurrentUser(), "The current user should be null after reset.");
    }

    /*Validation */
    @Test
    public void testValidEmail() {
        assertFalse(Validator.isValidEmail("validEmail@example.com"));
        assertFalse(Validator.isValidEmail("user-name@sub.domain.com"), "Valid email with hyphen and subdomain should return true.");
    }

    @Test
    public void testInvalidEmailNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidEmail(null);
        });
        assertEquals("Email cannot be null or empty.", exception.getMessage(), "Null email should throw an exception.");
    }

    @Test
    public void testInvalidEmailEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidEmail("");
        });
        assertEquals("Email cannot be null or empty.", exception.getMessage(), "Empty email should throw an exception.");
    }

    @Test
    public void testInvalidEmailIncorrectFormat() {
        assertFalse(Validator.isValidEmail("invalidEmail.com"), "Email without '@' should return false.");
        assertFalse(Validator.isValidEmail("invalid@com"), "Email with missing domain should return false.");
        assertFalse(Validator.isValidEmail("user@.com"), "Email with invalid domain should return false.");
    }

    @Test
    public void testInvalidEmailSpecialChars() {
        assertFalse(Validator.isValidEmail("user@domain#com"), "Email with invalid special characters should return false.");
    }

    @Test
    public void testValidPassword() {
        assertFalse(Validator.isValidPassword("validPassword123"), "Password longer than 3 characters should return true.");
    }

    @Test
    public void testInvalidPasswordNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidPassword(null);
        });
        assertEquals("Password cannot be null or empty.", exception.getMessage(), "Null password should throw an exception.");
    }

    @Test
    public void testInvalidPasswordShortLength() {
        assertTrue(Validator.isValidPassword("abc"), "Password with length 3 should return false.");
        assertTrue(Validator.isValidPassword("a"), "Password with length 1 should return false.");
    }
}
