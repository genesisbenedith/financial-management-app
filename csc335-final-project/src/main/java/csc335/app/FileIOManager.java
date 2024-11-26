package csc335.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;

public class FileIOManager {

    private static User currentUser;
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

    public static User loadUserData(String username, String password) throws IOException {

        File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
        BufferedReader reader = new BufferedReader(new FileReader(userFile));
        String line;
        User user = null;

        String email = null;
        
        Map<Category, Budget> budgets = new HashMap<>();
        List<Expense> expenses = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String key = parts[0].trim();
            String value = parts.length > 1 ? parts[1].trim() : "";
    
            switch (key) {
                case "Email":
                    email = value;
                    break;
                case "Budget":
                    // Expected format -> Budget: Groceries, 500.00
                    String[] budgetParts = value.split(",");
                    Category category = Category.valueOf(budgetParts[0].trim());
                    double limit = Double.parseDouble(budgetParts[1].trim());
                    budgets.put(category, new Budget(category, limit));
                    break;
                case "Transaction":
                    // Expected format -> Expense: 2024-01-01, Groceries, 50.00, Supermarket
                    String[] expenseParts = value.split(",");
                    String date = expenseParts[0].trim();
                    Category Category = csc335.app.models.Category.valueOf(expenseParts[1].trim());
                    double amount = Double.parseDouble(expenseParts[2].trim());
                    String description = expenseParts[3].trim();
                    expenses.add(new Expense(date, Category, amount, description));
                    break;
            }
        }
    
        reader.close();
    
        // Ensure all required fields are present
        if (username != null && email != null && password != null) {
            user = new User(username, email, password);

            // Add each budget found in the file to the user's budget map
            for (Category category : budgets.keySet()) { // Loop through each key in map
                Budget budget = budgets.get(category); // Get the value for the key
                user.setBudget(category, budget.getLimit()); // Set budget
            }
            
            // Add each expense found in the file to the user's expense list
            for (Expense expense : expenses){
                user.addExpense(expense);
            }

            
        } else {
            throw new IOException("Invalid user file format.");
        }
    
        return user;
    }

    public static void updateUserFile() {
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
            for (Map.Entry<Category, Budget> entry : currentUser.getBudgets().entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue().getLimit());
                writer.newLine();
            }

            writer.write("# Expenses");
            writer.newLine();
            for (Expense expense : currentUser.getExpenses()) {
                writer.write(expense.getDate() + ", " + expense.getCategory() + ", " + expense.getAmount() + ", "
                        + expense.getDescription());
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to update the user file.");
        }
    }
}
