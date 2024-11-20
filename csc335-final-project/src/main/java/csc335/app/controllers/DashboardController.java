package csc335.app.controllers;

import csc335.app.models.User;

public class DashboardController {

    private static User user;

    public static void loadUserInformation(String username) {
        user = new User(null, null, null);
    }

    
}
