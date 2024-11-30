// package csc335.app;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.Scanner;

// import csc335.app.models.Budget;
// import csc335.app.models.Category;
// import csc335.app.models.Expense;

// public class TextUIController {

//     private static TextUIModel model = null;
//     protected static Scanner scanner;
//     private static final String USER_DATA_DIRECTORY = "data/users";
//     private static String currentUsername;

//     public TextUIController(Scanner s) {
//         model = new TextUIModel();
//         scanner = s;
//         currentUsername = model.getUsername();
//     }

//     protected void dashboardMenu() {
//         System.out.println("--------------------------------------------");
//         System.out.println("Welcome, " + currentUsername + "! Choose what to do:");
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
//         scanner.nextLine(); 

//         switch (choice) {
//             case 1: // View current budgets
//                 viewBudgets();
//                 break;
//             case 2: // Add a new budget
//                 addBudget();
//                 System.out.println("Budget added successfully!");
//                 break;
//             case 3: // Edit an existing budget
//                 editBudget();
//                 System.out.println("Budget modified successfully!");
//                 break;
//             case 4: // Remove an existing budget
//                 removeBudget();
//                 System.out.println("Budget removed successfully!");
//                 break;
//             case 5: // View current expenses
//                 viewExpenses();
//                 break;
//             case 6: // Add an expense
//                 addExpense();
//                 System.out.println("Expense added successfully!");
//                 break;
//             case 7: // Edit an expense
//                 editExpense();
//                 System.out.println("Expense modified successfully!");
//                 break;
//             case 8: // Remove an expense
//                 System.out.println("Expense removed successfully!");
//                 removeExpense();
//                 break;
//             case 9: // Log out
//                 UserSessionManager.resetCurrentUser();
//                 System.out.println("Signed out successfully!");
//                 return;
//             default: // Invalid option
//                 System.out.println("Invalid option. Try again.");
//                 break;
//         }

//         dashboardMenu();
//     }

//     private static void viewBudgets() {
//         // If there are no current budgets, immediately return to main menu
//         List<Budget> currentBudgets = model.getAllBudgets();
//         if (currentBudgets.size() == 0) {
//             System.out.println("You have no budgets set up.");
//             return;
//         }

//         // Print out budgets
//         System.out.println("Your current budgets:");
//         for (Budget budget : currentBudgets) {
//             System.out.println(budget);
//         }
//     }

//     private static void addBudget() {
//         // Print all possible budget categories
//         System.out.println("Budget Categories:");
//         int index = 0;
//         for (Category category : Category.values()) {
//             System.out.println(Integer.toString(index + 1) + ". " + category.toString());
//             index++;
//         }

//         // Prompt user to enter category choice
//         System.out.println("Which category would you like to add a new budget for?");
//         int option = scanner.nextInt();
//         scanner.nextLine();

//         // Validate user's category choice
//         while (option < 1 || option > Category.values().length) {
//             System.out.println(
//                     "Please enter a valid option from 1 to " + Integer.toString(Category.values().length)
//                             + ".");
//             option = scanner.nextInt();
//             scanner.nextLine();
//         }

//         // Check if budget is already set for current category
//         List<Budget> currentBudgets = model.getAllBudgets();
//         List<Category> currentCategories = new ArrayList<>(currentBudgets.keySet());
//         Category selectedCategory = Category.values()[option - 1];

//         // If budget is already set, confirm budget reset
//         if (currentCategories.contains(selectedCategory)) {
//             System.out.println(
//                     "This category already has a budget. Adding a new budget will overwrite the current budget.\n Are you sure you would like to proceed? (Y/N).");
//             String confirmation = scanner.nextLine();

//             // Validate user's answer
//             while (!confirmation.toLowerCase().trim().equals("y")
//                     || !confirmation.toLowerCase().trim().equals("n")) {
//                 System.out.println(
//                         "Please enter a valid response(Y/N).");
//                 confirmation = scanner.nextLine();
//             }

//             // If user says no, immediately return to main menu
//             if (confirmation.toLowerCase().trim().equals("n")) {
//                 System.out.println("Returning to the main menu...");
//                 return;
//             }

//         }

//         // Prompt user to enter limit for new budget
//         System.out.println("Enter a limit for this budget:");
//         double limit = scanner.nextDouble();
//         scanner.nextLine();

//         // Validate user input for limit
//         while (limit < 0) {
//             System.out.println("Limit cannot be negative. Please try again.");
//             limit = scanner.nextDouble();
//             scanner.nextLine();
//         }

//         // Add new budget for user
//         model.setBudget(selectedCategory, limit);
//     }

//     private static void editBudget() {
//         // If there are no current budgets, immeditaely return to main menu
//         Map<Category, Budget> currentBudgets = model.getAllBudgets();
//         if (currentBudgets.size() == 0) {
//             System.out.println("You have no budgets to edit.");
//             return;
//         }

//         // Print user's current budgets
//         System.out.println("Your current budgets:");
//         int index = 0;
//         for (Map.Entry<Category, Budget> budget : currentBudgets.entrySet()) {
//             System.out.println(Integer.toString(index + 1) + ". " + budget.getValue().getCategory() + ": "
//                     + budget.getValue().getTotalSpent() + "/" + budget.getValue().getLimit());
//             index++;
//         }

//         // Prompt user to select a category
//         System.out.println("Which category would you like to modify?");
//         int option = scanner.nextInt();

//         // Validate user's category selection
//         while (option < 1 || option > currentBudgets.size()) {
//             System.out.println(
//                     "Please enter a valid option from 1 to " + Integer.toString(currentBudgets.size()) + ".");
//             option = scanner.nextInt();
//             scanner.nextLine();
//         }
//         List<Category> categories = new ArrayList<>(currentBudgets.keySet());
//         Category selectedCategory = categories.get(option - 1);

//         // Prompt for user to enter a budget limit
//         System.out.println("Enter a limit for this budget:");
//         double limit = scanner.nextDouble();
//         scanner.nextLine();

//         // Validate user input for limit
//         while (limit < 0) {
//             System.out.println("Limit cannot be negative. Please try again.");
//             limit = scanner.nextDouble();
//             scanner.nextLine();
//         }

//         // Set budget for user
//         model.setBudget(selectedCategory, limit);

//     }

//     private static void removeBudget() {
//         // If there are no current budgets, immeditaely return to main menu
//         Map<Category, Budget> currentBudgets = model.getAllBudgets();
//         if (currentBudgets.size() == 0) {
//             System.out.println("You have no budgets to remove.");
//             return;
//         }

//         // Print user's current budgets
//         System.out.println("Your current budgets:");
//         int index = 0;
//         for (Map.Entry<Category, Budget> budget : currentBudgets.entrySet()) {
//             System.out.println(Integer.toString(index + 1) + ". " + budget.getValue().getCategory() + ": "
//                     + budget.getValue().getTotalSpent() + "/" + budget.getValue().getLimit());
//             index++;
//         }

//         // Prompt user to select a category
//         System.out.println("Which category would you like to remove?");
//         int option = scanner.nextInt();

//         // Validate user's category selection
//         while (option < 1 || option > currentBudgets.size()) {
//             System.out.println(
//                     "Please enter a valid option from 1 to " + Integer.toString(currentBudgets.size())
//                             + ".");
//             option = scanner.nextInt();
//             scanner.nextLine();
//         }
//         List<Category> categories = new ArrayList<>(currentBudgets.keySet());
//         Category selectedCategory = categories.get(option - 1);

//         // Prompt user to confirm budget resetting to 0
//         System.out.println(
//                 "You are about to reset your current budget of "
//                         + currentBudgets.get(selectedCategory).getLimit() + " for the category "
//                         + selectedCategory.toString()
//                         + " to $0.\n Are you sure you would like to proceed? (Y/N).");
//         String confirmation = scanner.nextLine();

//         // Validate user input for confirmation
//         while (!confirmation.toLowerCase().trim().equals("y")
//                 || !confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println(
//                     "Please enter a valid response (Y/N).");
//             confirmation = scanner.nextLine();
//         }

//         // If user says no, immediately return to main menu
//         if (confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println("Returning to the main menu...");
//             return;
//         }

//         // Reset budget for the selected category to 0
//         model.setBudget(selectedCategory, 0);

//     }

//     private static void viewExpenses() {
//         List<Expense> currentExpenses = model.getAllExpenses();
//         System.out.println("Your expenses:");
//         if (currentExpenses.size() == 0) {
//             System.out.println("You have no expenses recorded.");
//             return;
//         }

//         for (Expense expense : currentExpenses) {
//             System.out.println(expense);
//         }

//     }

//     private static void addExpense() {
//         // Prompt user to enter transcation date
//         System.out.println("Enter the date of the transaction (YYYY-MM-DD):");
//         String date = scanner.nextLine();

//         // Validate user's input for date
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
//         LocalDate validDate = null;
//         try {
//             validDate = LocalDate.parse(date, formatter);
//             System.out.println("Valid date: " + validDate);
//         } catch (Exception e) {
//             System.out.println("Invalid date format: "
//                     + date);
//             System.out.println("Returning to main menu...");
//             return;
//         }

//         // Prompt user to enter transaction category
//         System.out.println(
//                 "Enter the category of the transaction (e.g., " + String.join(", ", Category.allValues()) + "):");
//         String category = scanner.nextLine();
//         Category validCategory = Category.valueOf(category.toUpperCase());

//         // Validate user's input for category
//         while (validCategory != null) {
//             System.out.println(
//                     "Enter a valid category (e.g., " + String.join(", ", Category.allValues()) + "):");
//             category = scanner.nextLine();
//         }

//         // Prompt user for transcation amount
//         System.out.println("Enter the amount spent:");
//         double amount = scanner.nextDouble();
//         scanner.nextLine();

//         // Validate user input for amount
//         while (amount < 0) {
//             System.out.println("Transcation amount cannot be negative. Please try again.");
//             amount = scanner.nextDouble();
//             scanner.nextLine();
//         }

//         // Prompt user for transcation description
//         System.out.println("Enter a description for the transaction:");
//         String desc = scanner.nextLine();

//         // Add transaction as a user expense
//         model.addExpense(validDate, validCategory, amount, desc);
//     }

//     private static void editExpense() {
//         // If there are no current expenses, immeditaely return to main menu
//         List<Expense> currentExpenses = model.getAllExpenses();
//         if (currentExpenses.size() == 0) {
//             System.out.println("You have no expenses to edit.");
//             return;
//         }

//         // Print user's current expenses
//         System.out.println("Your expenses:");
//         int index = 0;
//         for (Expense expense : currentExpenses) {
//             System.out.println(Integer.toString(index + 1) + ". " + expense);
//             index++;
//         }

//         // Prompt user to select an expense
//         System.out.println("Which expense would you like to edit?");
//         index = scanner.nextInt();

//         // Validate user's expense selection
//         while (index < 1 || index > currentExpenses.size()) {
//             System.out.println(
//                     "Please enter a valid option from 1 to " + Integer.toString(currentExpenses.size()) + ".");
//             index = scanner.nextInt();
//             scanner.nextLine();
//         }

//         System.out.println("Which changes would you like to make to this?");
//         System.out.println("1. Date");
//         System.out.println("2. Category");
//         System.out.println("3. Amount");
//         System.out.println("4. Description");
//         int option = scanner.nextInt();

//         // Validate user's selection for expense modifications
//         while (option < 1 || option > 4) {
//             System.out.println(
//                     "Please enter a valid option from 1 to 4.");
//             option = scanner.nextInt();
//             scanner.nextLine();
//         }
//         LocalDate validDate = currentExpenses.get(index - 1).getLocalDate();
//         Category validCategory = currentExpenses.get(index - 1).getCategory();
//         double amount = currentExpenses.get(index - 1).getAmount();
//         String desc = currentExpenses.get(index - 1).getDescription();

//         if (option == 1) {
//             // Prompt user to enter new transcation date
//             System.out.println("Enter the date of the transaction (YYYY-MM-DD):");
//             String date = scanner.nextLine();

//             // Validate user's input for date
//             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
//             validDate = LocalDate.parse(date, formatter);
//             try {
//                 validDate = LocalDate.parse(date, formatter);
//                 System.out.println("Valid date: " + validDate);
//             } catch (Exception e) {
//                 System.out.println("Invalid date format: "
//                         + date);
//                 System.out.println("Returning to main menu...");
//                 return;
//             }

//             // Set new date for expense
//         } else if (option == 2) {
//             // Prompt user to enter new transaction category
//             System.out.println(
//                     "Enter the category of the transaction (e.g., " + String.join(", ", Category.allValues()) + "):");
//             String category = scanner.nextLine();
//             validCategory = Category.valueOf(category.toUpperCase());

//             // Validate user's input for category
//             while (validCategory != null) {
//                 System.out.println(
//                         "Enter a valid category (e.g., " + String.join(", ", Category.allValues()) + "):");
//                 category = scanner.nextLine();
//             }
//         } else if (option == 3) {
//             // Prompt user for new transcation amount
//             System.out.println("Enter the amount spent:");
//             amount = scanner.nextDouble();
//             scanner.nextLine();

//             // Validate user input for amount
//             while (amount < 0) {
//                 System.out.println("Transcation amount cannot be negative. Please try again.");
//                 amount = scanner.nextDouble();
//                 scanner.nextLine();
//             }
//         } else {
//             // Prompt user for new transcation description
//             System.out.println("Enter a description for the transaction:");
//             desc = scanner.nextLine();
//         }

//         // Confirm transaction changes
//         System.out.println("You entered : " + validDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + ", "
//                 + validCategory.toString() + ", " + Double.toString(amount) + ", " + desc);
//         System.out.println("Are you sure you want to save your changes? This action cannot be undone. (Enter Y/N).");
//         String confirmation = scanner.nextLine();

//         // Validate user's answer
//         while (!confirmation.toLowerCase().trim().equals("y")
//                 || !confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println(
//                     "Please enter a valid response (Y/N).");
//             confirmation = scanner.nextLine();
//         }

//         // If user says no, immediately return to main menu
//         if (confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println("Returning to the main menu...");
//             return;
//         }

//         model.editExpense(index - 1, validDate, validCategory, amount, desc);

//     }

//     private static void removeExpense() {
//         // If there are no current expenses, immeditaely return to main menu
//         List<Expense> currentExpenses = model.getAllExpenses();
//         if (currentExpenses.size() == 0) {
//             System.out.println("You have no expenses to remove.");
//             return;
//         }

//         // Print user's current expenses
//         System.out.println("Your expenses:");
//         int index = 0;
//         for (Expense expense : currentExpenses) {
//             System.out.println(Integer.toString(index + 1) + ". " + expense);
//             index++;
//         }

//         // Prompt user to select an expense
//         System.out.println("Which expense would you like to remove?");
//         index = scanner.nextInt();

//         // Validate user's expense selection
//         while (index < 1 || index > currentExpenses.size()) {
//             System.out.println(
//                     "Please enter a valid option from 1 to " + Integer.toString(currentExpenses.size()) + ".");
//             index = scanner.nextInt();
//             scanner.nextLine();
//         }

//         // Prompt user to confirm deleting expense
//         System.out.println(
//                 "You are about to delete this expense: "
//                         + currentExpenses.get(index - 1).toString()
//                         + "\n Are you sure you would like to proceed? (Y/N).");
//         String confirmation = scanner.nextLine();

//         // Validate user input for confirmation
//         while (!confirmation.toLowerCase().trim().equals("y")
//                 || !confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println(
//                     "Please enter a valid response (Y/N).");
//             confirmation = scanner.nextLine();
//         }

//         // If user says no, immediately return to main menu
//         if (confirmation.toLowerCase().trim().equals("n")) {
//             System.out.println("Returning to the main menu...");
//             return;
//         }

//         // Remove expense
//         model.removeExpense(currentExpenses.get(index - 1));
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

// }
