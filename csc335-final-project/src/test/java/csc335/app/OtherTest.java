package csc335.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;

public class OtherTest {

    @Test
    void test_userSession() {
        User user = new User("karen", "karenrocks@aol.com", "nananabooboo", "pepper", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(user);
        assertTrue(user == UserSessionManager.SESSION.getCurrentUser());
        assertTrue(UserSessionManager.SESSION.hasActiveUser());
    }

}
