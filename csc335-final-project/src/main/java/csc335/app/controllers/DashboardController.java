package csc335.app.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import csc335.app.Category;
import csc335.app.models.Expense;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
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
        initializeBarChart();
    }

    private void initializeBarChart() {

        // Bar chart will show the last 3 months (including current month)

        // Set current date as end date
        Calendar endDate = Calendar.getInstance(Locale.getDefault());

        // Set start date to the first day of the month (2 months ago)
        Calendar startDate = Calendar.getInstance(Locale.getDefault());
        startDate.set(Calendar.MONTH, endDate.get(Calendar.MONTH - 2));
        startDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMinimum(Calendar.DAY_OF_MONTH));

        // The expenses within the 3-month date range
        List<Expense> expensesInRange = currentUser.getExpensesInRange(startDate, endDate);

        // Creating the legend for the bar chart that explains the colors
        for (Category category : Category.values()) {

            // Initialize a dataset to represent an expense category
            XYChart.Series<String, Double> series = new XYChart.Series<>();
            series.setName(category.toString());

            // Filter the expenses in range by category then filter them by month
            List<Expense> filteredExpenses = currentUser.filterExpenses(expensesInRange, category);
            for (int monthValue = startDate.get(Calendar.MONTH); monthValue <= endDate
                    .get(Calendar.MONTH); monthValue++) {

                // Get the name of the month
                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.set(Calendar.MONTH, monthValue);
                String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

                // Calculate total spent in the month
                double totalSpent = 0.0;
                for (Expense expense : filteredExpenses) {
                    if (monthValue == expense.getCalendarDate().get(Calendar.MONTH)) {
                        totalSpent += expense.getAmount();
                    }
                }

                // Add this month's data to the dataset for this category
                series.getData().add(new XYChart.Data<>(monthName, totalSpent));
            }

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
