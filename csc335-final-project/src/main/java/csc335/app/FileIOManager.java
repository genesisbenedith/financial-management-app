package csc335.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;

public class FileIOManager {

    private static final String DATABASE_DIRECTORY = "data/";

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
            System.err.println("Unable to read file: " + e.getMessage());
        }
    }

    /**
     * Saves user credentials to the file login_data.txt
     * 
     * @param username
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

            // Write to file
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
     * 
     * @param username
     * @return <User> object representing a user and their account details
     * @throws Exception
     */
    private static User loadUserAccount(String username) throws Exception {
        // Declare variables to store user credentials
        User user = null;
        String email = null;
        String password = null;

        // Get file that contains all registered accounts
        String path = DATABASE_DIRECTORY + "login_data.txt";
        File file = new File(path);

        // Search through accounts in file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read each line until EOF or until account for @param username is found
            String line;
            while ((line = br.readLine()) != null) {
                // Check if line contains characters (not including whitespace)
                if (!line.isEmpty()) {
                    // Look for account in expected format -> Username, Email, Password
                    String[] account = line.split(",");
                    if (account.length == 3) {
                        // Check if line contains account credentials for @param username
                        if (account[0].trim().equals(username)) {
                            // Extract email & password
                            email = account[1].trim();
                            password = account[2].trim();

                            // Check if any account credentials are blank
                            if (email.isBlank() || password.isBlank()) {
                                throw new Exception("User credentials are blank -> " + line);
                            }

                            // Load user account
                            user = new User(username, email, password);
                            break;
                        }

                    } else {
                        // Throw exception if any non-whitespace line is not in expected format
                        throw new Exception("An account is missing one or more credentials -> " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to read file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        return user;
    }

    private static User loadUserTransactions(User user) {
        // Get file that contains user's transcations
        String path = DATABASE_DIRECTORY + "users";
        File file = new File(path, user.getUsername() + ".txt");

        // Search through transcations in file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read each line until end of file
            String line;
            while ((line = br.readLine()) != null) {
                // Check if line contains characters (not including whitespace)
                if (!line.isEmpty()) {
                    // Look for budgets or expenses
                    // TODO: Confirm with TA if budgets will be stored in the user transactions file
                    String[] formattedLine = line.split(":");
                    if (formattedLine.length == 0) {
                        // Throw exception if any non-whitespace line is not in expected format
                        throw new Exception("Line contains invalid format ->" + line);
                    } else if (formattedLine[0].trim().equals("Budget")) {
                        // Extract budget
                        Budget budget = null;
                        try {
                            // Look for expected format -> Budget: Category, Limit
                            String[] parts = formattedLine[1].split(",");
                            Category category = Category.valueOf(parts[0].trim().toUpperCase());
                            double limit = Double.parseDouble(formattedLine[1].split(",")[1].trim().toUpperCase());

                            // Set budget
                            budget = new Budget(category, limit);
                        } catch (Exception e) {
                            System.err.println("User budget invalid: " + e.getMessage());
                            return user;
                        }

                        // Add budget to param currentUser
                        user.addBudget(budget);
                    } else if (formattedLine[0].trim().equals("Expense")) {
                        // Extract expense
                        Expense expense = null;
                        try {

                            // Look for expected format -> Expense: YYYY-MM-DD, Category, Amount,
                            // Description
                            String[] parts = formattedLine[1].split(",");
                            String[] date = parts[0].trim().split("-");
                            Category category = Category.valueOf(parts[1].trim().toUpperCase());
                            double amount = Double.parseDouble(parts[2].trim());
                            String description = parts[3].trim();

                            // Set calendar for expense date
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

                            // Load expense
                            expense = new Expense(calendar, category, amount, description);
                            user.addExpense(expense);
                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to read file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    public static File createUserReport(String username, LocalDate reportDate) {
        if (!username.equals(authenticatedUser)) {
            System.out.println("Error: Invalid user session.");
            return null;
        }

        File monthlyReport = new File(DATABASE_DIRECTORY + "exports", currentUser.getUsername() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(monthlyReport))) {
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
                    // Throw exception if any non-whitespace line is not in expected format
                    throw new Exception("An account is missing one or more credentials -> " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to read file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        return taken;
    }
}
