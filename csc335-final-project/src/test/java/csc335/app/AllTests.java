/** 
 * Author(s): Chelina Obiang
 * Desctription: Tests the java files inside the models folder (Budget.java and Expense.java)
*/
package csc335.app;


import java.util.Calendar;
import java.util.List;

import csc335.app.models.Budget;
import csc335.app.models.Expense;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class AllTests{
    
    /*---------------------------------------------------------Budget Tests---------------------------------------------------------------------------------------*/
    @Test 
    public void testgetCategory(){
        Budget myBudget = new Budget(Category.FOOD, 500.25);
        assertEquals(Category.FOOD, myBudget.getCategory());
    }

    @Test
    public void testgetLimit(){
        Budget myBudget = new Budget(Category.UTILITIES, 510.25);
        assertEquals(510.25, myBudget.getLimit());
    }

    @Test
    public void testgetTotalSpentandAddRemoveExpense(){
        Budget myBudget = new Budget(Category.FOOD, 500.25);
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
    public void testsetLimitandThrowException(){
        Budget myBudget = new Budget(Category.ENTERTAINMENT, 76);
        myBudget.setLimit(45.15);
        assertTrue(myBudget.getLimit() != 76);
        assertThrows(IllegalArgumentException.class,() -> myBudget.setLimit(-1.0));
    }

    @Test
    public void testgetPercentageandisExceeded(){
        Budget myBudget = new Budget(Category.HEALTHCARE, 1000);
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
        Budget myBudget = new Budget(Category.TRANSPORTATION, 7500);
        assertEquals("Transportation,7500.0", myBudget.toString());
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
