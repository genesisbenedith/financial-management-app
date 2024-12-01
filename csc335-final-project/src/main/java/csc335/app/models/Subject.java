package csc335.app.models;

import java.util.ArrayList;
import java.util.List;

import csc335.app.controllers.Observer;

public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    protected void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
