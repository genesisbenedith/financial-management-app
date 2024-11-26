// package csc335.app;

// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.Scanner;

// public class TextUI {

//     static Scanner scanner = new Scanner(System.in);
//     private static TextUIController controller;
//     private static final String DATA_DIRECTORY = "data/users";

//     public static void main(String[] args) throws IOException {

//         System.out.println("Welcome to Finantra!");
//         System.out.println("--------------------------------------------");
//         System.out.println("1. Sign In or 2. Sign Up?");
//         int option = scanner.nextInt();
//         scanner.nextLine(); 

//         if (option == 1) {
//             signIn();
//         } else if (option == 2) {
//             signUp();
//         } else {
//             System.out.println("Invalid option. Exiting...");
//         }

//         scanner.close();
//     }

//     private static void signIn() throws IOException {
//         System.out.println("Enter Username:");
//         String username = scanner.nextLine();

//         System.out.println("Enter Password:");
//         String password = scanner.nextLine();

//         boolean isAuthenticated = UserAuth.authenticateUser(username, password);

//         if (isAuthenticated) {
//             System.out.println("User authenticated successfully.");
//             controller = new TextUIController(username);
//             controller.dashboardMenu(username, scanner);
//         } else {
//             System.out.println("Invalid username or password. Please try again.");
//             System.out.println("--------------------------------------------");
//             main(null); // Restart the main menu
//         }
//     }

//     private static void signUp() throws IOException {
//         System.out.println("Enter a new Username:");
//         String username = scanner.nextLine();

//         System.out.println("Enter a new Password:");
//         String password = scanner.nextLine();

//         System.out.println("Confirm your Password:");
//         String confirmPassword = scanner.nextLine();

//         if (!password.equals(confirmPassword)) {
//             System.out.println("Passwords do not match. Please try again.");
//             signUp();
//             return;
//         }

//         boolean userCreated = registerUser(username, password);

//         if (userCreated) {
//             System.out.println("Account created successfully! Please sign in.");
//             System.out.println("--------------------------------------------");
//             signIn();
//         } else {
//             System.out.println("Username already exists. Please try a different username.");
//             signUp();
//         }
//     }

//     public static boolean registerUser(String username, String password) {
//         // Ensure the data directory exists
//         File directory = new File(DATA_DIRECTORY);
//         if (!directory.exists()) {
//             directory.mkdirs();
//         }

//         // Check if a user with the same username already exists
//         File userFile = new File(DATA_DIRECTORY, username + ".txt");
//         if (userFile.exists()) {
//             return false; // User already exists
//         }

//         // Create a new user file and write their data
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
//             writer.write("Username: " + username);
//             writer.newLine();
//             writer.write("Password: " + password);
//             writer.newLine();
//             // Add additional default data if needed, e.g., empty budgets or transactions
//             writer.write("Budgets: ");
//             writer.newLine();
//             writer.write("Transactions: ");
//             writer.newLine();
//             return true; // Registration successful
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return false; // Registration failed
//     }

    
// }
