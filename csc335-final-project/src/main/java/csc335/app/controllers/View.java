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
    EXPENSE("Expense", "Expense Tracker"),
    REPORT("Report", "Monthly Report");

    // [ ] Needs field comments
    private final String VIEW_LABEL;
    private final String VIEW_TITLE;
    private final String VIEW_NAME;
    private final String FXML_VIEW_DIRECTORY = "views";
    private final String FXML_VIEW_PATH;

    // [ ] Needs method comment
    View(String viewLabel, String viewTitle) {
        this.VIEW_LABEL = viewLabel;
        this.VIEW_TITLE = viewTitle;
        this.VIEW_NAME = viewLabel + " View";
        this.FXML_VIEW_PATH = Path.of(FXML_VIEW_DIRECTORY, VIEW_NAME + ".fxml").toString();
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
    public String getLabel() {
        return this.VIEW_LABEL;
    }
}