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

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;

/**
 * The `Database` enum is a singleton that handles database-related operations.
 * It manages user accounts, transactions, and file operations for data persistence.
 * 
 * Responsibilities:
 * - Create and save user accounts in the database.
 * - Load user accounts and their transactions from files.
 * - Refresh in-memory accounts from the database.
 * - Export and import user transaction data.
 * 
 * File: Database.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public enum Database {

    DATABASE; // A singleton instance of the app's database

    private final String DATABASE_DIRECTORY = "database";
    private final String ACCOUNTS_DIRECTORY = "accounts";
    private final String IMPORTS_DIRECTORY = "imports";
    private final String ACCOUNT_DATA = "_all_accounts.txt";
    private final Map<String, User> USER_ACCOUNTS = new HashMap<>();

    /**
     * Creates a new user account and saves it to the database file.
     * 
     * @param username      the username for the new account
     * @param email         the email for the new account
     * @param hashedPassword the hashed password for the account
     * @param salt          the salt used for password hashing
     * @throws IOException if an error occurs while saving the account
     */
    protected void createNewUserAccount(String username, String email, String hashedPassword, String salt)
            throws IOException {
        Path path = Path.of(DATABASE_DIRECTORY, ACCOUNT_DATA);
        File file = new File(path.toString());

        System.out.println("Creating new account...\n");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(String.join(",", username, email, hashedPassword, salt) + "\n");

            List<Budget> budgets = new ArrayList<>();
            for (Category category : Category.values()) {
                List<Expense> expenses = new ArrayList<>();
                Budget budget = new Budget(category, 0, expenses);
                budgets.add(budget);
            }

            User newUser = new User(username, email, hashedPassword, salt, budgets);

            saveUserFile(newUser);

            USER_ACCOUNTS.put(username, newUser);
            System.out.println("Account creation success.\n");
        } catch (IOException e) {
            throw new IOException("An error occurred adding the account to the database: " + e.getMessage());
        }
    }

    /**
     * Loads all user accounts from the database file into memory.
     * The file is expected to contain data formatted as:
     * Username,Email,HashedPassword,Salt
     * 
     * @throws RuntimeException if an error occurs while reading the file
     */
    private void loadUserAccounts() throws RuntimeException {
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNT_DATA);
        File file = filePath.toFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;

                String[] account = line.split(",");
                if (account.length != 4) {
                    System.err.println("\nAn account is missing credentials -> " + line);
                    continue;
                }

                String username = account[0].trim();
                String email = account[1].trim();
                String hashedPassword = account[2].trim();
                String salt = account[3].trim();

                if (USER_ACCOUNTS.containsKey(username)) {
                    System.err.println("\nDuplicate username found -> " + username);
                    continue;
                }

                List<Budget> budgets = new ArrayList<>();
                for (Category category : Category.values()) {
                    List<Expense> expenses = new ArrayList<>();
                    Budget budget = new Budget(category, 0, expenses);
                    budgets.add(budget);
                }

                User user = new User(username, email, hashedPassword, salt, budgets);

                loadUserExpenses(user);

                System.out.println("\nUser found -> " + username);
            }

        } catch (IOException e) {
            throw new RuntimeException("\nAn error occurred loading accounts from the database: " + e.getMessage());
        }
    }

    /**
     * Saves a user's transaction history to a file.
     * 
     * @param user the user whose data is being saved
     */
    protected void saveUserFile(User user) {
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File userFile = filePath.toFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            bw.write(user.toString());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }
    }

    /**
     * Refreshes the in-memory list of user accounts by reloading them from the database.
     * 
     * @throws RuntimeException if an error occurs while refreshing accounts
     */
    protected void refreshAccounts() throws RuntimeException {
        try {
            loadUserAccounts();
            System.out.println("\n" + USER_ACCOUNTS.values().size() + " accounts found.");
        } catch (RuntimeException e) {
            System.err.println("An error occurred while refreshing user accounts.");
        }
    }

    /**
     * Loads a user's expenses from their transaction file.
     * 
     * @param user the user whose expenses are being loaded
     * @throws RuntimeException if an error occurs while loading expenses
     */
    private void loadUserExpenses(User user) throws RuntimeException {
        Path filePath = Path.of(DATABASE_DIRECTORY, ACCOUNTS_DIRECTORY, user.getUsername() + "_transactions.txt");
        File file = filePath.toFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<Expense> expensesFoundInFile = new ArrayList<>();
            Map<Category, Budget> budgetsFoundInFile = new HashMap<>();

            String line;
            while ((line = br.readLine()) != null) {
                if (!(line.contains("-> Budget:") || line.contains("Expense:"))) continue;

                String[] data = line.split(":");
                if (data.length != 2) continue;

                if (data[0].trim().contains("Budget")) {
                    String[] parts = data[1].split(",");
                    Category category = Category.valueOf(parts[0].trim().toUpperCase());
                    double limit = Double.parseDouble(parts[1]);

                    Budget budget = new Budget(category, limit, new ArrayList<>());
                    budgetsFoundInFile.put(category, budget);
                } else if (data[0].trim().contains("Expense")) {
                    String[] parts = data[1].split(",");
                    String[] date = parts[0].trim().split("-");
                    int year = Integer.parseInt(date[0]);
                    int month = Integer.parseInt(date[1]);
                    int day = Integer.parseInt(date[2]);

                    Calendar calendar = CalendarConverter.INSTANCE.getCalendar(year, month, day);
                    Category category = Category.valueOf(parts[1].trim().toUpperCase());
                    double amount = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();

                    Expense expense = new Expense(calendar, category, amount, description);
                    expensesFoundInFile.add(expense);
                }
            }

            List<Budget> expensesByBudget = new ArrayList<>();
            for (Budget budgetFound : budgetsFoundInFile.values()) {
                for (Expense expense : expensesFoundInFile) {
                    if (expense.getCategory() == budgetFound.getCategory()) {
                        budgetFound.addExpense(expense);
                    }
                }
                expensesByBudget.add(budgetFound);
            }

            user.setBudgets(expensesByBudget);
            USER_ACCOUNTS.put(user.getUsername(), user);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred loading user expenses: " + e.getMessage());
        }
    }

    /**
     * Finds a user account based on a query and filter (Username or Email).
     * 
     * @param query  the search query
     * @param filter the search filter (Username or Email)
     * @return the matching user account, or null if no match is found
     */
    protected User findUserAccount(String query, String filter) {
        refreshAccounts();
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

    /**
     * Reads and imports expense data for a specific user.
     * 
     * @param username the username of the user
     */
    protected void readExpenseImport(String username) {
        Path filePath = Path.of(DATABASE_DIRECTORY, IMPORTS_DIRECTORY, username + "_import.txt");
        File file = filePath.toFile();

        List<Expense> expensesFoundInFile = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4) continue;

                String[] date = data[0].trim().split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);

                Calendar calendar = CalendarConverter.INSTANCE.getCalendar(year, month, day);
                Category category = Category.valueOf(data[1].trim().toUpperCase());
                double amount = Double.parseDouble(data[2].trim());
                String description = data[3].trim();

                Expense expense = new Expense(calendar, category, amount, description);
                expensesFoundInFile.add(expense);
            }

        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading the import file: " + e.getMessage());
        }

        User user = findUserAccount(username, "Username");

        for (Expense expense : expensesFoundInFile) {
            ExpenseTracker.TRACKER.addExpense(expense);
        }
    }

    /**
     * Exports a user's expenses to a file.
     * 
     * @param username the username of the user whose data is being exported
     * @return the created file
     * @throws IOException if an error occurs during file writing
     */
    protected File writeExpenseExport(String username) throws IOException {
        Path filePath = Path.of(DATABASE_DIRECTORY, "exports", username + "_transactions.txt");
        File exportFile = filePath.toFile();

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
}
