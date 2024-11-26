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

// public class TextUIModel {

//     static Scanner scanner = new Scanner(System.in);
//     private static User user;
//     private static final String DATA_DIRECTORY = "data/users";

//     public TextUIModel(User u) throws IOException {
//         user = u;
//     }

//     protected static void viewBudgets() {
//         System.out.println("Your current budgets:");
//         if (user != null && user.getBudgets().size() > 0) {
//             user.getBudgets().forEach((category, budget) -> System.out
//                     .println(category + ": " + budget.getSpent() + "/" + budget.getLimit()));
//         } else {
//             System.out.println("You have no budgets set up.");
//         }
//         System.out.println("--------------------------------------------");
//     }

//     protected static void addBudget() {
//         System.out.println("Enter a category for the budget (e.g., GROCERIES):");
//         String categoryName = scanner.nextLine();

//         System.out.println("Enter a limit for this budget:");
//         double limit = scanner.nextDouble();
//         scanner.nextLine(); // Consume newline after double input

//         try {
//             Category category = Category.valueOf(categoryName.toUpperCase());
//             user.setBudget(category, limit);
//             UserAuth.updateUserFile(user);
//             System.out.println("Budget added successfully!");
//         } catch (IllegalArgumentException e) {
//             System.out.println("Invalid category. Please try again.");
//         }
//         System.out.println("--------------------------------------------");
//     }

//     protected static void addExpense() {
//         System.out.println("Enter the date of the transaction (YYYY-MM-DD):");
//         String date = scanner.nextLine();

//         System.out.println("Enter the category of the transaction (e.g., GROCERIES):");
//         String categoryName = scanner.nextLine();

//         System.out.println("Enter the amount spent:");
//         double amount = scanner.nextDouble();
//         scanner.nextLine();

//         System.out.println("Enter a description for the transaction:");
//         String description = scanner.nextLine();

//         try {
//             Category category = Category.valueOf(categoryName.toUpperCase());
//             user.addExpense(new Expense(date, category, amount, description));
//             UserAuth.updateUserFile(user);
//             // Check if the budget for the category exists
//             if (user.getBudgets().containsKey(category)) {
//                 // Add the expense amount to the budget
//                 Budget budget = user.getBudgets().get(category);
//                 budget.addExpense(amount);

//                 // Update the user file after modifying the budget
//                 UserAuth.updateUserFile(user);
//             } else {
//                 System.out.println("No budget found for the category '" + category
//                         + "'. Expense recorded without budget tracking.");
//             }

//             System.out.println("Transaction added successfully!");
//         } catch (IllegalArgumentException e) {
//             System.out.println("Invalid category. Please try again.");
//         }
//         System.out.println("--------------------------------------------");
//     }

//     protected static void viewExpenses() {
//         System.out.println("Your expenses:");
//         if (user != null && user.getExpenses().size() > 0) {
//             for (Expense expense : user.getExpenses()) {
//                 System.out.println(expense);
//             }
//         } else {
//             System.out.println("You have no expenses recorded.");
//         }
//         System.out.println("--------------------------------------------");
//     }

//     protected static void updateUserFile(User user) {
//         File userFile = new File(DATA_DIRECTORY, user.getUsername() + ".txt");
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
//             writer.write("Username: " + user.getUsername());
//             writer.newLine();
//             writer.write("Email: " + user.getEmail());
//             writer.newLine();
//             writer.write("Password: " + user.getPassword());
//             writer.newLine();

//             writer.write("Budgets:");
//             writer.newLine();
//             for (Map.Entry<Category, Budget> entry : user.getBudgets().entrySet()) {
//                 writer.write(entry.getKey() + ", " + entry.getValue().getLimit());
//                 writer.newLine();
//             }

//             writer.write("Transactions:");
//             writer.newLine();
//             for (Expense expense : user.getExpenses()) {
//                 writer.write(expense.getDate() + ", " + expense.getCategory() + ", " + expense.getAmount() + ", "
//                         + expense.getDescription());
//                 writer.newLine();
//             }

//         } catch (IOException e) {
//             e.printStackTrace();
//             System.out.println("Error: Unable to update the user file.");
//         }
//     }

// }
