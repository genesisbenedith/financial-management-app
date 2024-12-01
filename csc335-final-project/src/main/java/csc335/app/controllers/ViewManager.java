package csc335.app.controllers;

// [ ] Finish header comment
/**
 * @author Genesis Benedith
 * 
 */

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// [ ] Complete class comment
public final class ViewManager {
    
    // [ ] Needs field comments
    private static ViewManager manager = null;
    private Stage primaryStage;
    private View currentView;

    // [ ] Needs method comment
    private ViewManager() {
    }

    // [ ] Needs method comment
    public static ViewManager getViewManager() {
        if (manager == null) {
            manager = new ViewManager();
        }
        return manager;
    }

    // [ ] Needs method comment
    private void showView(Parent parent) {
        if (primaryStage == null) {
            primaryStage = new Stage();
        }

        System.out.println("Setting the " + currentView.name() + "...");
        Scene scene = new Scene(parent); 
        primaryStage.setTitle(currentView.getTitle());

        // [ ] Implement pop up view

        primaryStage.setScene(scene);
        primaryStage.show();
    };
    
    // [ ] Needs method comment
    public void loadView(View view) {
        currentView = view;
        System.out.println("Loading the " + currentView.name() + "...");
        String fxmlPath = currentView.getFXMLPath(currentView.name());

        FXMLLoader fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
        try {
            Parent parent = fxmlView.load();
            showView(parent);
        } catch (IOException e) {
            throw new RuntimeException("View cannot be loaded -> " + e.getMessage());
        }
    }

}
