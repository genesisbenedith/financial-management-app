// package csc335.app;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Scanner;

// import csc335.app.models.Budget;
// import csc335.app.models.Category;
// import csc335.app.models.Expense;
// import csc335.app.models.User;

// public class TextUIController {

//     protected static TextUIModel model;
//     static Scanner scanner;
//     private static final String USER_DATA_DIRECTORY = "data/users";

//     public TextUIController(String username) throws IOException {
//         model = new TextUIModel(fetchUserData(username));
//     }

//     protected static void dashboardMenu(String username, Scanner s) {
//         scanner = s;

//         System.out.println("Welcome, " + username + "! Choose what to do:");
//         System.out.println("1. View Budgets");
//         System.out.println("2. Add a Budget");
//         System.out.println("3. Edit a Budget");
//         System.out.println("4. Remove a Budget");
//         System.out.println("5. Add an Expense");
//         System.out.println("6. Edit an Expense");
//         System.out.println("7. Remove an Expense");
//         System.out.println("8. View Expenses");
//         System.out.println("9. Sign Out");

//         int choice = scanner.nextInt();
//         scanner.nextLine(); // Consume newline after integer input

//         switch (choice) {
//             case 1:
//                 model.viewBudgets();
//                 dashboardMenu(username, s);
//             case 2:
//                 model.addBudget();
//                 dashboardMenu(username, s);
//             case 3:
//                 model.addExpense();
//                 dashboardMenu(username, s);
//             case 4:
//                 model.viewExpenses();
//                 dashboardMenu(username, s);
//             case 5:
//                 System.out.println("Signed out successfully.");
//                 break; // Return to the main menu
//             default:
//                 System.out.println("Invalid option. Try again.");
//                 dashboardMenu(username, s);
//                 break;
//         }
//     }

//     protected static boolean authenticateUser(String username, String password) {
//         File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
//         if (!userFile.exists()) {
//             return false; // Return false if user's file not found
//         }

//         try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 if (line.startsWith("Password: ")) {
//                     String storedPassword = line.substring(10); // Retrieve password
//                     return storedPassword.equals(password); // Compare passwords and return true if pasword is correct
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return false;
//     }

//     private User fetchUserData(String username) throws IOException {
//         File userFile = new File(USER_DATA_DIRECTORY, username + ".txt");
//         BufferedReader reader = new BufferedReader(new FileReader(userFile));
//         String line;
//         User user = null;

//         String email = null;
//         String password = null;

//         Map<Category, Budget> budgets = new HashMap<>();
//         List<Expense> expenses = new ArrayList<>();
    
//         while ((line = reader.readLine()) != null) {
//             String[] parts = line.split(":");
//             String key = parts[0].trim();
//             String value = parts.length > 1 ? parts[1].trim() : "";
    
//             switch (key) {
//                 case "Username":
//                     username = value;
//                 case "Email":
//                     email = value;
//                     break;
//                 case "Password":
//                     password = value;
//                     break;
//                 case "Budget":
//                     // Expected format -> Budget: Groceries, 500.00
//                     String[] budgetParts = value.split(",");
//                     Category category = Category.valueOf(budgetParts[0].trim());
//                     double limit = Double.parseDouble(budgetParts[1].trim());
//                     budgets.put(category, new Budget(category, limit));
//                     break;
//                 case "Transaction":
//                     // Expected format -> Expense: 2024-01-01, Groceries, 50.00, Supermarket
//                     String[] expenseParts = value.split(",");
//                     String date = expenseParts[0].trim();
//                     Category Category = csc335.app.models.Category.valueOf(expenseParts[1].trim());
//                     double amount = Double.parseDouble(expenseParts[2].trim());
//                     String description = expenseParts[3].trim();
//                     expenses.add(new Expense(date, Category, amount, description));
//                     break;
//             }
//         }
    
//         reader.close();
    
//         // Ensure all required fields are present
//         if (username != null && email != null && password != null) {
//             user = new User(username, email, password);

//             // Add each budget found in the file to the user's budget map
//             for (Category category : budgets.keySet()) { // Loop through each key in map
//                 Budget budget = budgets.get(category); // Get the value for the key
//                 user.setBudget(category, budget.getLimit()); // Set budget
//             }
            
//             // Add each expense found in the file to the user's expense list
//             for (Expense expense : expenses){
//                 user.addExpense(expense);
//             }

//         } else {
//             throw new IOException("Invalid user file format.");
//         }
    
//         return user;
//     }

// }
