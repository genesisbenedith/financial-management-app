package csc335.app.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This file defines the `View` enum, which manages the different views (scenes)
 * and pop-up windows for the Finantra application. Each enum constant
 * corresponds
 * to a specific view and contains information about its title and FXML path.
 * The `View` enum handles the creation, navigation, and management of both
 * primary
 * scenes and modal pop-ups.
 * 
 * @author Genesis Benedith
 */
public enum View {
    SPLASH("Splash", "Finantra: Personal Finance Assistant"),
    REGISTER("SignUp", "Finantra: Personal Finance Assistant"),
    LOGIN("SignIn", "Finantra: Personal Finance Assistant"),
    DASHBOARD("Dashboard", "Dashboard"),
    BUDGET("Budget", "Budget Tracker"),
    EXPENSE("Expense", "Add an Expense"),
    EXPENSES("Expenses", "Expense Tracker"),
    ALERT("N/A", "Alert"),
    CHOOSER("N/A", "Chooser");

    /** The title of the view displayed in the stage. */
    private final String VIEW_TITLE;

    /** The name of the view, used to generate its FXML file path. */
    private final String VIEW_NAME;

    /** The base directory where FXML files are located. */
    private final String FXML_VIEW_DIRECTORY = Path.of(File.separator + "views").toString();

    /** The full path to the FXML file for this view. */
    private final String FXML_VIEW_PATH;

    /** The main application stage (primary window). */
    private static Stage primaryStage;

    /** A stack to manage the history of scenes in the main stage. */
    private final Deque<View> sceneStack = new ArrayDeque<>();

    /** The stage for modal pop-up windows. */
    private Stage popUpStage;

    /** A stack to manage the history of pop-up scenes. */
    private final Deque<View> popUpSceneStack = new ArrayDeque<>();

    /**
     * Constructor for the `View` enum.
     * 
     * @param viewName  The name of the view.
     * @param viewTitle The title displayed in the window for the view.
     */
    private View(String viewName, String viewTitle) {
        this.VIEW_TITLE = viewTitle;
        this.VIEW_NAME = viewName;
        this.FXML_VIEW_PATH = Path.of(FXML_VIEW_DIRECTORY, VIEW_NAME + "View.fxml").toString();
    }

    /**
     * Gets the FXML file path for the view.
     * 
     * @param viewName The name of the view.
     * @return The FXML file path.
     */
    public String getFXMLPath(String viewName) {
        return this.FXML_VIEW_PATH;
    }

    /**
     * Gets the title of the view.
     * 
     * @return The title of the view.
     */
    public String getTitle() {
        return this.VIEW_TITLE;
    }

    /**
     * Gets the name of the view.
     * 
     * @return The name of the view.
     */
    public String getName() {
        return this.VIEW_NAME;
    }

    /**
     * Displays the pop-up window for the current view.
     * 
     * @throws IOException If the FXML file cannot be loaded.
     */
    public void showPopUp() throws IOException {
        showPopUp(primaryStage);
    }

    /**
     * Displays the pop-up window for the current view with a specified parent
     * window.
     * 
     * @param parentWindow The parent window for the pop-up.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public void showPopUp(Window parentWindow) throws IOException {
        if (popUpStage == null) {
            popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(parentWindow);
        }

        setPopUpScene();
        popUpStage.centerOnScreen();
        popUpStage.showAndWait();
    }

    /**
     * Closes the pop-up window and clears the pop-up scene stack.
     */
    public void closePopUpWindow() {
        if (popUpStage != null) {
            popUpStage.close();
            popUpStage = null;
        }
        popUpSceneStack.clear();
    }

    /**
     * Sets the scene for the pop-up window.
     */
    public void setPopUpScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.FXML_VIEW_PATH));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            popUpStage.setTitle(this.VIEW_TITLE);
            popUpStage.setScene(scene);

            popUpStage.setOnCloseRequest(e -> {
                goBackPopUp();
                e.consume();
            });

            popUpSceneStack.push(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Navigates back to the previous pop-up scene or closes the pop-up if no
     * previous scene exists.
     */
    public void goBackPopUp() {
        closePopUpWindow();
    }

    /**
     * Displays the main view in the primary stage.
     */
    public void loadView() {
        System.out.println("Loading the " + this.name() + "...");
        String fxmlPath = this.getFXMLPath(this.name());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        try {
            Parent parent = loader.load();
            showView(parent);
        } catch (IOException e) {
            throw new RuntimeException("View cannot be loaded -> " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     * 
     * @param alertType The type of the alert (e.g., INFORMATION, ERROR).
     * @param title     The title of the alert.
     * @param message   The message displayed in the alert.
     */
    public void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Opens a file chooser dialog and returns the selected file path.
     * 
     * @return The absolute path of the selected file, or null if no file is
     *         selected.
     */
    public String showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File file = fileChooser.showOpenDialog(primaryStage);
        return file != null ? file.getAbsolutePath() : null;
    }

    /**
     * Sets the main application stage with the specified parent node and displays
     * the view.
     * If the primary stage is null, it initializes a new stage. Updates the stage
     * title,
     * sets the scene, and shows the view.
     * 
     * @param parent The root node of the scene to be displayed.
     */
    private void showView(Parent parent) {
        if (primaryStage == null) {
            primaryStage = new Stage(); // Initialize primary stage if it doesn't exist
        }

        System.out.println("Setting the " + this.name() + "...");

        // Create a new scene with the provided root node
        Scene scene = new Scene(parent);

        // Set the title of the stage to the title of the view
        primaryStage.setTitle(this.getTitle());

        // Set the scene to the primary stage and display it
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
