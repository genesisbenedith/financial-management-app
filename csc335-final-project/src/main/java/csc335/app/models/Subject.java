package csc335.app.models;

import csc335.app.controllers.Observer;

public interface Subject {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
