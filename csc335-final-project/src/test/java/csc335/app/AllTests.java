/** 
 * Description: This class contains unit tests for the models in the `csc335.app.models` package, specifically 
 * testing the functionality of the `Budget` and `Expense` classes. The tests include methods to check 
 * the behavior of budget limits, expense tracking, and budget calculations such as total spending and percentage 
 * exceeded, among other features. The tests ensure that the methods in these classes behave as expected and handle 
 * edge cases such as invalid inputs and exceptions.
 * 
 * Unit testing is performed using JUnit 5 framework.
 * @author Chelina Obiang
*/
package csc335.app;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.BudgetTracker;


public class AllTests{
    
    /*---------------------------------------------------------Budget Tests---------------------------------------------------------------------------------------*/
    @Test 
    public void testgetCategory(){
        Budget myBudget = new Budget(Category.FOOD, 500.25, new ArrayList<>());
        assertEquals(Category.FOOD, myBudget.getCategory());
    }

    @Test
    public void testgetLimit(){
        Budget myBudget = new Budget(Category.UTILITIES, 510.25, new ArrayList<>());
        assertEquals(510.25, myBudget.getLimit());
    }

    @Test
    public void testgetTotalSpentandAddRemoveExpense(){
        List<Budget> budgets = new ArrayList<>();
        for (Category category : Category.values()) {
            budgets.add(new Budget(category, 0, new ArrayList<>()));
        }
        
        User user = new User("sally", "sally@aol.com", "1234", "1234", budgets);
        UserSessionManager.SESSION.setCurrentUser(user);
        User currentUser = UserSessionManager.SESSION.getCurrentUser();
        System.out.print(currentUser.getUsername());
        Budget myBudget = new Budget(Category.FOOD, 500.25, new ArrayList<>());
        BudgetTracker.TRACKER.updateBudget(myBudget);
        
        assertEquals(500.25, myBudget.getLimit());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 12, 1);
        Expense expense = new Expense(calendar, Category.FOOD, 80.71, "Panera Catering");
        myBudget.addExpense(expense);
        assertEquals(80.71, myBudget.getTotalSpent());

        myBudget.removeExpense(expense);
        assertFalse(80.71 == myBudget.getTotalSpent());
        assertFalse(myBudget.isExceeded());
    }

    @Test
    public void testaddAndsetExpense(){
        Budget myBudget = new Budget(Category.UTILITIES, 340, new ArrayList<>());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 12, 1);
        Expense e = new Expense(calendar, Category.UTILITIES, 50.74, "Yes");
        myBudget.addExpense(e);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(e);
        assertEquals(expenses, myBudget.getExpenses());
        List<Expense> elist = new ArrayList<Expense>();
        Expense e1 = new Expense(calendar, Category.UTILITIES, 50.01, "null"); 
        Expense e2 = new Expense(calendar, Category.ENTERTAINMENT, 13, "Yes");
        elist.add(e2); elist.add(e1);
        myBudget.setExpenses(elist);
        assertEquals(50.01, myBudget.getTotalSpent());
    }

    @Test
    public void testsetLimitandThrowException(){
        Budget myBudget = new Budget(Category.ENTERTAINMENT, 76, new ArrayList<>());
        myBudget.setLimit(45.15);
        assertTrue(myBudget.getLimit() != 76);
        assertThrows(IllegalArgumentException.class,() -> myBudget.setLimit(-1.0));
    }

    @Test
    public void testgetPercentageandisExceeded(){
        Budget myBudget = new Budget(Category.HEALTHCARE, 100, new ArrayList<>());
        List<Expense> expenses = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 8, 14);
        Expense expense = new Expense(calendar, Category.HEALTHCARE, 768.64, "Covid Vaccine and Check up");
        calendar.set(2022, 10, 1);
        Expense expense2 = new Expense(calendar, Category.HEALTHCARE, 61.11, "Covid Vaccine and Check up");
        expenses.add(expense2); expenses.add(expense);
        myBudget.addExpenses(expenses);
        assertTrue(myBudget.isExceeded());
        assertEquals(82.98, myBudget.getPercentage());
    }

    @Test
    public void testToString(){
        Budget myBudget = new Budget(Category.TRANSPORTATION, 7500, new ArrayList<>());
        assertEquals("Transportation,7500.0", myBudget.toString());
    }

    @Test
    public void testToStringDetailed(){
        Budget mBudget = new Budget(Category.TRANSPORTATION, 65.50, new ArrayList<>());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 8, 14);
        mBudget.addExpense(new Expense(calendar, Category.TRANSPORTATION, 10, "null"));
        String strBudget = mBudget.toStringDetailed();
        assertEquals("TRANSPORTATION CATEGORY\n" + //
                        "\tBudget: (Category,Limit)\n" + //
                        "\t-> Budget: Transportation,65.5" +
                        "\n\t\t2022-9-14,Transportation,10.0,null", strBudget);
    }

    /*---------------------------------------------------Expense Test--------------------------------------- */

    @Test
    public void testgetDateandDescription(){
        Calendar date = Calendar.getInstance();
        date.set(2023, 1, 1);
        Expense myExpense = new Expense(date, Category.OTHER, 1.25, "Tipping");
        Calendar newDate = Calendar.getInstance();
        newDate.set(2024, 1, 1);
        myExpense.setDate(newDate);
        assertEquals(newDate, myExpense.getCalendarDate());
        myExpense.setDescription("Tip for service");
        assertTrue("Tip for service".equals(myExpense.getDescription()));
    }

    @Test
    public void testgetCategoryandAmount(){
        Calendar date = Calendar.getInstance();
        date.set(2018, 7, 21);
        Expense myExpense = new Expense(date, null, 0, null);
        myExpense.setAmount(345.74);
        myExpense.setCategory(Category.ENTERTAINMENT);
        assertEquals(345.74, myExpense.getAmount());
        assertEquals(Category.ENTERTAINMENT, myExpense.getCategory());
        assertThrows(IllegalArgumentException.class,() -> myExpense.setAmount(-1.0));
    }

    @Test
    public void testStrings(){
        Calendar date = Calendar.getInstance();
        date.set(2021, Calendar.JUNE, 4);
        Expense myExpense = new Expense(null, Category.UTILITIES, 105, "Light Bill");
        myExpense.setDate(date);
        assertEquals("2021-6-4", myExpense.getStringDate());
        assertEquals("2021-6-4,Utilities,105.0,Light Bill", myExpense.toString());
    }
}
