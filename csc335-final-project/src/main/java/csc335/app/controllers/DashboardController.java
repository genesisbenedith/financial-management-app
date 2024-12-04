package csc335.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
// import java.util.Map;
import java.util.ResourceBundle;

// import csc335.app.Category;
// import csc335.app.models.Budget;
import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
// import csc335.app.persistence.User;
// import csc335.app.persistence.UserSessionManager;
import javafx.fxml.Initializable;

/**
 * ${file_name}
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public class DashboardController implements Subject, Initializable {

    private static final List<Observer> observers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to the dashboard!");

        addObserver(AccountRepository.getAccountRepository());
        notifyObservers();
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
