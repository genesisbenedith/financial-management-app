package csc335.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import csc335.app.models.Budget;
import csc335.app.models.Category;
import csc335.app.models.User;
import csc335.app.persistence.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class HeaderController implements Initializable {

    @FXML
    private ComboBox<String> notificationComboBox; // ComboBox for notifications

    private static User currentUser;
    private List<String> notificationsRecord; // Store the notification records

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        notificationsRecord = new ArrayList<>();
        currentUser = UserSessionManager.SESSION.getCurrentUser();
        loadNotifications();
        handleNotifications();
    }

    // Loads existing notifications into the combo box
    private void loadNotifications() {
        // Loop through each notification record
        for (String notification : notificationsRecord) {
            String[] parts = notification.split(":");

            // Assuming the first part is the category, and second is the color (e.g., "red", "yellow", "green")
            String category = parts[0];  // Category as string
            String color = parts[1];  // Color of the notification

            // Create a label for each notification
            String notificationText = category + " - " + color;
            addNotificationToComboBox(notificationText, color);
        }
    }

    // Add new notification to the ComboBox with the appropriate color
    private void addNotificationToComboBox(String notificationText, String color) {
        // Add color-styled notification to the ComboBox
        if (!notificationComboBox.getItems().contains(notificationText)) {
            notificationComboBox.getItems().add(notificationText);

            // Change color of the notification in ComboBox using styling
            switch (color.toLowerCase()) {
                case "red":
                    notificationComboBox.setStyle("-fx-background-color: #ff3b30; -fx-text-fill: white;");
                    break;
                case "yellow":
                    notificationComboBox.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");
                    break;
                case "green":
                    notificationComboBox.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                    break;
                default:
                    System.out.println("Unknown notification color: " + color);
                    break;
            }
        }
    }

    // Handle notifications based on budget state (exceeded, close, within limit)
    private void handleNotifications() {
        for (Budget budget : currentUser.getBudgets()) {
            Category category = budget.getCategory();

            // Check if the category exceeded its limit
            if (budget.isExceeded()) {
                addOrUpdateNotification(category.name(), "red");
            }
            // Check if the budget is 80% or more of the limit
            else if (budget.getPercentage() >= 80.0) {
                addOrUpdateNotification(category.name(), "yellow");
            }
            // Otherwise, budget is under control
            else {
                addOrUpdateNotification(category.name(), "green");
            }
        }
    }

    // Add or update notification based on category and color
    private void addOrUpdateNotification(String category, String newColor) {
        boolean notificationExists = false;

        // Loop through the existing notifications to check if one already exists for the category
        for (String notification : notificationsRecord) {
            String[] parts = notification.split(":");
            if (parts[0].equalsIgnoreCase(category)) {
                // Remove the old notification if it exists
                notificationsRecord.remove(notification);
                notificationExists = true;
                break;
            }
        }

        // Add the new notification and show it in the ComboBox
        if (notificationExists || "yellow".equals(newColor)) {
            notificationsRecord.add(category + ":" + newColor);

            // Add to the ComboBox based on the new notification
            addNotificationToComboBox(category + " - " + newColor, newColor);
        }
    }
}