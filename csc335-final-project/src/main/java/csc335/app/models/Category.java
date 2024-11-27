package csc335.app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Author(s): Genesis Benedith
 * File: Category.java
 * Description: Model enum that represents an expense category
 */

public enum Category {
    GROCERIES,
    ENTERTAINMENT,
    TRANSPORTATION,
    UTILITIES,
    HEALTHCARE,
    OTHER;

    /**
     * 
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

    public static List<String> allValues() {
        List<String> result = new ArrayList<>();
        for (Category category : values()) {
            result.add(category.toString());
        }
        return result;
    }
}
