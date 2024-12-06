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

// [ ] Finish file comment
/**
 * @author Genesis Benedith
 */

// [ ] Needs class comment
public enum View {
    SPLASH("Splash", "Finantra: Personal Finance Assistant"),
    REGISTER("SignUp", "Finantra: Personal Finance Assistant"),
    LOGIN("SignIn", "Finantra: Personal Finance Assistant"),
    DASHBOARD("Dashboard", "Dashboard"),
    BUDGET("Budget", "Budget Tracker"),
    EXPENSE("Expense", "Add an Expense"),
    EXPENSES("Expenses", "Expense Tracker"),
    REPORT("Report", "Monthly Report"),
    ALERT("N/A", "Alert"),
    CHOOSER("N/A", "Chooser");

    // [ ] Needs field comments
    private final String VIEW_TITLE;
    private final String VIEW_NAME;
    private final String FXML_VIEW_DIRECTORY = Path.of("/views").toString();
    private final String FXML_VIEW_PATH;

    private static Stage primaryStage;// Main application window (primary stage)
    private final Deque<View> sceneStack = new ArrayDeque<>(); // Stack to manage the history of scenes shown in the main stage
    private Stage popUpStage; // Stage for pop-up windows
    private final Deque<View> popUpSceneStack = new ArrayDeque<>(); // Stack to manage the history of pop-up scenes

    // [ ] Needs method comment
    private View(String viewName, String viewTitle) {
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

    /**
     * Gets the current scene from the scene stack.
     * @return The current scene.
     */
    public View getCurrentScene() {
        return sceneStack.peek();
    }

    /**
     * Displays a pop-up window with the given scene.
     * @param fxml The enum representing the pop-up scene to be displayed.
     * @throws IOException If the FXML file can't be loaded.
     */
    public void showPopUp() throws IOException {
        showPopUp(primaryStage);  // Show the pop-up with the primary stage as the parent
    }

    /**
     * Displays a pop-up window with the given scene and a specified parent window.
     * @param fxml The enum representing the pop-up scene to be displayed.
     * @param parentWindow The parent window for the pop-up.
     * @throws IOException If the FXML file can't be loaded.
     */
    public void showPopUp(Window parentWindow) throws IOException {
        if (popUpStage == null) {
            popUpStage = new Stage();  // Create pop-up stage if it doesn't exist
            popUpStage.initModality(Modality.APPLICATION_MODAL);  // Make it modal (blocking input to parent)
            popUpStage.initOwner(parentWindow);  // Set the parent window
        }

        setPopUpScene();  // Set the scene for the pop-up
        popUpStage.centerOnScreen();  // Center the pop-up on the screen
        popUpStage.showAndWait();  // Show the pop-up and wait until it's closed
    }

    /**
     * Closes the pop-up window and clears the pop-up stack.
     */
    public void closePopUpWindow() {
        if (popUpStage != null) {
            popUpStage.close();  // Close the pop-up stage
            popUpStage = null;
        }
        popUpSceneStack.clear();  // Clear the pop-up stack
    }

    /**
     * Sets the pop-up scene and handles its display.
     * @param fxml The enum representing the pop-up scene.
     */
    public void setPopUpScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.FXML_VIEW_PATH));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);  // Create a new Scene for the pop-up
            popUpStage.setTitle(this.VIEW_TITLE);  // Set the title of the pop-up
            popUpStage.setScene(scene);  // Set the scene

            // root.setOnKeyPressed(keyEvent -> {
            //     KeyHandler.getInstance().keyPressed(keyEvent);  // Handle key presses
            // });

            // Handle closing the pop-up
            popUpStage.setOnCloseRequest(e -> {
                goBackPopUp();  // Go back to the previous pop-up scene
                e.consume();  // Prevent default close behavior
            });

            popUpSceneStack.push(this);  // Add the pop-up scene to the stack
        } catch (IOException e) {
            throw new RuntimeException(e);  // Handle loading errors
        }
    }

    /**
     * Goes back to the previous pop-up scene or closes the pop-up if there's no previous scene.
     */
    public void goBackPopUp() {
        closePopUpWindow();  
    }

    /**
     * Gets the title of the current scene in the main stack.
     * @return The title of the current scene.
     */
    public String getCurrentSceneTitle() {
        return sceneStack.peek().getTitle();
    }

    /**
     * Gets the current pop-up scene.
     * @return The current pop-up scene.
     */
    public View getCurrentPopUpScene() {
        return popUpSceneStack.peek();
    }

    /**
     * Returns the number of pop-up scenes in the stack.
     * @return The size of the pop-up scene stack.
     */
    public int getPopUpSceneStackSize() {
        return popUpSceneStack.size();
    }

    /**
     * Returns the main scene stack.
     * @return The main scene stack.
     */
    public Deque<View> getSceneStack() {
        return sceneStack;
    }

    /**
     * Returns the pop-up scene stack.
     * @return The pop-up scene stack.
     */
    public Deque<View> getPopUpSceneStack() {
        return popUpSceneStack;
    }

    // [ ] Needs method comment
    private void showView(Parent parent) {
        if (primaryStage == null) {
            primaryStage = new Stage();
        }

        System.out.println("Setting the " + this.name() + "...");
        Scene scene = new Scene(parent); 
        primaryStage.setTitle(this.getTitle());

        // // Handle key press events for the scene
        // scene.setOnKeyPressed(keyEvent -> {
        //     KeyHandler.getInstance().keyPressed(keyEvent);
        // });

        primaryStage.setScene(scene);
        primaryStage.show();
    };

    // [ ] Needs method comment
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

    // [ ] Needs method comment
    /**
     * 
     * @param alertType
     * @param title
     * @param message
     */
    public void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            return file.getAbsolutePath();
        } 
        return null;
    }
}