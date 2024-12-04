package csc335.app.persistence;

public final class AccountManager {
    
    private static AccountManager manager = null;
    private static UserSessionManager session = null;
    private static User activeUser;

    private AccountManager() {
    }

    public static AccountManager getAccountManager() {
        if (manager == null) {
            manager = new AccountManager();
        }
        return manager;
    }
    
    public static String registerAccount(String username, String email, String password) {
        String status = "";
        try {
            // if (!ValidationUtil.isValidEmail(email)) throw new IllegalArgumentException("Invalid email");
            // if (!ValidationUtil.isValidPassword(password)) throw new IllegalArgumentException("Invalid password");

            if (AccountRepository.isEmailTaken(email)) {
                status = "Email is already taken!";
                return status;
            }
            
            if (AccountRepository.isUsernameTaken(username)) {
                status = "Username is already taken!";
                return status;
            } 
            String salt = Hasher.generateSalt();
            String hashedPassword = Hasher.hashPassword(password, salt);
            
            if (AccountRepository.addAccount(username, email, hashedPassword, salt)) {
                status = "Success";
                return status;
            }

        } catch (Exception e) {
            status = "Uh, oh! There's been an error.";
        }

        return status;
    }

    public static boolean authenticateUser(String username, String password) {
        String[] userCredentials = AccountRepository.getCredentials(username);
        if (userCredentials == null) {
            return false;
        }

        String storedHashedPassword = userCredentials[0];
        String storedSalt = userCredentials[1];
        
        String hashedPassword = Hasher.hashPassword(password, storedSalt);
        boolean authenticated = hashedPassword.equals(storedHashedPassword);
        if (authenticated) {
            activeUser = AccountRepository.loadUser(username);
        }
        
        UserSessionManager.setCurrentUser(activeUser);
        System.out.println("LOGIN SUCCESS! THIS USER IS AUTHENTICATED!: " + activeUser.toString());
        return authenticated;
    }

    protected static User getActiveUser() {
        return activeUser;
    }


}
