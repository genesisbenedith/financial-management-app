package csc335.app.persistence;

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


import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;

public enum Database {

    INSTANCE; // Singleton instance

    private  final String DATABASE_DIRECTORY = "database";
    private  final String ACCOUNTS_DIRECTORY = "accounts";
    private  final String ACCOUNT_DATA = "_all_accounts.txt";
    private final Map<String, User> USER_ACCOUNTS = new HashMap<>();

    /**
     * Saves the credentials of a new user to the file login_data.txt
     * 
     * @param username the username
     * @param email
     * @param password
     * @throws RuntimeException
     */
    protected void addNewUserAccount(String username, String email, String hashedPassword, String salt)
            throws RuntimeException {
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
            throw new RuntimeException("An error occured adding account to database: " + e.getMessage());
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write("\n-------------------- Expenses --------------------");
            bw.newLine();
            bw.write(user.toString());
                bw.newLine();
                
        } catch (IOException e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }
    }

    /**
     * Loads the accounts for all registered users 
     * 
     * @return a key-value map of every username and their account data
     * @throws RuntimeException if there's an error accessing the database  
     */
    protected Map<String, User> loadUserAccounts() throws RuntimeException{
        List<User> usersFound = new ArrayList<>();
        Map<String, User> usersLoaded = new HashMap<>(); // Holds all user accounts

        /* The path to the file that contains all registered accounts */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNT_DATA);
        File file = filePath.toFile();

        /* Searching through the registered accounts in the file */
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            /* Reading each line until EOF  */
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (line.isEmpty()) {
                    continue;
                }

                /* Expecting accounts to be in the following format -> Username,Email,HashedPassword,Salt
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
                if (usersLoaded.containsKey(username)) {
                    System.err.println("\nMultiple accounts with the same username ->" + username);
                    continue;
                }

                List<Budget> budgets = new ArrayList<>();
                for (Category category : Category.values()) {
                    List<Expense> expenses = new ArrayList<>();
                    Budget budget = new Budget(category, 0, expenses);
                    budgets.add(budget);
                }

                /* Instantiate a new <User> object to represent user */
                User user = new User(username, email, hashedPassword, salt, budgets);
                usersFound.add(user);
                System.out.println("\nUser found -> " + username);  
            }

        } catch (IOException e) {
            throw new RuntimeException("\nAn error occured loading accounts from database: " + e.getMessage());
        }
        
        System.out.println("\n" + Integer.toString(usersFound.size()) + " users found.");
        
        /* Load the account for every user found */
        for (User usr : usersFound ) {
            try {
                loadUserAccount(usr);
                System.out.println("\nUser account loaded --------------------");
                System.out.println(usr.toString()); 
                usersLoaded.put(usr.getUsername(), usr);
            } catch (RuntimeException e) {
                System.err.println("An error occurred loading the account history for -> " + usr.getUsername());
                
            }
        }

        System.out.println("\n" + Integer.toString(usersLoaded.values().size()) + " accounts loaded.");
        return usersLoaded;
    }

    /**
     * Loads the account of a specific user
     * 
     * @param username the username that is registered to the account
     * @throws RuntimeException if the user's transaction history file
     * cannot be found
     */
    private void loadUserAccount(User user) throws RuntimeException {
        /* The path to the file that contains all registered accounts */
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File file = filePath.toFile();
        
        /* Searching through user's transcations in the file */
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            List<Expense> expenses = new ArrayList<>();
            Map<Category, Budget> budgetsByCategory = new HashMap<>();

            /* Read each line until end of file */
            String line;
            while ((line = br.readLine()) != null) {
                
                /* Skipping line if empty */
                if (!(line.contains("-> Budget") || line.contains("Expense"))) {
                    continue;
                }
                System.out.println("READING A LINE -> " + line);

                String[] data = line.split(":");
                if (data.length != 2)
                    continue;


                // Checking if the line contains a budget or an expense
                if (data[0].trim().contains("Budget")) {
                    System.out.println("BUDGET FOUND!");
                    /* Extracting budget details from line */
                    String[] parts = data[1].split(",");
                    Category category = Category.valueOf(parts[0].trim().toUpperCase());
                    double limit = Double.parseDouble(parts[1]);

                    /* Instantiate a new <Budget> object to represent user's budget */
                    List<Expense> budgetExpenses = new ArrayList<>();
                    Budget budget = new Budget(category, limit, budgetExpenses);
                    budgetsByCategory.put(category, budget);

                } else if (data[0].trim().contains("Expense")) {

                    /* Extracting the transaction details from the line*/
                    String[] parts = data[1].split(",");
                    String[] date = parts[0].trim().split("-");
                    Category category = Category.valueOf(parts[1].trim().toUpperCase());
                    double amount = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();

                    System.out.println("EXPENSE FOUND!");

                    /* Setting the calendar for transaction date */
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

                    /* Instantiate a new <Expense> object to represent user's transaction */
                    Expense expense = new Expense(calendar, category, amount, description);
                    expenses.add(expense);
                }
            }

            /* Loops through all the expenses found and finding the 
             * assigned budget. Also adds the expense to the Budget class.
             */
            for (Expense expense : expenses) {
                Budget assignedBudget = budgetsByCategory.get(expense.getCategory());
                assignedBudget.addExpense(expense);
            }

            /* Indiviually sets each budget for the User class 
             * with the expenses already loaded
            */
            for (Budget budget : budgetsByCategory.values())
                user.setBudget(budget);
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("An error occured loading account from database: " + e.getMessage());
        }


    }
}
