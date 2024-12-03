package csc335.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import csc335.app.Category;
import csc335.app.models.Budget;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import csc335.app.persistence.User;
import csc335.app.persistence.UserSessionManager;
import javafx.fxml.Initializable;

/**
 * ${file_name}
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Subject, Initializable {

    private static final List<Observer> observers = new ArrayList<>();
    private static User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");

        UserSessionManager.getUserSessionManager();
        currentUser = UserSessionManager.getCurrentUser();


        initializeSpinners();
        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
    }

    public void initializeSpinners() {
        // Check


        Map<Category, Budget> budgets = currentUser.getBudgetsByCategory();
        for (Category category : Category.values()) {
            if (budgets.containsKey(category)) {
                switch(category) {
                    case FOOD -> {

                    } 
                    case ENTERTAINMENT -> {
                        
                    }
                    case TRANSPORTATION -> {
                        
                    }
                    case UTILITIES -> {
                        
                    }
                    case HEALTHCARE -> {
                        
                    }
                    case OTHER -> {
                        
                    }
                }
            }
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
