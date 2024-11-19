package csc335.app;

import java.util.HashMap;

public class Budget {
    private HashMap<String, Double> budget;

    // Constructor to initialize budget categories
    public Budget() {
        budget = new HashMap<>();
        budget.put("Food", 0.0);
        budget.put("Transportation", 0.0);
        budget.put("Entertainment", 0.0);
        budget.put("Utilities", 0.0);
        budget.put("Misc", 0.0);
    }

    // Setters for each category
    public void setFood(double food) {
        budget.put("Food", food);
    }

    public void setTransport(double transport) {
        budget.put("Transportation", transport);
    }

    public void setEntertain(double entertain) {
        budget.put("Entertainment", entertain);
    }

    public void setUtility(double utility) {
        budget.put("Utilities", utility);
    }

    public void setMisc(double misc) {
        budget.put("Misc", misc);
    }

    
    public void alert(double threshold) {
        for (String category : budget.keySet()) {
            if (budget.get(category) > threshold) {
                System.out.println("Alert: " + category + " budget exceeds " + threshold);
            }
        }
    }

}
