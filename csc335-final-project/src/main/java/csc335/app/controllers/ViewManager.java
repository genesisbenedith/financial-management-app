package csc335.app.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// [ ] Finish header comment
/**
 * @author Genesis Benedith
 * 
 */

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
    public void showView(Parent parent) {
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

    // private FXMLLoader setController() {
    //     String fxmlPath = currentView.getFXMLPath(currentView.name());
    //     System.out.println("Path " + fxmlPath);
        
    //     FXMLLoader fxmlView = null;
    //     if (null != currentView) switch (currentView) {
    //         case BUDGET -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(BudgetController.getBudgetController());
    //         }
    //         case DASHBOARD -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(DashboardController.getDashboardController());
    //         }
    //         case REGISTER -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(SignUpController.getSignUpController());
    //         }
    //         case REPORT -> {
    //         }
    //         case LOGIN -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(SignInController.getSignInController());
    //         }
    //         case SPLASH -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(SplashController.getSplashController());
    //         }
    //         case EXPENSE -> {
    //             fxmlView = new FXMLLoader(getClass().getResource(fxmlPath));
    //             fxmlView.setController(ExpensesController.getExpensesController());
    //         }
    //         default -> {
    //         }
    //     }

    //     return fxmlView;

    // }

}
