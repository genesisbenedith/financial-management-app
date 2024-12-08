package csc335.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import csc335.app.persistence.AccountManager;

// Author: Genesis Benedith
// File AccountManagerTest.java
// Course: CSC 335 (Fall 2024)

public class AccountManagerTest {

    @Test
    void testSetNewUser_SuccessfulRegistration() throws IOException {
        String username = "new_user";
        String email = "new_user@example.com";
        String password = "SecurePassword123";

        int result = AccountManager.ACCOUNT.setNewUser(username, email, password);

        assertEquals(1, result, "The user should be successfully registered.");
    }

    @Test
    void testSetNewUser_EmailAlreadyTaken() throws IOException {
        String username = "unique_user";
        String email = "existing_email@example.com";
        String password = "SecurePassword123";

        int result = AccountManager.ACCOUNT.setNewUser(username, email, password);

        assertEquals(1, result, "The email is already taken.");
    }

    @Test
    void testSetNewUser_UsernameAlreadyTaken() throws IOException {
        String username = "existing_user";
        String email = "unique_email@example.com";
        String password = "SecurePassword123";

        int result = AccountManager.ACCOUNT.setNewUser(username, email, password);

        assertEquals(1, result, "The email is already taken.");
    }

    @Test
    void testAuthenticateUser_SuccessfulLogin() {
        String username = "existing_user";
        String password = "SecurePassword123";

        int result = AccountManager.ACCOUNT.authenticateUser(username, password);

        assertEquals(0, result, "The user should be successfully authenticated.");
    }

    @Test
    void testAuthenticateUser_UsernameNotFound() {
        String username = "nonexistent_user";
        String password = "SomePassword";

        int result = AccountManager.ACCOUNT.authenticateUser(username, password);

        assertEquals(1, result, "The username does not exist.");
    }

    @Test
    void testAuthenticateUser_IncorrectPassword() {
        String username = "existing_user";
        String password = "WrongPassword";

        int result = AccountManager.ACCOUNT.authenticateUser(username, password);

        assertEquals(2, result, "The password is incorrect.");
    }

    @Test
    void testExportFile_SuccessfulExport() throws IOException {
        File exportedFile = AccountManager.ACCOUNT.exportFile();

        assertNotNull(exportedFile, "The exported file should not be null.");
        assertTrue(exportedFile.exists(), "The exported file should exist.");
    }
}
