package csc335.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import csc335.app.CalendarConverter;
import csc335.app.Category;
import csc335.app.models.Expense;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * ${file_name}
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Subject, Initializable {

    @FXML 
    private BarChart<CategoryAxis, NumberAxis> barChart;

    private static final List<Observer> observers = new ArrayList<>();
    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");

        UserSessionManager.getUserSessionManager();
        currentUser = UserSessionManager.getCurrentUser();
        initializeBarChart();

        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
    }

    private void initializeBarChart() {
        int currentMonth = CalendarConverter.getCurrentMonth();
        
        List<XYChart.Series<CategoryAxis, NumberAxis>> barChartKeyList = new ArrayList<>();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart bc = new BarChart(xAxis, yAxis);

        // Creating the key for the bar chart so they user can know what each bar refers to
        for (Category category : Category.values()) {
            Map<String, Double> monthlyTotals = new HashMap<>();
            List<Expense> categoryExpenses = currentUser.getBudgetsByCategory().get(category);
            XYChart.Series<CategoryAxis, NumberAxis> categoryData = new XYChart.Series<CategoryAxis, NumberAxis>();
            categoryData.setName(category.toString());
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    

}
