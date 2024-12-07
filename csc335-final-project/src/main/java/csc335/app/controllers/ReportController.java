package csc335.app.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.AvatarView;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * ${file_name}
 * 
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class ReportController implements Initializable {

    @FXML
    private TableView<Expense> tableView;

    @FXML
    private TableColumn<Expense, String> dateColumn;

    @FXML
    private TableColumn<Expense, String> expenseColumn;

    @FXML
    private TableColumn<Expense, String> categoryColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

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

    @FXML
    private Label seeAllLabel;

    @FXML
    private Label importLabel;

    @FXML
    private VBox recentExpensesBox;

    @FXML 
    private MenuButton menuButton;

    @FXML
    private Pane expensePane; // Template for an expense pane

    @FXML
    private Pane advertisementPane; // Advertisement pane to add at the end

    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        System.out.println("Current user: " + currentUser.getUsername());

        seeAllLabel.setOnMouseClicked(event -> {
            View.EXPENSES.loadView();
        });

        initializeMonthMenu();
        initializePieChart();
        configureTableColumns();
        populateTableView(ExpenseTracker.TRACKER.filterExpenses(CalendarConverter.INSTANCE.getCalendar()));
    }


    private void initializeMonthMenu() {
        // Get all expenses
        List<Expense> expenses = ExpenseTracker.TRACKER.getExpenses();
    
        // Extract unique months (YYYY-MM) from the Calendar dates in expenses
        ObservableList<String> months = FXCollections.observableArrayList(
            expenses.stream()
                    .map(expense -> {
                        Calendar date = expense.getCalendarDate();
                        int year = date.get(Calendar.YEAR);
                        int month = date.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
                        return String.format("%04d-%02d", year, month); // Format as YYYY-MM
                    })
                    .distinct()
                    .sorted()
                    .toList()
        );
    
        // Add a MenuItem for each month
        for (String month : months) {
            MenuItem monthItem = new MenuItem(month);
            monthItem.setOnAction(event -> refreshPageForMonth(month));
            menuButton.getItems().add(monthItem);
        }
    
        // Set a default text for the MenuButton
        menuButton.setText("Select Month");
    }
     menuButton.setText("Select Month");
}

private void refreshPageForMonth(String month) {
    // Update the MenuButton text to show the selected month
    menuButton.setText(month);

    // Parse the selected month (YYYY-MM)
    String[] parts = month.split("-");
    int selectedYear = Integer.parseInt(parts[0]);
    int selectedMonth = Integer.parseInt(parts[1]);

    // Filter expenses for the selected month
    List<Expense> filteredExpenses = ExpenseTracker.TRACKER.getAllExpenses().stream()
        .filter(expense -> {
            Calendar date = expense.getDate();
            int expenseYear = date.get(Calendar.YEAR);
            int expenseMonth = date.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
            return expenseYear == selectedYear && expenseMonth == selectedMonth;
        })
        .toList();

    // Refresh the TableView, PieChart, etc.
    populateTableView(filteredExpenses);
    refreshPieChart(filteredExpenses);
    refreshBarChart(filteredExpenses); // Optional, if you have a BarChart to refresh
}


private void refreshPieChart(List<Expense> expenses) {
    pieChart.getData().clear();

    for (Category category : Category.values()) {
        double total = ExpenseTracker.TRACKER.calculateTotalExpenses(
            expenses.stream()
                    .filter(expense -> expense.getCategory().equals(category.toString()))
                    .toList()
        );

        if (total > 0) {
            PieChart.Data slice = new PieChart.Data(category.toString(), total);
            pieChart.getData().add(slice);
        }
    }

    for (PieChart.Data slice : pieChart.getData()) {
        Node pieNode = slice.getNode();
        String nodeStyle = "-fx-pie-color: ";
        pieNode.setStyle(nodeStyle + Category.valueOf(slice.getName().toUpperCase()).getDefaultColor());
        Tooltip.install(pieNode, new Tooltip(slice.getName() + ":\n$" + slice.getPieValue()));
    }
}

    private void configureTableColumns() {
        // Bind each column to a property in the Expense object
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Format the amount column to display two decimal places
        amountColumn.setCellFactory(tc -> new TableCell<Expense, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });
    }

    private void populateTableView(List<Expense> expenses) {
        // Convert the list of expenses into an ObservableList
        ObservableList<Expense> observableExpenses = FXCollections.observableArrayList(expenses);

        // Set the ObservableList to the TableView
        tableView.setItems(observableExpenses);
    }

    private void initializePieChart() {

        Calendar cal = CalendarConverter.INSTANCE.getCalendar();
        List<Expense> expenses = ExpenseTracker.TRACKER.filterExpenses(cal);

        for (Category category : Category.values()) {
            PieChart.Data slice = new Data(category.toString(),
                    ExpenseTracker.TRACKER.calculateTotalExpenses(expenses));
            pieChart.getData().add(slice);
        }

        for (Data pieSlice : pieChart.getData()) {
            Node pieNode = pieSlice.getNode();
            String nodeStyle = "-fx-pie-color: ";
            pieNode.setStyle(nodeStyle + Category.valueOf(pieSlice.getName().toUpperCase()).getDefaultColor());
            Tooltip.install(pieNode, new Tooltip(pieSlice.getName() + ":\n$" + pieSlice.getPieValue()));
            pieNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
                addHoverEffect(nodeStyle, pieNode, Category.valueOf(pieSlice.getName().toUpperCase()));
            });

            pieNode.setOnMouseClicked(
                    event -> System.out.println("Clicked: " + pieSlice.getName() + " -> " + pieSlice.getPieValue()));

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

    private void addHoverEffect(String style, Node node, Category category) {
        node.setOnMouseEntered(event -> {
            node.setStyle(style + category.getHoverColor() + ";");
        });

        node.setOnMouseExited(event -> {
            node.setStyle(style + category.getDefaultColor() + ";"); // Reverts to default color
        });
    }

}
