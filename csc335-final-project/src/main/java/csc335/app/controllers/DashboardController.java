package csc335.app.controllers;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.dlsc.gemsfx.SVGImageView;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * ${file_name}
 * 
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Initializable {

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
    private Label seeAllLabel;

    @FXML
    private VBox recentExpensesBox;

    @FXML
    private Label monthLabel;

    @FXML
    private MenuButton menuButton;

    @FXML
    private Pane advertisementPane; // Advertisement pane to add at the end

    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        System.out.println("Current user: " + currentUser.getUsername());

        seeAllLabel.setOnMouseClicked(_ -> {
            View.EXPENSES.loadView();
        });

        setMonthLabelToCurrentMonth();

        initializeMonthMenu();
        initializePieChart();
        initializeBarChart();
        configureTableColumns();
        populateTableView(ExpenseTracker.TRACKER.filterExpenses(CalendarConverter.INSTANCE.getCalendar()));

        try {
            populateRecentExpenses(ExpenseTracker.TRACKER.sortExpenses());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private void populateRecentExpenses(List<Expense> expenses) {
        // Preserve the title pane
        Node titlePane = recentExpensesBox.getChildren().get(0);

        // Clear all children except the title pane
        recentExpensesBox.getChildren().clear();
        recentExpensesBox.getChildren().add(titlePane);

        // Add up to 7 expense panes
        for (int i = 0; i < 7; i++) {
            Pane expensePaneCopy;
            if (i < expenses.size()) {
                // Populate with expense data
                Expense expense = expenses.get(i);
                expensePaneCopy = createExpensePane(expense);
            } else {
                // Use an empty placeholder
                expensePaneCopy = createEmptyExpensePane();
            }
            recentExpensesBox.getChildren().add(expensePaneCopy);
        }

        // Add advertisement pane at the end
        recentExpensesBox.getChildren().add(advertisementPane);
    }

    private Pane createExpensePane(Expense expense) {
        // Main Pane for the expense
        Pane pane = new Pane();
        pane.setPrefWidth(300.0);
        pane.setPrefHeight(46.0);
        pane.getStyleClass().add("expense-pane"); // Apply general expense-pane style
        VBox.setVgrow(pane, javafx.scene.layout.Priority.ALWAYS); // Ensure vgrow is inherit for the Pane

        // BorderPane inside the Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(300.0);
        borderPane.setPrefHeight(46.0);
        borderPane.setPadding(new Insets(0, 15, 0, 15)); // Padding: 0 15 0 15

        // Left Pane for SVG Icon
        Pane iconPane = new Pane();
        iconPane.setPrefWidth(32.0);
        iconPane.setPrefHeight(32.0);
        // iconPane.getStyleClass().add("content-background"); // Apply background style
        BorderPane.setMargin(iconPane, new Insets(0, 10, 0, 0)); // Padding: 0 10 0 0
        BorderPane.setAlignment(iconPane, javafx.geometry.Pos.CENTER); // Center alignment

        SVGImageView expenseIcon = new SVGImageView();
        expenseIcon.setFitWidth(21.0);
        expenseIcon.setFitHeight(21.0);
        expenseIcon.setTranslateX(4.5); // Center within Pane
        expenseIcon.setTranslateY(4.5);

        borderPane.setLeft(iconPane);

        // Center VBox for Description and Date
        VBox centerBox = new VBox();
        centerBox.setPrefWidth(120.0);
        centerBox.setPrefHeight(10.0);
        centerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT); // Auto alignment

        Label descriptionLabel = new Label(expense.getDescription());
        descriptionLabel.getStyleClass().add("expense-description"); // Apply description style
        VBox.setMargin(descriptionLabel, new Insets(8, 0, 0, 0)); // Margin: 8 0 0 0
        VBox.setVgrow(descriptionLabel, javafx.scene.layout.Priority.ALWAYS); // Inherit vgrow

        Label dateLabel = new Label(expense.getStringDate());
        dateLabel.getStyleClass().add("expense-date"); // Apply date style
        VBox.setMargin(dateLabel, new Insets(0, 0, 8, 0)); // Margin: 0 0 8 0

        centerBox.getChildren().addAll(descriptionLabel, dateLabel);
        borderPane.setCenter(centerBox);

        // Right Label for Expense Amount
        Label expenseAmount = new Label(String.format("$%.2f", expense.getAmount()));
        expenseAmount.getStyleClass().add("expense-amount"); // Apply amount style
        BorderPane.setAlignment(expenseAmount, javafx.geometry.Pos.CENTER_RIGHT);
        borderPane.setRight(expenseAmount);

        // Add the BorderPane to the main Pane
        pane.getChildren().add(borderPane);

        return pane;
    }

    private Pane createEmptyExpensePane() {
        Pane emptyPane = new Pane();
        emptyPane.setPrefHeight(46.0);
        emptyPane.setPrefWidth(300.0);
        emptyPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #fff;");
        return emptyPane;
    }

    private void setMonthLabelToCurrentMonth() {
        // Get the current month and year
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);

        // Set the label text to "Month YYYY" format
        monthLabel.setText(month + " " + year);
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
                        .toList());

        // Add a MenuItem for each month
        for (String month : months) {
            MenuItem monthItem = new MenuItem(month);
            monthItem.setOnAction(_ -> refreshPageForMonth(month));
            menuButton.getItems().add(monthItem);
        }

        // Set a default text for the MenuButton
        menuButton.setText("Select Month");
    }

    private void refreshMonthLabel(String selectedMonth) {
        // Parse the selected month (YYYY-MM)
        String[] parts = selectedMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        // Get the month name
        String monthName = new DateFormatSymbols().getMonths()[month - 1]; // Month is 0-based

        // Update the monthLabel text
        monthLabel.setText(String.format("%s %d", monthName, year));
    }

    private void refreshPageForMonth(String month) {
        // Update the MenuButton text to show the selected month
        menuButton.setText(month);
        refreshMonthLabel(month);
    
        // Parse the selected month (YYYY-MM)
        String[] parts = month.split("-");
        int selectedYear = Integer.parseInt(parts[0]);
        int selectedMonth = Integer.parseInt(parts[1]);
    
        // Filter expenses for the selected month
        List<Expense> filteredExpenses = ExpenseTracker.TRACKER.getExpenses().stream()
            .filter(expense -> {
                Calendar date = expense.getCalendarDate();
                int expenseYear = date.get(Calendar.YEAR);
                int expenseMonth = date.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
                return expenseYear == selectedYear && expenseMonth == selectedMonth;
            })
            .toList();
    
        // Refresh the TableView, PieChart, and BarChart
        populateTableView(filteredExpenses);
        refreshPieChart(filteredExpenses);
        refreshBarChart(filteredExpenses); // Ensure BarChart refresh is called
    }
    

    private void refreshPieChart(List<Expense> expenses) {
        pieChart.getData().clear();
    
        // Group expenses by category and calculate totals
        Map<Category, Double> categoryTotals = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)
            ));
    
        // Add data slices to PieChart
        for (Map.Entry<Category, Double> entry : categoryTotals.entrySet()) {
            Category category = entry.getKey();
            double total = entry.getValue();
    
            if (total > 0) {
                PieChart.Data slice = new PieChart.Data(category.toString(), total);
                pieChart.getData().add(slice);
            }
        }
    
        pieChart.applyCss();
        pieChart.layout(); // Force layout to ensure nodes are created
    
        // Apply styles to PieChart slices
        for (PieChart.Data slice : pieChart.getData()) {
            Node pieNode = slice.getNode();
            if (pieNode != null) {
                String color = Category.valueOf(slice.getName().toUpperCase()).getDefaultColor();
                pieNode.setStyle("-fx-pie-color: " + color + ";");
                Tooltip.install(pieNode, new Tooltip(slice.getName() + ":\n$" + slice.getPieValue()));
            }
        }
    }
    
    


    private void configureTableColumns() {
        // Set the value factory for the date column to use getStringDate
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStringDate()));

        // Bind other columns to their respective properties
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Format the amount column to display two decimal places
        amountColumn.setCellFactory(_ -> new TableCell<Expense, Double>() {
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

    private void refreshBarChart(List<Expense> expenses) {
        // Clear existing data from the BarChart
        barChart.getData().clear();
    
      
    
        // Create a series for each category and add it to the BarChart
        for (Category category : Category.values()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(category.toString());

            double total = ExpenseTracker.TRACKER
                    .calculateTotalExpenses(ExpenseTracker.TRACKER.filterExpenses(expenses, category));
            series.getData().add(new XYChart.Data<>(category.toString(), total));

            barChart.getData().add(series);
        }
    
        // Apply styles and ensure nodes are created after layout pass
        barChart.applyCss();
        barChart.layout(); // Force layout to ensure nodes are created
    
        // Style each series after nodes are created
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                if (barNode != null) { // Ensure node exists before applying styles
                    String color = Category.valueOf(series.getName().toUpperCase()).getDefaultColor();
                    barNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            }
        }
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
            pieNode.hoverProperty().addListener((_, _, _) -> {
                addHoverEffect(nodeStyle, pieNode, Category.valueOf(pieSlice.getName().toUpperCase()));
            });

            pieNode.setOnMouseClicked(
                    _ -> System.out.println("Clicked: " + pieSlice.getName() + " -> " + pieSlice.getPieValue()));

        }
        pieChart.applyCss(); // Apply CSS styles to the chart
        pieChart.layout(); // Force layout to ensure legend nodes are created

        pieChart.lookupAll(".chart-legend-item-symbol").forEach(node -> {
            String label = ((PieChart.Data) node.getUserData()).getName();
            if (Category.valueOf(label.toUpperCase()) != null) {
                node.setStyle("-fx-background-color: " + Category.valueOf(label.toUpperCase()).getDefaultColor() + ";");
            }
        });

    }

    private void initializeBarChart() {
        // Get current cal date
        Calendar endCal = CalendarConverter.INSTANCE.getCalendar();
        Calendar startCal = CalendarConverter.INSTANCE.getCalendar(1);
        // The expenses within the last month
        List<Expense> expensesInRange = ExpenseTracker.TRACKER.filterExpensesInRange(startCal, endCal);
  

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
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                String nodeStyle = "-fx-bar-fill: ";
                barNode.setStyle(nodeStyle + Category.valueOf(series.getName().toUpperCase()).getDefaultColor());
                barNode.hoverProperty().addListener((_, _, _) -> {
                    addHoverEffect(nodeStyle, barNode, Category.valueOf(series.getName().toUpperCase()));
                });

                Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + ":\n$" + data.getYValue()));
            }
        }
    }

    private void addHoverEffect(String style, Node node, Category category) {
        node.setOnMouseEntered(_ -> {
            node.setStyle(style + category.getHoverColor() + ";");
        });

        node.setOnMouseExited(_ -> {
            node.setStyle(style + category.getDefaultColor() + ";"); // Reverts to default color
        });
    }

}