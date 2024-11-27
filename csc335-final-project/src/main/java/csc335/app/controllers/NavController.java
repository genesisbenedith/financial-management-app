package csc335.app.controllers;

//import java.io.IOException;

import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
//import csc335.app.controllers.*;

public class NavController {
    @FXML
    private BudgetController BudgetFace;

    @FXML
    private DashboardController DashboardFace;

    //Need the Reports, Expenses, Logout

    @FXML
    private Pane anchor;

    @FXML
    private Pane dashboard;

    @FXML
    private Pane expense;

    @FXML
    private Pane budget;

    @FXML
    private Pane logout;

    @FXML
    private Pane reports;

    private Pane[] panes = {reports, dashboard, logout, expense, budget};
    
    @FXML
    protected void resetSelected(Pane target) {
        for(Pane p: panes){
            if(target == p){
                target.setStyle("-fx-background-color: #698abf");
            }
        }
    }

    @FXML
    protected void goToDashboard(){
        AuthController.getInstance().loadContent("/views/DashboardView.fxml");
        resetSelected(dashboard);
    }
        
    @FXML
    protected void goToBudget(){
        AuthController.getInstance().loadContent("/views/BudgetView.fxml");
        resetSelected(budget);
    }

    @FXML
    protected void goToReports(){

    }

    @FXML
    protected void goToExpense(){

    }


}