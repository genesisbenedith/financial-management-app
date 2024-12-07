package csc335.app.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.AvatarView;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * DashboardController is responsible for managing the user dashboard view.
 * It connects the graphical interface with the logic to display user 
 * information, expense data, and charts like bar and pie charts for 
 * visualizing financial information.
 * 
 * This class handles user-related details like the profile avatar, email, and 
 * username while also preparing data visualizations for expenses categorized 
 * by time periods or spending categories.
 * File: DashboardController.java
 * @author Genesis Benedith
 */

public class DashboardController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private MFXListView<Expense> expenseListView;

    @FXML
    private AvatarView userAvatar;

    @FXML
    private Label username;

    @FXML
    private Label email;

    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        System.out.println("Current user: " + currentUser.getUsername());

        // expenseListView = new MFXListView<>((ObservableList<Expense>) ExpenseTracker.TRACKER.getExpenses());

        expenseListView = new MFXListView<>();
        initializeUserInfo();
        initializeBarChart();
        initializePieChart();
    }

    /**
     * Loads the user's profile information like avatar, username, and email.
     */
    public void initializeUserInfo() {
        userAvatar = currentUser.getAvatar();
        username.setText(currentUser.getUsername());
        email.setText(currentUser.getEmail());

    }

    /**
     * Sets up the pie chart to show expenses divided into categories.
     * Each slice represents a category, and hovering shows detailed 
     * information about spending in that category.
     */
    private void initializePieChart() {
        for (Category category : Category.values()) {
            PieChart.Data slice = new Data(category.toString(),
                    ExpenseTracker.TRACKER.calculateTotalExpenses(category));
            pieChart.getData().add(slice);
        }

        for (Data pieSlice : pieChart.getData()) {
            Node pieNode = pieSlice.getNode();
            String nodeStyle = "-fx-pie-color: ";
            pieNode.setStyle(nodeStyle + Category.valueOf(pieSlice.getName().toUpperCase()).getDefaultColor());
            Tooltip.install(pieNode, new Tooltip(pieSlice.getName() + ":\n$" + pieSlice.getPieValue()));
            pieNode.hoverProperty().addListener((_, _, _) -> {
                addHoverEffect(nodeStyle, pieNode, Category.valueOf(pieSlice.getName().toUpperCase()));
            });

            pieNode.setOnMouseClicked(
                    _ -> System.out.println("Clicked: " + pieSlice.getName() + " -> " + pieSlice.getPieValue()));

        }
        pieChart.applyCss(); // Apply CSS styles to the chart
        pieChart.layout(); // Force layout to ensure legend nodes are created
        pieChart.setLegendVisible(true); // Ensure the legend is enabled

        System.out.println(pieChart.lookupAll(".chart-legend-item-symbol").size());
        pieChart.lookupAll(".chart-legend-item-symbol").forEach(node -> {
            String label = ((PieChart.Data) node.getUserData()).getName();
            System.out.println(label);
            if (Category.valueOf(label.toUpperCase()) != null) {
                node.setStyle("-fx-background-color: " + Category.valueOf(label.toUpperCase()).getDefaultColor() + ";");
            }
        });

    }


    private void profileView() {
        Button button = new Button("Choose an image: ");
        button.setOnMouseClicked(
                _ -> {
                    String imagePath = View.CHOOSER.showFileChooser();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        try {
                            AccountManager.ACCOUNT.changeAvatarView(imagePath);
                        } catch (Exception e) {
                            System.err.print("Unable to save user avatar.");
                            View.ALERT.showAlert(AlertType.ERROR, "Error", "Unable to save user avatar.");
                        }
                    } else {
                        // [ ] Do something
                    }
                });
    }

    /**
     * Sets up the bar chart to display category-wise spending summaries.
     * The bar chart shows spending data for a specific time range, 
     * such as the past month or quarter.
     */
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
        System.out.println("Start date: " + CalendarConverter.INSTANCE.getStringDate(startCal));
        System.out.println("End date: " + CalendarConverter.INSTANCE.getStringDate(endCal));

        // The expenses within the last month
        List<Expense> expensesInRange = ExpenseTracker.TRACKER.filterExpensesInRange(startCal, endCal);
        System.out.println("All of the transactions for the last 3 calendar months: ");
        for (Object elem : expensesInRange) {
            System.out.println(elem);
        }

        // Creating the legend for the bar chart that explains the colors
        for (Category category : Category.values()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(category.toString());

            double total = ExpenseTracker.TRACKER
                    .calculateTotalExpenses(ExpenseTracker.TRACKER.filterExpenses(expensesInRange, category));
            series.getData().add(new XYChart.Data<>(category.toString(), total));

            barChart.getData().add(series);
        }

        barChart.setBarGap(0); // Sets the gap between bars in the same category
        barChart.setCategoryGap(0); // Sets the gap between different categories (months)
        barChart.setStyle("-fx-background-color: #fff");

        // Add click listeners for debugging (optional)
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            System.out.println(series.getData().size() + "in " + series.getName());
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                barNode.setScaleX(6);
                barNode.setTranslateX(40); // Manually adjust position
                String nodeStyle = "-fx-bar-fill: ";
                barNode.setStyle(nodeStyle + Category.valueOf(series.getName().toUpperCase()).getDefaultColor());
                barNode.hoverProperty().addListener((_, _, _) -> {
                    addHoverEffect(nodeStyle, barNode, Category.valueOf(series.getName().toUpperCase()));
                });

                Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + ":\n$" + data.getYValue()));
            }
        }
    }

    /**
     * Adds a hover effect to chart nodes, changing their color when the mouse 
     * is over them and reverting the color when it leaves.
     * 
     * @param style  the CSS style to apply
     * @param node   the chart node being styled
     * @param category the category associated with the node
     */
    private void addHoverEffect(String style, Node node, Category category) {
        node.setOnMouseEntered(_ -> {
            node.setStyle(style + category.getHoverColor() + ";");
        });

        node.setOnMouseExited(_ -> {
            node.setStyle(style + category.getDefaultColor() + ";"); // Reverts to default color
        });
    }

}