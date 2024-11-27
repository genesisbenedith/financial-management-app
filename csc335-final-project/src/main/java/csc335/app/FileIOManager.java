package csc335.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.Report;
import csc335.app.models.User;

public class FileIOManager {

    private static User currentUser = UserSessionManager.getCurrentUser();
    private static String authenticatedUser;
    private static final String USER_DATA_DIRECTORY = "data/users";

    public static boolean authenticateUser(String username, String password) {
        File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
        if (!userFile.exists()) {
            return false; // Return false if user's file not found
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Password: ")) {
                    String storedPassword = line.substring(10); // Retrieve password stored in user file
                    if (storedPassword.equals(password)) { // Compare passwords and return true if pasword is correct
                        authenticatedUser = username;
                        User user = loadUserData(authenticatedUser);
                        UserSessionManager.setCurrentUser(user);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param username
     * @param email
     * @param password
     * @return
     */
    public static boolean createUserFile(String username, String email, String password) {
        File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
        try (FileWriter writer = new FileWriter(userFile)) {
            writer.write("# User Info\n");
            writer.write("Username: " + username + "\n");
            writer.write("Email: " + email + "\n");
            writer.write("Password: " + password + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User loadUserData(String username) throws IOException {
        // Check if the user's file exists
        File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
        if (!userFile.exists()) {
            throw new IOException("User file does not exist.");
        }

        // // Check if the user is authenticated for this current session
        // if (!UserSessionManager.getCurrentUser().getUsername().equals(username)) {
        //     throw new IOException("User is not authenticated.");
        // }

        // Read each line in the user's file
        BufferedReader reader = new BufferedReader(new FileReader(userFile));
        String line;
        User user = null;
        String password = null;
        String email = null;
        Category category = null;
        Map<Category, Budget> budgets = new HashMap<>();
        List<Expense> expenses = new ArrayList<>();
        List<Report> reports = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String key = parts[0].trim();
            String value = parts.length > 1 ? parts[1].trim() : "";

            switch (key) {
                case "Password":
                    password = value;
                    break;
                case "Email":
                    email = value;
                    break;
                case "Budget":
                    // Expected format -> Budget: Groceries, 500.00
                    String[] budgetParts = value.split(",");
                    category = Category.valueOf(budgetParts[0].trim());
                    double limit = Double.parseDouble(budgetParts[1].trim());
                    budgets.put(category, new Budget(category, limit));
                    break;
                case "Expense":
                    // Expected format -> Expense: 2024-01-01, Groceries, 50.00, Supermarket
                    String[] expenseParts = value.split(",");
                    LocalDate date = LocalDate.parse(expenseParts[0].trim(), DateTimeFormatter.ofPattern("YYYY-MM-DD"));
                    category = Category.valueOf(expenseParts[1].trim());
                    double amount = Double.parseDouble(expenseParts[2].trim());
                    String description = expenseParts[3].trim();
                    expenses.add(new Expense(date, category, amount, description));
                    break;
            }
        }

        reader.close();

        // Ensure all required fields are present
        if (username != null && email != null && password != null) {
            user = new User(username, email, password);

            // Add each budget found in the file to the user's budget map
            for (Category cat : budgets.keySet()) { // Loop through each key in map
                Budget budget = budgets.get(cat); // Get the value for the key
                user.setBudget(cat, budget.getLimit()); // Set budget
            }

            // Add each expense found in the file to the user's expense list
            for (Expense expense : expenses) {
                user.addExpense(expense);
            }

        } else {
            throw new IOException("Invalid user file format.");
        }

        return user;
    }

    public static void saveUserData(User user) {
        if (user.equals(UserSessionManager.getCurrentUser())) {
            File userFile = new File(USER_DATA_DIRECTORY, currentUser.getUsername() + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
                writer.write("# User Info\n");
                writer.write("Username: " + currentUser.getUsername());
                writer.newLine();
                writer.write("Email: " + currentUser.getEmail());
                writer.newLine();
                writer.write("Password: " + currentUser.getPassword());
                writer.newLine();

                writer.write("# Budgets");
                writer.newLine();
                for (Budget categorizedBudget : currentUser.getAllBudgets()) {
                    writer.write("Budget: " + categorizedBudget);
                    writer.newLine();
                }

                writer.write("# Expenses");
                writer.newLine();
                for (Expense expense : currentUser.getAllExpenses()) {
                    writer.write(
                            "Expense: " + expense);
                    writer.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error: Unable to update the user file.");
            }
        } else {
            System.out.println("Error: Invalid user session.");
        }

    }

    
}
