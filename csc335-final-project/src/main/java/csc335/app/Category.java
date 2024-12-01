package csc335.app;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Genesis Benedith
 * Model enum that represents an expense category
 */

public enum Category {
    GROCERIES,
    FOOD,
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
