package csc335.app.persistence;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dlsc.gemsfx.AvatarView;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
// import csc335.app.services.ExpenseTracker;
// import csc335.app.utils.CalendarConverter;
import javafx.scene.image.Image;

public enum Database {

    DATABASE; // A singleton instance of the app's database

    private final String DATABASE_DIRECTORY = "database";
    private final String ACCOUNTS_DIRECTORY = "accounts";
    private final String AVATAR_DIRECTORY = "avatars";
    private final String IMPORTS_DIRECTORY = "imports";
    private final String ACCOUNT_DATA = "_all_accounts.txt";
    private final Map<String, User> USER_ACCOUNTS = new HashMap<>();

    /**
     * Saves the credentials of a new user to the file login_data.txt
     * 
     * @param username the username
     * @param email
     * @param password
     * @throws IOException
     */
    protected void createNewUserAccount(String username, String email, String hashedPassword, String salt)
            throws IOException {
        // Get file that contains all registered accounts
        Path path = Path.of(DATABASE_DIRECTORY, ACCOUNT_DATA);
        File file = new File(path.toString());

        System.out.println("Creating new account...\n");

        /*
         * Write the user's login credentials to the end of file
         * Expected format -> Username, Email, HashedPassword, Salt
         */
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println("Setting up profile...\n");
            bw.write(String.join(",", username, email, hashedPassword, salt) + "\n");

            List<Budget> budgets = new ArrayList<>();
            for (Category category : Category.values()) {
                List<Expense> expenses = new ArrayList<>();
                Budget budget = new Budget(category, 0, expenses);
                budgets.add(budget);
            }

            System.out.println("Completing profile...\n");
            User newUser = new User(username, email, hashedPassword, salt, budgets);
            System.out.println(newUser.toString());

            System.out.println("Saving new account...\n");
            saveUserFile(newUser);

            System.out.println("Account creation success.\n");
            USER_ACCOUNTS.put(username, newUser);
            System.out.println(Integer.toString(USER_ACCOUNTS.values().size()) + " accounts in database.");

        } catch (IOException e) {
            throw new IOException("An error occured adding account to database: " + e.getMessage());
        }
    }

    /**
     * Finds all of the user accounts in the database. Database is expected 
     * to contain a file with every user's login data. Login data is expected 
     * to be formatted as -> Username,Email,HashedPassword,Salt
     * 
     * @throws RuntimeException
     */
    private void loadUserAccounts() throws RuntimeException {
        /* The path to the file that contains all registered accounts */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNT_DATA);
        File file = filePath.toFile();

        /* Searching through the registered accounts in the file */
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            /* Reading each line until EOF */
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (line.isEmpty()) {
                    continue;
                }

                /*
                 * Expected format:
                 * -> Username,Email,HashedPassword,Salt
                 *
                 * Skipping any lines that do not have FOUR comma-separated values
                 */
                String[] account = line.split(",");
                if (account.length != 4) {
                    System.err.println("\nAn account is missing one or more credentials -> " + line);
                    continue;
                }

                /* Extracting the credentials from line */
                String username = account[0].trim();
                String email = account[1].trim();
                String hashedPassword = account[2].trim();
                String salt = account[3].trim();

                /* Skipping any duplicate accounts */
                if (USER_ACCOUNTS.containsKey(username)) {
                    System.err.println("\nMultiple accounts with the same username ->" + username);
                    continue;
                }

                /* Instantiate a new <User> object, a List<Budget> object to hold their 
                 * budgets, and a List<Expense> to represent a user's account */
                List<Budget> budgets = new ArrayList<>();
                for (Category category : Category.values()) {
                    List<Expense> expenses = new ArrayList<>();
                    Budget budget = new Budget(category, 0, expenses);
                    budgets.add(budget);
                }

                User user = new User(username, email, hashedPassword, salt, budgets);
                loadAvatarView(user);
                loadUserExpenses(user);
                USER_ACCOUNTS.put(username, user);

                System.out.println("\nUser found -> " + username);
            }

        } catch (IOException e) {
            throw new RuntimeException("\nAn error occured loading accounts from database: " + e.getMessage());
        }
    }

    /**
     * Creates a new file for the user to store their transaction history
     * 
     * @param user
     */
    protected void saveUserFile(User user) {
        /* The path to the directory that contains all account transaction histories */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File userFile = filePath.toFile();

        // Write expenses to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            bw.write(user.toString());
            bw.newLine();

        } catch (IOException e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }
    }

    /**
     * Loads the accounts & transactions for all registered users found in
     * the database. 
     * 
     * @throws RuntimeException if there's an error loading the user accounts
     */
    protected void refreshAccounts() throws RuntimeException {
        try {
                loadUserAccounts();
                System.out.println("\n" + Integer.toString(USER_ACCOUNTS.values().size()) + " accounts found.");
            } catch (RuntimeException e) {
                System.err.println("An error occurred loading user accounts.");

            }
    }

    /**
     * Loads the transactions of a specific user. Looks for a specific
     * file in the database that contains the user's transaction
     * history and uploads them to the their user's account.
     * 
     * @param user the account the transactions belong to 
     * @throws RuntimeException if the user's transaction history file
     *                          cannot be found
     */
    private void loadUserExpenses(User user) throws RuntimeException {
        /* The path to the file that contains the transaction history for the user */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File file = filePath.toFile();

        /* Searching through user's transcations in the file */
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            List<Expense> expensesFoundInFile = new ArrayList<>();
            Map<Category, Budget> budgetsfoundInFile = new HashMap<>();

            /* Read each line until end of file */
            String line;
            while ((line = br.readLine()) != null) {

                /* Skipping line if empty */
                if (!(line.contains("-> Budget:") || line.contains("Expense:"))) {
                    continue;
                }
                System.out.println("READING A LINE -> " + line);

                String[] data = line.split(":");
                if (data.length != 2)
                    continue;

                // Checking if the line contains a budget or an expense
                if (data[0].trim().contains("Budget")) {

                    /* Extracting budget details from line */
                    String[] parts = data[1].split(",");
                    Category category = Category.valueOf(parts[0].trim().toUpperCase());
                    double limit = Double.parseDouble(parts[1]);

                    /* Adds budget to the list of budgets found in file */
                    List<Expense> budgetExpenses = new ArrayList<>();
                    Budget budget = new Budget(category, limit, budgetExpenses);
                    budgetsfoundInFile.put(category, budget);

                } else if (data[0].trim().contains("Expense")) {

                    /* Extracting the transaction details from the line */
                    String[] parts = data[1].split(",");
                    String[] date = parts[0].trim().split("-");
                    int year = Integer.parseInt(date[0]);
                    int month = Integer.parseInt(date[1]);
                    int day = Integer.parseInt(date[2]);

                    /* Setting the calendar for transaction date */
                    Calendar calendar = CalendarConverter.INSTANCE.getCalendar(year, month, day);
                    Category category = Category.valueOf(parts[1].trim().toUpperCase());
                    double amount = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();

                    /* Add expense to the list of expenses found in the file */
                    Expense expense = new Expense(calendar, category, amount, description);
                    expensesFoundInFile.add(expense);
                }
            }

            /*
             * Loops through all the expenses found in file,
             * and adds the expense to the assigned budget
             */
            for (Expense expense : expensesFoundInFile) {
                Budget assignedBudget = budgetsfoundInFile.get(expense.getCategory());
                assignedBudget.addExpense(expense);
            }

            /*
             * Indiviually sets each budget for the User class
             * with the expenses already loaded
             */
                user.setBudgets(new ArrayList<>(budgetsfoundInFile.values()));

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("An error occured loading account from database: " + e.getMessage());
        }

    }

    /**
     * Sends a call to refresh database, searches through the users,
     * and finds a user based on a specific query (Email or Username),
     * 
     * @param query the search query
     * @param filter the search filter
     * @return the account if the user exists, or null if otherwise
     */
    protected User findUserAccount(String query, String filter) {
        refreshAccounts();
        System.out.println("Query: " + query);
        System.out.println("Filter: " + filter);
        System.out.println("USER_ACCOUNTS: " + USER_ACCOUNTS.keySet());
        if ("Email".equals(filter)) {
            for (User account : USER_ACCOUNTS.values()) {
                if (account.getEmail().equalsIgnoreCase(query)) {
                    return account;
                }
            }
        } 
        if ("Username".equals(filter)) {
            return USER_ACCOUNTS.get(query);
        }

        return null;
    }

    protected void readExpenseImport(String username) {
        /* The path to the file that contains the transaction history for the user */
        Path filePath = Path.of(DATABASE_DIRECTORY, IMPORTS_DIRECTORY, username + "_import.txt");
        File file = filePath.toFile();

        List<Expense> expensesFoundInFile = new ArrayList<>();

        /* Searching through user's transcations in the file */
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {


            /* Read each line until end of file */
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4)
                    continue;
                
                /* Extracting the transaction details from the line */
                String[] parts = data[1].split(",");
                String[] date = parts[0].trim().split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);

                /* Setting the calendar for transaction date */
                Calendar calendar = CalendarConverter.INSTANCE.getCalendar(year, month, day);
                Category category = Category.valueOf(parts[1].trim().toUpperCase());
                double amount = Double.parseDouble(parts[2].trim());
                String description = parts[3].trim();

                /* Add expense to the list of expenses found in the file */
                Expense expense = new Expense(calendar, category, amount, description);
                expensesFoundInFile.add(expense);

            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("An error occured loading account from database: " + e.getMessage());
        }

        User user = findUserAccount(username, "Username");

        for (Expense expense : expensesFoundInFile) {
            ExpenseTracker.TRACKER.addExpense(expense);
        }
        
    }
    
    /**
     * Creates a new file and writes a user's expenses found in their
     * account transaction history for file exporting 
     * 
     * @param username the user the export file belongs to 
     * @return
     * @throws IOException
     */
    protected File writeExpenseExport(String username) throws IOException {
        /* The path to the directory that contains the user's transaction history */
        Path filePath = Path.of(DATABASE_DIRECTORY, "exports", username + "_transactions.txt");
        File exportFile = filePath.toFile();

        /* Write expenses to file */
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFile))) {
            List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
            for (Expense expense : expenses) {
                bw.write(expense.toString());
                bw.newLine();
            }
            return exportFile;
        } catch (IOException e) {
            throw new IOException("Unable to write to file -> " + e.getMessage());
        }
    }

    /**
     * Searches through the database and finds an image stored for a user's
     * avatar view
     * 
     * @param username the user the image belongs to
     * @return a jpeg image of the user's avatar or null if the image doesn't exist
     */
    protected Image findUserAvatarImage(String username) {
        /* The expected path to the file that contains the user's avatar image */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, AVATAR_DIRECTORY, username + ".jpeg");
        File file = filePath.toFile();

        /* Return the image if the file exists */
        if (file.exists() && file.isFile()) {
            Image image = new Image(filePath.toString());
            return image;
        }

        System.out.println("Could not find an image for the user: " + username);
        return null;
    }

    /**
     * Loads the avatar view for a specific user. The avatar view can either
     * display an avatar image or the user's initials if no if there is no image 
     * on file for the user
     * 
     * @param user the user the avatar view belongs to 
     */
    private void loadAvatarView(User user) {
        String username = user.getUsername();
        Image avatarImage = findUserAvatarImage(username);
        AvatarView avatarView = new AvatarView(username.substring(0,1), avatarImage);
        user.setAvatar(avatarView);
    }

    /**
     * Renders an image for a user's avatar and saves it to the database
     * as a jpeg file
     * 
     * @param imagePath the path of uploaded image 
     * @param username the user the image belongs to
     * @throws Exception if there is an error rendering or saving the image from the given path
     */
    protected void saveUserAvatarImage(String imagePath, String username) throws Exception {
        if (imagePath != null && !imagePath.isEmpty() && !username.isEmpty()){
            try {
                BufferedImage image = ImageIO.read(new File(imagePath));
                Path outputPath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, AVATAR_DIRECTORY, username + ".jpeg");
                File outputFile = new File(outputPath.toString());
                ImageIO.write(image, "jpg", outputFile);
            } catch (IOException e) {
                throw new Exception("Unable to save image file.");
            }
        }
    }

    
}