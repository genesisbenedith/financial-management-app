package csc335.app.controllers;

// [ ] Finish file comment
/**
 * @author Genesis Benedith
 */
import java.nio.file.Path;

// [ ] Needs class comment
public enum View {
    SPLASH("Splash", "Finantra: Personal Finance Assistant"),
    REGISTER("SignUp", "Finantra: Personal Finance Assistant"),
    LOGIN("SignIn", "Finantra: Personal Finance Assistant"),
    DASHBOARD("Dashboard", "Dashboard"),
    BUDGET("Budget", "Budget Tracker"),
    EXPENSES("Expenses", "Expense Tracker"),
    EXPENSE("Expense", "Add an Expense"),
    REPORT("Report", "Monthly Report");

    // [ ] Needs field comments
    private final String VIEW_TITLE;
    private final String VIEW_NAME;
    // private final String FXML_VIEW_DIRECTORY = Path.of("..", "..", "..", "resources", "views").toString();
    private final String FXML_VIEW_DIRECTORY = Path.of("/views").toString();
    private final String FXML_VIEW_PATH;

    // [ ] Needs method comment
    View(String viewName, String viewTitle) {
        this.VIEW_TITLE = viewTitle;
        this.VIEW_NAME = viewName;
        this.FXML_VIEW_PATH = Path.of(FXML_VIEW_DIRECTORY, VIEW_NAME + "View.fxml").toString();
    }

    // [ ] Needs method comment
    public String getFXMLPath(String viewName){
        return this.FXML_VIEW_PATH;
    }

    // [ ] Needs method comment
    public String getTitle() {
        return this.VIEW_TITLE;
    }

    // [ ] Needs method comment
    public String getName() {
        return this.VIEW_NAME;
    }
}