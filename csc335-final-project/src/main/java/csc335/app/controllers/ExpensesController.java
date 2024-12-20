package csc335.app.controllers;

/**
 * @author Lauren Schroeder
 * Course: CSC 335 (Fall 2024)
 * File: ExpensesController.java
 * Description: Controller class that controls the window and functions of the Expenses page
 */

//-----------------------imports----------------------------
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import csc335.app.models.Category;
import csc335.app.models.Expense;
import csc335.app.models.User;
import csc335.app.persistence.AccountManager;
import csc335.app.persistence.UserSessionManager;
import csc335.app.services.BudgetTracker;
import csc335.app.services.ExpenseTracker;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.cell.MFXDateCell;
import io.github.palexdev.materialfx.utils.DateTimeUtils;
import io.github.palexdev.materialfx.utils.others.dates.DateStringConverter;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Handles the user interface (UI) logic for managing and displaying expenses within the application.
 * Responsible for initializing the UI elements, handling user interactions, filtering and displaying expenses, 
 * importing expense data from external files, and updating the UI accordingly. The class interacts with other components such as 
 * the ExpenseTracker, UserSessionManager, and various UI elements (e.g., MFXDatePicker, MFXButton, MFXScrollPane) to provide an 
 * effiecient and responsive experience for managing a user's expenses.
 * 
 * Supports the following features:
 * - displaying and filtering expenses by date range and category
 * - importing expense data from external text files
 * - managing and displaying various progress bars representing different budget categories
 * - providing interactive UI elements such as buttons for adding new expenses and downloading files
 * 
 * The class implements the Initializable interface, initializing UI components and data when the view is loaded.
 */
public class ExpensesController implements Initializable {

    // -------------fields from the view----------------------
    @FXML
    private AnchorPane contentArea;

    @FXML
    private MFXButton clearButton;

    @FXML
    private MFXDatePicker dateFrom;
    LocalDate startDate;

    @FXML
    private MFXDatePicker dateTo;
    LocalDate endDate;

    @FXML
    private Pane expensePane;

    @FXML
    private Pane expenseTemplate;

    @FXML
    private MFXScrollPane expenseList;

    @FXML
    private VBox vBox;

    @FXML
    private Label amountChild;

    @FXML
    private Label categoryChild;

    @FXML
    private Label dateChild;

    @FXML
    private Label summaryChild;

    @FXML
    private ImageView edit;

    @FXML
    private ImageView delete;

    @FXML
    private HBox percentBar;

    @FXML
    private Pane foodBar;

    @FXML
    private Pane enterntainmentBar;

    @FXML
    private Pane transportBar;

    @FXML
    private Pane utilityBar;

    @FXML
    private Pane healthcareBar;

    @FXML
    private Pane otherBar;

    @FXML
    private Label expensePercentageLabel;

    @FXML
    private Label budgetHeader;

    @FXML
    private Label expensesHeader;

    @FXML
    private Label totalBudgetAmt;

    @FXML
    private Label totalExpensesAmt;

    @FXML
    private Pane downloadFile;

    @FXML
    private Pane importFile;

    @FXML
    private Pane addNewExpense;

    //--------------------instance variables----------------------

    /**
     * currentUser: the currently logged-in user. This holds the user's data and account information
     * categoryClicked: the category that was clicked on. This is used to filter expenses by category
     */
    private User currentUser;
    private Category categoryClicked;

    /**
     * loads the window and the information/visuals needed with their implementations
     * @param location   The location of the FXML file.
     * @param resources  The resource bundle for the application.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Expense Controller initialized.");

        // Get current user
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        List<Expense> expenses = ExpenseTracker.TRACKER.sortExpenses();
        dateFrom.setGridAlgorithm(DateTimeUtils::partialIntMonthMatrix);
        dateFrom.setConverterSupplier(() -> new DateStringConverter("dd/MM/yyyy", dateFrom.getLocale()));
        dateTo.setGridAlgorithm(DateTimeUtils::partialIntMonthMatrix);
        dateTo.setConverterSupplier(() -> new DateStringConverter("dd/MM/yyyy", dateTo.getLocale()));
        initializeDatePickers();
        loadExpenses(expenses);
        initializeProgressBars();
        setupimportFile();
        setupdownloadFile();
        addNewExpenseClick();
    }

    /**
    * Sets up the file import functionality and enablesthe user to import expenses from a file.
    * This method makes the import pane clickable and adds hover effects for better UX.
    * Sets up the click handler to trigger the import file selection dialog.
    */
    private void setupimportFile() {
        // Make sure the pane is clickable
        importFile.setCursor(Cursor.HAND);

        // Add hover effect
        importFile.setOnMouseEntered(e -> importFile.setStyle("-fx-background-color: derive(-fx-background, -10%);"));

        importFile.setOnMouseExited(e -> importFile.setStyle("-fx-background-color: #E6E6FA;"));

        // Add click handler
        importFile.setOnMouseClicked(e -> importFileClick());
    }

    /**
     * Handles the click event for importing a file by openeing method a FileChooser dialog and
     * allowing the user to select a file. After selection, the file's content is read in line by line,
     * and each line is processed to extract expense data, and the expense tracker is updated.
     * Displays a success or error message based on the result.
     * 
     * @throws IOException
     */
    private void importFileClick() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Import File");

        // Set initial directory to user's home directory
        String userHome = System.getProperty("user.home");
        chooseFile.setInitialDirectory(new File(userHome));

        // Add file extensions filter
        chooseFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Get the window for proper modal dialog display
        Window window = importFile.getScene().getWindow();
        File selectedFile = chooseFile.showOpenDialog(window);

        if (selectedFile != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        processExpenseLine(line);
                    } catch (Exception ex) {
                        System.err.println("Error processing line: " + line);
                        ex.printStackTrace();
                    }
                }
                // Refresh the expense list
                loadExpenses(ExpenseTracker.TRACKER.getExpenses());

                // Show success message
                View.ALERT.showAlert(
                        AlertType.INFORMATION,
                        "Success",
                        "File imported successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                View.ALERT.showAlert(
                        AlertType.ERROR,
                        "Error",
                        "Failed to import file: " + e.getMessage());
            }
        }
    }

    /**
     * Processes a single line of expense data from the imported file.
     * Parses the line, creates an Expense object, and adds it to the expense tracker.
     * @param line The line of expense data to be processed.
     */
    private void processExpenseLine(String line) {
        String[] expenseInfo = line.split(",");
        if (expenseInfo.length < 4) {
            throw new IllegalArgumentException("Invalid expense format");
        }

        // Parse date
        String[] date = expenseInfo[0].trim().split("-");
        if (date.length != 3) {
            throw new IllegalArgumentException("Invalid date format");
        }

        // Parse other fields
        Category category = Category.valueOf(expenseInfo[1].trim().toUpperCase());
        double amount = Double.parseDouble(expenseInfo[2].trim());
        String description = expenseInfo[3].trim();

        // Create calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                Integer.parseInt(date[0]), // year
                Integer.parseInt(date[1]) - 1, // month (0-based)
                Integer.parseInt(date[2]) // day
        );

        // Create and add expense
        Expense expense = new Expense(calendar, category, amount, description);
        ExpenseTracker.TRACKER.addExpense(expense);
        AccountManager.ACCOUNT.saveUserAccount();
    }

    /**
     * Sets up the file download functionality, enabling the user to export expenses to a file and
     * makes the download pane clickable and adds hover effects for better UX.
     * Sets up the click handler to trigger the file export dialog.
     */
    private void setupdownloadFile() {
        // Make sure the pane is clickable
        downloadFile.setCursor(Cursor.HAND);

        // Add hover effect
        downloadFile.setOnMouseEntered(e -> {
            downloadFile.setStyle("-fx-background-color: derive(-fx-background, -10%);");
        });

        downloadFile.setOnMouseExited(e -> {
            downloadFile.setStyle("-fx-background-color: #E6E6FA;");

        });

        // Add click handler
        downloadFile.setOnMouseClicked(e -> downloadFileClick());
    }

    /**
     * Handles the click event for exporting expenses to a file by
     * opening a file chooser dialog for the user to select a target file location.
     * If a file is selected, the expenses are exported to that file.
     * Displays a success or error message based on the result.
     */
    private void downloadFileClick() {
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Export Expenses");
        chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));

        // Set initial directory to user's home directory with fallback
        String userHome = System.getProperty("user.home");
        File initialDir = new File(userHome);
        if (!initialDir.exists() || !initialDir.isDirectory()) {
            initialDir = new File("."); // Default to current working directory
        }
        chooseFile.setInitialDirectory(initialDir);

        // Get the window for proper modal dialog display
        Window window = downloadFile.getScene().getWindow();

        // Show save dialog and get the selected file
        File selectedFile = chooseFile.showSaveDialog(window);

        if (selectedFile != null) {
            try {
                // Ensure the file has the correct .txt extension
                String targetFilePath = selectedFile.getAbsolutePath();
                if (!targetFilePath.endsWith(".txt")) {
                    targetFilePath += ".txt";
                }

                // Get the file generated by the export method
                File fileToDownload = AccountManager.ACCOUNT.exportFile();
                if (fileToDownload == null || !fileToDownload.exists()) {
                    View.ALERT.showAlert(
                            AlertType.ERROR,
                            "Export Error",
                            "The exported file does not exist. Please try again.");
                    return;
                }

                // Copy the file to the user's selected location
                Files.copy(fileToDownload.toPath(),
                        new File(targetFilePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                // Show success message
                View.ALERT.showAlert(
                        AlertType.INFORMATION,
                        "Success",
                        "Expenses exported successfully to:\n" + targetFilePath);

            } catch (IOException e) {
                e.printStackTrace();
                View.ALERT.showAlert(
                        AlertType.ERROR,
                        "Export Error",
                        "Failed to export expenses: " + e.getMessage());
            }
        } else {
            System.out.println("File save operation canceled by the user.");
        }
        refreshPage();
    }

    /**
     * Handles the click event for adding a new expense. When the add button is clicked
     * a new popup window for entering the expense details is opened. It also sets
     * mouse hover effects for better user interaction.
     * 
     * @throws IOException
     */
    private void addNewExpenseClick() {
        addNewExpense.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.PRIMARY) {
                try {
                    View.EXPENSE.showPopUp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addNewExpense.setCursor(Cursor.HAND);
        addNewExpense.setOnMouseEntered(e -> {
            addNewExpense.setStyle("-fx-background-color: derive(-fx-background, -10%);");
        });
        addNewExpense.setOnMouseExited(e -> {
            addNewExpense.setStyle("-fx-background-color: #E6E6FA;");
        });
        refreshPage();
    }

    /**
     * Initializes the date pickers for filtering expenses by date range.
     * Sets up the date pickers with default values, minimum and maximum dates,
     * and adds listeners to handle date changes.
     */
    public void initializeDatePickers() {
        // Initialize date pickers with default values if needed
        dateFrom.setValue(null);
        dateTo.setValue(null);

        // Add listeners to both date pickers
        dateFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            // When start date changes, update end date's minimum date
            if (newValue != null) {
                dateTo.setCellFactory(date -> {
                    return new MFXDateCell(dateTo, date) {
                        @Override
                        public void updateItem(LocalDate date) {
                            super.updateItem(date);
                            setStyle("-fx-background-color: #f4f4f4;");
                            // Customize cell appearance here
                            // For example, to disable weekends:
                            if (date != null) {
                                setDisable(date.compareTo(newValue) < 0);
                            }
                        }
                    };
                });
            }
            filterExpensesByDateRange();
        });
        dateTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateFrom.setCellFactory((picker) -> {
                    return new MFXDateCell(dateFrom, picker) {
                        @Override
                        public void updateItem(LocalDate item) {
                            super.updateItem(item);
                            if (item != null) {
                                // Disable dates after newValue
                                setDisable(item.compareTo(newValue) > 0);
                            }
                        }
                    };
                });
            }
            filterExpensesByDateRange(); // Ensure this method properly handles null values
        });
    }

    /**
     * Filters expenses based on the selected date range and category.
     * Updates the UI with the filtered expenses.
     */
    private void filterExpensesByDateRange() {
        LocalDate startDate = dateFrom.getValue();
        LocalDate endDate = dateTo.getValue();

        List<Expense> allExpenses = ExpenseTracker.TRACKER.getExpenses();
        List<Expense> filteredExpenses;

        if (startDate == null && endDate == null) {
            // If no dates selected, show all expenses
            filteredExpenses = allExpenses;
        } else {
            // Convert dates only if they exist
            Calendar startCal = startDate != null ? convertLocalDateToCalendar(startDate) : null;
            Calendar endCal = endDate != null ? convertLocalDateToCalendar(endDate) : null;

            filteredExpenses = allExpenses.stream()
                    .filter(expense -> {
                        boolean matchesStartDate = startCal == null ||
                                (expense.getCalendarDate().compareTo(startCal) >= 0);
                        boolean matchesEndDate = endCal == null ||
                                (expense.getCalendarDate().compareTo(endCal) <= 0);
                        boolean matchesCategory = categoryClicked == null ||
                                expense.getCategory() == categoryClicked;

                        return matchesStartDate && matchesEndDate && matchesCategory;
                    })
                    .collect(Collectors.toList());
        }

        // Update the UI with filtered expenses
        loadExpenses(filteredExpenses);
    }

    /**
     * Converts a LocalDate object to a Calendar object.
     * @param localDate The LocalDate to be converted.
     * @return A Calendar object representing the same date and time.
     */
    private Calendar convertLocalDateToCalendar(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDate.getYear(),
                localDate.getMonthValue() - 1, // Calendar months are 0-based
                localDate.getDayOfMonth());
        return calendar;
    }

    /**
     * Loads the expenses into the UI.
     * @param expenses The list of expenses to be displayed.
     * 
     * @throws IOException
     */
    public void loadExpenses(List<Expense> expenses) {
        if (vBox.getChildren().size() > 1) {
            vBox.getChildren().retainAll(vBox.getChildren().get(0));
        }
        
        List<Expense> filteredExpenses = ExpenseTracker.TRACKER.getExpenses();
        if (categoryClicked != null) {
            budgetHeader.setText(categoryClicked.toString() + " Budget");
            expensesHeader.setText(categoryClicked.toString() + " Expenses");
            totalBudgetAmt.setText("$" + BudgetTracker.TRACKER.getBudgetLimit(categoryClicked));
            totalExpensesAmt.setText("$" + ExpenseTracker.TRACKER.calculateTotalExpenses(categoryClicked));
        } else {
            budgetHeader.setText("Total Budget");
            expensesHeader.setText("Total Expenses");
            totalBudgetAmt.setText("$" + BudgetTracker.TRACKER.getTotalBudgetLimits());
            totalExpensesAmt.setText("$" + ExpenseTracker.TRACKER.calculateTotalExpenses(expenses));
        }

        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setFillWidth(true);
        vBox.setMinWidth(760); // Match the pane width
        vBox.setPrefWidth(760);
        vBox.setMinHeight(Region.USE_PREF_SIZE);
        vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vBox.setMaxHeight(Double.MAX_VALUE);

        for (Expense expense : expenses) {
            Pane clonedPane = new Pane();
            clonedPane.setStyle(expensePane.getStyle());
            clonedPane.setPrefHeight(50.0);
            clonedPane.setPrefWidth(760.0);
            clonedPane.setMinWidth(760.0);
            clonedPane.getStyleClass().add("totalspent");
            VBox.setMargin(clonedPane, new Insets(5, 0, 5, 0)); // top, right, bottom, left
            // Create HBox for horizontal layout with spacing
            HBox contentBox = new HBox(30); // 30 pixels spacing between elements
            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setLayoutY((clonedPane.getPrefHeight() - 40) / 2);
            contentBox.setLayoutX(20);

            // Create labels with specific widths to prevent overlap
            Label dateLabel = new Label(expense.getStringDate());
            dateLabel.setPrefWidth(100);
            dateLabel.setFont(new Font(18.0));

            Label summaryLabel = new Label(expense.getDescription());
            summaryLabel.setPrefWidth(250);
            summaryLabel.setFont(new Font(18.0));

            Label categoryLabel = new Label(expense.getCategory().toString());
            categoryLabel.setPrefWidth(100);
            categoryLabel.setFont(new Font(18.0));

            Label amountLabel = new Label("$ " + String.valueOf(expense.getAmount()));
            amountLabel.setPrefWidth(100);
            amountLabel.setFont(new Font(18.0));

            // Clone the edit and delete ImageViews for each expense
            ImageView editClone = new ImageView();
            editClone.setImage(edit.getImage());
            editClone.setFitHeight(edit.getFitHeight());
            editClone.setFitWidth(edit.getFitWidth());
            editClone.setPreserveRatio(edit.isPreserveRatio());

            ImageView deleteClone = new ImageView();
            deleteClone.setImage(delete.getImage());
            deleteClone.setFitHeight(delete.getFitHeight());
            deleteClone.setFitWidth(delete.getFitWidth());
            deleteClone.setPreserveRatio(delete.isPreserveRatio());

            // Create HBox for icons with less spacing
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(editClone, deleteClone);

            // Add all elements to the content box in the desired order
            contentBox.getChildren().addAll(
                    dateLabel,
                    summaryLabel,
                    categoryLabel,
                    amountLabel,
                    buttonBox);

            // Add edit functionality
            editClone.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    try {
                        View.EXPENSE.showPopUp();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ExpenseController controller = new ExpenseController();
                    controller.addCategories();
                    controller.setContentText(expense);
                    // Add your edit logic here
                    ExpenseTracker.TRACKER.removeExpense(expense);
                    AccountManager.ACCOUNT.saveUserAccount();
                    refreshPage();
                }
            });

            // Add delete functionality
            deleteClone.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog Box");
                    alert.setHeaderText("Please Confirm!");
                    alert.setContentText("Are you sure want to delete expense?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        vBox.getChildren().remove(clonedPane);
                        ExpenseTracker.TRACKER.removeExpense(expense);
                        AccountManager.ACCOUNT.saveUserAccount();
                        refreshPage();
                    }
                }
            });

            // Add hover effect for better UX
            editClone.setOnMouseEntered(e -> editClone.setCursor(Cursor.HAND));
            editClone.setOnMouseExited(e -> editClone.setCursor(Cursor.DEFAULT));

            deleteClone.setOnMouseEntered(e -> deleteClone.setCursor(Cursor.HAND));
            deleteClone.setOnMouseExited(e -> deleteClone.setCursor(Cursor.DEFAULT));

            // Add the HBox to the cloned pane
            clonedPane.getChildren().add(contentBox);

            // Add the cloned pane to the main vBox
            vBox.getChildren().add(clonedPane);
            vBox.getChildren().addListener(new ListChangeListener<Node>() {
                @Override
                public void onChanged(Change<? extends Node> c) {
                    while (c.next()) {
                        if (c.wasAdded() || c.wasRemoved()) {
                            // rezise VBox in dependency of the vvalue of your scrollpane
                            double totalHeight = vBox.getSpacing() * (vBox.getChildren().size() - 1);
                            for (Node node : vBox.getChildren()) {
                                totalHeight += node.getBoundsInLocal().getHeight();
                            }
                            totalHeight += vBox.getPadding().getTop() + vBox.getPadding().getBottom();
                            vBox.setPrefHeight(totalHeight);
                            expenseList.layout();
                            expenseList.setVvalue(expenseList.getVvalue());
                        }
                    }
                }
            });
        }

        // Make sure VBox can grow within ScrollPane
        VBox.setVgrow(expenseList, Priority.ALWAYS);
        expenseList.setContent(vBox);

        // Set up the VBox to properly fill the ScrollPane
        vBox.setMinHeight(Region.USE_PREF_SIZE);
        vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vBox.setMaxHeight(Double.MAX_VALUE);
    }

    /**
     * Clears the expenses and resets the UI.
     */
    @FXML
    private void clearButtonClick() {
        // just put the total budget and total expenses labels and dollar amount back to
        // total
        refreshPage();
    // -------------duplicate code----------------------------------
    //     budgetHeader.setText(" Total Budget");
    //     expensesHeader.setText("Total Expenses");
    //     totalBudgetAmt.setText("$" + BudgetTracker.TRACKER.getBudgetLimit(categoryClicked));
    //     totalExpensesAmt.setText("$" + ExpenseTracker.TRACKER.calculateTotalExpenses());
    //     loadExpenses(ExpenseTracker.TRACKER.getExpenses());
    }

    /**
     * Initializes the progress bars for each category.
     */
    private void initializeProgressBars() {
        HBox progressBarContainer = percentBar;
        progressBarContainer.setStyle(null);
        double totalBudget = BudgetTracker.TRACKER.getTotalBudgetLimits();

        for (Category category : Category.values()) {
            // Create progress bar and label container
            // HBox barContainer = new HBox(10); // 10px spacing
            VBox barAndLabelContainer = new VBox(5); // 5px spacing between bar and label
            barAndLabelContainer.setAlignment(Pos.CENTER); // Center the contents vertically

            // Create and style the progress bar
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(200); // Adjust width as needed
            progressBar.setPrefHeight(30);
            progressBar.setMinHeight(20);

            // Calculate percentage
            double categoryExpense = ExpenseTracker.TRACKER.calculateTotalExpenses(category);
            double percentage = totalBudget > 0 ? categoryExpense / totalBudget : 0;
            progressBar.setProgress(percentage);

            // Style the progress bar with category color
            String barStyle = "-fx-accent: " + category.getDefaultColor() + ";";
            progressBar.setStyle(barStyle);
            Tooltip.install(progressBar, new Tooltip(
                    category.toString() + ":\n$" + ExpenseTracker.TRACKER.calculateTotalExpenses(category)));

            // Add hover effect
            addHoverEffect("-fx-accent: ", progressBar, category);

            // Create label for category name and percentage
            Label label = new Label();
            if (category == Category.ENTERTAINMENT) {
                label = new Label(String.format("%s: %.1f%%",
                        "Ent", percentage * 100));
            } else if (category == Category.TRANSPORTATION) {
                label = new Label(String.format("%s: %.1f%%",
                        "Transport", percentage * 100));
            } else {
                label = new Label(String.format("%s: %.1f%%",
                        category.toString(), percentage * 100));
            }
            label.setAlignment(Pos.CENTER);
            label.setWrapText(true); // Enable text wrapping if needed
            label.setTextAlignment(TextAlignment.CENTER);
            // label.setMaxWidth(200);
            final Category finalCategory = category; // Need final variable for lambda
            progressBar.setOnMouseClicked(event -> {
                progressBarClicked(finalCategory, percentage);
            });
            label.setOnMouseClicked(event -> {
                progressBarClicked(finalCategory, percentage);
            });
            // Add components to container
            barAndLabelContainer.getChildren().addAll(progressBar, label);

            // Add some horizontal spacing between category sections
            HBox.setMargin(barAndLabelContainer, new Insets(0, 10, 0, 0));

            // Add to main horizontal container
            progressBarContainer.getChildren().add(barAndLabelContainer);
        }
    }

    /**
     * Handles the click event on a progress bar.
     *
     * @param category The category associated with the progress bar.
     * @param categoryExpensePercentage The percentage of the budget used for the category.
     */
    private void progressBarClicked(Category category, double categoryExpensePercentage) {
        vBox.getChildren().clear();
        HBox progressBarContainer = percentBar;
        progressBarContainer.setStyle("-fx-background-color: #ececec");
        categoryClicked = category;

        // Load only the expenses for the specific category
        List<Expense> categoryExpenses = ExpenseTracker.TRACKER.filterExpenses(category);
        loadExpenses(categoryExpenses);

        double categoryExpensesAmount = ExpenseTracker.TRACKER.calculateTotalExpenses(category);
        double categoryBudgetLimit = BudgetTracker.TRACKER.getBudgetLimit(category);
        budgetHeader.setText(categoryClicked.toString() + "Budget");
        expensesHeader.setText(categoryClicked.toString() + "Expenses");
        totalBudgetAmt.setText("$" + categoryBudgetLimit);
        totalExpensesAmt.setText("$" + categoryExpensesAmount);

        // Calculate the percentage of the budget used for this category
        double categoryUsagePercentage = categoryExpensesAmount / categoryBudgetLimit;

        // Get the HBox where we want to display the percentage bar
        HBox percentBar = this.percentBar;

        // Clear the current content of the HBox before adding the new pane
        percentBar.getChildren().clear();

        // Create a new colored Pane that represents the percentage used of the budget
        Pane categoryBar = new Pane();
        categoryBar.setPrefWidth(percentBar.getWidth() * categoryUsagePercentage); // Set the width relative to the
                                                                                   // percentage used
        categoryBar.setStyle("-fx-background-color: " + category.getDefaultColor()); // Use the default color of the
                                                                                     // category
        addHoverEffect("-fx-background-color: ", categoryBar, category);

        // Add the category bar to the HBox
        percentBar.getChildren().add(categoryBar);

        updateCategoryExpensePercentage(categoryUsagePercentage);

        System.out.println("Category: " + category + ", Budget: $" + BudgetTracker.TRACKER.getBudgetLimit(category)
                + ", Expenses: $" + ExpenseTracker.TRACKER.calculateTotalExpenses(category));
    }

    /**
     * Adds hover effect to a node.
     *
     * @param style The style to apply on hover.
     * @param node The node to apply the hover effect.
     * @param category The category associated with the node.
     */
    private void addHoverEffect(String style, Node node, Category category) {
        node.setOnMouseEntered(event -> {
            node.setStyle(style + category.getHoverColor() + ";");
        });

        node.setOnMouseExited(event -> {
            node.setStyle(style + category.getDefaultColor() + ";"); // Reverts to default color
        });
    }

    /**
     * Updates the category expense percentage label.
     *
     * @param categoryExpensePercentage The percentage of the budget used for the category.
     */
    private void updateCategoryExpensePercentage(double categoryExpensePercentage) {
        // Update the UI with the percentage, this can be displayed in a label or
        // progress bar
        // Example:

        expensePercentageLabel
                .setText("Category spent: " + String.format("%.2f", categoryExpensePercentage * 100) + "%");
    }

    /**
     * Refreshes the page by reloading expenses and updating UI components
     */
    private void refreshPage(){
        vBox.getChildren().clear();
        loadExpenses(ExpenseTracker.TRACKER.getExpenses());
        percentBar.getChildren().clear();
        initializeProgressBars();
        if (categoryClicked != null) {
            double categoryExpensesAmount = ExpenseTracker.TRACKER.calculateTotalExpenses(categoryClicked);
            double categoryBudgetLimit = BudgetTracker.TRACKER.getBudgetLimit(categoryClicked);
            double categoryUsagePercentage = categoryBudgetLimit;
            if (categoryUsagePercentage > 0){
                categoryUsagePercentage = categoryExpensesAmount / categoryBudgetLimit;
            } 
            else{
                categoryUsagePercentage = 0;
            }
            
            updateCategoryExpensePercentage(categoryUsagePercentage);
        }
        
        // Force layout update
        expenseList.layout();
        expenseList.setVvalue(expenseList.getVvalue());
    }

}