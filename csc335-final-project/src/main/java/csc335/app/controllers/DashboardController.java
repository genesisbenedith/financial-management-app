package csc335.app.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * ${file_name}
 * 
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Initializable {

    @FXML
    private BarChart<String, Double> barChart;

    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");
        currentUser = UserSessionManager.INSTANCE.getCurrentUser();
        System.out.println("Current user: " + currentUser.getUsername());
        initializeBarChart();
    }

    public String getStringDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-indexed
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    private void initializeBarChart() {

        // Bar chart will show the category spending summary in the last month (including current month)
        // If the date range button in dropdown is changed, get the new set value then...

        // Get current cal date
        Calendar startCal = CalendarConverter.INSTANCE.getCalendar(1);
        Calendar endCal = CalendarConverter.INSTANCE.getCalendar();
        /*
         * if set to past 30 days, then
         * getCalendar(1)
         * 
         * if set to past 3 months, then 
         * getCalendar(3)
         * 
         * and so on...
         */

        // Print the start and end dates
        System.out.println("Start date: " + getStringDate(startCal));
        System.out.println("End date: " + getStringDate(endCal));

        // The expenses within the last month
        List<Expense> expensesInRange = ExpenseTracker.filterExpensesInRange(startCal, endCal);
        System.out.println("All of the transactions in the last 30 days: ");
        for (Object elem : expensesInRange) {
            System.out.println(elem);
        }

        // Creating the legend for the bar chart that explains the colors
        for (Category category : Category.values()) {

            // Initialize a dataset to represent an expense category
            XYChart.Series<String, Double> series = new XYChart.Series<>();
            series.setName(category.toString());

            // Filter the expenses in range by category
            List<Expense> filteredExpenses = ExpenseTracker.filterExpenses(expensesInRange, category);
            // ExpenseTracker.filterExpenses(expensesInRange, category);
            // Calculate total spent in the month
                double totalSpent = 0.0;
                for (Expense expense : filteredExpenses) {
                    totalSpent += expense.getAmount();
                }
                
                // Add this category's data to the dataset for this month
                series.getData().add(new XYChart.Data<>(category.toString(), totalSpent));

            // Add hover effect to each bar
            for (XYChart.Data<String, Double> data : series.getData()) {
                Node barNode = data.getNode(); // Bar node will be available after adding data to chart
                
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        addHoverEffect(newNode, category);
                    }
            });
        }

            // Add this dataset to the bar chart
            barChart.getData().add(series);
        }
    }

    private void addHoverEffect(Node node, Category category) {
        node.setOnMouseEntered(event -> {
            node.setStyle("-fx-bar-fill: " + category.getHoverColor() + ";"); 
            node.setScaleX(1.1);
            node.setScaleY(1.1);
        });

        node.setOnMouseExited(event -> {
            node.setStyle(category.getDefaultColor()); // Reverts to default color
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

}