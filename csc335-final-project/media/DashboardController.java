package csc335.app.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.AvatarView;
import com.dlsc.gemsfx.SVGImageView;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.ExpenseTracker;
import csc335.app.utils.CalendarConverter;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
    private Label reportLabel;

    @FXML
    private VBox recentExpensesBox;

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

        reportLabel.setOnMouseClicked(event -> {
            View.REPORT.loadView();
        });

    
        initializeBarChart();
        initializePieChart();
        try {
            populateRecentExpenses(ExpenseTracker.TRACKER.sortExpenses());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
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

        String svgUrl = expense.getCategory().getSvgUrl(); // Assume `getSvgIconUrl()` returns the correct URL for the
                                                           // category

        System.out.println(expense.toString());
        System.out.println("Attempting to load SVG: " + svgUrl);

        URL resource = getClass().getResource(svgUrl);
        if (resource == null) {
            System.err.println("Resource not found: " + svgUrl);
        } else {
            String svgPath = resource.toString(); // Use .toString() instead of .toExternalForm()
            System.out.println("Resolved SVG Path: " + svgPath);
            expenseIcon.setSvgUrl(svgPath);
        }
        System.out.println(expenseIcon.getSvgUrl());
        System.out.println(expenseIcon.svgUrlProperty());
        // iconPane.getChildren().add(expenseIcon);
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
            pieNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
                addHoverEffect(nodeStyle, pieNode, Category.valueOf(pieSlice.getName().toUpperCase()));
            });

            pieNode.setOnMouseClicked(
                    event -> System.out.println("Clicked: " + pieSlice.getName() + " -> " + pieSlice.getPieValue()));

        }
        pieChart.applyCss(); // Apply CSS styles to the chart
        pieChart.layout(); // Force layout to ensure legend nodes are created

        pieChart.lookupAll(".chart-legend-item-symbol").forEach(node -> {
            String label = ((PieChart.Data) node.getUserData()).getName();
            System.out.println(label);
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
        System.out.println("All of the transactions in the last month: ");
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
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                barNode.setScaleX(6);
                barNode.setTranslateX(40); // Manually adjust position
                String nodeStyle = "-fx-bar-fill: ";
                barNode.setStyle(nodeStyle + Category.valueOf(series.getName().toUpperCase()).getDefaultColor());
                barNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
                    addHoverEffect(nodeStyle, barNode, Category.valueOf(series.getName().toUpperCase()));
                });

                Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + ":\n$" + data.getYValue()));
            }
        }
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
