package csc335.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Subject;
import csc335.app.persistence.AccountRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

// [ ] Needs class comment
public class SplashController implements Subject, Initializable {

    @FXML
    private ImageView arrow; // JavaFX element that displays a button

    private static final List<Observer> observers = new ArrayList<>();

    // [ ] Complete method comment

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Welcome to Finantra, the Personal Finance Assistant!\n Click the white arrow to continue.");

        addObserver(AccountRepository.getAccountRepository());
    }


    // Handling click event for the arrow image, redirecting client to login page
    @FXML
    private void handleArrowClick(MouseEvent event) {
        ViewManager.getViewManager().loadView(View.LOGIN);
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
