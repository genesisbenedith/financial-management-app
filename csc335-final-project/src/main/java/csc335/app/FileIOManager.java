package csc335.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import csc335.app.enums.Category;
import csc335.app.models.Budget;
import csc335.app.models.Expense;
import csc335.app.models.User;

public class FileIOManager {

    private final UserSessionManager sessionManager;
    private static final String DATABASE_DIRECTORY = "data";
    private static final String TRANSACTIONS_DIRECTORY = "data";
    private static final String IMPORTS_DIRECTORY = "data";
    private static final String EXPORTS_DIRECTORY = "data";

    public FileIOManager() {

    }
    /**
     * TODO: IMPLEMENT HASHING
     * 
     * @param username
     * @param password
     * @throws Exception
     */
    public static void validateUserLogIn(String username, String password) throws Exception {
        // Open file that contains all registered users
        File userFile = new File(DATABASE_DIRECTORY + "users", username + ".txt");
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            // Read each line in user's file
            String line;
            while ((line = br.readLine()) != null) {
                // Look for the line that contains the user's password
                if (line.startsWith("Password: ")) {

                    // Check is password match
                    String storedPassword = line.substring(10);
                    if (storedPassword.equals(password)) {

                        // Load user account
                        try {
                            User authenticatedUser = loadUserAccount(username);
                            authenticatedUser = loadUserTransactions(authenticatedUser);
                            UserSessionManager.setCurrentUser(authenticatedUser);
                        } catch (IllegalArgumentException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                    }
                }
            }
            System.err.println("Incorrect password.");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File cannot be opened or read: " + e.getMessage());
        }
    }

    /**
     * Saves user credentials to the file login_data.txt
     * 
     * @param #username the username 
     * @param email
     * @param password
     * @throws Exception
     */
    public static void createUserAccount(String username, String email, String password) throws Exception {
        // Check if email and username are unique
        if (!isEmailTaken(email) && !isUsernameTaken(username)) {
            // Get file that contains all registered accounts
            String path = DATABASE_DIRECTORY + "login_data.txt";
            File file = new File(path);

            // Write user's account credentials in file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                // Expected format -> Username, Email, Password
                bw.write(username + ", " + email + ", " + password + "\n");
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Unable to write to file: " + e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            throw new Exception("One or more credentials is not unique -> (@" + username + ", " + email + ")");
        }
    }

    /**
     * Loads the account of a given user
     * 
     * @param username the username that is registered to the account
     * @return the user's account <User> object representing a user and their
     *         account details
     * @throws Exception
     */
    private static User loadUserAccount(String username) throws Exception {
        // Defining null <User> object as a placeholder for user's account details
        User user = null;

        // Getting the file that contains all registered accounts
        String path = DATABASE_DIRECTORY + "login_data.txt";
        File file = new File(path);

        // Searching through the registered accounts in the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Reading each line until EOF or until the account for @param username is found
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (!line.isEmpty()) {
                    // Expecting accounts to be in the following format -> Username, Email, Password
                    String[] account = line.split(",");
                    if (account.length == 3) {
                        // Checking if the line contains the account for @param username
                        if (account[0].trim().equals(username)) {
                            // Checking if the <User> object hasn't been reassigned to an account yet
                            if (user == null) {
                                // Extracting email & password
                                try {
                                    // Defining email & pass word for user
                                    String email = account[1].trim();
                                    String password = account[2].trim();

                                    // Reassigning <User> object to reference new instance
                                    user = new User(username, email, password);
                                } catch (Exception e) {
                                    // Throwing exception if the account has invalid credentials
                                    throw new Exception("User " + username + " has invalid account credentials -> "
                                            + e.getMessage());
                                }
                            } else {
                                // Throwing exception if there is more than one account for @param username
                                throw new Exception("Multiple accounts with the same username ->" + username);
                            }
                        }
                    } else {
                        // Throwing exception if a non-whitespace line in the file has an incorrect
                        // format
                        throw new Exception("An account is missing one or more credentials -> " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("File cannot be opened or read: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("File contains invalid data: " + e.getMessage(), e);
        }

        return user;
    }

    /**
     * Expected formats for budgets & expenses
     * -> Budget: Category, Limit
     * -> Expense: YYYY-MM-DD, Category, Amount, Description ->
     * 
     * Valid format examples
     * -> Food, 500.00
     * -> 2022-04-05, Food, 275.00
     * 
     * @param user
     * @return
     * @throws IOException
     */
    private static User loadUserTransactions(User user) throws FileNotFoundException, IOException, Exception {
        // Getting the file that contains the user's transcations
        Path path = Path.of(DATABASE_DIRECTORY, "users", user.getUsername() + "_transactions.txt");
        File file = path.toFile();
        if (!file.exists()) {
            // Throwing exception if file cannot be found
            throw new FileNotFoundException("File does not exist: " + path);
        }

        // Search through user's transcations in the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Reading each line until end of file
            String line;
            while ((line = br.readLine()) != null) {
                // Checking if the line contains characters (not including whitespace)
                if (!line.isEmpty()) {
                    // Expecting one of the formats specified in method comment
                    String[] data = line.split(":");
                    if (data.length == 2) {
                        // Checking if the line contains a budget or an expense
                        if (data[0].trim().equals("Budget")) {
                            // Extracting budget
                            String[] parts = data[1].split(",");
                            try {
                                // Defining category and limit for budget
                                Category category = Category.valueOf(parts[0].trim().toUpperCase());
                                double limit = Double.parseDouble(parts[1]);

                                // Defining a <Budget> object to hold user's budget
                                Budget budget = new Budget(category, limit);
                                user.addBudget(budget);
                            } catch (Exception e) {
                                // Throwing exception if the budget has invalid data
                                throw new Exception(
                                        "User " + user.getUsername() + " has an invalid budget -> " + e.getMessage());
                            }
                        } else if (data[0].trim().equals("Expense")) {
                            // Extracting expense
                            String[] parts = data[1].split(",");
                            try {
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
                            } catch (Exception e) {
                                // Throwing exception if the expense has invalid data
                                throw new Exception(
                                        "User " + user.getUsername() + " has an invalid expense -> " + e.getMessage());
                            }
                        }
                    } else {
                        // Throwing exception if a non-whitespace line is not in the expected format
                        throw new Exception("Line contains invalid format ->" + line);
                    }
                }
            }
        } catch (IOException e) {
            // Throwing exception if the file reader failed
            throw new FileNotFoundException("File cannot be opened or read: " + e.getMessage());
        } catch (Exception e) {
            // Throwing exception if any other errors occurred during file reading
            throw new Exception(e.getMessage());
        }

        return user;
    }

    public static void saveUserData(String username) throws FileNotFoundException {
        if (!username.equals(UserSessionManager.getUsername())) {
            throw new IllegalArgumentException("No active session for " + username + ".");
        }

        // Open and read file
        File userFile = new File(DATABASE_DIRECTORY, username + "_transactions.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            // Write budgets to file
            bw.write("Budgets ----------");
            bw.newLine();
            List<Budget> budgets = UserSessionManager.getBudgets();
            for (Budget budget : budgets) {
                bw.write("Budget: " + budget);
                bw.newLine();
            }

            // Write expenses to file
            bw.write("Expenses ----------");
            bw.newLine();
            List<Expense> expenses = UserSessionManager.getExpenses();
            for (Expense expense : expenses) {
                bw.write("Expense: " + expense);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }
    }

    public static File createUserReport(String username, Calendar calendar) {
        if (!username.equals(UserSessionManager.getUsername())) {
            throw new IllegalArgumentException("No active session for " + username + ".");
            return null;
        }

        // Extract month & year for report from calendar date
        String month = Integer.toString(calendar.get(Calendar.MONTH));
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String path = DATABASE_DIRECTORY + "exports" + username + ".txt";

        // Write user's report in file
        File file = new File(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# " + reportDate.getMonth() + " " + Integer.toString(reportDate.getYear())
                    + " MONTHLY SUMMARY FOR " + username + "\n");

            writer.write("# Budgets by Category");
            writer.newLine();
            for (Map.Entry<Category, Budget> budgetCategory : currentUser.getCategorizedBudgets().entrySet()) {
                writer.write("Budget: " + budgetCategory.getValue());
                writer.newLine();
            }

            writer.write("# Expenses by Category");
            writer.newLine();
            for (Map.Entry<Category, List<Expense>> expenseCategory : currentUser.getCategorizedExpenses().entrySet()) {
                double totalSpent = 0.0;
                for (Expense expense : expenseCategory.getValue()) {
                    writer.write("Expense: " + expense);
                    writer.newLine();
                    totalSpent += expense.getAmount();
                }

                System.out.println(
                        "Total amount spent for " + expenseCategory.getKey() + ": $" + Double.toString(totalSpent));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: Unable to generate user report.");
            return null;
        }

        return monthlyReport;
    }

    /**
     * 
     * @param username
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isUsernameTaken(String username) throws FileNotFoundException {
        File userDir = new File(DATABASE_DIRECTORY + "users");

        // Check if database directory exists
        if (userDir.exists() && userDir.isDirectory()) {
            File[] userFiles = userDir.listFiles();
            for (File userFile : userFiles) {
                if (userFile.isFile() && userFile.getName().split(".")[0].equals(username)) {
                    System.out.println("Username is already taken.");
                    return true;
                }
            }
        } else {
            throw new FileNotFoundException("ERROR: Unable to access database.");
        }

        return false;
    }

    public static boolean isEmailTaken(String email) throws Exception {
        boolean taken = false;

        // Get file that contains all registered accounts
        String path = DATABASE_DIRECTORY + "login_data.txt";
        File file = new File(path);

        // Read file
        try (BufferedReader reader = new BufferedReader(new FileReader(userManagerFile))) {
            // Read each line until EOF or until @param email is found
            String line;
            while ((line = reader.readLine()) != null) {
                // Look for account in expected format -> Username, Email, Password
                String[] account = line.split(",");
                if (account.length == 3) {
                    // Check if account is registered to email
                    if (account[2].trim().equalsIgnoreCase(email)) {
                        System.err.println("Email is already taken -> " + email);
                        taken = true;
                    }
                } else {
                    // Throw exception if any non-whitespace line is not in the expected format
                    throw new Exception("An account is missing one or more credentials -> " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File cannot be opened or read: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        return taken;
    }
}
