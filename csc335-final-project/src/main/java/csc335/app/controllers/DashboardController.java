package csc335.app.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import csc335.app.CalendarConverter;
import csc335.app.Category;
import csc335.app.models.Expense;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * ${file_name}
 * 
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

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

        // Bar chart will show the category spending summary in the last month
        // (including current month)
        // If the date range button in dropdown is changed, get the new set value
        // then...

        // Get current cal date
        Calendar endCal = CalendarConverter.INSTANCE.getCalendar();
        Calendar startCal = CalendarConverter.INSTANCE.getCalendar(1);

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
        System.out.println("All of the transactions for the last 3 calendar months: ");
        for (Object elem : expensesInRange) {
            System.out.println(elem);
        }

        // Creating the legend for the bar chart that explains the colors
        for (Category category : Category.values()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(category.toString());

            double total = ExpenseTracker
                    .calculateTotalExpenses(ExpenseTracker.filterExpenses(expensesInRange, category));
            series.getData().add(new XYChart.Data<>(category.toString(), total));

            // Add hover effect and tooltip
            Platform.runLater(() -> {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    addHoverEffect(data.getNode(), category);
                    Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + ":\n$" + data.getYValue()));
                }
            });

            barChart.getData().add(series);
        }

        barChart.setBarGap(0); // Sets the gap between bars in the same category
        barChart.setCategoryGap(0); // Sets the gap between different categories (months)
       

        // Add click listeners for debugging (optional)
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            System.out.println(series.getData().size() + "in " + series.getName());
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                // barNode.setScaleX(0.5);
                barNode.setStyle(Category.valueOf(series.getName().toUpperCase()).getDefaultColor());
                barNode.setOnMouseEntered(
                        event -> addHoverEffect(barNode, Category.valueOf(series.getName().toUpperCase())));
                barNode.setOnMousePressed(event -> System.out
                        .println("Clicked: " + series.getName() + " - " + data.getXValue() + ": " + data.getYValue()));
            }
        }
    }

    private void addHoverEffect(Node node, Category category) {
        node.setOnMouseEntered(event -> {
            node.setStyle("-fx-bar-fill: " + category.getHoverColor() + ";");
        });

        node.setOnMouseExited(event -> {
            node.setStyle(category.getDefaultColor()); // Reverts to default color
        });
    }

}
