package csc335.app.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc335.app.Category;
import csc335.app.controllers.Observer;
import csc335.app.models.Budget;
import csc335.app.models.Expense;

// [ ] Complete file coment
/**
 * @author Genesis Benedith
 */

 // [ ] Complete class coment
/**
 * 
 */
public final class AccountRepository implements Observer {

    private static AccountRepository repository = null;
    private static final String REPOSITORY = "repository";
    private static final String ACCOUNTS_DIRECTORY = "accounts";
    private static final String ACCOUNTS_DATABASE = "_all_accounts.txt";
    private static Map<String, User> accounts = new HashMap<>();

    private AccountRepository() {
    }

    public static AccountRepository getAccountRepository() {
        if (repository == null) {
            repository = new AccountRepository();
        }
        return repository;
    }

    @Override
    public void update() {
        System.out.println("Account Repository Observer notified.");
        loadAccounts();
    }

    /**
     * Saves user credentials to the file login_data.txt
     * 
     * @param username the username 
     * @param email
     * @param password
     */
    protected static boolean addAccount(String username, String email, String hashedPassword, String salt) {
        // Get file that contains all registered accounts
        Path path = Path.of(REPOSITORY, ACCOUNTS_DATABASE);
        File file = new File(path.toString());

        // Write user's account credentials in file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            // Expected format -> Username, Email, Password
            bw.write(username + ", " + email + ", " + hashedPassword + ", " + salt + "\n");
        } catch (IOException e) {
            return false;
        }

        User newUser = new User(username, email, hashedPassword, salt);
        newUser.setBudgets();
        saveUser(newUser);
        return true;
    }

    protected static String[] getCredentials(String username) {
        if (accounts.containsKey(username)) {
            return new String[] { accounts.get(username).getHashedPassword(), accounts.get(username).getSalt() };
        }

        return null;
    }

    private static void loadAccounts() {
        int count = 0;
        Map<String, User> loadedAccounts = new HashMap<>();
        // Getting the file that contains all registered accounts
        Path path = Path.of(REPOSITORY, ACCOUNTS_DATABASE);
        File file = new File(path.toString());

        // Searching through the registered accounts in the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Reading each line until EOF 
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (line.isEmpty()) {
                    continue;
                }

                // Expecting accounts to be in the following format -> Username, Email, HashedPassword, Salt
                String[] account = line.split(",");
                if (account.length != 4) {
                    System.err.println("An account is missing one or more credentials -> " + line);
                    continue;
                }

                String username = account[0].trim();
                String email = account[1].trim();
                String hashedPassword = account[2].trim();
                String salt = account[3].trim();

                if (loadedAccounts.containsKey(username)) {
                    System.err.println("Multiple accounts with the same username ->" + username);
                    continue;
                }

                User acc = new User(username, email, hashedPassword, salt);
                loadedAccounts.put(username, acc);
                System.out.println("Account added.");
                System.out.println(acc);    
                count += 1;
            }

        } catch (IOException e) {
            throw new RuntimeException("An error occured loading accounts from database: " + e.getMessage());
        }

        accounts = loadedAccounts;
        System.out.println(Integer.toString(count) + " accounts added.");
    }


     /**
     * Loads the account of a given user
     * 
     * @param username the username that is registered to the account
     * @return the user's account <User> object representing a user and their
     *         account details
     * @throws Exception
     */
    protected static User loadUser(String username) {
        // Getting the file that contains the user's transcations
        Path path = Path.of(REPOSITORY, ACCOUNTS_DIRECTORY, username + "_transactions.txt");
        File file = path.toFile();
        if (!file.exists()) {
            return accounts.get(username);
        }

        User user = accounts.get(username);

        // Search through user's transcations in the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Reading each line until end of file
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (!line.isEmpty()) {
                    continue;
                }

                // Expecting one of the formats specified in method comment
                String[] data = line.split(":");
                if (data.length != 2) {
                    System.err.println("Invalid data ->" + line);
                    continue;
                }

                // Checking if the line contains a budget or an expense
                if (data[0].trim().equals("Budget")) {
                    // Extracting budget
                    String[] parts = data[1].split(",");
                    
                    // Defining category and limit for budget
                    Category category = Category.valueOf(parts[0].trim().toUpperCase());
                    double limit = Double.parseDouble(parts[1]);

                    // Defining a <Budget> object to hold user's budget
                    Budget budget = new Budget(category, limit);
                    user.setBudget(budget);
                } else if (data[0].trim().equals("Expense")) {
                    // Extracting expense
                    String[] parts = data[1].split(",");

                    // Defining date, category, amount, and description for expense
                    String[] date = parts[0].trim().split("-");
                    Category category = Category.valueOf(parts[1].trim().toUpperCase());
                    double amount = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();

                    // Defining calendar for expense date
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

                    // Defining an <Expense> object to hold user's expense
                    Expense expense = new Expense(calendar, category, amount, description);
                    user.addExpense(expense);
                }
                } 
            } catch (Exception e) {
                throw new RuntimeException("An error occured loading accounts from database: " + e.getMessage());
            } 

        return user;
    }

    public static void saveUser(User user) {
        // Open and read file
        Path path = Path.of(REPOSITORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File userFile = path.toFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            // Write budgets to file
            bw.write("Budgets ----------");
            bw.newLine();
            Map<Category, Budget> budgets = user.getBudgetsByCategory();
            for (Category category : budgets.keySet()) {
                bw.write("Budget: " + budgets.get(category));
                bw.newLine();
            }

            // Write expenses to file
            bw.write("Expenses ----------");
            bw.newLine();
            Map<Category, List<Expense>> expenses = user.getExpensesByCategory();
            for (Category category : expenses.keySet()) {
                for (Expense expense : expenses.get(category)) {
                    bw.write("Expense: " + expense);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }
    }

    protected static File createUserReport(String username, Calendar calendar) {
        User user = accounts.get(username);

        // Extract month & year for report from calendar date
        String month = Integer.toString(calendar.get(Calendar.MONTH)).toUpperCase();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String path = REPOSITORY + "exports" + username + "_" + month + year + ".txt";

        // Write monthly expense report in file
        File file = new File(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# " + month + "/" + year + " EXPENSE REPORT FOR " + username + "\n");

            writer.write("## Budgets by Category");
            writer.newLine();
            Map<Category, Budget> bMap = user.getBudgetsByCategory();
            for (Category category : bMap.keySet()) {
                writer.write("## " + category);
                writer.write("Budget: " + bMap.get(category));
                writer.newLine();
            }

            writer.write("## Expenses by Category");
            writer.newLine();
            Map<Category, List<Expense>> eMap = user.getExpensesByCategory();
            for (Category category : eMap.keySet()) {
                writer.write("## " + category);

                double totalSpent = 0.0;
                for (Expense expense : eMap.get(category)) {
                    writer.write("Expense: " + expense);
                    writer.newLine();
                    totalSpent += expense.getAmount();
                }

                System.out.println(
                        "Total amount spent for " + category + ": $" + Double.toString(totalSpent));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("An error occured generating report: " + e.getMessage());
        }


        return file;
    }

    /**
     * 
     * @param username
     * @return
     */
    public static boolean isUsernameTaken(String username) {
        Path path = Path.of(REPOSITORY, ACCOUNTS_DIRECTORY, username + ".txt");
        File file = new File(path.toString());
        return file.exists();
    }

    /**
     * 
     * @param email
     * @return
     * @throws Exception
     */
    public static boolean isEmailTaken(String email) throws Exception {
        for (User account : accounts.values()) {
            if (account.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }
}


