package csc335.app;

import csc335.app.controllers.View;
import javafx.application.Application;
import javafx.stage.Stage;

// [ ]: Change App to FinanceAssistant

// [ ] Needs class comment
public final class App extends Application {

    // [ ] Compete method comment
    /**
     * @param 
     */
    @Override
    public void start(Stage stage) {
        System.out.println("Starting application...");
        View.SPLASH.loadView();
    }

    // [ ] Compete method comment
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        App.launch(args);
    }
}
