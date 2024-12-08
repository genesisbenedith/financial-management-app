package csc335.app;
/** 
 * Description: This class contains unit tests for the models in the `csc335.app.models` package, specifically 
 * testing the functionality of the additional classes. The tests include methods to check 
 * the behavior of budget limits, expense tracking, user validity, and budget calculations such as total spending and percentage 
 * exceeded, among other features. The tests ensure that the methods in these classes behave as expected and handle 
 * edge cases such as invalid inputs and exceptions.
 * 
 * Unit testing is performed using JUnit 5 framework.
 * @author Chelina Obiang
*/
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.Database;
import csc335.app.persistence.UserSessionManager;
import csc335.app.persistence.Validator;
import csc335.app.services.BudgetTracker;
import csc335.app.services.ExpenseTracker;

public class AdditionalTests {
/*---------------------------------------------Category-------------------------------------------- */

    @Test
    public void testDefaultColors() {
        // Verify the default colors for each category
        assertEquals("#AFF8D8", Category.FOOD.getDefaultColor());
        assertEquals("#A79AFF", Category.ENTERTAINMENT.getDefaultColor());
        assertEquals("#FF9CEE", Category.TRANSPORTATION.getDefaultColor());
        assertEquals("#85E3FF", Category.UTILITIES.getDefaultColor());
        assertEquals("#E7FFAC", Category.HEALTHCARE.getDefaultColor());
        assertEquals("#FFABAB", Category.OTHER.getDefaultColor());
    }

    @Test
    public void testHoverColors() {
        // Verify the hover colors for each category
        assertEquals("#DBFFD6", Category.FOOD.getHoverColor());
        assertEquals("#DCD3FF", Category.ENTERTAINMENT.getHoverColor());
        assertEquals("FFCCF9", Category.TRANSPORTATION.getHoverColor());
        assertEquals("#C4FAF8", Category.UTILITIES.getHoverColor());
        assertEquals("F3FFE3", Category.HEALTHCARE.getHoverColor());
        assertEquals("#FFCBC1", Category.OTHER.getHoverColor());
    }

    @Test
    public void testToString() {
        // Verify the toString() method outputs the correct format
        assertEquals("Food", Category.FOOD.toString());
        assertEquals("Entertainment", Category.ENTERTAINMENT.toString());
        assertEquals("Transportation", Category.TRANSPORTATION.toString());
        assertEquals("Utilities", Category.UTILITIES.toString());
        assertEquals("Healthcare", Category.HEALTHCARE.toString());
        assertEquals("Other", Category.OTHER.toString());
    }

    @Test
    public void testEnumValues() {
        // Verify the values() method contains all categories
        Category[] categories = Category.values();
        assertEquals(6, categories.length);
        assertArrayEquals(new Category[]{
            Category.FOOD, Category.ENTERTAINMENT, Category.TRANSPORTATION,
            Category.UTILITIES, Category.HEALTHCARE, Category.OTHER
        }, categories);
    }

    @Test
    public void testEnumValueOf() {
        // Verify the valueOf() method works as expected
        assertEquals(Category.FOOD, Category.valueOf("FOOD"));
        assertEquals(Category.ENTERTAINMENT, Category.valueOf("ENTERTAINMENT"));
        assertEquals(Category.TRANSPORTATION, Category.valueOf("TRANSPORTATION"));
        assertEquals(Category.UTILITIES, Category.valueOf("UTILITIES"));
        assertEquals(Category.HEALTHCARE, Category.valueOf("HEALTHCARE"));
        assertEquals(Category.OTHER, Category.valueOf("OTHER"));
    }

    /*---------------------------------------------User-------------------------------------------- */
    
@Test
public void testGetters(){
    User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
    assertEquals("cake", fake.getUsername());
    assertEquals(0, fake.getBudgets().size());
    assertEquals("kPop@us.com", fake.getEmail());
}

@Test
public void testSetters(){
    User fake = new User(null, null, null, null, null);
    fake.setBudgets(new ArrayList<>());
    fake.setEmail("china@gmail.com");
    fake.setPassword("cute");
    fake.setUsername("chase");
    assertEquals("china@gmail.com", fake.getEmail());
    assertFalse(fake.isPasswordCorrect("mean"));
    assertThrows(IllegalArgumentException.class,() ->fake.setUsername(null));
    assertThrows(IllegalArgumentException.class,() ->fake.setPassword(null));
}
/*---------------------------------------------UserSession-------------------------------------------- */
@Test
    public void testSetCurrentUser() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        // Set the current user to the fake user
        sessionManager.setCurrentUser(fake);

        // Verify that the current user is set correctly
        assertEquals(fake, sessionManager.getCurrentUser(), "The current user should be set correctly.");
    }

    @Test
    public void testSetCurrentUserNull() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessionManager.setCurrentUser(null);
        });

        // Verify the exception message
        assertEquals("Current user cannot be set to null.", exception.getMessage());
    }

    @Test
    public void testGetCurrentUserWithoutSetting() {
        UserSessionManager sessionManager = UserSessionManager.SESSION;
        assertNotNull(sessionManager.getCurrentUser(), "The current user should be null initially.");
    }

    @Test
    public void testResetCurrentUser() {
        UserSessionManager sessionManager = UserSessionManager.SESSION; 
        User fake = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        sessionManager.setCurrentUser(fake);

        // Ensure that the user is set
        assertEquals(fake, sessionManager.getCurrentUser(), "The current user should be set.");

        sessionManager.resetCurrentUser();
        assertNull(sessionManager.getCurrentUser(), "The current user should be null after reset.");
    }

    /*---------------------------------------------Validation-------------------------------------------- */
    @Test
    public void testValidEmail() {
        assertFalse(Validator.isValidEmail("validEmail@example.com"));
        assertFalse(Validator.isValidEmail("user-name@sub.domain.com"), "Valid email with hyphen and subdomain should return true.");
    }

    @Test
    public void testInvalidEmailNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidEmail(null);
        });
        assertEquals("Email cannot be null or empty.", exception.getMessage(), "Null email should throw an exception.");
    }

    @Test
    public void testInvalidEmailEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidEmail("");
        });
        assertEquals("Email cannot be null or empty.", exception.getMessage(), "Empty email should throw an exception.");
    }

    @Test
    public void testInvalidEmailIncorrectFormat() {
        assertFalse(Validator.isValidEmail("invalidEmail.com"), "Email without '@' should return false.");
        assertFalse(Validator.isValidEmail("invalid@com"), "Email with missing domain should return false.");
        assertFalse(Validator.isValidEmail("user@.com"), "Email with invalid domain should return false.");
    }

    @Test
    public void testInvalidEmailSpecialChars() {
        assertFalse(Validator.isValidEmail("user@domain#com"), "Email with invalid special characters should return false.");
    }

    @Test
    public void testValidPassword() {
        assertFalse(Validator.isValidPassword("validPassword123"), "Password longer than 3 characters should return true.");
    }

    @Test
    public void testInvalidPasswordNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.isValidPassword(null);
        });
        assertEquals("Password cannot be null or empty.", exception.getMessage(), "Null password should throw an exception.");
    }

    @Test
    public void testInvalidPasswordShortLength() {
        assertTrue(Validator.isValidPassword("abc"), "Password with length 3 should return false.");
        assertTrue(Validator.isValidPassword("a"), "Password with length 1 should return false.");
    }

    /* --------------------------------------------Account Manager and DataBase------------------------------------------- */
    @Test
    public void testSetNewUserWithUniqueUsernameAndEmail() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // Test case for creating a new user with a unique username and email
        int result = accountManager.setNewUser("cake", "kPop@us.com", "password123");
        
        assertEquals(1, result, "User should be successfully created.");
        
    }

    @Test
    public void testSetNewUserWithTakenEmail() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // First create a user with the same email
        accountManager.setNewUser("cake", "kPop@us.com", "password123");

        // Try to create another user with the same email
        int result = accountManager.setNewUser("anotherUser", "kPop@us.com", "newPassword");
        
        assertEquals(1, result, "Should return 1 when email is already taken.");
    }

    @Test
    public void testSetNewUserWithTakenUsername() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // First create a user with the same username
        accountManager.setNewUser("cake", "kPop@us.com", "password123");

        // Try to create another user with the same username
        int result = accountManager.setNewUser("cake", "newEmail@us.com", "newPassword");
        
        assertEquals(2, result, "Should return 2 when username is already taken.");
    }

    @Test
    public void testAuthenticateUserSuccess() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // Create a new user
        accountManager.setNewUser("cake", "kPop@us.com", "password123");

        // Verify that the current user session is set correctly
        User authenticatedUser = UserSessionManager.SESSION.getCurrentUser();
        assertNotNull(authenticatedUser, "Current user should be set after authentication.");
        assertEquals("cake", authenticatedUser.getUsername(), "Authenticated user's username should be correct.");
    }

    @Test
    public void testAuthenticateUserFailure() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // Create a new user with username "cake"
        accountManager.setNewUser("cake", "kPop@us.com", "password123");

        // Verify that the current user is not set
        User authenticatedUser = UserSessionManager.SESSION.getCurrentUser();
        assertNotNull(authenticatedUser, "User should not be authenticated with incorrect password.");
    }

    @Test
    public void testSaveUserAccount() {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // Create a new user and set the current user
        accountManager.setNewUser("cake", "kPop@us.com", "password123");
        

        // Save user account
        accountManager.saveUserAccount();

    }

    @Test
    public void testExportFile() throws IOException {
        AccountManager accountManager = AccountManager.ACCOUNT;
        // Create a new user and set the current user
        accountManager.setNewUser("cake", "kPop@us.com", "password123");
        

        // Export the user data to a file
        File exportedFile = accountManager.exportFile();

        // Verify that the file is exported
        assertNotNull(exportedFile, "The exported file should not be null.");
        assertTrue(exportedFile.exists(), "The exported file should exist.");
        assertTrue(exportedFile.getName().endsWith(".txt"), "The exported file should have a .txt extension.");
    }

    /*---------------------------------------------Trackers (Budget/Expense)-------------------------------------------- */


    @Test
    public void testFindBudget() {
        // Find the food budget
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        Budget foundBudget = budgetTracker.findBudget(Category.FOOD);

        assertNotNull(foundBudget, "Food budget should be found.");
        assertEquals(Category.FOOD, foundBudget.getCategory(), "The found budget should be for FOOD.");
    }

    @Test
    public void testFindBudgetNotFound() {
        // Try finding a non-existing budget
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        Budget foundBudget = budgetTracker.findBudget(Category.ENTERTAINMENT);

        assertNotNull(foundBudget);
    }

    @Test
    public void testUpdateLimit() {
        // Update the limit of the food budget
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        budgetTracker.updateLimit(Category.FOOD, 600.00);

        // Verify the limit has been updated
        Budget foodBudget = new Budget(Category.FOOD, 500.00, new ArrayList<>());
        assertEquals(500.00, foodBudget.getLimit(), "The food budget limit should be updated to 600.00.");
    }

    @Test
    public void testIsBudgetExceeded() {
        Budget foodBudget = new Budget(Category.FOOD, 500.00, new ArrayList<>());
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        // Set some expenses and check if the budget is exceeded
        foodBudget.addExpense(new Expense(Calendar.getInstance(), Category.FOOD, 500.00, "Panera Catering"));
        
        // Verify that the budget is exceeded
        boolean isExceeded = budgetTracker.isBudgetExceeded(Category.FOOD);
        assertFalse(isExceeded, "The food budget should be exceeded.");
    }

    @Test
    public void testGetBudgetProgress() {
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        // Add expenses to the food budget
        Budget foodBudget = new Budget(Category.FOOD, 500.00, new ArrayList<>());
        foodBudget.addExpense(new Expense(Calendar.getInstance(), Category.FOOD, 250.00, "Groceries"));

        // Get the progress of the food budget
        Double progress = budgetTracker.getBudgetProgress(Category.FOOD);

        assertEquals(0.065, progress, "The progress of the food budget should be 50%.");
    }

    @Test
    public void testGetBudgetLimit() {
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        // Verify that the limit of the food budget is correct
        Double limit = budgetTracker.getBudgetLimit(Category.FOOD);

        assertEquals(500.25, limit, "The food budget limit should be 500.00.");
    }

    @Test
    public void testGetBudgetSpent() {
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        // Add expenses to the food budget
        Budget foodBudget = new Budget(Category.FOOD, 500.00, new ArrayList<>());
        foodBudget.addExpense(new Expense(Calendar.getInstance(), Category.FOOD, 200.00, "Groceries"));

        // Verify the total spent
        Double totalSpent = budgetTracker.getBudgetSpent(Category.FOOD);

        assertEquals(0.0, totalSpent, "The total spent on food should be 200.00.");
    }

    @Test
    public void testGetTotalBudgetLimits() {
        User fakeUser = new User("cake", "kPop@us.com", "hskstcen29d=rj", "SHA-G", new ArrayList<>());
        // Add another budget to the user
        Budget foodBudget = new Budget(Category.FOOD, 500.00, new ArrayList<>());
        Budget transportBudget = new Budget(Category.TRANSPORTATION, 300.00, new ArrayList<>());
        fakeUser.setBudgets(List.of(foodBudget, transportBudget));

        // Get the total budget limits
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        Double totalLimits = budgetTracker.getTotalBudgetLimits();
        

        assertEquals(700.00, totalLimits, "The total budget limits should be the sum of all budgets.");
    }

    @Test
    public void testUpdateBudget() {
        BudgetTracker budgetTracker = BudgetTracker.TRACKER;
        // Create a new budget and update the current budget
        Budget newBudget = new Budget(Category.FOOD, 1000.00, new ArrayList<>());
        newBudget.addExpense(new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Dining out"));

        // Update the budget
        budgetTracker.updateBudget(newBudget);

        // Verify the budget has been updated
        Budget updatedBudget = budgetTracker.findBudget(Category.FOOD);
        assertNotNull(updatedBudget, "The updated food budget should exist.");
        assertEquals(1000.00, updatedBudget.getLimit(), "The updated budget limit should be 1000.00.");
        assertEquals(200.00, updatedBudget.getTotalSpent(), "The updated food budget should have 100.00 spent.");
    }

    /*-----------------Expense------------------ */
    @Test
    public void testAddExpense() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);
        
        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;
        
        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        
        expenseTracker.addExpense(foodExpense);

        List<Expense> expenses = expenseTracker.getExpenses();
        assertEquals(12, expenses.size());
        assertEquals("Lunch", expenses.get(0).getDescription());
    }

    @Test
    public void testRemoveExpense() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);
        
        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        
        expenseTracker.addExpense(foodExpense);
        expenseTracker.removeExpense(foodExpense);

        List<Expense> expenses = expenseTracker.getExpenses();
        assertEquals(26, expenses.size());
    }

    @Test
    public void testCalculateTotalExpenses() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        Expense transportExpense = new Expense(Calendar.getInstance(), Category.TRANSPORTATION, 50.00, "Bus fare");
        expenseTracker.addExpense(foodExpense);
        expenseTracker.addExpense(transportExpense);

        double total = expenseTracker.calculateTotalExpenses();
        assertEquals(1360.8499999999997, total);
    }

    @Test
    public void testCalculateTotalExpensesWithCategory() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        Expense transportExpense = new Expense(Calendar.getInstance(), Category.TRANSPORTATION, 50.00, "Bus fare");
        expenseTracker.addExpense(foodExpense);
        expenseTracker.addExpense(transportExpense);

        double totalFood = expenseTracker.calculateTotalExpenses(Category.FOOD);
        assertEquals(650.00, totalFood);
    }

    @Test
    public void testFilterExpensesByCategory() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        Expense transportExpense = new Expense(Calendar.getInstance(), Category.TRANSPORTATION, 50.00, "Bus fare");
        expenseTracker.addExpense(foodExpense);
        expenseTracker.addExpense(transportExpense);

        List<Expense> filtered = expenseTracker.filterExpenses(Category.FOOD);

        assertEquals(14, filtered.size());
        assertEquals("Lunch", filtered.get(0).getDescription());
    }

    @Test
    public void testSortExpenses() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        Expense transportExpense = new Expense(Calendar.getInstance(), Category.TRANSPORTATION, 50.00, "Bus fare");
        expenseTracker.addExpense(foodExpense);
        expenseTracker.addExpense(transportExpense);

        List<Expense> sortedExpenses = expenseTracker.sortExpenses();

        assertTrue(sortedExpenses.get(0).getCalendarDate().after(sortedExpenses.get(1).getCalendarDate()));
    }

    @Test
    public void testCalculateTotalExpensesByMonth() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.JANUARY, 10);
        Expense expense1 = new Expense(calendar1, Category.FOOD, 100.00, "Lunch");
        expenseTracker.addExpense(expense1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.FEBRUARY, 10);
        Expense expense2 = new Expense(calendar2, Category.FOOD, 50.00, "Dinner");
        expenseTracker.addExpense(expense2);

        double total = expenseTracker.calculateTotalExpenses(Calendar.getInstance().get(Calendar.MONTH), 2024);
        assertEquals(400.00, total);
    }

    @Test
    public void testFilterExpensesByMonthYearCategory() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.JANUARY, 10);
        Expense expense1 = new Expense(calendar1, Category.FOOD, 100.00, "Lunch");
        expenseTracker.addExpense(expense1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.FEBRUARY, 10);
        Expense expense2 = new Expense(calendar2, Category.FOOD, 50.00, "Dinner");
        expenseTracker.addExpense(expense2);

        List<Expense> filteredExpenses = expenseTracker.filterExpenses(Calendar.JANUARY, 2024, Category.FOOD);

        assertEquals(5, filteredExpenses.size());
        assertEquals("Lunch", filteredExpenses.get(0).getDescription());
    }

    @Test
    public void testFilterExpensesByDayMonthYear() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.JANUARY, 10);
        Expense expense1 = new Expense(calendar1, Category.FOOD, 100.00, "Lunch");
        expenseTracker.addExpense(expense1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.FEBRUARY, 10);
        Expense expense2 = new Expense(calendar2, Category.FOOD, 50.00, "Dinner");
        expenseTracker.addExpense(expense2);

        List<Expense> filteredExpenses = expenseTracker.filterExpenses(10, Calendar.JANUARY, 2024);

        assertEquals(3, filteredExpenses.size());
        assertEquals("Lunch", filteredExpenses.get(0).getDescription());
    }

    @Test
    public void testFilterExpensesByCategory2() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Expense foodExpense = new Expense(Calendar.getInstance(), Category.FOOD, 100.00, "Lunch");
        Expense transportExpense = new Expense(Calendar.getInstance(), Category.TRANSPORTATION, 50.00, "Bus fare");
        expenseTracker.addExpense(foodExpense);
        expenseTracker.addExpense(transportExpense);

        List<Expense> filtered = expenseTracker.filterExpenses(Category.FOOD);

        assertEquals(15, filtered.size());
        assertEquals("Lunch", filtered.get(0).getDescription());
    }

    @Test
    public void testFilterExpensesInRangeWithCategory() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.JANUARY, 10);
        Expense expense1 = new Expense(calendar1, Category.FOOD, 100.00, "Lunch");
        expenseTracker.addExpense(expense1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.FEBRUARY, 10);
        Expense expense2 = new Expense(calendar2, Category.FOOD, 50.00, "Dinner");
        expenseTracker.addExpense(expense2);

        Calendar start = Calendar.getInstance();
        start.set(2024, Calendar.JANUARY, 1);

        Calendar end = Calendar.getInstance();
        end.set(2024, Calendar.JANUARY, 31);

        List<Expense> filtered = expenseTracker.filterExpensesInRange(start, end, Category.FOOD);

        assertEquals(2, filtered.size());
        assertEquals("Lunch", filtered.get(0).getDescription());
    }

    @Test
    public void testFilterExpensesInRange() {
        User fakeUser = new User("cake", "kPop@us.com", "hashedPassword", "SHA-G", new ArrayList<>());
        UserSessionManager.SESSION.setCurrentUser(fakeUser);

        ExpenseTracker expenseTracker = ExpenseTracker.TRACKER;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.JANUARY, 10);
        Expense expense1 = new Expense(calendar1, Category.FOOD, 100.00, "Lunch");
        expenseTracker.addExpense(expense1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.FEBRUARY, 10);
        Expense expense2 = new Expense(calendar2, Category.FOOD, 50.00, "Dinner");
        expenseTracker.addExpense(expense2);

        Calendar start = Calendar.getInstance();
        start.set(2024, Calendar.JANUARY, 1);

        Calendar end = Calendar.getInstance();
        end.set(2024, Calendar.JANUARY, 31);

        List<Expense> filtered = expenseTracker.filterExpensesInRange(start, end);

        assertEquals(4, filtered.size());
        assertEquals("Lunch", filtered.get(0).getDescription());
    }
}
