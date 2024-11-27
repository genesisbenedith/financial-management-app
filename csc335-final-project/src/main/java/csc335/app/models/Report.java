package csc335.app.models;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Report {
    private LocalDate reportDate;
    private List<Expense> monthExpenses;
    private String reportFilePath;

    /* ------------------------------ Constructor ------------------------------ */

    public Report(String username, String date) {
        this.reportFilePath = "data/exports" + username + "_" + Integer.toString(reportDate.getMonthValue()) + "_"
                + Integer.toString(reportDate.getYear()) + ".txt";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
        try {
            this.reportDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date format: "
                    + date);
            return;
        }
    }

    /* ------------------------------ Getters ------------------------------ */

    public int getMonth() {
        return reportDate.getMonthValue();
    }

    public int getYear() {
        return reportDate.getYear();
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public double getTotalSpent() {
        double totalSpent = 0.0;
        for (Expense expense : monthExpenses) {
            totalSpent += expense.getAmount();
        }
        return totalSpent;
    }

    // public List<Expense> getMonthExpenses() {
    //     return Collections.unmodifiableList(expenses);
    // }

    public String getMonthlyReportPath() {
        return this.reportFilePath;
    }

     /* ------------------------------ Setters ------------------------------ */

    public void setMonthExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            // Check the date of the expense
            if (expense.getLocalDate().getMonthValue() == getMonth()
                    && expense.getLocalDate().getYear() == getYear()) {
                this.monthExpenses.add(expense);
            }
        }
    }

    public void getExpensesByCategory() {

    }

    public void setLocalDate() {

    }


    public void generateMonthlyReport() {
        
    }

    @Override
    public String toString() {
        return "Monthly Reports";
    }
}
