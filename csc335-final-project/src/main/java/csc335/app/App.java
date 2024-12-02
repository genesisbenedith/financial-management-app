package csc335.app;

import csc335.app.controllers.View;
import csc335.app.controllers.ViewManager;
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
        ViewManager.getViewManager().loadView(View.SPLASH);

        // String fxmlPath = View.SPLASH.getFXMLPath(View.SPLASH.name());
        // System.out.println("Path " + fxmlPath);
        // FXMLLoader fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
        
        // String currentDirectory = System.getProperty("user.dir");
        // System.out.println("Current working directory: " + currentDirectory);
        // try {
        //     Parent parent = fxmlView.load();
        //     ViewManager.getViewManager().showView(parent);
        // } catch (IOException e) {
        //     throw new RuntimeException("View cannot be loaded -> " + e.getMessage());
        // }
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
